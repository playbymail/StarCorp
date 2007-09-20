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
package starcorp.server;

import java.util.ResourceBundle;

import starcorp.common.types.GalacticDate;

/**
 * starcorp.server.ServerConfiguration
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 20 Sep 2007
 */
public class ServerConfiguration {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("server");

	public static final String FETCHER_USER = bundle.getString("fetcher.user");
	public static final String FETCHER_PASSWORD = bundle.getString("fetcher.password");
	public static final String FETCHER_SERVER = bundle.getString("fetcher.server");
	public static final String FETCHER_PROVIDER = bundle.getString("fetcher.provider");
	
	public static final String SMTP_HOST_NAME = bundle.getString("smtp.host");
	public static final String SMTP_PORT = bundle.getString("smtp.port");
	public static final String SMTP_AUTH_USER = bundle.getString("smtp.user");
	public static final String SMTP_AUTH_PASSWORD = bundle.getString("smtp.password");
	
	public static final String SERVER_EMAIL_TURNS = bundle.getString("server.email.turns");
	public static final String SERVER_EMAIL_BUGS = bundle.getString("server.email.bugs");

	public static final int SETUP_INITIAL_CREDITS = Integer.parseInt(bundle.getString("setup.corporation.credits"));
	public static final String SETUP_DESCRIPTION = bundle.getString("setup.corporation.description");
	
	private static GalacticDate currentDate;
	
	public static GalacticDate getCurrentDate() {
		if(currentDate == null) {
			int year = Integer.parseInt(bundle.getString("current.year"));
			int month = Integer.parseInt(bundle.getString("current.month"));
			currentDate = new GalacticDate(year,month);
		}
		return currentDate;
	}
	
}
