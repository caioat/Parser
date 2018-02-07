package com.ef.Parser;

public class Main 
{
	public static void main(String[] args) 
	{
		CommandLineData.getInstance().parseCommandLine(args);

		LogParser logParser = new LogParser();
		logParser.execute();
	}
}
