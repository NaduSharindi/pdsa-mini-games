package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class TravelingSalesManView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int noOfCities = 10;
	private JToggleButton[] sourceCitiesBtn = null;
	private JToggleButton[] destinationCitiesBtn = null;
	private JLabel[][] weightsLbl = null;
	private JPanel mainPanel = null;
	private JPanel controlPanel = null;
	private JTextPane userSourceTxtBx = null;
	private JTextPane userDistanceTxtBx = null;
	private JTextArea userSelectedPathTxtBx = null;
	private JButton shortestPathBtn = null;
	private JTextPane calcSourceTxtBx = null;
	private JTextPane calcDistanceTxtBx = null;
	private JTextArea calcSelectedPathTxtBx = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TravelingSalesManView frame = new TravelingSalesManView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TravelingSalesManView() {
		setTitle("Traveling Sales Man Game");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(new Dimension(1024, 576));
		setPreferredSize(new Dimension(1024, 576));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
	}

	/**
	 * Initialize game components and layout
	 */
	public void initUI() {
		// initialize game components
		sourceCitiesBtn = new JToggleButton[noOfCities];
		destinationCitiesBtn = new JToggleButton[noOfCities];
		weightsLbl = new JLabel[noOfCities][noOfCities];
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		userSourceTxtBx = new JTextPane();
		userSourceTxtBx.setEditable(false);
		userDistanceTxtBx = new JTextPane();
		userDistanceTxtBx.setEditable(false);
		userSelectedPathTxtBx = new JTextArea();
		userSelectedPathTxtBx.setEditable(false);
		shortestPathBtn = new JButton("Find Shortest Path");
		calcSourceTxtBx = new JTextPane();
		calcSourceTxtBx.setEditable(false);
		calcDistanceTxtBx = new JTextPane();
		calcDistanceTxtBx.setEditable(false);
		calcSelectedPathTxtBx = new JTextArea();
		calcSelectedPathTxtBx.setEditable(false);

		// main panel initialization
		for (int i = 0; i < noOfCities; i++) {
			// Initialize source and destination cities buttons
			char cityName = (char) (i + 65);
			sourceCitiesBtn[i] = new JToggleButton("City " + cityName);
			sourceCitiesBtn[i].setName("" + cityName);
			destinationCitiesBtn[i] = new JToggleButton("City " + cityName);
			destinationCitiesBtn[i].setName("" + cityName);

			// initialize labels of weights
			for (int j = 0; j < noOfCities; j++) {
				char cityName2 = (char) (j + 65);
				weightsLbl[i][j] = new JLabel("" + cityName + cityName2);
				weightsLbl[i][j].setName("" + cityName + cityName2);
			}
		}

		// initialize game UI
		// placing source cities
		JPanel gridPanel = new JPanel(new GridLayout(noOfCities + 1, noOfCities));
		JPanel leftLblPanel = new JPanel(new GridLayout(noOfCities, 1));

		// assign toggle buttons to left panel
		for (int i = 0; i < noOfCities; i++) {
			leftLblPanel.add(sourceCitiesBtn[i]);
		}

		// assign JLabel for city distances and JToggle buttons for the last row
		for (int i = 0; i <= noOfCities; i++) {
			for (int j = 0; j < noOfCities; j++) {
				if (i == noOfCities) {
					gridPanel.add(destinationCitiesBtn[j]);
				} else {
					JPanel weightPanel = new JPanel();
					weightPanel.setLayout(new FlowLayout());
					weightPanel.add(weightsLbl[i][j]);
					gridPanel.add(weightPanel);
				}
			}
		}
		// add panels to the main panel
		mainPanel.add(gridPanel, BorderLayout.CENTER);
		mainPanel.add(leftLblPanel, BorderLayout.WEST);

		// control panel initialization
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(2, 2));
		userPanel.add(new JLabel("Source City"));
		userPanel.add(new JLabel("Distance"));
		userSourceTxtBx.setText("HELLO");
		userDistanceTxtBx.setText("WORLD");
		userPanel.add(userSourceTxtBx);
		userPanel.add(userDistanceTxtBx);
		JPanel userPath = new JPanel();
		userPath.add(userSelectedPathTxtBx);
		
		controlPanel.add(userPanel);
		controlPanel.add(userPath);
		// add main and control panels to content pane
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(controlPanel, BorderLayout.EAST);
	}
}
