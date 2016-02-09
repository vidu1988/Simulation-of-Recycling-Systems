package ecoRe.recyclingSystem;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilCalendarModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import ecoRe.recyclingSystem.RecyclingTransaction.TransactionType;

public class ViewStatsDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final List<RecyclingMachine> rcmList;
	private static final String[] Queries = { "Total number of items recycled",
			"Total coupons issued", "Total cash given by machine",
			"Total number of times machine was emptied",
			"Most used machine in time",
			"Number of items returned by all machines" };
	private List<JCheckBox> rcmsCheckBoxes;

	private interface TransactionListProcessor {
		double processItems(List<RecyclingTransaction> transactions);
	}

	public ViewStatsDialog(List<RecyclingMachine> rcms) {
		super();
		rcmList = rcms;

	}

	public void init() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("View statistics");
		setBounds(10, 10, (int) (screenSize.getWidth() * 0.9),
				(int) (screenSize.getHeight() * 0.9));
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		JPanel rcmSelectPanel = new JPanel();
		rcmSelectPanel.setLayout(new GridBagLayout());
		rcmSelectPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Select RCM"));
		getContentPane().add(rcmSelectPanel, c);
		rcmsCheckBoxes = new ArrayList<>();

		for (int index = 0; index < rcmList.size(); index++) {
			RecyclingMachine rcm = rcmList.get(index);
			JCheckBox checkBox = new JCheckBox();
			rcmsCheckBoxes.add(checkBox);
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = index;
			c.weightx = 1.0;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.fill = GridBagConstraints.HORIZONTAL;
			checkBox.setText(rcm.getName());
			checkBox.setActionCommand(rcm.getName());
			rcmSelectPanel.add(checkBox, c);
		}

		JPanel querySelectPanel = new JPanel();
		querySelectPanel.setLayout(new GridBagLayout());
		querySelectPanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Select Query"));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;

		// c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;

		getContentPane().add(querySelectPanel, c);
		for (int i = 0; i < Queries.length; i++) {
			prepareDateQueryPanel(Queries[i], querySelectPanel, i);
		}

		setVisible(true);
	}

	private JDatePickerImpl createDatePicker(Calendar cal) {
		JDatePanelImpl datePanel = new JDatePanelImpl(
				new UtilCalendarModel(cal), new Properties());
		return new JDatePickerImpl(datePanel, new DateComponentFormatter());
	}

	private void prepareDateQueryPanel(String queryName, JPanel rootPanel,
			final int index) {
		JPanel queryPanel = new JPanel();
		queryPanel.setBorder(new TitledBorder(new EtchedBorder(), queryName));
		queryPanel.setLayout(new GridLayout(0, 2, 20, 10));
		final JDatePickerImpl toDatePicker = createDatePicker(Calendar
				.getInstance());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		final JDatePickerImpl fromDatePicker = createDatePicker(calendar);
		queryPanel.add(new JLabel("From:"));
		queryPanel.add(fromDatePicker);
		queryPanel.add(new JLabel("To:"));
		queryPanel.add(toDatePicker);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = index;
		c.weightx = 0.5;
		c.ipady = 30;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		JButton queryStart = new JButton();
		queryStart.setText("View");
		queryStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<RecyclingMachine> selectedRcms = new ArrayList<>();
				if (rcmsCheckBoxes != null) {
					Set<String> selected = new HashSet<>();
					for (JCheckBox check : rcmsCheckBoxes) {
						if (check.isSelected()) {
							selected.add(check.getActionCommand());
						}
					}
					for (RecyclingMachine rcm : rcmList) {
						if (selected.contains(rcm.getName())) {
							selectedRcms.add(rcm);
						}
					}
				}
				querySelected(index, selectedRcms, (Calendar) fromDatePicker
						.getModel().getValue(), (Calendar) toDatePicker
						.getModel().getValue());
			}
		});
		queryPanel.add(queryStart);

		rootPanel.add(queryPanel, c);

	}

	private void querySelected(int queryIndex,
			List<RecyclingMachine> rcmsSelected, Calendar from, Calendar to) {
		if (!rcmsSelected.isEmpty()) {
			switch (queryIndex) {
			case 0:
				totalNoRecycledQuery(Queries[queryIndex], rcmsSelected, from,
						to);
				break;
			case 1:
				totalCouponsQuery(Queries[queryIndex], rcmsSelected, from, to);
				break;
			case 2:
				totalCashQuery(Queries[queryIndex], rcmsSelected, from, to);
				break;
			case 3:
				totalEmptiedQuery(Queries[queryIndex], rcmsSelected, from, to);
				break;
			case 4:
				mostUsedMachineQuery(Queries[queryIndex], rcmsSelected, from,
						to);
				break;
			case 5:
				itemTypeCountsQuery(Queries[queryIndex], rcmsSelected, from, to);
				break;
			}
		}
	}

	private void totalGeneralQuery(String title, List<RecyclingMachine> rcms,
			Calendar from, Calendar to, TransactionListProcessor processor,
			String xTitle, String yTitle) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, Map<String, List<RecyclingTransaction>>> rcmPerTrans = new TreeMap<>();
		for (RecyclingMachine rcm : rcms) {
			rcmPerTrans.put(rcm.getName(), getTransactionsPerDate(rcm));
		}
		for (String date : validDates(from, to)) {
			for (String rcm : rcmPerTrans.keySet()) {
				double val = 0;
				if (rcmPerTrans.get(rcm).containsKey(date)) {
					val = processor
							.processItems(rcmPerTrans.get(rcm).get(date));
				} else {
					val = processor
							.processItems(new ArrayList<RecyclingTransaction>());
				}
				dataset.addValue(val, rcm, date);
			}
		}

		JFreeChart chart = ChartFactory.createBarChart(title, xTitle, yTitle,
				dataset);
		ChartPanel c = new ChartPanel(chart, false);
		c.setVisible(true);
		JDialog dialog = new JDialog();
		dialog.setSize(1024, 760);
		dialog.getContentPane().add(c);
		dialog.setVisible(true);
		// getContentPane().removeAll();
	}

	private void totalNoRecycledQuery(String title,
			List<RecyclingMachine> rcms, Calendar from, Calendar to) {
		TransactionListProcessor processor = new TransactionListProcessor() {

			@Override
			public double processItems(List<RecyclingTransaction> transactions) {
				int itemCount = 0;
				for (RecyclingTransaction t : transactions) {
					itemCount += t.getItemList().size();
				}
				return itemCount;
			}
		};
		totalGeneralQuery(title, rcms, from, to, processor, "Dates",
				"Item count");
	}

	private void totalCouponsQuery(String title, List<RecyclingMachine> rcms,
			Calendar from, Calendar to) {
		TransactionListProcessor processor = new TransactionListProcessor() {

			@Override
			public double processItems(List<RecyclingTransaction> transactions) {
				int couponCount = 0;
				for (RecyclingTransaction t : transactions) {
					couponCount += (t.getTransactionType() == TransactionType.COUPON) ? 1
							: 0;
				}
				return couponCount;
			}
		};
		totalGeneralQuery(title, rcms, from, to, processor, "Dates",
				"Coupon count");
	}

	private void totalCashQuery(String title, List<RecyclingMachine> rcms,
			Calendar from, Calendar to) {
		TransactionListProcessor processor = new TransactionListProcessor() {

			@Override
			public double processItems(List<RecyclingTransaction> transactions) {
				double cash = 0;
				for (RecyclingTransaction t : transactions) {
					cash += t.getTotalCost();
				}
				return cash;
			}
		};
		totalGeneralQuery(title, rcms, from, to, processor, "Dates", "Cash $");
	}

	private void totalEmptiedQuery(String title, List<RecyclingMachine> rcms,
			Calendar from, Calendar to) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, Map<String, List<Date>>> rcmPerTrans = new TreeMap<>();
		for (RecyclingMachine rcm : rcms) {
			rcmPerTrans.put(rcm.getName(), emptiedTimePerDate(rcm));
		}
		for (String date : validDates(from, to)) {
			for (String rcm : rcmPerTrans.keySet()) {
				double val = 0;
				if (rcmPerTrans.get(rcm).containsKey(date)) {
					val = rcmPerTrans.get(rcm).get(date).size();
				}
				dataset.addValue(val, rcm, date);
			}
		}

		JFreeChart chart = ChartFactory.createBarChart(title, "Dates",
				"Number of times emptied", dataset);
		ChartPanel c = new ChartPanel(chart, false);
		c.setVisible(true);
		JDialog dialog = new JDialog();
		dialog.setSize(1024, 760);
		dialog.getContentPane().add(c);
		dialog.setVisible(true);
	}

	private void mostUsedMachineQuery(String title,
			List<RecyclingMachine> rcms, Calendar from, Calendar to) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, Map<String, List<RecyclingTransaction>>> rcmPerTrans = new TreeMap<>();
		for (RecyclingMachine rcm : rcms) {
			rcmPerTrans.put(rcm.getName(), getTransactionsPerDate(rcm));
		}
		for (String rcm : rcmPerTrans.keySet()) {
			double val = 0;
			for (String date : validDates(from, to)) {
				if (rcmPerTrans.get(rcm).containsKey(date)) {
					val += rcmPerTrans.get(rcm).get(date).size();
				}
			}
			dataset.addValue(val, rcm, rcm);
		}

		JFreeChart chart = ChartFactory.createBarChart(title, "RCMs",
				"Number of transactions", dataset);
		ChartPanel c = new ChartPanel(chart, false);
		c.setVisible(true);
		JDialog dialog = new JDialog();
		dialog.setSize(1024, 760);
		dialog.getContentPane().add(c);
		dialog.setVisible(true);
	}

	private void itemTypeCountsQuery(String title, List<RecyclingMachine> rcms,
			Calendar from, Calendar to) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<String, Map<String, List<RecyclingTransaction>>> rcmPerTrans = new TreeMap<>();
		for (RecyclingMachine rcm : rcms) {
			rcmPerTrans.put(rcm.getName(), getTransactionsPerDate(rcm));
		}
		for (String rcm : rcmPerTrans.keySet()) {
			Map<String, Integer> itemTypeCountMap = new TreeMap<>();

			// First per date find the transcations.
			// then accumulate the counts of transacations per item.
			for (String date : validDates(from, to)) {
				if (rcmPerTrans.get(rcm).containsKey(date)) {
					for (RecyclingTransaction t : rcmPerTrans.get(rcm)
							.get(date)) {
						for (Item item : t.getItemList()) {
							String itemType = item.getItemType();
							if (!itemTypeCountMap.containsKey(itemType)) {
								itemTypeCountMap.put(itemType, 0);
							}
							itemTypeCountMap.put(itemType,
									itemTypeCountMap.get(itemType) + 1);
						}
					}
				}
			}
			for (String itemType : itemTypeCountMap.keySet()) {
				dataset.addValue(itemTypeCountMap.get(itemType), rcm, itemType);
			}
		}

		JFreeChart chart = ChartFactory.createBarChart(title, "RCMs",
				"Number of Items", dataset);
		ChartPanel c = new ChartPanel(chart, false);
		c.setVisible(true);
		JDialog dialog = new JDialog();
		dialog.setSize(1024, 760);
		dialog.getContentPane().add(c);
		dialog.setVisible(true);
	}

	private List<String> validDates(Calendar from, Calendar to) {
		Calendar i = Calendar.getInstance();
		i.setTime(from.getTime());
		List<String> dateStrings = new ArrayList<String>();
		while (i.compareTo(to) <= 0) {
			dateStrings.add(dateOnlyString(i.getTime()));
			i.add(Calendar.DATE, 1);
		}
		return dateStrings;
	}

	private Map<String, List<RecyclingTransaction>> getTransactionsPerDate(
			RecyclingMachine rcm) {
		Map<String, List<RecyclingTransaction>> transPerDateMap = new HashMap<>();

		for (RecyclingTransaction t : rcm.getAllTransactions()) {
			String date = dateOnlyString(t.getStartTime());
			if (!transPerDateMap.containsKey(date)) {
				transPerDateMap.put(date, new ArrayList<>());
			}

			transPerDateMap.get(date).add(t);
		}
		return transPerDateMap;
	}

	private Map<String, List<Date>> emptiedTimePerDate(RecyclingMachine rcm) {
		Map<String, List<Date>> emptyPerDateMap = new HashMap<>();

		for (Date d : rcm.getLastEmptiedTimeList()) {
			String date = dateOnlyString(d);
			if (!emptyPerDateMap.containsKey(date)) {
				emptyPerDateMap.put(date, new ArrayList<>());
			}

			emptyPerDateMap.get(date).add(d);
		}
		return emptyPerDateMap;
	}

	private String dateOnlyString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
		return dateFormat.format(date);
	}
}
