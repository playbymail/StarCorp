/**
 *  Copyright 2007 Seyed Razavi
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and limitations under the License. 
 */
package starcorp.server.turns;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import starcorp.common.entities.Colony;
import starcorp.common.entities.Corporation;
import starcorp.common.entities.Planet;
import starcorp.common.entities.Starship;
import starcorp.common.entities.StarshipDesign;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnError;
import starcorp.common.turns.TurnOrder;
import starcorp.common.turns.TurnReport;
import starcorp.common.types.AItemType;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.Items;
import starcorp.common.types.OrderType;
import starcorp.common.util.SendEmail;
import starcorp.server.ServerConfiguration;
import starcorp.server.Util;
import starcorp.server.engine.AServerTask;

/**
 * starcorp.server.turns.TurnProcessor
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class TurnProcessor extends AServerTask {
	
	private static final Log log = LogFactory.getLog(TurnProcessor.class);
	private final SendEmail sendEmail;
	private final File reportsFolder;
	
	private int processed;
	
	public TurnProcessor() {
		this.sendEmail = new SendEmail(ServerConfiguration.SMTP_HOST_NAME,ServerConfiguration.SMTP_PORT,ServerConfiguration.SMTP_AUTH_USER,ServerConfiguration.SMTP_AUTH_PASSWORD);
		this.reportsFolder = new File(ServerConfiguration.REPORTS_FOLDER);
		if(!reportsFolder.exists()) {
			reportsFolder.mkdirs();
		}
	}
	
	private void processFolder(File folder) {
		File[] turns = folder.listFiles();
		SAXReader reader = new SAXReader();
		for(int i = 0; i < turns.length; i++) {
			if(turns[i].isDirectory()) {
				processFolder(turns[i]);
			}
			else {
				Turn turn = null;
				try {
					Document doc = reader.read(turns[i]);
					turn = new Turn(doc.getRootElement().element("turn"));
					Corporation corp = authorize(turn.getCorporation());
					if(corp == null) {
						corp = register(turn.getCorporation());
						if(corp == null) {
							turn.add(TurnError.ERROR_AUTHORIZATION_FAILED);
						}
					}
					if(corp != null){
						turn.setCorporation(corp);
					}
				}
				catch(Exception e) {
					log.warn(this + ": Error reading turn file " + turns[i].getName() + " because " + e.getMessage());
				}
				if(turn != null) {
					try {
						TurnReport report = process(turn);
						GalacticDate date = ServerConfiguration.getCurrentDate();
						Corporation corp = report.getTurn().getCorporation();
						String filename = ServerConfiguration.REPORTS_FOLDER + "/" + corp.getPlayerEmail() + "-turn-" + date.getMonth() + "-" +  date.getYear() + ".xml";
						report.write(new FileWriter(filename));
						
						String[] to = {corp.getPlayerName() + "<" + corp.getPlayerEmail() + ">"};
						String subject = "Starcorp turn " + date.getMonth() + "." + date.getYear();
						String message = "Please find your reports file attached.  Save to a convenient location and open it with your StarCorp client.";
						String from = ServerConfiguration.SERVER_EMAIL_TURNS;
						sendEmail.send(to, null, null, subject, message, from, filename);
						turns[i].deleteOnExit();
					}
					catch(Exception e) {
						log.error(this + ": Error sending report for " + turn + " because " + e.getMessage(),e);
					}
				}
			}
		}
	}
	
	public Log getLog() {
		return log;
	}
	
	public void doJob() {
		log.info(this + ": Started processing turns.");
		processed = 0;
		File turnFolder = new File(ServerConfiguration.TURNS_FOLDER);
		if(turnFolder.exists()) {
			processFolder(turnFolder);
		}
		log.info(this + ": Finished processing turns.");
	}
	
	public TurnReport process(Turn turn) {
		Corporation corp = turn.getCorporation();
		TurnReport report = new TurnReport(turn);
		if(corp != null){
			GalacticDate lastTurn = corp.getLastTurnDate();
			GalacticDate currentDate = ServerConfiguration.getCurrentDate();
			if(lastTurn != null && !lastTurn.before(currentDate)) {
				if(log.isDebugEnabled())
					log.debug("Turn early " + turn);
				turn.add(TurnError.ERROR_EARLY_TURN);
			}
			else {
				for(TurnOrder order : turn.getOrders()) {
					order.setCorp(corp);
					TurnError error = process(order);
					if(error != null) {
						turn.add(error);
					}
					if(log.isDebugEnabled())
						log.debug("Processed " + order + " : error = " + error);
				}
				turn.setProcessedDate(ServerConfiguration.getCurrentDate());
				report.addPlayerEntities(entityStore.listDesigns(corp));
				report.addPlayerEntities(entityStore.listFacilities(corp));
				report.addPlayerEntities(entityStore.listShips(corp));
				report.setMarket(entityStore.listMarket(1));
				report.setLaws(entityStore.listLaws());
				report.setItems(entityStore.listItems(turn.getCorporation()));
				// TODO add factory queue items to report
				// TODO add facility workers to report
				// TODO add CreditAccount for corporation to report
				// TODO add recent CashTransaction for corporation to report
				// TODO add known systems (requires changes to entities)
				// TODO add count of facilities by type for each known colony
			}
		}
		log.info(this + ": Processed turn from " + turn.getCorporation() + ". Order: " + turn.getOrders().size() + ". Errors: " + turn.getErrors().size());
		processed++;
		return report;
	}
	
	private Corporation register(Corporation corp) {
		Corporation existing = entityStore.getCorporation(corp.getPlayerEmail());
		if(existing == null) {
			corp.setFoundedDate(ServerConfiguration.getCurrentDate());
			entityStore.create(corp);
			entityStore.addCredits(corp, ServerConfiguration.SETUP_INITIAL_CREDITS, ServerConfiguration.SETUP_DESCRIPTION);
			createStartingPosition(corp);
			return corp;
		}
		else {
			return null;
		}
	}
	
	private void createStartingPosition(Corporation corp){
		// TODO make this configurable esp starting colony
		StarshipDesign design = new StarshipDesign();
		design.setDesignDate(ServerConfiguration.getCurrentDate());
		design.setName("Surveyer");
		design.setOwner(corp);
		design.setHulls(new Items(AItemType.getType("command-deck"),1));
		design.setHulls(new Items(AItemType.getType("crew-deck"),2));
		design.setHulls(new Items(AItemType.getType("impulse-drive"),1));
		design.setHulls(new Items(AItemType.getType("warp-drive-I"),1));
		design.setHulls(new Items(AItemType.getType("lab"),1));
		design.setHulls(new Items(AItemType.getType("light-cargo"),1));
		design.setHulls(new Items(AItemType.getType("scanner"),1));
		design.setHulls(new Items(AItemType.getType("probe"),1));
		design = (StarshipDesign) entityStore.create(design);
		Starship ship = new Starship();
		ship.setBuiltDate(ServerConfiguration.getCurrentDate());
		ship.setDesign(design);
		ship.setName("SS " + corp.getName());
		List<Colony> colonies = entityStore.listColonies();
		Colony colony = colonies.get(Util.rnd.nextInt(colonies.size()));
		Planet planet = (Planet) entityStore.load(Planet.class, colony.getPlanetID());
		ship.setColony(colony);
		ship.setPlanet(planet);
		ship.setPlanetLocation(colony.getLocation());
		ship.setLocation(planet.getLocation());
		ship.setOwner(corp);
		entityStore.create(ship);
	}
	
	private Corporation authorize(Corporation corp) {
		return entityStore.getCorporation(corp.getPlayerEmail(), corp.getPlayerPassword());
	}
	
	private TurnError process(TurnOrder order) {
		TurnError error = null;
		OrderType type = order.getType();
		AOrderProcessor processor = AOrderProcessor.getProcessor(type.getKey());
		if(processor == null) {
			error = new TurnError(TurnError.INVALID_ORDER_TYPE,order);
		}
		else {
			processor.setEntityStore(entityStore);
			error = processor.process(order);
		}
		return error;
		
	}

	public int getProcessed() {
		return processed;
	}
}
