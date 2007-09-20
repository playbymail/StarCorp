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
package starcorp.client.turns;

import java.io.FileWriter;
import java.io.IOException;
import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import starcorp.client.ClientConfiguration;
import starcorp.common.entities.Corporation;
import starcorp.common.mail.EmailUtil;
import starcorp.common.turns.Turn;
import starcorp.common.turns.TurnOrder;
import starcorp.common.types.GalacticDate;
import starcorp.common.types.OrderType;

/**
 * starcorp.client.turns.TurnSubmitter
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 19 Sep 2007
 */
public class TurnSubmitter {

	private static Log log = LogFactory.getLog(TurnSubmitter.class);
	
	public static void main(String[] args) {
		try {
			Turn turn = new Turn();
			Corporation corporation = new Corporation("monkeyx@gmail.com","test");
			turn.setCorporation(corporation);
			turn.setSubmittedDate(GalacticDate.getCurrentDate());
			for(int i = 0; i < 10; i++) {
				TurnOrder order = new TurnOrder();
				order.setCorp(corporation);
				order.setType(OrderType.getType("design-ship"));
				order.add("command-deck");
				order.add("crew-deck");
				order.add("impulse-drive");
				order.add("light-cargo");
				turn.add(order);
			}
			submit(turn);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static EmailUtil email = new EmailUtil(ClientConfiguration.SMTP_HOST_NAME, ClientConfiguration.SMTP_PORT, ClientConfiguration.SMTP_AUTH_USER, ClientConfiguration.SMTP_AUTH_PASSWORD);
	
	public static void submit(Turn turn) throws IOException, MessagingException {
		GalacticDate date = GalacticDate.getCurrentDate();
		String filename = "turn-" + date.getMonth() + "-" + date.getYear() + ".xml";
		// TODO switch to compact format to save space after debugging
		// OutputFormat format = OutputFormat.createCompactFormat();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(
			new FileWriter(filename), format
		);
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("starcorp");
		turn.toXML(root);
		
		writer.write(doc);
		writer.close();
		
		String from = turn.getCorporation().getPlayerEmail();
		String[] to = {ClientConfiguration.SERVER_EMAIL_TURNS};
		String[] cc = {turn.getCorporation().getPlayerEmail()};
		email.send(to, cc, null, "StarCorp Turn Submission " + date, "", from, filename);
		log.info("Submitted turn.");
	}
}
