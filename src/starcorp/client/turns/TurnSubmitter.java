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
import starcorp.client.ClientConfiguration;
import starcorp.common.turns.Turn;
import starcorp.common.util.SendEmail;

/**
 * starcorp.client.turns.TurnSubmitter
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 19 Sep 2007
 */
public class TurnSubmitter {

	private static Log log = LogFactory.getLog(TurnSubmitter.class);
	
	private static SendEmail email = new SendEmail(ClientConfiguration.SMTP_HOST_NAME, ClientConfiguration.SMTP_PORT, ClientConfiguration.SMTP_AUTH_USER, ClientConfiguration.SMTP_AUTH_PASSWORD);
	
	public static void submit(Turn turn) throws IOException, MessagingException {
		String filename = "submitted-turn.xml";
		turn.write(new FileWriter(filename));
		
		String from = turn.getCorporation().getPlayerEmail();
		String[] to = {ClientConfiguration.SERVER_EMAIL_TURNS};
		String[] cc = {turn.getCorporation().getPlayerEmail()};
		email.send(to, cc, null, "StarCorp Turn Submission", "", from, filename);
		log.info("Submitted turn.");
	}
}
