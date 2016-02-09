package ecoRe.recyclingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class CostCalculation {
	private final Map<String, Double> itemToTotalCostMap = new HashMap<>();
	private final Map<String, Double> itemToTotalWeightMap = new HashMap<>();
	private final List<Item> itemList = new ArrayList<>();

	private double totalCost;
	private double totalWeight;

	public void addItem(Item item) {
		String itemType = item.getItemType();
		double itemCost = item.getItemPrice() * item.getItemWeight();
		totalCost += itemCost;
		totalWeight += item.getItemWeight();
		itemList.add(item);
		if (itemToTotalCostMap.containsKey(itemType)) {
			itemToTotalCostMap.put(itemType, itemToTotalCostMap.get(itemType)
					+ itemCost);
		} else {
			itemToTotalCostMap.put(itemType, itemCost);
		}

		if (itemToTotalWeightMap.containsKey(itemType)) {
			itemToTotalWeightMap.put(itemType,
					itemToTotalWeightMap.get(itemType) + item.getItemWeight());
		} else {
			itemToTotalWeightMap.put(itemType, item.getItemWeight());
		}
	}
	
	public List<Item> getItemList() {
		return itemList;
	}

	/* Calculates total cost(money due to customer) */
	public double getTotalCost() {
		return totalCost;
	}

	/* Calculates total weight(entered by customer) */
	public double getTotalWeight() {
		return totalWeight;
	}

	/* Calculates item wise weight(entered by customer) */
	public double getWeightForItem(String itemType) {
		return itemToTotalWeightMap.get(itemType);
	}

	/* Calculates item wise cost(amount due to customer) */
	public double getCostForItem(String itemType) {
		return itemToTotalCostMap.get(itemType);
	}

	public Set<String> getAllItems() {
		return itemToTotalCostMap.keySet();
	}
	
	public void reset() {
		itemToTotalCostMap.clear();
		itemToTotalWeightMap.clear();
	}
	
	public static CostCalculation createFromJSONObject(JSONObject object) {
		JSONObject contents = object.getJSONObject("CostCalculation");
		JSONArray itemArray = contents.getJSONArray("itemList");
		CostCalculation costCalculation = new CostCalculation();

		for(int i=0; i< itemArray.length(); i++) {
			Item item = Item.createFromJSONObject(itemArray.getJSONObject(i));
			costCalculation.addItem(item);
		}
		return costCalculation;
	}
	
	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject();
		JSONArray itemArray = new JSONArray();
		for(Item item: itemList) {
			itemArray.put(item.asJSONObject());
		}
		contents.put("itemList", itemArray);
		object.put("CostCalculation", contents);
		return object;
	}
}
