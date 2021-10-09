/***************************************************************************************
*	Title: Apache Commons Math
*	Author: Apache Software Foundation
*	Date: 2016
*	Code version: 3.6.1
*	Availability: http://commons.apache.org/proper/commons-math/download_math.cgi
***************************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

import org.apache.commons.math3.stat.regression.SimpleRegression;



/*the launch class is meant to represent all the data collected from a single launch
 *i.e, it's supposed to have all the data collected from the launch(which would be 
 *stored inside a graph object. this allows eventual implemenation of multiple launch
 *processing, but as a starter is necessary because it's the easiest way to interact
 *with the GUi - putting all this in main would be a nightmare which requires a seperate
 *class to do all the calculation
 */
public class Launch {
	//VARIABLES
	//arrays that store all the values
	private Graph altitude;
	private Graph heat;
	private Graph voltage;
	
	//calculated values
	private double apogee;
	private double timeToApogee;
	private double flightTime;
	private SimpleRegression descentSpeed = null;
	
	//crucial internal values:
	//the time when the flight actually starts(i.e. liftoff rather than start recording)
	private double flightStartTime;
	//the time when the flight reaches 0(landing)
	private double flightEndTime;
	
	//CONSTRUCTORS
	//scanner from file imported
	public Launch(Scanner sc, String delimiter)
	{
		
		//put all it into a list - you can't find the length of the scanner without consuming the tokens
		int arraySize = 0;
		List<String> tokens = new LinkedList<String>();
		
		if(delimiter == ",")
		{
			while(sc.hasNextLine())
			{
				tokens.add(sc.nextLine());
				arraySize++;
			}
		}
		
		if(delimiter == " ")
		{
			while(sc.hasNext())
			{
				tokens.add(sc.next());
				arraySize++;
			}
			
			arraySize /= 4;
		}
		
		//initialize the arrays with the correct size
		double[] timeArray = new double[arraySize];
		double[] altitudeArray = new double[arraySize];
		double[] heatArray = new double[arraySize];
		double[] voltageArray = new double[arraySize];
		
		//put the values in the arrays
		for(int i = 0; i < tokens.size(); i++)
		{
			String[] temp;
			if(delimiter == ",")
			{
				temp = tokens.get(i).split(delimiter);
				timeArray[i] = Double.parseDouble(temp[0]);
				altitudeArray[i] = Double.parseDouble(temp[1]);
				heatArray[i] = Double.parseDouble(temp[2]);
				voltageArray[i] = Double.parseDouble(temp[3]);
			}
			
			else
			{
				if(i % 4 == 0)
				{
					timeArray[i / 4] = Double.parseDouble(tokens.get(i));
				}
				
				if(i % 4 == 1)
				{
					altitudeArray[i / 4] = Double.parseDouble(tokens.get(i));
				}
		
				if(i % 4 == 2)
				{
					heatArray[i / 4] = Double.parseDouble(tokens.get(i));
				}
				
				if(i % 4 == 3)
				{
					voltageArray[i / 4] = Double.parseDouble(tokens.get(i));
				}
			}
		}
		
		altitude = new Graph(timeArray, altitudeArray);
		heat = new Graph(timeArray, heatArray);
		voltage = new Graph(timeArray, voltageArray);
		
		calculateFlightTimes();
		calculateApogee();
		calculateTimeToApogee();
		calculateFlightTime();
		calculateDescentSpeed();
	}
	
	//MUTATORS
	//crucial internal value calculator
	private void calculateFlightTimes()
	{
		//checks for the first time with a significant altitude change - altimeter starts recording before the flight
		int i = 0;
		double change = 0;
		while(!(altitude.getData(i) < RocketryCalc.heightLimit ^ change < 40))
		{
			change = altitude.getData(i + 1) - altitude.getData(i);
			i++;
		}
		flightStartTime = altitude.getTime(i);
		System.out.println(flightStartTime);
		
		//checks for the first time altitude change is 0 after launch - altimeter doesn't stop as soon as it lands
		int j = i + 1;
		change = 0;
		while(altitude.getData(j) > RocketryCalc.heightLimit && !(change < -40))
		{
			j++;
		}
		flightEndTime = altitude.getTime(j);
		System.out.println(flightEndTime);
	}
	
	//calculates apogee - is private bc it should be called by constructors - accessors do stuff 
	private void calculateApogee()
	{	
		for(int i = 0; i < altitude.getLength(); i++)
		{
			if(altitude.getData(i) > apogee)
			{
				apogee = altitude.getData(i);
			}
		}
	}
	
	//same rationale as above
	private void calculateTimeToApogee()
	{
		double time = altitude.getTime(apogee);
		timeToApogee = time - flightStartTime;
	}
	
	private void calculateFlightTime()
	{
		flightTime = flightEndTime - flightStartTime;
	}
	
	private void calculateDescentSpeed()
	{
		descentSpeed = new SimpleRegression();
		
		System.out.println(altitude.getDataIndex(apogee));
		System.out.println(altitude.getTimeIndex(flightEndTime));
		
		for(int i = altitude.getDataIndex(apogee); i < altitude.getTimeIndex(flightEndTime); i++)
		{
			descentSpeed.addData(altitude.getTime(i), altitude.getData(i));
		}
	}
	
	//ACCESSORS
	public Graph getAltitude()
	{
		return altitude;
	}
	
	public Graph getHeat()
	{
		return heat;
	}
	
	public Graph getVoltage()
	{
		return voltage;
	}
	
	public double getApogee()
	{
		return apogee;
	}
	
	public double getTimeToApogee()
	{
		return timeToApogee;
	}
	
	public double getFlightTime()
	{
		return flightTime;
	}
	
	public int getFlightStartTime()
	{
		return (int)flightStartTime;
	}
	
	public int getFlightEndTime()
	{
		return (int)flightEndTime;
	}
		
	public SimpleRegression getDescentSpeed()
	{
		return descentSpeed;
	}
}
