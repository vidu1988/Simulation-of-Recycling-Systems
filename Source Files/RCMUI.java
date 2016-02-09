package ecoRe.recyclingSystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ecoRe.recyclingSystem.NewEcoRe.RCMObserver;
import ecoRe.recyclingSystem.RecyclingTransaction.TransactionType;

public class RCMUI {
	private final RecyclingMachine rcm;
	private final List<JComponent> allLabelsAndButtons; 
	private final JPanel topPanel;
	private final JLabel lblOnOff;
	private final JTextArea textRCMScreen;
	private final JProgressBar progressBar;
	private final JLabel lblItemSlot;
	public JLabel labelValidatingProduct;
	
	private final JPanel panelButtons;
	private final RCMObserver rcmObserver;
	private final ScheduledExecutorService progressBarSchedulor = new ScheduledThreadPoolExecutor(
			1);

	private static final String DEFAULT_ICON = "C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Eco-Re.png";

	private static ImageIcon getIconForType(String itemType) {
		if (itemType.equals(Data.ALUMINIUM)) {
			return new ImageIcon(
					"C:\\Java Practice\\EcoRe\\aluminium_packaging.jpg");
		}
		if (itemType.equals(Data.GLASS)) {
			return new ImageIcon("C:\\Java Practice\\EcoRe\\glass.jpg");
		}

		return new ImageIcon(DEFAULT_ICON);
	}

	/**
	 * @return the lblOnOff
	 */
	public JLabel getLblOnOff() {
		return lblOnOff;
	}

