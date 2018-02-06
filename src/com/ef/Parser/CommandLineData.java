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
		    System.out.println("Input parameters error - " + e.getMessage());
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp( "parser [options]", options );
		    System.exit(0);
		}		
	}
	
	private Options buildOptions()
	{
		Options options = new Options();
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_STARTDATE)
										  .desc("Connection start date (format: " + Constants.STARTDATE_FORMAT + ")")
										  .hasArg()
										  .required()
										  .build());
		
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_DURATION)
						                  .desc("Connection duration (" + Constants.DURATION_HOURLY + " or " + Constants.DURATION_DAILY + ")")
						                  .hasArg()
						                  .required()
						                  .build());
		
		options.addOption(Option.builder().longOpt(Constants.ARGUMENT_THRESHOLD)
						                  .desc("Number of requests threshold")
						                  .hasArg()
						                  .required()
						                  .build());
		
		return options;
	}
	
	private void validateInputArguments(CommandLine parsedCommandline) throws java.text.ParseException, ParseException
	{
		if( parsedCommandline.hasOption("startDate")) 
	    {
	    	mStartDate = parsedCommandline.getOptionValue("startDate");
	    	SimpleDateFormat format = new java.text.SimpleDateFormat(Constants.STARTDATE_FORMAT);
	    	format.parse(mStartDate);
	    }
	    
	    if( parsedCommandline.hasOption("duration"))
	    {
	    	mDuration = parsedCommandline.getOptionValue("duration");
	    	if(!StringUtils.equalsAny(mDuration, Constants.DURATION_HOURLY, Constants.DURATION_DAILY))
	    	{
	    		throw new ParseException("Duration parameter invalid: \"" + mDuration + "\"");
	    	}
	    }
	    
	    if( parsedCommandline.hasOption("threshold"))
	    {
	    	mThreshold = parsedCommandline.getOptionValue("threshold");
	    	if(!StringUtils.isNumeric(mThreshold))
	    	{
	    		throw new ParseException("Threshold parameter is not an integer: \"" + mThreshold + "\"");
	    	}
	    }
	}
	
	private void printErrorAndHelp(Exception e)
	{
		
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
