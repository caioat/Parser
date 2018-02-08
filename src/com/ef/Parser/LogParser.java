package com.ef.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ef.Utils.Constants;

public class LogParser
{
	Timestamp durationStartDate;
	Timestamp durationEndDate;
	int requestsThreshold;
	
	public LogParser()
	{
		translateInputParameters();
	}
	
	public void execute()
	{
		boolean finishedSuccessfully = true;
		Map<String,Integer> summedLogDataMap = new HashMap<String,Integer>();
		
		try(BufferedReader br = new BufferedReader(new FileReader("access.log")))
		{
			String[] splittedLogLine;
			
		    for(String line; (line = br.readLine()) != null; )
		    {
		    	splittedLogLine = StringUtils.split(line, Constants.LOG_DATA_SEPARATOR);
		    	//insert here
		    	
		    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    	    Date parsedDate = dateFormat.parse(splittedLogLine[0]);
	    	    Timestamp timestamp = new Timestamp(parsedDate.getTime());
	    	    
	    	    if(timestamp.compareTo(durationStartDate) >= 0 && timestamp.compareTo(durationEndDate) < 0)
	    	    {
	    	    	if(summedLogDataMap.containsKey(splittedLogLine[1]))
			    	{
	    	    		summedLogDataMap.replace(splittedLogLine[1], summedLogDataMap.get(splittedLogLine[1]) + 1);
			    	}
	    	    	else
	    	    	{
	    	    		summedLogDataMap.put(splittedLogLine[1], 1);
	    	    	}
	    	    }
		    }
		} 
		catch (IOException | ParseException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			finishedSuccessfully = false;
		}
		finally
		{
			for(String key : summedLogDataMap.keySet()) 
			{
				if(summedLogDataMap.get(key) >= requestsThreshold)
				{
					//insert 2
				}
			}
			
			if(finishedSuccessfully)
			{
				//commit
			}
			else
			{
				//rollback
				System.exit(0);
			}
		}
	}
	
	private void translateInputParameters()
	{
		try 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    Date parsedDate = dateFormat.parse(CommandLineData.getInstance().getStartDate());
		    
		    durationStartDate = new Timestamp(parsedDate.getTime());
		    durationEndDate = new Timestamp(parsedDate.getTime());
		    
		    if(CommandLineData.getInstance().getDuration() == Constants.DURATION_HOURLY)
		    {
		    	durationEndDate.setTime(durationEndDate.getTime() + (((60 * 60) + 59)* 1000));
		    }
		    else
		    {
		    	durationEndDate.setTime(durationEndDate.getTime() + (((1440 * 60) + 59)* 1000));
		    }
		    
		    requestsThreshold = Integer.parseInt(CommandLineData.getInstance().getThreshold());
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
}
