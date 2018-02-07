package com.ef.Utils;

public class Constants 
{
	//Codewise constants
	public static final String ARGUMENT_STARTDATE = "startDate";
	public static final String ARGUMENT_DURATION = "duration";
	public static final String ARGUMENT_THRESHOLD = "threshold";
	
	public static final String DURATION_HOURLY = "hourly";
	public static final String DURATION_DAILY = "daily";
	public static final String STARTDATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
	
	public static final char LOG_DATA_SEPARATOR = '|';
	
	//Message constants
	public static final String ARGUMENT_STARTDATE_DESC = "Connection start date (format: " + Constants.STARTDATE_FORMAT + ")";
	public static final String ARGUMENT_DURATION_DESC = "Connection duration (" + Constants.DURATION_HOURLY + " or " + Constants.DURATION_DAILY + ")";
	public static final String ARGUMENT_THRESHOLD_DESC = "Requests threshold";
	
	public static final String ERROR_PREFIX = "Input parameters error"; 
	public static final String ERROR_INVALID_DURATION = "Duration parameter invalid:";
	public static final String ERROR_INVALID_THRESHOLD = "Threshold parameter is not an integer:";
}
