package com.ef.Parser;

public class Main 
{
	public static void main(String[] args) 
	{
		CommandLineData inputData = new CommandLineData(args);
		System.out.println(inputData.getStartDate());
		System.out.println(inputData.getDuration());
		System.out.println(inputData.getThreshold());
	}
}
