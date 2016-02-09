package ecoRe.recyclingSystem;

import java.io.OutputStream;
import java.io.Writer;
import java.time.Instant;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import ecoRe.recyclingSystem.RecyclingTransaction.TransactionType;

public class RecyclingMachine {
	
	// Our Assumption : RCM can hold maximum this much weight
	static final double maxRCMWeight = 50.0;
	private final int machineId;
	private final String machineLocation;
	// Lists thats maintains the lists of transactions 
	private final List<RecyclingTransaction> transactions;
	private final LinkedList<Date> lastEmptiedTimeList;
	private int totalCouponsLeft;
	private boolean machineStatus;
	private double totalMoneyLeft;
	private double totalWeight;
	private final Data itemPrices;
	
	public RecyclingMachine(int machineId, String machineLocation,
			double totalMoneyLeft, int totalCouponsLeft) {
		this(machineId, machineLocation, totalMoneyLeft, totalCouponsLeft, 
				false, // machine status
				new ArrayList<Date>(), // last emptied times
				new ArrayList<RecyclingTransaction>(), // transactions
				new Data() // itemPrices
				);
	}
	
	public RecyclingMachine(int machineId, String machineLocation,
			double totalMoneyLeft, int totalCouponsLeft, boolean machineStatus, List<Date> lastEmptiedTimes,
			List<RecyclingTransaction> transactionList, Data itemPrices) {
		this.machineId = machineId;
		this.machineLocation = machineLocation;
		this.totalMoneyLeft = totalMoneyLeft;
		this.totalCouponsLeft = totalCouponsLeft;
		this.machineStatus = machineStatus;
		this.totalWeight = 0;
		this.itemPrices = itemPrices;
		this.lastEmptiedTimeList = new LinkedList<Date>(lastEmptiedTimes);
		this.transactions = transactionList;
	}


	public String getName() {
		return "RCM " + machineId;
	}

	/**
	 * @return the machineId
	 */
	public int getMachineId() {
		return machineId;
	}

	/**
	 * @return the machineLocation
	 */
	public String getMachineLocation() {
		return machineLocation;
	}

	/**
	 * @return the lastEmptiedTime
	 */
	public Date getLastEmptiedTime() {
		return lastEmptiedTimeList.isEmpty() ? null: lastEmptiedTimeList.getLast();
	}

	public List<Date> getLastEmptiedTimeList() {
		return new ArrayList<Date>(lastEmptiedTimeList);
	}

	/**
	 * @param totalMoneyLeft
	 *            the totalMoneyLeft to set
	 */
	public void settotalMoneyLeft(double totalMoneyLeft) {
		this.totalMoneyLeft = totalMoneyLeft;
	}

	/**
	 * @param totalCouponsLeft
	 *            the totalCouponsLeft to set
	 */
	public void settotalCouponsLeft(int totalCouponsLeft) {
		this.totalCouponsLeft = totalCouponsLeft;
	}

	/**
	 * @param machineStatus
	 *            the machineStatus to set
	 */
	public void setMachineStatus(boolean machineStatus) {
		this.machineStatus = machineStatus;
	}

	public void startNewTransaction() {
		transactions.add(new RecyclingTransaction(new Date(), itemPrices
				.getMap().keySet()));
	}

	public Double pricePerLbForItem(String itemType) {
		return itemPrices.getMap().get(itemType);
	}

	public void endTransaction(TransactionType type) {
		RecyclingTransaction transaction = getLatestTransaction();
		if(transaction.isClosed()) {
			throw new IllegalStateException();
		}
		totalWeight += transaction.getTotalWeight();
		if (type == TransactionType.MONEY) {
			totalMoneyLeft -= transaction.getTotalCost();
		} else {
			totalCouponsLeft -= 1;
		}

		transaction.close(type);
	}

	public RecyclingTransaction getLatestTransaction() {
		return transactions.isEmpty() ? null : transactions.get(transactions
				.size() - 1);
	}
	
	public List<RecyclingTransaction> getAllTransactions() {
		return new ArrayList<>(transactions);
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public double getTotalMoneyLeft() {
		return this.totalMoneyLeft;
	}

	public int gettotalCouponsLeft() {
		return this.totalCouponsLeft;
	}

	public boolean getMachineStatus() {
		return this.machineStatus;
	}

	public void empty() {
		Date date = new Date();
		lastEmptiedTimeList.add(date);
		totalWeight = 0;
	}

	public Set<String> getValidItems() {
		return new TreeSet<>(itemPrices.getMap().keySet());
	}

	public void removeItemType(String itemType) {
		itemPrices.removeItemType(itemType);
	}

	public void addItemType(String itemType, double price) {
		itemPrices.addItemType(itemType, price);
	}
	
	public static RecyclingMachine createFromJSONObject(JSONObject object) {
		JSONObject contents = object.getJSONObject("RecyclingMachine");
		JSONArray transArray = contents.getJSONArray("transactions");
		List<RecyclingTransaction> transactions = new ArrayList<>();

		for(int i=0; i< transArray.length(); i++) {
			RecyclingTransaction r = RecyclingTransaction
					.createFromJSONObject(transArray.getJSONObject(i));
			transactions.add(r);
		}
		List<Date> lastEmptyTimes = new ArrayList<>();
		JSONArray datesArray = contents.getJSONArray("lastEmptiedTimeList");
		for(int i=0; i< datesArray.length(); i++) {
			Date d = Date.from(Instant.ofEpochMilli(datesArray.getLong(i))); 
			lastEmptyTimes.add(d);
		}

		
		
		return new RecyclingMachine(
				contents.getInt("machineId"),
				contents.getString("machineLocation"),
				contents.getDouble("totalMoneyLeft"),
				contents.getInt("totalCouponsLeft"),
				contents.getBoolean("machineStatus"),
				lastEmptyTimes,
				transactions,
				Data.createFromJSONObject(contents.getJSONObject("itemPrices")));
	}

	
	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject();
		contents.put("machineId", machineId);
		contents.put("machineLocation", machineLocation);
		JSONArray transArray = new JSONArray();
		for(RecyclingTransaction r: transactions) {
			transArray.put(r.asJSONObject());
		}
		contents.put("transactions", transArray);
		JSONArray lastEmptyArray = new JSONArray();
		for(Date d: lastEmptiedTimeList) {
			lastEmptyArray.put(d.toInstant().toEpochMilli());
		}
		contents.put("lastEmptiedTimeList", lastEmptyArray);
		contents.put("totalCouponsLeft", totalCouponsLeft);
		contents.put("machineStatus", machineStatus);
		contents.put("totalMoneyLeft", totalMoneyLeft);
		contents.put("totalWeight", totalWeight);
		contents.put("itemPrices", itemPrices.asJSONObject());

		object.put("RecyclingMachine", contents);
		return object;
	}

}