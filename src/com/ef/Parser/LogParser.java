package com.ef.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ef.Utils.Constants;

public class LogParser
{
	public LogParser()
	{
		
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
		    			    	
		    	if(summedLogDataMap.containsKey(splittedLogLine[0]))
		    	{
		    		//check timestamp and duration
		    		//count++ (or not or add to the list)
		    	}
		    }
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			for(String key : summedLogDataMap.keySet()) 
			{
			    //insert 2
			}
			
			if(finishedSuccessfully)
			{
				//commit
			}
			else
			{
				//rollback
			}
		}
	}
}
