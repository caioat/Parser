package com.ef.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public boolean connect(String url, String username, String password)
	{
		if(connection == null)
		{
			try 
			{
//				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(url, username, password);
				connection.setAutoCommit(false);
			}
			catch (SQLException | ClassNotFoundException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return true;
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
	
	public boolean executeSqlStatement(String statement)
	{
		Statement sqlStatement = null;
		ResultSet resultSet = null;
		
		try
		{
			sqlStatement = connection.createStatement();
			if(sqlStatement.execute(statement))
			{
				resultSet = sqlStatement.getResultSet();
			}
		}
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally 
		{
			if (resultSet != null) 
			{
		        try 
		        {
		        	resultSet.close();
		        } catch (SQLException sqlEx) { } // ignore

		        resultSet = null;
		    }

		    if (sqlStatement != null)
		    {
		        try 
		        {
		        	sqlStatement.close();
		        } catch (SQLException sqlEx) { } // ignore

		        sqlStatement = null;
		    }
		}
		return true;
	}
	
	public void commit()
	{
		if(connection != null)
		{
			try 
			{
				connection.commit();
			}
			catch (SQLException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void rollback()
	{
		if(connection != null)
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
