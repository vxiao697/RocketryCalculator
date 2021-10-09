/***************************************************************************************
*	Title: JFreeChart
*	Author: Gilbert, D. and Object Refinery LTD.
*	Date: 2016
*	Code version: 1.0.19
*	Availability: http://www.jfree.org/jfreechart/
***************************************************************************************/
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.JComboBox;

public class RocketryCalc {

	private JFrame frame;
	private JTextField apogeeField;
	private JTextField timeToApogeeField;
	private JTextField flightTimeField;
	private JTextField descentSpeedField;
	private JLabel lblApogee;
	private JLabel lblTimeToApogee;
	private JLabel lblFlightTime;
	private JLabel lblDescentSpeed;
	private JFreeChart chart;
	private ChartPanel chPanel;
	private JTextField inputTextField;
	private JTextField regressionField;
	public static int heightLimit = 5;
	private String selectedGraph;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RocketryCalc window = new RocketryCalc();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public RocketryCalc() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//intialize frame
		frame = new JFrame("Rocketry Calculator");
		frame.setBounds(100, 100, 800, 635);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		//menu creation - settings thing
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Settings");
		menuBar.add(menu);
		JMenuItem itemValues = new JMenuItem("Change Values");
		itemValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				heightLimit = Integer.parseInt(JOptionPane.showInputDialog("Enter Height Limit"));
			}
		});
		menu.add(itemValues);
		frame.setJMenuBar(menuBar);
		
		//dropdown box that allows you to select which graph is displayede
		String[] graphList = { "Altitude", "Heat", "Voltage"};
		JComboBox graphSelectorBox = new JComboBox(graphList);
		graphSelectorBox.setToolTipText("Change Graph");
		graphSelectorBox.setBounds(692, 1, 86, 26);
		graphSelectorBox.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e)
		    {
		        String selectedItem = (String) graphSelectorBox.getSelectedItem();
		        selectedGraph = selectedItem;
		    }
		});
		frame.getContentPane().add(graphSelectorBox);
		
		//text fields/labels
		apogeeField = new JTextField();
		apogeeField.setBounds(0, 518, 115, 26);
		frame.getContentPane().add(apogeeField);
		apogeeField.setColumns(10);
		
		timeToApogeeField = new JTextField();
		timeToApogeeField.setBounds(161, 518, 115, 26);
		frame.getContentPane().add(timeToApogeeField);
		timeToApogeeField.setColumns(10);
		
		flightTimeField = new JTextField();
		flightTimeField.setBounds(333, 518, 115, 26);
		frame.getContentPane().add(flightTimeField);
		flightTimeField.setColumns(10);
		
		descentSpeedField = new JTextField();
		descentSpeedField.setBounds(503, 518, 115, 26);
		frame.getContentPane().add(descentSpeedField);
		descentSpeedField.setColumns(10);
		
		lblApogee = new JLabel("Apogee", SwingConstants.CENTER);
		lblApogee.setBounds(0, 493, 115, 29);
		frame.getContentPane().add(lblApogee);
		
		lblTimeToApogee = new JLabel("Time to Apogee", SwingConstants.CENTER);
		lblTimeToApogee.setBounds(161, 494, 115, 26);
		frame.getContentPane().add(lblTimeToApogee);
		
		lblFlightTime = new JLabel("Flight Time", SwingConstants.CENTER);
		lblFlightTime.setBounds(333, 497, 115, 20);
		frame.getContentPane().add(lblFlightTime);
		
		lblDescentSpeed = new JLabel("Descent Speed", SwingConstants.CENTER);
		lblDescentSpeed.setBounds(503, 497, 115, 20);
		frame.getContentPane().add(lblDescentSpeed);
		
		JLabel lblRegressionSumSquared = new JLabel("r^2", SwingConstants.CENTER);
		lblRegressionSumSquared.setBounds(662, 497, 116, 20);
		frame.getContentPane().add(lblRegressionSumSquared);
		
		regressionField = new JTextField();
		regressionField.setBounds(663, 518, 115, 26);
		frame.getContentPane().add(regressionField);
		regressionField.setColumns(10);
		
		//jchart
		double[][] in = {{0, 0}, {0, 0}};
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("launch1", in);
		chart = ChartFactory.createXYLineChart("", "time", "altitude", ds, PlotOrientation.VERTICAL, true, true, false);
		chPanel = new ChartPanel(chart);
		chPanel.setSize(778, 462);
		chPanel.setLocation(0, 30);
		frame.getContentPane().add(chPanel);
		
		inputTextField = new JTextField();
		inputTextField.setEditable(true);
		inputTextField.setBounds(252, 0, 146, 29);
		frame.getContentPane().add(inputTextField);
		inputTextField.setColumns(10);
		
		//import button 1 - straight from text
		JButton btnTextImport = new JButton("Import Text");
		btnTextImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Launch launch = null;
		
				Scanner sc = new Scanner(inputTextField.getText());
				launch = new Launch(sc, " ");
				
				createGraph(launch);
			}
		});
		btnTextImport.setBounds(123, 0, 124, 29);
		frame.getContentPane().add(btnTextImport);
		
		//import button 2 - from a csv
		JButton btnCSVImport = new JButton("Import CSV");
		btnCSVImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);

				Launch launch = null;
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					
					Scanner sc = null;
					try {
						sc = new Scanner(selectedFile).useDelimiter(",");
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, "invalid file");
					}
					
					launch = new Launch(sc, ",");
				}
				
				createGraph(launch);
			}
		});
		
		btnCSVImport.setBounds(0, 0, 115, 29);
		frame.getContentPane().add(btnCSVImport);
	}
	
	//launch processing - creates the charts and etc
	private void createGraph(Launch launch)
	{
		double[][] chartData;
		double[][] regressionData = {{0.0},{0.0}};
		
		if(selectedGraph == "Heat")
		{
			chartData = new double[2][launch.getHeat().getLength()];
			for(int i = 0; i < launch.getHeat().getLength(); i++)
			{
				chartData[0][i] = launch.getHeat().getTime(i);
				chartData[1][i] = launch.getHeat().getData(i);
			}
		}
		
		else if(selectedGraph == "Voltage")
		{
			chartData = new double[2][launch.getVoltage().getLength()];
			for(int i = 0; i < launch.getVoltage().getLength(); i++)
			{
				chartData[0][i] = launch.getVoltage().getTime(i);
				chartData[1][i] = launch.getVoltage().getData(i);
			}
		}
		
		else
		{
			chartData = new double[2][launch.getAltitude().getLength()];
			for(int i = 0; i < launch.getAltitude().getLength(); i++)
			{
				chartData[0][i] = launch.getAltitude().getTime(i);
				chartData[1][i] = launch.getAltitude().getData(i);
			}
			
			regressionData = new double[2][(int) (Math.round(launch.getFlightEndTime()) - Math.round(launch.getTimeToApogee()))];
			for(int i = (int) Math.round(launch.getTimeToApogee()); i < (Math.round(launch.getFlightEndTime())); i++)
			{
				regressionData[0][(i) - (int) Math.round(launch.getTimeToApogee())] = i;
				regressionData[1][(i) - (int) Math.round(launch.getTimeToApogee())] = (launch.getDescentSpeed().getSlope() * i) + launch.getDescentSpeed().getIntercept();
			}
		}
		
		apogeeField.setText(Double.toString(launch.getApogee()));
		timeToApogeeField.setText(Double.toString(launch.getTimeToApogee()));
		flightTimeField.setText(Double.toString(launch.getFlightTime()));
		descentSpeedField.setText(Double.toString(launch.getDescentSpeed().getSlope()).substring(0, 5));
		regressionField.setText(Double.toString(launch.getDescentSpeed().getRegressionSumSquares()).substring(0, 5));
		
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("launch1", chartData);
		ds.addSeries("regression1", regressionData);
		chart = ChartFactory.createXYLineChart("Rocketry Calc", "time", "altitude", ds, PlotOrientation.VERTICAL, true, true, false);
		chPanel.setChart(chart);
	}
}
