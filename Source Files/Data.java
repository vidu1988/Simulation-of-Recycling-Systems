package ecoRe.recyclingSystem;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Data {
	public static final String ALUMINIUM = "Aluminium";
	public static final String GLASS = "GLASS";

	private final Map<String, Double> itemTypeToCostPerLb;

	public Data() {
		itemTypeToCostPerLb = new HashMap<String, Double>();
		itemTypeToCostPerLb.put(ALUMINIUM, 4.0);
		itemTypeToCostPerLb.put(GLASS, 3.0);
	}
	
	public Data(Map<String, Double> map) {
		itemTypeToCostPerLb = map;
	}

	public void addItemType(String itemType, double price) {
		itemTypeToCostPerLb.put(itemType, price);
	}

	// Returns Item Map
	public Map<String, Double> getMap() {
		return itemTypeToCostPerLb;
	}

	// Returns the value(i.e. price/lb) of item type
	public double returnItemPriceDetails(String itemType) {
		return itemTypeToCostPerLb.get(itemType);
	}

	public void removeItemType(String itemType) {
		itemTypeToCostPerLb.remove(itemType);
	}
	
	public static Data createFromJSONObject(JSONObject object) {
		JSONObject contents = object.getJSONObject("Data");
		Map<String, Double> itemValues = new HashMap<>();
		for(String key: contents.keySet()) {
			itemValues.put(key, contents.getDouble(key));
		}
		return new Data(itemValues);
	}
 

	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject(itemTypeToCostPerLb);
		object.put("Data", contents);
		return object;
	}
}
