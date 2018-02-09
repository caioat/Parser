package com.ef;

public class Parser 
{
	public static void main(String[] args) 
	{
		CommandLineData.getInstance().parseCommandLine(args);

		LogParser logParser = new LogParser();
		logParser.execute();
	}
}
