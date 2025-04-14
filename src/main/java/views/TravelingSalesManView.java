package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Scrollbar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import utils.constants.TravelingSalesManConstants;

public class TravelingSalesManView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JToggleButton[] sourceCitiesBtn = null;
	private JToggleButton[] destinationCitiesBtn = null;
	private JLabel[][] weightsLbl = null;
	private JPanel mainPanel = null;
	private JPanel controlPanel = null;
	private JTextPane userSourceTxtBx = null;
	private JTextPane userDistanceTxtBx = null;
	private JScrollPane userSelectedPathScroll;
	private JTextArea userSelectedPathTxtBx = null;
	private JButton shortestPathBtn = null;
	private JTextPane calcSourceTxtBx = null;
	private JTextPane calcDistanceTxtBx = null;
	private JScrollPane calcSelectedPathScroll;
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
		sourceCitiesBtn = new JToggleButton[TravelingSalesManConstants.NO_OF_SOURCE_CITIES];
		destinationCitiesBtn = new JToggleButton[TravelingSalesManConstants.NO_OF_DESTINATION_CITIES];
		weightsLbl = new JLabel[TravelingSalesManConstants.NO_OF_SOURCE_CITIES][TravelingSalesManConstants.NO_OF_DESTINATION_CITIES];
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(250, 200));
		controlPanel.setMaximumSize(new Dimension(250, 200));
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

		userSourceTxtBx = new JTextPane();
		userSourceTxtBx.setEditable(false);
		userSourceTxtBx.setPreferredSize(new Dimension(110, 30));
		userSourceTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		userDistanceTxtBx = new JTextPane();
		userDistanceTxtBx.setEditable(false);
		userDistanceTxtBx.setPreferredSize(new Dimension(110, 30));
		userDistanceTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		userSelectedPathTxtBx = new JTextArea(5, 20);
		userSelectedPathTxtBx.setEditable(false);
		userSelectedPathTxtBx.setLineWrap(true);
		userSelectedPathTxtBx.setMaximumSize(new Dimension(200, 100));
		userSelectedPathTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		userSelectedPathScroll = new JScrollPane(userSelectedPathTxtBx);
		userSelectedPathScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		shortestPathBtn = new JButton("Find Shortest Path");

		calcSourceTxtBx = new JTextPane();
		calcSourceTxtBx.setEditable(false);
		calcSourceTxtBx.setPreferredSize(new Dimension(110, 30));
		calcSourceTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		calcDistanceTxtBx = new JTextPane();
		calcDistanceTxtBx.setText("0");
		calcDistanceTxtBx.setEditable(false);
		calcDistanceTxtBx.setPreferredSize(new Dimension(110, 30));
		calcDistanceTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		calcSelectedPathTxtBx = new JTextArea(5, 20);
		calcSelectedPathTxtBx.setEditable(false);
		calcSelectedPathTxtBx.setMaximumSize(new Dimension(200, 100));
		calcSelectedPathTxtBx.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		calcSelectedPathTxtBx.setLineWrap(true);

		calcSelectedPathScroll = new JScrollPane(calcSelectedPathTxtBx);
		calcSelectedPathScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// main panel initialization
		// Initialize source city buttons
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
			char cityName = (char) (i + 65);
			sourceCitiesBtn[i] = new JToggleButton("City " + cityName);
			sourceCitiesBtn[i].setName("" + cityName);
		}

		// initialize destination buttons
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; i++) {
			char cityName = (char) (i + 65);
			destinationCitiesBtn[i] = new JToggleButton("City " + cityName);
			destinationCitiesBtn[i].setName("" + cityName);
		}

		// initialize labels of weights
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
			for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; j++) {
				String cityName = "" + (char) (i + 65) + (char) (j + 65);
				weightsLbl[i][j] = new JLabel(cityName);
				weightsLbl[i][j].setName(cityName);
			}
		}

		// initialize game UI
		// placing source cities, destination cities and weight labels
		JPanel gridPanel = new JPanel(new GridLayout(TravelingSalesManConstants.NO_OF_SOURCE_CITIES + 1,
				TravelingSalesManConstants.NO_OF_DESTINATION_CITIES + 1));

		// assign JLabels to the grid panel
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES + 1; i++) {
			for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES + 1; j++) {
				if (j == 0 && i != TravelingSalesManConstants.NO_OF_SOURCE_CITIES) {
					gridPanel.add(sourceCitiesBtn[i]);
				} else if (j == 0 && i == TravelingSalesManConstants.NO_OF_SOURCE_CITIES) {
					gridPanel.add(new JPanel());
				} else if (j > 0 && i != TravelingSalesManConstants.NO_OF_SOURCE_CITIES) {
					JPanel weightPnl = new JPanel(new BorderLayout());
					JPanel wrapperPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
					wrapperPnl.add(weightsLbl[i][j - 1], BorderLayout.CENTER);
					weightPnl.add(wrapperPnl);
					gridPanel.add(weightPnl);
				} else if (j > 0 && i == TravelingSalesManConstants.NO_OF_SOURCE_CITIES) {
					gridPanel.add(destinationCitiesBtn[j - 1]);
				}
			}
		}

		// add panels to the main panel
		mainPanel.add(gridPanel, BorderLayout.CENTER);

		// control panel initialization
		JPanel[] controlRowPnls = new JPanel[5];
		for (int i = 0; i < controlRowPnls.length; i++) {
			controlRowPnls[i] = new JPanel();
			controlRowPnls[i].setLayout(new FlowLayout(FlowLayout.LEFT));
		}

		JPanel sourceCityPnl = new JPanel();
		sourceCityPnl.setLayout(new BorderLayout());
		sourceCityPnl.add(new JLabel("Source City"), BorderLayout.NORTH);
		sourceCityPnl.add(userSourceTxtBx, BorderLayout.CENTER);
		controlRowPnls[0].add(sourceCityPnl);

		JPanel distancePnl = new JPanel();
		distancePnl.setLayout(new BorderLayout());
		distancePnl.add(new JLabel("Distance"), BorderLayout.NORTH);
		distancePnl.add(userDistanceTxtBx, BorderLayout.CENTER);
		controlRowPnls[0].add(distancePnl);

		controlRowPnls[0].setPreferredSize(new Dimension(250, 60));
		controlRowPnls[0].setMaximumSize(new Dimension(250, 60));

		JPanel userSelectedPathPnl = new JPanel();
		userSelectedPathPnl.setLayout(new BorderLayout());
		userSelectedPathPnl.add(new JLabel("User Selected Path"), BorderLayout.NORTH);
		userSelectedPathPnl.add(userSelectedPathScroll, BorderLayout.CENTER);
		controlRowPnls[1].add(userSelectedPathPnl);

		controlRowPnls[1].setPreferredSize(new Dimension(250, 150));
		controlRowPnls[1].setMaximumSize(new Dimension(250, 150));

		JPanel findBtnPnl = new JPanel();
		findBtnPnl.setLayout(new BorderLayout());
		findBtnPnl.add(shortestPathBtn);
		controlRowPnls[2].add(findBtnPnl);

		controlRowPnls[2].setPreferredSize(new Dimension(250, 50));
		controlRowPnls[2].setMaximumSize(new Dimension(250, 50));

		JPanel calcSourcePanel = new JPanel();
		calcSourcePanel.setLayout(new BorderLayout());
		calcSourcePanel.add(new JLabel("Source City"), BorderLayout.NORTH);
		calcSourcePanel.add(calcSourceTxtBx, BorderLayout.CENTER);
		controlRowPnls[3].add(calcSourcePanel);

		JPanel calcDistancePnl = new JPanel();
		calcDistancePnl.setLayout(new BorderLayout());
		calcDistancePnl.add(new JLabel("Distance"), BorderLayout.NORTH);
		calcDistancePnl.add(calcDistanceTxtBx, BorderLayout.CENTER);
		controlRowPnls[3].add(calcDistancePnl);

		controlRowPnls[3].setPreferredSize(new Dimension(250, 60));
		controlRowPnls[3].setMaximumSize(new Dimension(250, 60));

		JPanel computedPathPnl = new JPanel();
		computedPathPnl.setLayout(new BorderLayout());
		computedPathPnl.add(new JLabel("Computed Path"), BorderLayout.NORTH);
		computedPathPnl.add(calcSelectedPathScroll, BorderLayout.CENTER);
		controlRowPnls[4].add(computedPathPnl);

		controlRowPnls[4].setPreferredSize(new Dimension(250, 150));
		controlRowPnls[4].setMaximumSize(new Dimension(250, 150));

		controlPanel.add(controlRowPnls[0]);
		controlPanel.add(controlRowPnls[1]);
		controlPanel.add(controlRowPnls[2]);
		controlPanel.add(controlRowPnls[3]);
		controlPanel.add(controlRowPnls[4]);
		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(controlPanel, BorderLayout.EAST);
	}

	public JToggleButton[] getSourceCitiesBtn() {
		return sourceCitiesBtn;
	}

	public void setSourceCitiesBtn(JToggleButton[] sourceCitiesBtn) {
		this.sourceCitiesBtn = sourceCitiesBtn;
	}

	public JToggleButton[] getDestinationCitiesBtn() {
		return destinationCitiesBtn;
	}

	public void setDestinationCitiesBtn(JToggleButton[] destinationCitiesBtn) {
		this.destinationCitiesBtn = destinationCitiesBtn;
	}

	public JLabel[][] getWeightsLbl() {
		return weightsLbl;
	}

	public void setWeightsLbl(JLabel[][] weightsLbl) {
		this.weightsLbl = weightsLbl;
	}

	public JTextPane getUserSourceTxtBx() {
		return userSourceTxtBx;
	}

	public void setUserSourceTxtBx(JTextPane userSourceTxtBx) {
		this.userSourceTxtBx = userSourceTxtBx;
	}

	public JTextPane getUserDistanceTxtBx() {
		return userDistanceTxtBx;
	}

	public void setUserDistanceTxtBx(JTextPane userDistanceTxtBx) {
		this.userDistanceTxtBx = userDistanceTxtBx;
	}

	public JTextArea getUserSelectedPathTxtBx() {
		return userSelectedPathTxtBx;
	}

	public void setUserSelectedPathTxtBx(JTextArea userSelectedPathTxtBx) {
		this.userSelectedPathTxtBx = userSelectedPathTxtBx;
	}

	public JButton getShortestPathBtn() {
		return shortestPathBtn;
	}

	public void setShortestPathBtn(JButton shortestPathBtn) {
		this.shortestPathBtn = shortestPathBtn;
	}

	public JTextPane getCalcSourceTxtBx() {
		return calcSourceTxtBx;
	}

	public void setCalcSourceTxtBx(JTextPane calcSourceTxtBx) {
		this.calcSourceTxtBx = calcSourceTxtBx;
	}

	public JTextPane getCalcDistanceTxtBx() {
		return calcDistanceTxtBx;
	}

	public void setCalcDistanceTxtBx(JTextPane calcDistanceTxtBx) {
		this.calcDistanceTxtBx = calcDistanceTxtBx;
	}

	public JTextArea getCalcSelectedPathTxtBx() {
		return calcSelectedPathTxtBx;
	}

	public void setCalcSelectedPathTxtBx(JTextArea calcSelectedPathTxtBx) {
		this.calcSelectedPathTxtBx = calcSelectedPathTxtBx;
	}
}