	public RCMUI(RecyclingMachine machine, RCMObserver rcmObserver) {
		rcm = machine;
		this.rcmObserver = rcmObserver;
		allLabelsAndButtons = new ArrayList<>();
		topPanel = new JPanel();
	
		topPanel.setBackground(Color.white);

		topPanel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "EcoRe RCM #"
				+ rcm.getMachineId(), TitledBorder.LEADING, TitledBorder.TOP,
				null, new Color(0, 0, 0)));
		// setPanelEnabled(panelRCM1, false);
		topPanel.setLayout(null);

		JPanel panelSlot = new JPanel();
		
		panelSlot.setBackground(Color.white);
		panelSlot.setLayout(null);
		panelSlot.setBorder(new TitledBorder(null, "Slots",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSlot.setBounds(10, 623, 673, 57);
		addToPanel(panelSlot, topPanel, allLabelsAndButtons);

		lblItemSlot = new JLabel("Item Slot");
		
		lblItemSlot.setBounds(25, 13, 160, 32);
		addToPanel(lblItemSlot, panelSlot, allLabelsAndButtons);

		JLabel lblMoneySlot = new JLabel("Money Slot");
		lblMoneySlot.setBounds(197, 13, 128, 32);
		addToPanel(lblMoneySlot, panelSlot, allLabelsAndButtons);

		JLabel lblReceiptSlot = new JLabel("Receipt Slot");
		lblReceiptSlot.setBounds(355, 13, 135, 32);
		addToPanel(lblReceiptSlot, panelSlot, allLabelsAndButtons);

		JLabel lblCouponSlot = new JLabel("Coupon Slot");
		lblCouponSlot.setBounds(502, 13, 142, 32);
		addToPanel(lblCouponSlot, panelSlot, allLabelsAndButtons);

		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBounds(12, 117, 707, 329);
		scrollPane.setBounds(12, 117, 900, 330);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		addToPanel(scrollPane, topPanel, allLabelsAndButtons);

		textRCMScreen = new JTextArea();
		scrollPane.setViewportView(textRCMScreen);
		textRCMScreen.setLineWrap(true);
		textRCMScreen.setEditable(false);
		textRCMScreen.setWrapStyleWord(true);
		textRCMScreen.setFont(new Font("Monospaced", Font.BOLD, 18));

		textRCMScreen.setBackground(Color.BLACK);
		textRCMScreen.setForeground(Color.white);
		allLabelsAndButtons.add(textRCMScreen);
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setMaximum(100);
		//JLabel labelValidatingProduct = new JLabel("Validating Product");
		
		labelValidatingProduct = new JLabel("Validating Product");
		//labelValidatingProduct.setBounds(369, 15, 111, 35);
		labelValidatingProduct.setBounds(241, 3, 176, 26);
		
		
		labelValidatingProduct.setVisible(false);
		
		addToPanel(labelValidatingProduct, topPanel, allLabelsAndButtons);
		
		JLabel labelRCMIcon = new JLabel("");
		labelRCMIcon.setIcon(new ImageIcon(
				"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Eco-Re.png"));
		labelRCMIcon.setBounds(13,15 ,104,100);
		addToPanel(labelRCMIcon, topPanel, allLabelsAndButtons);
		

		labelValidatingProduct.setLabelFor(progressBar);
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(false);
		progressBar.setForeground(new Color(34, 139, 34));
		//progressBar.setBounds(492, 25, 176, 25);
		progressBar.setBounds(225, 25, 176, 25);
		
		progressBar.setVisible(false);
		
		addToPanel(progressBar, topPanel, allLabelsAndButtons);

		panelButtons = new JPanel();
		panelButtons.setBorder(new TitledBorder(new EtchedBorder(),
				"Select Items"));
		GridLayout buttonGridLayout = new GridLayout(1, 0, 10, 10);
		panelButtons.setLayout(buttonGridLayout);
		panelButtons.setBackground(Color.white);

		panelButtons.setBounds(12, 450, 400, 150);

		addToPanel(panelButtons, topPanel, allLabelsAndButtons);
		
        JLabel lblSelectOptions = new JLabel("Select Your options");
		lblSelectOptions.setBounds(408, 417, 134, 31);
		addToPanel(lblSelectOptions, topPanel, allLabelsAndButtons);

		JButton buttonTransactionSummary = new JButton("Transaction Summary");
		buttonTransactionSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				
				textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
						+ "      " + new Date().toString() + "\n Location : "
						+ rcm.getMachineLocation());
				RecyclingTransaction recyclingTransaction = rcm
						.getLatestTransaction();
				if (recyclingTransaction == null
						|| recyclingTransaction.isClosed()) {
					
					textRCMScreen
							.append("\n\n\nNo current transactions in progress\n");
				} else {
					
					textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
							+ "      " + new Date().toString() + "\nLocation : "
							+ rcm.getMachineLocation());
					textRCMScreen.append(" \n\n Item Wise total weight \n ");
					textRCMScreen.append(recyclingTransaction
							.getItemWeightSummary());
					textRCMScreen.append(" \n\n Item Wise total Money Due:\n ");
					textRCMScreen.append(recyclingTransaction
							.getItemCostSummary());
				}

			}
		});
		
		// button_12.setBackground(SystemColor.menu);
		buttonTransactionSummary.setBounds(508, 447, 175, 25);
		addToPanel(buttonTransactionSummary, topPanel, allLabelsAndButtons);

		JButton buttonTotWeightEntered = new JButton("Total Weight Entered");
		buttonTotWeightEntered.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
						+ "      " + new Date().toString() + "\nLocation : "
						+ rcm.getMachineLocation());
				RecyclingTransaction recyclingTransaction = rcm
						.getLatestTransaction();
				if (recyclingTransaction == null
						|| recyclingTransaction.isClosed()) {
					textRCMScreen
							.append("\n\n\nNo current transactions in progress\n");
				} else {
					textRCMScreen.append("\n\n\nTotal Weight Entered : "
							+ recyclingTransaction.getTotalWeight() + " lb");
				}

			}
		});
		buttonTotWeightEntered.setBounds(508, 471, 175, 25);
		addToPanel(buttonTotWeightEntered, topPanel, allLabelsAndButtons);

		JButton btnTotalMoneyDue = new JButton("Total Money Due");
		btnTotalMoneyDue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
						+ "      " + new Date().toString() + "\nLocation : "
						+ rcm.getMachineLocation());

				RecyclingTransaction recyclingTransaction = rcm
						.getLatestTransaction();
				if (recyclingTransaction == null
						|| recyclingTransaction.isClosed()) {
					textRCMScreen
							.append("\n\nNo current transactions in progress\n");
				} else {
					textRCMScreen.append("\n\nTotal Money Due: " + "$ "
							+ recyclingTransaction.getTotalCost());
				}
			}
		});
		btnTotalMoneyDue.setBounds(508, 495, 175, 25);
		addToPanel(btnTotalMoneyDue, topPanel, allLabelsAndButtons);
		
		

		JButton buttonCollectMoneyReceipt = new JButton("Collect Money/Receipt");
		buttonCollectMoneyReceipt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
						+ "      " + new Date().toString() + "\nLocation : "
						+ rcm.getMachineLocation());
				RecyclingTransaction recyclingTransaction = rcm
						.getLatestTransaction();
				if (recyclingTransaction == null
						|| recyclingTransaction.isClosed()) {
					textRCMScreen
							.append("\n\nNo current transactions in progress\n");
				} else {
					textRCMScreen.append("\n\n\n\nProcessing Money....: "
							+ "$ " + recyclingTransaction.getTotalCost());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (recyclingTransaction.getTotalCost() <= rcm
							.getTotalMoneyLeft()) {
						textRCMScreen
								.append("\n\n Collect your money from money Slot");

						textRCMScreen
								.append("\n\n Thank You for Transaction!!");
						lblMoneySlot.setText("");
						lblMoneySlot
								.setIcon(new ImageIcon(
										"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\money.jpg"));
						lblReceiptSlot.setText("");
						lblReceiptSlot
								.setIcon(new ImageIcon(
										"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Receipt.png"));
						 
						rcm.endTransaction(TransactionType.MONEY);
					} else {
						textRCMScreen
								.append("\n\nNot enough money available. Please generate coupon equivalent to your transaction!");
						textRCMScreen
								.append("\n\nThank You for Transaction!!");						
						// To inform RMOS that no money is left in RCM
						rcmObserver.onRCMNoMoneyLeft(rcm);

						

					}

				}

			}
		});
		// button_13.setBackground(SystemColor.menu);
		
		buttonCollectMoneyReceipt.setBounds(508, 519, 175, 25);
		addToPanel(buttonCollectMoneyReceipt, topPanel, allLabelsAndButtons);

		

		JButton buttonPrintCoupon = new JButton("Print Coupon");
		buttonPrintCoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
						+ "      " + new Date().toString() + "\n Location : "
						+ rcm.getMachineLocation());
				RecyclingTransaction recyclingTransaction = rcm
						.getLatestTransaction();
				if (recyclingTransaction == null
						|| recyclingTransaction.isClosed()) {
					textRCMScreen
							.append("\nNo current transactions in progress\n");
				} else {
					textRCMScreen
							.append("\n\n\n\nPrinting coupon. Please Wait.......: Coupon Value: "
									+ "$ "
									+ recyclingTransaction.getTotalCost());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					textRCMScreen.append("\n\n Thank You for Transaction!!");
					lblCouponSlot.setText("");
					lblCouponSlot
							.setIcon(new ImageIcon(
									"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Receipt.png"));

					rcm.endTransaction(TransactionType.COUPON);
				}
			}
		});
		
		// button_15.setBackground(SystemColor.menu);
		buttonPrintCoupon.setBounds(508, 543, 175, 25);
		addToPanel(buttonPrintCoupon, topPanel, allLabelsAndButtons);

		lblOnOff = new JLabel("OFF");
		lblOnOff.setForeground(Color.RED);
		
		lblOnOff.setBounds(120, 100, 56, 16);
		addToPanel(lblOnOff, topPanel, allLabelsAndButtons);

		

	}

	/**
	 * @return the allLabelsAndButtons
	 */
	public List<JComponent> getAllLabelsAndButtons() {
		return allLabelsAndButtons;
	}

	private static void addToPanel(JComponent component, JPanel jPanel,
			List<JComponent> componentList) {
		if (componentList.indexOf(component) != -1) {
			throw new IllegalArgumentException("Item already present.");
		}
		jPanel.add(component);
		componentList.add(component);
		if (component instanceof JLabel) {
			JLabel label = (JLabel) component;
			label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		if (component instanceof JButton) {
			JButton button = (JButton) component;
			button.setFont(new Font("Tahoma", Font.PLAIN, 13));
		}
		component.setEnabled(false);
	}

	private JButton createRCMItemButton(String itemType) {
		ImageIcon icon = getIconForType(itemType);
		JButton btnItem = new JButton(itemType + " $"
				+ rcm.pricePerLbForItem(itemType) + "/lb");
		btnItem.setBackground(Color.white);

		btnItem.setIcon(icon);
		btnItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * When user clicks on an item button, that item is created and its weight is randomly generated */
				   animateProgressBarAndThen(new Runnable() {

					@Override
					public void run() {
						
						labelValidatingProduct.setVisible(true);
						Double itemPrice = rcm.pricePerLbForItem(itemType);
						/* Item price is null then item is not valid, or for the purpose of simulation we are randomly 
						discarding elements as invalid .*/
						if (itemPrice == null || (new Random().nextInt(5) < 2)) {
							textRCMScreen
									.append("\nUnknown Item Detected: Check Again!");
							lblItemSlot.setForeground(Color.RED);
							lblItemSlot.setText("InValid Item: Check Again");
							return;

						}

						Item item = new Item(itemType, itemPrice.doubleValue(),
								new Random().nextInt(10) + 1);
						RecyclingTransaction transaction = rcm
								.getLatestTransaction();
						if (transaction == null || transaction.isClosed()) {
							rcm.startNewTransaction();
							transaction = rcm.getLatestTransaction();
						}

						double totalWeight = transaction.getTotalWeight()
								+ item.getItemWeight();

						if (((rcm.getTotalWeight() + totalWeight) <= RecyclingMachine.maxRCMWeight)
								&& (rcm.getMachineStatus())) {
							transaction.addItem(item);

							// Displaying entered item detail on RCM Display
							// Screen
							textRCMScreen.append("\nItem :"
									+ item.getItemType() + "   Quantity :"
									+ item.getItemWeight() + "   Price : "
									+ item.getItemPrice()
									* item.getItemWeight() + "   Time: "
									+ new Date().toString());
							// Changing labels at Enter Item Slot, to depict
							// state
							// change
							lblItemSlot.setForeground(Color.green);
							lblItemSlot.setText("Valid Item: Enter Item");

						} else {
							textRCMScreen
									.append("\n\n\n Error: Cannot Enter more items. Machine is full.");
							btnItem.setEnabled(false);
							// Notify RMOS that machine is full and now  ready to get emptied.
							rcmObserver.onRCMFull(rcm);

						}
					} // run()
				});// Runnable
			} // ActionPerformed
		});
		addToPanel(btnItem, panelButtons, allLabelsAndButtons);
		btnItem.setHorizontalTextPosition(SwingConstants.CENTER);
		btnItem.setVerticalTextPosition(SwingConstants.BOTTOM);

		return btnItem;
	}

	public void setEnabled(boolean enabled) {
		rcm.setMachineStatus(enabled);
		for (JComponent c : allLabelsAndButtons) {
			c.setEnabled(enabled);
		}
		if (enabled) {
			lblOnOff.setText("ON");
			lblOnOff.setForeground(Color.GREEN);
			textRCMScreen.setText("Machine Id# " + rcm.getMachineId()
					+ "      " + new Date().toString() + "\n Location : "
					+ rcm.getMachineLocation());
		} else {
			lblOnOff.setText("OFF");
			lblOnOff.setForeground(Color.RED);
		}

	}

	public void updateItemButtons() {
		panelButtons.removeAll();
		for (String itemType : rcm.getValidItems()) {
			createRCMItemButton(itemType);
		}
		panelButtons.updateUI();
		if (rcm.getMachineStatus()) {
			setEnabled(true);
		}
	}

	private void animateProgressBarAndThen(Runnable finalRunnable) {
		setProgressValue(0);
		
		
		Runnable runnable = new Runnable() {
			public void run() {
				int value = progressBar.getValue();
				if (value < progressBar.getMaximum()) {
					setProgressValue(value + 1);
					
					progressBarSchedulor.schedule(this, 10,
							TimeUnit.MILLISECONDS);
				} else {
					finalRunnable.run();
				}
			}

		};
		SwingUtilities.invokeLater(runnable);
	}

	private void setProgressValue(int value) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				progressBar.setVisible(true);
				
				progressBar.setValue(value);

			}
		});

	}

	public JPanel getTopPane() {
		return topPanel;
	}
}
