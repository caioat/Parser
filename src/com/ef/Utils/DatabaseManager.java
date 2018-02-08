package com.ef.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager 
{
	static DatabaseManager mInstance = null;
	
	Connection connection;
	
	private DatabaseManager()
	{
		connection = null;
	}
	
	public static DatabaseManager getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new DatabaseManager();
		}
		
		return mInstance;
	}
	
	public int connect()
	{
		if(connection == null)
		{
			try 
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(Constants.MYSQL_DATABASE_URL, Constants.MYSQL_USERNAME, Constants.MYSQL_PASSWORD);
				connection.setAutoCommit(false);
			}
			catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public void disconnect()
	{
		if(connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void commit()
	{
		
	}
	
	public void rollback()
	{
		
	}
}
