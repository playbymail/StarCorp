package starcorp.common.types;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public abstract class AFactoryItem extends AItemType {

	public Set<Items> getComponent() {
		Set<Items> items = new HashSet<Items>();
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			int i;
			if((i = key.indexOf(".component.type")) != -1) {
				String componentKey = key.substring(0, i);
				String componentNumber = key.substring(key.lastIndexOf(".") + 1);
				Items item = new Items();
				item.setType(bundle.getString(key));
				item.setQuantity(Integer.parseInt(bundle.getString(componentKey + ".component.qty." + componentNumber)));
				items.add(item);
			}
		}
		return items;
	}

}