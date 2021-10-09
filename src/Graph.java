/*the graph object represents a single "graphable" unit/collection of data 
 *i.e. it has an array for time and an array for the data stored and can 
 *access both of those simultaneously - operations on these data points should 
 *be easier than having to construct very large 2 dimensional arrays*/
public class Graph {
	private double[] time;
	private double[] data;
	
	private int length;
	
	//constructor that takes a time array and a data array
	public Graph(double[] timeArray, double[] dataArray)
	{
		time = timeArray;
		data = dataArray;
		
		length = timeArray.length;
	}
	
	public int getLength()
	{
		return length;
	}
	//accessors that return the value at the index of the arrays
	public double getData(int index)
	{
		return data[index];
	}
	
	public double getTime(int index)
	{
		return time[index];
	}
	
	//accessors that get the value of one array that corresponds to the value of another array
	
	//get the data point that corresponds to a time point
	public double getData(double value)
	{
		int index = 0;
		
		for(int i = 0; i < time.length; i++)
		{
			if(time[i] == value)
			{
				index = i;
			}
		}
		
		return data[index];
	}
	
	//get time a data point was at
	public double getTime(double value)
	{
		int index = 0;
		
		for(int i = 0; i < data.length; i++)
		{
			if(data[i] == value)
			{
				index = i;
			}
		}
		
		return time[index];
	}
	
	//accesssors that give index based off of the value(i.e. a reverse array)
	public int getDataIndex(double value)
	{
		int index = 0;
		
		for(int i = 0; i < data.length; i++)
		{
			if(data[i] == value)
			{
				index = i;
			}
		}
		
		return index;
	}
	
	public int getTimeIndex(double value)
	{
		int index = 0;
		
		for(int i = 0; i < time.length; i++)
		{
			if(time[i] == value)
			{
				index = i;
			}
		}
		
		return index;
	}
}