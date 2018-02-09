package com.ef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class LogParser
{
	Timestamp durationStartDate;
	Timestamp durationEndDate;
	int requestsThreshold;
	
	DatabaseManager dbManager;
	
	public LogParser()
	{
		translateInputParameters();
		dbManager = DatabaseManager.getInstance();
	}
	
	public void execute()
	{
		boolean finishedSuccessfully = true;
		BufferedReader logFileBufferedReader = null;
		Map<String,Integer> summedLogDataMap = new HashMap<String,Integer>();
		String sqlStatement = null;
		
		if(dbManager.connect(Constants.MYSQL_DATABASE_URL, Constants.MYSQL_USERNAME, Constants.MYSQL_PASSWORD))
		{
			try
			{
				int processedRowsCount = 0;
				String[] splittedLogLine;
				SimpleDateFormat dateFormat = null;
				Date parsedDate = null;
				Timestamp timestamp = null;
				
				logFileBufferedReader = new BufferedReader(new FileReader(CommandLineData.getInstance().getAccessLog()));
				
				System.out.println("Processing access log file, this can take some time due the filesize");
			    for(String line; (line = logFileBufferedReader.readLine()) != null && finishedSuccessfully; )
			    {
			    	splittedLogLine = StringUtils.split(line, Constants.LOG_DATA_SEPARATOR);
			    	
			    	dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    	    parsedDate = dateFormat.parse(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_TIMESTAMP]);
		    	    timestamp = new Timestamp(parsedDate.getTime());
		    	    
			    	sqlStatement = "INSERT INTO WEBSERVER_ACCESS_PARSED_LOG VALUES ('" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP] +
										    			                        "', '" + timestamp + 
										    			                        "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_REQUEST] +
										    			                        "', '" + Integer.parseInt(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_STATUS]) +
										    			                        "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_USER_AGENT] + "')";
			    	
			    	finishedSuccessfully = dbManager.executeSqlStatement(sqlStatement);
			    	if(finishedSuccessfully)
			    	{
			    	    if(timestamp.compareTo(durationStartDate) >= 0 && timestamp.compareTo(durationEndDate) < 0)
			    	    {
			    	    	if(summedLogDataMap.containsKey(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP]))
					    	{
			    	    		summedLogDataMap.replace(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP],
			    	    								 summedLogDataMap.get(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP]) + 1);
					    	}
			    	    	else
			    	    	{
			    	    		summedLogDataMap.put(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP], 1);
			    	    	}
			    	    }
			    	}
			    	
			    	processedRowsCount++;
			    	if((processedRowsCount % 10000) == 0)
			    	{
			    		System.out.println("Rows processed so far: " + processedRowsCount);
			    	}
			    }
			    System.out.println("Process finished, total of rows processed: " + processedRowsCount);
			} 
			catch (IOException | ParseException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				finishedSuccessfully = false;
			}
			finally
			{
				try
				{
					logFileBufferedReader.close();
				}
				catch (IOException e) 
				{
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
				if(finishedSuccessfully)
				{
					finishedSuccessfully = processThresholdExceededEntries(summedLogDataMap);
				}
				
				if(finishedSuccessfully)
				{
					dbManager.commit();
				}
				else
				{
					dbManager.rollback();
				}
				
				dbManager.disconnect();
			}
		}
	}

	private boolean processThresholdExceededEntries(Map<String, Integer> summedLogDataMap)
	{
		boolean ret = true;
		String sqlStatement;
		
		System.out.println("-----------------------");
		System.out.println("Threshold exceeded IPs:");
		System.out.println("-----------------------");
		for(String key : summedLogDataMap.keySet()) 
		{
			if(summedLogDataMap.get(key) >= requestsThreshold && ret)
			{
				System.out.println(key);
				
				sqlStatement = "INSERT INTO WEBSERVER_BLOCKED_IPS VALUES ('" + key +
		                       "', 'Threshold value " + requestsThreshold + " exceeded during timeframe [" + durationStartDate.toString() + " - " + durationEndDate.toString() + "]')";
				ret = dbManager.executeSqlStatement(sqlStatement);
			}
			else if(!ret)
			{
				break; 
			}
		}
		return ret;
	}
	
	private void translateInputParameters()
	{
		try 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
		    Date parsedDate = dateFormat.parse(CommandLineData.getInstance().getStartDate());
		    
		    durationStartDate = new Timestamp(parsedDate.getTime());
		    durationEndDate = new Timestamp(parsedDate.getTime());
		    
		    if(CommandLineData.getInstance().getDuration().equals(Constants.DURATION_HOURLY))
		    {
		    	durationEndDate.setTime(durationEndDate.getTime() + (((59 * 60) + 59) * 1000));
		    }
		    else
		    {
		    	durationEndDate.setTime(durationEndDate.getTime() + (((1439 * 60) + 59)* 1000));
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
