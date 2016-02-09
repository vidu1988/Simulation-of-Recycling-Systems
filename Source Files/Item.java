package ecoRe.recyclingSystem;

import org.json.JSONObject;

public class Item {
	private final String itemType;
	private final  double itemPrice;
	private final double itemWeight;

	public Item(String itemType, double itemPrice, double itemWeight) {
		this.itemType = itemType;
		this.itemPrice = itemPrice;
		this.itemWeight = itemWeight;
	}

	public String getItemType() {
		return this.itemType;
	}

	public double getItemPrice() {
		return this.itemPrice;
	}

	public double getItemWeight() {
		return this.itemWeight;
	}

	public String toString() {
		return "Item[Type = " + itemType + ", Price = " + itemPrice
				+ ", Weight = " + itemWeight + "]";
	}
	
	public static Item createFromJSONObject(JSONObject object) {
		JSONObject contents = object.getJSONObject("Item");
		return new Item(contents.getString("itemType"),
				contents.getDouble("itemPrice"),
				contents.getDouble("itemWeight"));
	}

	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject();
		contents.put("itemType", itemType);
		contents.put("itemPrice", Double.toString(itemPrice));
		contents.put("itemWeight", Double.toString(itemWeight));
		object.put("Item", contents);
		return object;
	}

}
