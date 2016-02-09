package ecoRe.recyclingSystem;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecyclingTransaction {
	private final CostCalculation costCalculation;
	private final Set<String> validItemTypes;

	private final Date startTime;
	private boolean isClosed;
	private TransactionType transactionType;

	public enum TransactionType {
		MONEY, COUPON
	}

	public RecyclingTransaction(Date date, Set<String> validItemTypes) {
		this(date, validItemTypes, null, false, new CostCalculation());
	}
	
	public RecyclingTransaction(Date date, Set<String> validItemTypes, 
			TransactionType transactionType, boolean isClosed, CostCalculation costCalculation) {
		startTime = date;
		this.validItemTypes = new HashSet<>(validItemTypes);
		this.transactionType = transactionType;
		this.isClosed = isClosed;
		this.costCalculation = costCalculation;
	}

	
	public boolean isValidItem(String itemType) {
		return validItemTypes.contains(itemType);
	}

	public void close(TransactionType type) {
		isClosed = true;
		transactionType = type;
	}

	/**
	 * @return the itemList
	 */
	public List<Item> getItemList() {
		return costCalculation.getItemList();
	}

	public String getItemWeightSummary() {
		StringBuilder stringBuilder = new StringBuilder();

		for (String item : costCalculation.getAllItems()) {
			stringBuilder.append(item + "   -->   " + costCalculation.getWeightForItem(item) + " lb\n");
		}
		return stringBuilder.toString();
	}

	public String getItemCostSummary() {
		StringBuilder stringBuilder = new StringBuilder();

		for (String item : costCalculation.getAllItems()) {
			stringBuilder.append(item + "   -->   " + "$ " + costCalculation.getCostForItem(item) + "\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * @return the isClosed
	 */
	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	public void addItem(Item item) {
		costCalculation.addItem(item);
	}

	public CostCalculation getCostCalculation() {
		return costCalculation;
	}

	public double getTotalWeight() {
		return costCalculation.getTotalWeight();
	}

	public double getTotalCost() {
		return costCalculation.getTotalCost();
	}

	/**
	 * @return the transactionType
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public static RecyclingTransaction createFromJSONObject(JSONObject object) {
		JSONObject contents = object.getJSONObject("RecyclingTransaction");
		CostCalculation costCalculation = 
				CostCalculation.createFromJSONObject(contents.getJSONObject("costCalculation"));
		Set<String> validTypes = new HashSet<>();
		JSONArray validObjectTypes = contents.getJSONArray("validItemTypes");

		for(int i=0; i< validObjectTypes.length(); i++) {
			validTypes.add(validObjectTypes.getString(i));
		}
		Date startTime = Date.from(Instant.ofEpochMilli(contents.getLong("startTime")));
		boolean isClosed = contents.getBoolean("isClosed");
		TransactionType trans = null;
		if(contents.has("transactionType")) {
			trans = TransactionType.valueOf(contents.getString("transactionType"));
		}

		return new RecyclingTransaction(startTime, validTypes, trans, isClosed, costCalculation);
	}

	
	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject();
		contents.put("costCalculation", costCalculation.asJSONObject());
		JSONArray validObjectTypes = new JSONArray();
		for(String v: validItemTypes) {
			validObjectTypes.put(v);
		}
		contents.put("validItemTypes", validObjectTypes);
		contents.put("startTime" , startTime.toInstant().toEpochMilli());
		contents.put("isClosed", isClosed);
		contents.put("transactionType", transactionType);
		object.put("RecyclingTransaction", contents);
		return object;
	}

}
