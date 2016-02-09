package ecoRe.recyclingSystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewEcoRe {
	private JFrame frmEcoReRecycling;
	private JTextArea textRMOSScreen;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JPanel panelTemplateActionRCM;

	private JPanel panelRMOSButtons;
	private JPanel panelRMOSScreen;
	private CardLayout cardLayoutRMOS;
	private JTabbedPane tabbedPane;
	private final List<RecyclingMachine> allRCMs = new ArrayList<>();
	private final Map<RecyclingMachine, RCMActivator> rcmToActivatorMap = new HashMap<>();

	private final RCMObserver rcmObserver = new RCMObserver() {

		@Override
		public void onRCMNoMoneyLeft(RecyclingMachine rcm) {
			textRMOSScreen.append("\n\n\nRecycling machine: " + rcm.getName()
					+ " has no money left.\n");

		}

		@Override
		public void onRCMFull(RecyclingMachine rcm) {
			textRMOSScreen.append("\n\n\nRecycling machine: " + rcm.getName()
					+ " is full.\n");
		}
	};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException e) {
		    // handle exception
		} catch (ClassNotFoundException e) {
		    // handle exception
		} catch (InstantiationException e) {
		    // handle exception
		} catch (IllegalAccessException e) {
		    // handle exception
		}*/
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewEcoRe window = new NewEcoRe(RMOSStateSerializer.readStateFromFile());
					window.frmEcoReRecycling
							.addWindowListener(new WindowListener() {

								@Override
								public void windowOpened(WindowEvent arg0) {

								}

								@Override
								public void windowIconified(WindowEvent arg0) {

								}

								@Override
								public void windowDeiconified(WindowEvent arg0) {

								}

								@Override
								public void windowDeactivated(WindowEvent arg0) {

								}

								@Override
								public void windowClosing(WindowEvent arg0) {
									try {
										RMOSStateSerializer.saveState(window);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

								@Override
								public void windowClosed(WindowEvent arg0) {
								}

								@Override
								public void windowActivated(WindowEvent arg0) {

								}
							});
					window.frmEcoReRecycling.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewEcoRe(List<RecyclingMachine> recyclingMachines) {
		initialize(recyclingMachines);
	}

	private void initialize(List<RecyclingMachine> recyclingMachines) {
		initializeUI();
		for(RecyclingMachine rcm: recyclingMachines) {
			configureRCMPane(rcm);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeUI() {
		frmEcoReRecycling = new JFrame();
		frmEcoReRecycling.getContentPane().setBackground(Color.white);
		frmEcoReRecycling.getContentPane().setForeground(Color.BLACK);
		frmEcoReRecycling.setTitle("Eco Recycling Systems");
		frmEcoReRecycling
				.setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage(
								"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Eco-Re.png"));
		// logo_1857073_web.jpg
		//frmEcoReRecycling.setBounds(100, 100, 1357, 818);
		frmEcoReRecycling.setResizable(true);
		frmEcoReRecycling.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEcoReRecycling.getContentPane().setMinimumSize(new Dimension(1357, 818));
		frmEcoReRecycling.setSize(1357, 818);
		frmEcoReRecycling.getContentPane().setBackground(Color.white);
		
		frmEcoReRecycling.getContentPane().setLayout(new GridBagLayout());

		JLabel lblNewLabel = new JLabel("Welcome To EcoRe Monitoring Systems");
		lblNewLabel.setFont(new Font("Monotype Corsiva", Font.BOLD
				| Font.ITALIC, 40));
		lblNewLabel.setBackground(Color.white);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 0.05;
        c.weightx = 1.0;
		//lblNewLabel.setBounds(459, 0, 631, 46);
		frmEcoReRecycling.getContentPane().add(lblNewLabel, c);
		JPanel contentPanel = new JPanel();
		c= new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;

		frmEcoReRecycling.getContentPane().add(contentPanel,c);
		contentPanel.setBackground(Color.white); 
		contentPanel.setLayout(new GridLayout(1,2,0,0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 35, 736, 723);
		contentPanel.add(tabbedPane);
		JPanel panelRMOS = new JPanel();
		panelRMOS.setBackground(Color.white); 
		panelRMOS.setBorder(new TitledBorder(null, "EcoRe RMOS",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRMOS.setBounds(760, 41, 579, 706);
		contentPanel.add(panelRMOS);
		panelRMOS.setLayout(null);

		panelRMOSScreen = new JPanel();
		panelRMOSScreen.setBackground(Color.white);
		panelRMOSScreen.setVisible(false);
		panelRMOSScreen.setBounds(12, 199, 622, 507);
		panelRMOS.add(panelRMOSScreen);
		panelRMOSScreen.setLayout(null);

		JScrollPane scrollPaneRMOS = new JScrollPane();
		scrollPaneRMOS.setSize(600, 225);
		//scrollPaneRMOS.setSize(700, 400);
		scrollPaneRMOS.setLocation(10, 0);
		

		scrollPaneRMOS
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneRMOS
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		panelRMOSScreen.add(scrollPaneRMOS);

		textRMOSScreen = new JTextArea();
		scrollPaneRMOS.setViewportView(textRMOSScreen);
		textRMOSScreen.setEnabled(false);
		textRMOSScreen.setLineWrap(true);
		textRMOSScreen.setEditable(false);
		textRMOSScreen.setWrapStyleWord(true);
		textRMOSScreen.setFont(new Font("Monospaced", Font.BOLD, 18));

		textRMOSScreen.setForeground(Color.white);
		textRMOSScreen.setBackground(Color.BLACK);
		
		//textRMOSScreen.setBounds(30, 0, 650, 405);

		// panelRMOSScreen.add(textRMOSScreen);
		// textRMOSScreen.setColumns(10);

		panelRMOSButtons = new JPanel();
		panelRMOSButtons.setVisible(false);
		
		panelRMOSButtons.setBackground(Color.white);
		panelRMOSButtons.setBounds(10, 301, 534, 195);
		
		panelRMOSScreen.add(panelRMOSButtons);
		cardLayoutRMOS = new CardLayout();
		panelRMOSButtons.setLayout(cardLayoutRMOS);

		prepareRCMTemplateForm();

		prepareAddNewRCMForm();
		prepareActivateRCMForm();
		prepareDeleteRCMForm();
		prepareRCMCheckerForm();
		prepareEmptyRCMForm();
		prepareChangeItemRCMForm();
		prepareViewStats();

		userNameField = new JTextField();
		userNameField.setBounds(288, 75, 189, 30);
		panelRMOS.add(userNameField);
		userNameField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(288, 116, 189, 30);
		panelRMOS.add(passwordField);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(201, 69, 77, 43);
		panelRMOS.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(201, 116, 71, 30);
		panelRMOS.add(lblPassword);

		JButton btnLogin = new JButton("Login");
		

		JButton Logoutbtn = new JButton("Logout");
		
		Logoutbtn.setVisible(false);
		Logoutbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userNameField.setText("");
				passwordField.setText("");
				setVisible(true, lblUsername, lblPassword, userNameField,
						passwordField, btnLogin);
				setVisible(false, Logoutbtn, panelRMOSScreen);
			}
		});
		Logoutbtn.setEnabled(true);
		Logoutbtn.setBounds(533, 177, 89, 23);
		panelRMOS.add(Logoutbtn);
		
		JLabel lblLoginError = new JLabel("");
		lblLoginError.setVisible(false);
		lblLoginError.setFont(new Font("Monospaced", Font.BOLD, 14));
		lblLoginError.setForeground(Color.RED);
		lblLoginError.setBounds(182, 45, 347, 16);
		panelRMOS.add(lblLoginError);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Administrator obj = new Administrator("ecore", "santaclara");

				String user = userNameField.getText();
				String pass = String.valueOf(passwordField.getPassword());
				if (user.equals(obj.getUsername()) &&
						pass.equals(obj.getPassword())) {
					setVisible(false, lblUsername, lblPassword, userNameField,
							passwordField, btnLogin);
					setVisible(true, Logoutbtn, panelRMOSScreen);
					lblLoginError.setVisible(false);
					panelRMOSScreen.setVisible(true);
					textRMOSScreen
							.setText("Welcome To Eco-Re Monitoring Station");
				} else{
					lblLoginError.setVisible(true);
					lblLoginError.setText("Unable to login! Enter again");
					userNameField.setText("");
					passwordField.setText("");
					
					
				}
					
			}
		});

		btnLogin.setBounds(216, 163, 97, 25);
		panelRMOS.add(btnLogin);

		JLabel label_9 = new JLabel("");
		label_9.setBackground(Color.white);
		label_9.setBounds(10, 22, 104, 113);
		panelRMOS.add(label_9);
		label_9.setIcon(new ImageIcon(
				"C:\\Java Practice\\EcoRe\\src\\ecoRe\\recyclingSystem\\Eco-Re.png"));

		label_9.setLabelFor(panelRMOS);
		
		
	}

	private void configureRCMPane(RecyclingMachine rcm) {
		allRCMs.add(rcm);
		final String rcmName = rcm.getName();
		RCMUI rcmUI = new RCMUI(rcm, rcmObserver);
		tabbedPane.addTab(rcmName, null, rcmUI.getTopPane(), null);
		rcmUI.updateItemButtons();

		final RCMActivator rcmActivator = new RCMActivator(rcmUI);
		rcmToActivatorMap.put(rcm, rcmActivator);
	}

	private void prepareAddNewRCMForm() {
		JPanel panelAddNewRCM = new JPanel();
		
		panelAddNewRCM.setBackground(Color.white);
		final String key = "newRCMPanel";
		panelRMOSButtons.add(panelAddNewRCM, key);

		JButton btnAddNewRCM = new JButton("Add New RCM");
		
		
		btnAddNewRCM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayoutRMOS.show(panelRMOSButtons, key);
				setVisible(true, panelRMOSButtons);
			}
		});

		btnAddNewRCM.setBounds(40, 238, 124, 23);
		panelRMOSScreen.add(btnAddNewRCM);
		panelAddNewRCM.setLayout(new GridBagLayout());
		panelAddNewRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Add New RCM"));

		JPanel panelForm = new JPanel();

		panelAddNewRCM.add(panelForm);

		panelForm.setLayout(new GridLayout(0, 2, 10, 10));

		String[] fields = { "Machine Id", "Location", "Total money",
				"Total  coupons" };
		Map<String, JTextField> fieldToText = new HashMap<>();
		for (String field : fields) {
			JLabel l = new JLabel(field, JLabel.TRAILING);
			panelForm.add(l);
			JTextField textField = new JTextField(10);
			l.setLabelFor(textField);
			panelForm.add(textField);
			fieldToText.put(field, textField);
		}

		JButton btnSubmitRCMDetail = new JButton("Submit");
		
		
		panelForm.add(btnSubmitRCMDetail);
		btnSubmitRCMDetail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> vals = new ArrayList<>();
				for (String field : fields) {
					vals.add(fieldToText.get(field).getText());
				}
				Map<String, Object> result = validateNewRCMEntry(vals.get(0),
						vals.get(1), vals.get(2), vals.get(3));
				if (result.containsKey("ERRORS")) {
					textRMOSScreen
							.append("\nErrors encountered while adding Recycling machine\n");
					List<String> errors = (List<String>) result.get("ERRORS");
					for (String error : errors) {
						textRMOSScreen.append(error + "\n");
					}
				} else {
					RecyclingMachine r = (RecyclingMachine) result.get("RCM");
					configureRCMPane(r);
					textRMOSScreen
							.append("\nRecycling machine successfully added.\n");
					for(JTextField t: fieldToText.values()) {
						t.setText("");
					}
				}

			}

		});

		// machineid, location, total money, total coupons.

	}

	private Map<String, Object> validateNewRCMEntry(String machineId,
			String location, String totalMoney, String totalCoupons) {
		machineId = machineId.trim();
		Map<String, Object> result = new HashMap<>();
		List<String> errors = new ArrayList<>();
		int numMachineId = -1;
		try {
			numMachineId = Integer.parseInt(machineId);
			if (numMachineId <= 0) {
				errors.add("Invalid machine id: " + machineId
						+ " is not positive.");
			}
			for (RecyclingMachine r : allRCMs) {
				if (r.getMachineId() == numMachineId) {
					errors.add("Invalid machine id: " + numMachineId
							+ " already exists.");
					break;
				}
			}
		} catch (NumberFormatException n) {
			errors.add("Invalid machine id: " + machineId);
		}

		location = location.trim();
		if (location.isEmpty()) {
			errors.add("Invalid location: location is empty");
		}

		totalMoney = totalMoney.trim();
		double totalMoneyVal = -1;
		try {
			totalMoneyVal = Double.parseDouble(totalMoney);
			if (totalMoneyVal <= 0) {
				errors.add("Invalid total money: " + totalMoney
						+ " is not positive.");
			}
		} catch (NumberFormatException n) {
			errors.add("Invalid total money: " + totalMoney);
		}

		totalCoupons = totalCoupons.trim();
		int totalCouponsVal = -1;
		try {
			totalCouponsVal = Integer.parseInt(totalCoupons);
			if (totalCouponsVal <= 0) {
				errors.add("Invalid total coupons: " + totalCoupons
						+ " is not positive.");
			}
		} catch (NumberFormatException n) {
			errors.add("Invalid total coupons: " + totalCoupons);
		}

		if (errors.isEmpty()) {
			RecyclingMachine r = new RecyclingMachine(numMachineId, location,
					totalMoneyVal, totalCouponsVal);
			result.put("RCM", r);
		} else {
			result.put("ERRORS", errors);
		}

		return result;
	}

	private void prepareRCMTemplateForm() {
		panelTemplateActionRCM = new JPanel();
		
		panelTemplateActionRCM.setBackground(Color.white);
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				""));
		String key = "panelTemplateActionRCM";
		panelRMOSButtons.add(panelTemplateActionRCM, key);
	}

	private void setTemplateAction(String borderTitle,
			CheckBoxSelectedAction checkBoxAction) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				borderTitle));

		for (int index = 0; index < allRCMs.size(); index++) {
			RecyclingMachine rcm = allRCMs.get(index);
			JCheckBox chkbxrcm = new JCheckBox(rcm.getName());
			panelTemplateActionRCM.add(chkbxrcm);
			final int tabIndex = index;
			chkbxrcm.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					checkBoxAction.onCheckBoxSelected(tabIndex,
							e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}
		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);
	}

	private void prepareActivateRCMForm() {
		CheckBoxSelectedAction checkBoxAction = new CheckBoxSelectedAction() {

			@Override
			public void onCheckBoxSelected(int index, boolean selected) {
				RecyclingMachine rcm = allRCMs.get(index);

				if (selected) {
					rcmToActivatorMap.get(rcm).activateMachine();
					textRMOSScreen.append("\n" + rcm.getName()
							+ " is activated");
				} else {
					rcmToActivatorMap.get(rcm).deactivateMachine();
					textRMOSScreen.append("\n" + rcm.getName()
							+ " is not activated");
				}

			}
		};

		prepareForm("Activate RCM", checkBoxAction, 163, 238, 125, 23);
	}

	private void prepareEmptyRCMForm() {
		CheckBoxSelectedAction checkBoxAction = new CheckBoxSelectedAction() {

			@Override
			public void onCheckBoxSelected(int index, boolean selected) {
				RecyclingMachine rcm = allRCMs.get(index);

				if (selected) {
					rcm.empty();
					if (rcm.getMachineStatus()) {
						// In case machine was disabled.
						rcmToActivatorMap.get(rcm).activateMachine();
					}
					textRMOSScreen
							.append("\n" + rcm.getName() + " is emptied.");
				}
			}
		};

		prepareForm("Empty RCM", checkBoxAction, 342, 267, 125, 23);
	}

	private void prepareChangeItemRCMForm() {
		CheckBoxSelectedAction checkBoxAction = new CheckBoxSelectedAction() {

			@Override
			public void onCheckBoxSelected(int index, boolean selected) {
				RecyclingMachine rcm = allRCMs.get(index);

				if (selected) {
					setFormForItemActions(rcm);
				}
			}
		};

		prepareForm("Item/Value Change", checkBoxAction, 191, 267, 152, 23);
	}
	
	private void prepareViewStats() {
		JButton button = new JButton("View Statistics");
		
		button.setBounds(283, 238, 125, 23);
		panelRMOSScreen.add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewStatsDialog dialog = new ViewStatsDialog(allRCMs);
				dialog.init();
			}
		});

	}

	private void setFormForItemActions(RecyclingMachine rcm) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Add/Edit Items: " + rcm.getName()));

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 0, 10));
		String[] actions = { "Add Item", "Delete Item", "Update Item" };
		for (int i = 0; i < actions.length; i++) {
			JButton button = new JButton();
			buttonPanel.add(button);
			button.setText(actions[i]);
			final int index = i;
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					switch (index) {
					case 0:
						clearFormAndShowAddItemForm(rcm);
						break;
					case 1:
						clearFormAndShowDeleteItemForm(rcm);
						break;
					case 2:
						clearFormAndShowUpdateItemForm(rcm);
						break;
					default:
						throw new IllegalStateException();
					}

				}
			});

		}
		panelTemplateActionRCM.add(buttonPanel);
		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);

	}

	private void clearFormAndShowDeleteItemForm(RecyclingMachine rcm) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Delete Item: " + rcm.getName()));
		JPanel deleteItemPanel = new JPanel(new GridLayout(0, 1, 0, 10));
		ButtonGroup group = new ButtonGroup();
		for (String item : rcm.getValidItems()) {
			JRadioButton button = new JRadioButton(item);
			button.setActionCommand(item);
			deleteItemPanel.add(button);
			group.add(button);
		}
		JButton submit = new JButton("Submit");
		
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonModel button = group.getSelection();

				if (button != null) {
					String itemTypeToDelete = button.getActionCommand();
					RCMUI rcmUI = rcmToActivatorMap.get(rcm).getMachine();
					rcm.removeItemType(itemTypeToDelete);
					rcmUI.updateItemButtons();
					clearFormAndShowDeleteItemForm(rcm);
				}
			}
		});
		deleteItemPanel.add(submit);
		panelTemplateActionRCM.add(deleteItemPanel);
		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);
	}

	private void clearFormAndShowUpdateItemForm(RecyclingMachine rcm) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Update Item: " + rcm.getName()));

		JPanel updateItemPanel = new JPanel(new GridLayout(0, 1, 0, 10));

		ButtonGroup group = new ButtonGroup();
		for (String item : rcm.getValidItems()) {
			JRadioButton button = new JRadioButton(item);
			button.setActionCommand(item);
			updateItemPanel.add(button);
			group.add(button);
		}
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonModel button = group.getSelection();

				if (button != null) {
					String itemTypeToUpdate = button.getActionCommand();
					displayUpdateItemForm(rcm, itemTypeToUpdate);
				}
			}
		});
		updateItemPanel.add(submit);
		panelTemplateActionRCM.add(updateItemPanel);

		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);
	}

	private void displayUpdateItemForm(RecyclingMachine rcm, String itemType) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Update Item: " + rcm.getName() + " Item: " + itemType));
		JPanel updateItemPanel = new JPanel(new GridLayout(0, 2, 20, 10));
		updateItemPanel.add(new JLabel("Item type"));
		updateItemPanel.add(new JLabel(itemType));
		updateItemPanel.add(new JLabel("Old price per lb"));
		JTextField oldPrice = new JTextField(Double.toString(rcm
				.pricePerLbForItem(itemType)));
		updateItemPanel.add(oldPrice);
		oldPrice.setEditable(false);
		updateItemPanel.add(new JLabel("New price per lb"));
		JTextField newPrice = new JTextField();
		newPrice.setColumns(10);
		updateItemPanel.add(newPrice);

		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double newPriceVal = -1;
				try {
					newPriceVal = Double.parseDouble(newPrice.getText());
					if (newPriceVal <= 0) {
						textRMOSScreen.append("\n Invalid value for price: "
								+ newPrice.getText());
					}
				} catch (NumberFormatException n) {
					textRMOSScreen.append("\n Invalid value for price: "
							+ newPrice.getText());
				}

				if (newPriceVal > 0) {
					rcm.addItemType(itemType, newPriceVal);
					RCMUI rcmUI = rcmToActivatorMap.get(rcm).getMachine();
					rcmUI.updateItemButtons();
					textRMOSScreen.append("\n Price for : " + itemType
							+ " updated from " + oldPrice.getText() + " to "
							+ newPriceVal + " per lb.");
				}
				clearFormAndShowUpdateItemForm(rcm);
			}
		});
		updateItemPanel.add(submit);
		panelTemplateActionRCM.add(updateItemPanel);

		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);
	}

	private void clearFormAndShowAddItemForm(RecyclingMachine rcm) {
		panelTemplateActionRCM.removeAll();
		String key = "panelTemplateActionRCM";
		panelTemplateActionRCM.setBorder(new TitledBorder(new EtchedBorder(),
				"Add Item: " + rcm.getName() + ": Item Details"));
		JPanel addItemPanel = new JPanel(new GridLayout(0, 2, 20, 10));
		addItemPanel.add(new JLabel("Item type"));
		JTextField itemTypeText = new JTextField();
		addItemPanel.add(itemTypeText);
		addItemPanel.add(new JLabel("Price per lb"));
		JTextField newPrice = new JTextField();
		addItemPanel.add(newPrice);

		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double newPriceVal = -1;
				String itemType = itemTypeText.getText().trim();
				try {
					newPriceVal = Double.parseDouble(newPrice.getText());
					if (newPriceVal <= 0) {
						textRMOSScreen.append("\n Invalid value for price: "
								+ newPrice.getText());
					}
				} catch (NumberFormatException n) {
					textRMOSScreen.append("\n Invalid value for price: "
							+ newPrice.getText());
				}

				if (newPriceVal > 0 && !itemType.isEmpty()) {
					rcm.addItemType(itemType, newPriceVal);
					RCMUI rcmUI = rcmToActivatorMap.get(rcm).getMachine();
					rcmUI.updateItemButtons();
					textRMOSScreen.append("\nAdd new " + itemType
							+ " with price: " + newPriceVal + " per lb, to "
							+ rcm.getName());
				}
				clearFormAndShowAddItemForm(rcm);
			}
		});
		addItemPanel.add(submit);
		panelTemplateActionRCM.add(addItemPanel);

		panelTemplateActionRCM.updateUI();
		cardLayoutRMOS.show(panelRMOSButtons, key);
		setVisible(true, panelRMOSButtons);
	}

	private void prepareDeleteRCMForm() {
		final CheckBoxSelectedAction checkBoxAction = new CheckBoxSelectedAction() {

			@Override
			public void onCheckBoxSelected(int index, boolean selected) {
				if (selected) {
					RecyclingMachine rcm = allRCMs.get(index);

					tabbedPane.removeTabAt(index);
					rcmToActivatorMap.remove(rcm);
					allRCMs.remove(rcm);
					textRMOSScreen.append("\n" + rcm.getName() + " is deleted");
					setTemplateAction("Delete RCM", this);

				}
			}
		};
		prepareForm("Delete RCM", checkBoxAction, 408, 238, 125, 23);
	}

	private void prepareRCMCheckerForm() {
		final CheckBoxSelectedAction checkBoxAction = new CheckBoxSelectedAction() {

			@Override
			public void onCheckBoxSelected(int index, boolean selected) {
				if (selected) {
					RecyclingMachine rcm = allRCMs.get(index);
					textRMOSScreen
							.append("\n############################################\n");
					textRMOSScreen.append("\n Status for " + rcm.getName()
							+ "\n");
					textRMOSScreen
							.append("############################################\n");
					textRMOSScreen.append("Machine Id:" + rcm.getMachineId()
							+ "\n");
					textRMOSScreen.append("Machine activated:"
							+ rcm.getMachineStatus() + "\n");
					textRMOSScreen.append("Total weight:"
							+ rcm.getTotalWeight() + "\n");
					textRMOSScreen.append("Total money left:"
							+ rcm.getTotalMoneyLeft() + "\n");
					textRMOSScreen.append("Last emptied time:"
							+ rcm.getLastEmptiedTime() + "\n");
					textRMOSScreen.append("Total coupons left:"
							+ rcm.gettotalCouponsLeft() + "\n");
					//textRMOSScreen.append("Total coupons left:"
							//+ rcm.asJSONObject() + "\n");
					textRMOSScreen
							.append("############################################\n");

				}
			}
		};
		prepareForm("Check RCM Status", checkBoxAction, 58, 267, 150, 23);
	}

	private void prepareForm(String buttonName,
			CheckBoxSelectedAction checkBoxAction, int buttonX, int buttonY,
			int buttonWidth, int buttonHeight) {
		JButton button_1 = new JButton(buttonName);

		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTemplateAction(buttonName, checkBoxAction);
				setVisible(true, panelRMOSButtons);
			}
		});
		button_1.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
		panelRMOSScreen.add(button_1);
	}

	public JSONObject asJSONObject() {
		JSONObject object = new JSONObject();
		JSONObject contents = new JSONObject();
		JSONArray allRcmArray = new JSONArray();
		for (RecyclingMachine rcm : allRCMs) {
			allRcmArray.put(rcm.asJSONObject());
		}
		contents.put("allRcms", allRcmArray);
		object.put("RMOS", contents);
		return object;
	}
	
	private static void setVisible(boolean visible, JComponent... components) {
		for (JComponent c : components) {
			c.setVisible(visible);
		}
	}

	private interface CheckBoxSelectedAction {
		void onCheckBoxSelected(int index, boolean selected);
	}

	public interface RCMObserver {
		// Function which indicates that machine is full
		void onRCMFull(RecyclingMachine recyclingMachine);
		// Function which indicates that no money is left
		void onRCMNoMoneyLeft(RecyclingMachine recyclingMachine);
	}
}
