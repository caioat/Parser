package com.ef.Parser;

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

import com.ef.Utils.Constants;
import com.ef.Utils.DatabaseManager;

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
				String[] splittedLogLine;
				logFileBufferedReader = new BufferedReader(new FileReader("access.log"));
				
			    for(String line; (line = logFileBufferedReader.readLine()) != null && finishedSuccessfully; )
			    {
			    	splittedLogLine = StringUtils.split(line, Constants.LOG_DATA_SEPARATOR);
			    	sqlStatement = "INSERT INTO WEBSERVER_ACCESS_PARSED_LOG VALUES '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_IP] +
										    			                       "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_TIMESTAMP] + 
										    			                       "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_REQUEST] +
										    			                       "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_STATUS] +
										    			                       "', '" + splittedLogLine[Constants.LOG_ARRAY_DATA_POS_USER_AGENT];
			    	
			    	finishedSuccessfully = dbManager.executeSqlStatement(sqlStatement);
			    	if(finishedSuccessfully)
			    	{
			    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			    	    Date parsedDate = dateFormat.parse(splittedLogLine[Constants.LOG_ARRAY_DATA_POS_TIMESTAMP]);
			    	    Timestamp timestamp = new Timestamp(parsedDate.getTime());
			    	    
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
					for(String key : summedLogDataMap.keySet()) 
					{
						if(summedLogDataMap.get(key) >= requestsThreshold && finishedSuccessfully)
						{
							System.out.println(key);
							
							sqlStatement = "INSERT INTO WEBSERVER_BLOCKED_IPS VALUES '" + key +
				                           "', 'Threshold exceeded during timeframe'";
							finishedSuccessfully = dbManager.executeSqlStatement(sqlStatement);
						}
						else
						{
							break; 
						}
					}
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
