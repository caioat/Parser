package com.ef.Parser;

import java.text.SimpleDateFormat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class CommandLineData 
{
	private String mStartDate = "";
	private String mDuration = "";
	private String mThreshold = "";
	
	public CommandLineData(String... args)
	{
		parseCommandLine(args);
	}
	
	private void parseCommandLine(String... args)
	{
		CommandLineParser parser = new DefaultParser();
		Options options = buildOptions();
		
		try
		{
		    CommandLine parsedCommandline = parser.parse(options, args);
		    validateInputArguments(parsedCommandline);
		}
		catch( ParseException | java.text.ParseException e )
		{
		    System.out.println(Constants.ERROR_PREFIX + " - " + e.getMessage());
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp("parser", options);
		    System.exit(0);
		}		
	}
	
	private Options buildOptions()
	{
		Options options = new Options();
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_STARTDATE)
										  .desc(Constants.ARGUMENT_STARTDATE_DESC)
										  .hasArg()
										  .argName("timestamp")
										  .required()
										  .build());
		
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_DURATION)
						                  .desc(Constants.ARGUMENT_DURATION_DESC)
						                  .hasArg()
						                  .argName("string")
						                  .required()
						                  .build());
		
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_THRESHOLD)
						                  .desc(Constants.ARGUMENT_THRESHOLD_DESC)
						                  .hasArg()
						                  .argName("integer")
						                  .required()
						                  .build());
		
		return options;
	}
	
	private void validateInputArguments(CommandLine parsedCommandline) throws java.text.ParseException, ParseException
	{
		if( parsedCommandline.hasOption(Constants.ARGUMENT_STARTDATE)) 
	    {
	    	mStartDate = parsedCommandline.getOptionValue(Constants.ARGUMENT_STARTDATE);
	    	SimpleDateFormat format = new java.text.SimpleDateFormat(Constants.STARTDATE_FORMAT);
	    	format.parse(mStartDate);
	    }
	    
	    if( parsedCommandline.hasOption(Constants.ARGUMENT_DURATION))
	    {
	    	mDuration = parsedCommandline.getOptionValue(Constants.ARGUMENT_DURATION);
	    	if(!StringUtils.equalsAny(mDuration, Constants.DURATION_HOURLY, Constants.DURATION_DAILY))
	    	{
	    		throw new ParseException(Constants.ERROR_INVALID_DURATION + " \"" + mDuration + "\"");
	    	}
	    }
	    
	    if( parsedCommandline.hasOption(Constants.ARGUMENT_THRESHOLD))
	    {
	    	mThreshold = parsedCommandline.getOptionValue(Constants.ARGUMENT_THRESHOLD);
	    	if(!StringUtils.isNumeric(mThreshold))
	    	{
	    		throw new ParseException(Constants.ERROR_INVALID_THRESHOLD + " \"" + mThreshold + "\"");
	    	}
	    }
	}
	
	public String getStartDate() 
	{
		return mStartDate;
	}
	
	public void setStartDate(String startDate) 
	{
		this.mStartDate = startDate;
	}
	
	public String getDuration() 
	{
		return mDuration;
	}
	
	public void setDuration(String duration) 
	{
		this.mDuration = duration;
	}
	
	public String getThreshold() 
	{
		return mThreshold;
	}
	
	public void setThreshold(String threshold) 
	{
		this.mThreshold = threshold;
	}
	
	
}
