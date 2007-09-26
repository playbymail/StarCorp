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
package starcorp.client;

import java.util.ResourceBundle;

/**
 * starcorp.client.ClientConfiguration
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class ClientConfiguration {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("client");

	public static final String SMTP_HOST_NAME = bundle.getString("smtp.host");
	public static final String SMTP_PORT = bundle.getString("smtp.port");
	public static final String SMTP_AUTH_USER = bundle.getString("smtp.user");
	public static final String SMTP_AUTH_PASSWORD = bundle.getString("smtp.password");
	
	public static final String SERVER_EMAIL_TURNS = bundle.getString("server.email.turns");
	public static final String SERVER_EMAIL_BUGS = bundle.getString("server.email.bugs");

}
