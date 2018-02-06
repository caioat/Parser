package com.ef.Parser;

public class Constants 
{
	//Codewise constants
	protected static final String ARGUMENT_STARTDATE = "startDate";
	protected static final String ARGUMENT_DURATION = "duration";
	protected static final String ARGUMENT_THRESHOLD = "threshold";
	
	protected static final String DURATION_HOURLY = "hourly";
	protected static final String DURATION_DAILY = "daily";
	protected static final String STARTDATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
	
	//Message constants
	protected static final String ARGUMENT_STARTDATE_DESC = "Connection start date (format: " + Constants.STARTDATE_FORMAT + ")";
	protected static final String ARGUMENT_DURATION_DESC = "Connection duration (" + Constants.DURATION_HOURLY + " or " + Constants.DURATION_DAILY + ")";
	protected static final String ARGUMENT_THRESHOLD_DESC = "Requests threshold";
	
	protected static final String ERROR_PREFIX = "Input parameters error"; 
	protected static final String ERROR_INVALID_DURATION = "Duration parameter invalid:";
	protected static final String ERROR_INVALID_THRESHOLD = "Threshold parameter is not an integer:";
}
