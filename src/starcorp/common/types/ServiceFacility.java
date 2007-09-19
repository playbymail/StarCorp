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
package starcorp.common.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import starcorp.common.entities.Workers;

/**
 * starcorp.common.types.ServiceFacility
 *
 * @author Seyed Razavi <monkeyx@gmail.com>
 * @version 16 Sep 2007
 */
public class ServiceFacility extends AFacilityType {

	public static List<AFacilityType> listMedical(int quality) {
		List<AFacilityType> types = new ArrayList<AFacilityType>();
		Iterator<AFacilityType> i = AFacilityType.listTypes(ServiceFacility.class).iterator();
		while(i.hasNext()) {
			ServiceFacility facility = (ServiceFacility) i.next();
			if(facility.isMedical() && facility.getQuality() == quality) {
				types.add(facility);
			}
		}
		return types;
	}
	

	public static List<AFacilityType> listFitness(int quality) {
		List<AFacilityType> types = new ArrayList<AFacilityType>();
		Iterator<AFacilityType> i = AFacilityType.listTypes(ServiceFacility.class).iterator();
		while(i.hasNext()) {
			ServiceFacility facility = (ServiceFacility) i.next();
			if(facility.isFitness() && facility.getQuality() == quality) {
				types.add(facility);
			}
		}
		return types;
	}
	
	public static List<AFacilityType> listEntertainment(int quality) {
		List<AFacilityType> types = new ArrayList<AFacilityType>();
		Iterator<AFacilityType> i = AFacilityType.listTypes(ServiceFacility.class).iterator();
		while(i.hasNext()) {
			ServiceFacility facility = (ServiceFacility) i.next();
			if(facility.isEntertainment() && facility.getQuality() == quality) {
				types.add(facility);
			}
		}
		return types;
	}
	
	public static List<AFacilityType> listEducation(int quality) {
		List<AFacilityType> types = new ArrayList<AFacilityType>();
		Iterator<AFacilityType> i = AFacilityType.listTypes(ServiceFacility.class).iterator();
		while(i.hasNext()) {
			ServiceFacility facility = (ServiceFacility) i.next();
			if(facility.isEducation() && facility.getQuality() == quality) {
				types.add(facility);
			}
		}
		return types;
	}
	

	@Override
	public String getSubCategory() {
		return " || Service (" + 
		(isMedical() ? " medical / " : "") + 
		(isFitness() ? " fitness / " : "") + 
		(isEntertainment() ? " ent / " : "") + 
		(isEducation() ? " edu / " : "") + 
		getQuality() + ") || ";
	}
	
	public int getQuality() {
		return Integer.parseInt(getResource(this, "quality"));
	}
	
	public int getTotalColonistsServiceable(List<Workers> currentWorkers) {
		double efficiency = getEfficiency(currentWorkers);
		
		if(efficiency < 1) {
			return 0;
		}
		else if(efficiency < 10) {
			return 50;
		}
		else if (efficiency < 25) {
			return 100;
		}
		else if (efficiency < 50) {
			return 250;
		}
		else if (efficiency < 75) {
			return 500;
		}
		else if (efficiency < 100) {
			return 1000;
		}
		else {
			return 2000;
		}
	}

	public boolean isMedical() {
		return Boolean.parseBoolean(getResource(this, "medical"));
	}

	public boolean isFitness() {
		return Boolean.parseBoolean(getResource(this, "fitness"));
	}
	
	public boolean isEntertainment() {
		return Boolean.parseBoolean(getResource(this, "entertainment"));
	}
	
	public boolean isEducation() {
		return Boolean.parseBoolean(getResource(this, "education"));
	}




}
