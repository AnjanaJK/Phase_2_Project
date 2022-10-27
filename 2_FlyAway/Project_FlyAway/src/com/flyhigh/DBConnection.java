package com.flyhigh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private Connection connection; // Encapsulation - Data Hiding
	
	public DBConnection(String dbURL, String user, String password) throws ClassNotFoundException, SQLException 
	{
		Class.forName("com.mysql.cj.jdbc.Driver"); // Register the Driver
		this.connection = DriverManager.getConnection(dbURL,user,password); // Establish and obtain the connection
	}
	
	public Connection getConnection()
	{
		return this.connection;
	}
	
	public void closeConnection() throws SQLException
	{
		if(this.connection!=null)
		{
			this.connection.close();
		}
	}

}
