package com.qibu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TJDBCManager {

	static String mUser = "root";
	static String mPass = "000000";
//	static String mUser = "start"; 
//	static String mPass = "z1111111";
	static String mUrl = "jdbc:mysql://localhost:3306/start?useUnicode=true&characterEncoding=utf8";

	public TJDBCManager()
	  {
		    try
		    {
		      Class.forName("com.mysql.jdbc.Driver");
		    }
		    catch (Exception e) {
		      System.out.println(e);
		    }
	  }

	  public static final TJDBCManager getInstance() {
	    return new TJDBCManager();
	  }

	  public static Connection getConnection() {
	    try {
	      return DriverManager.getConnection(mUrl, mUser, mPass); 
	      } catch (SQLException e) {
	    	  throw new RuntimeException("获取连接异常 ", e);
	    }
	   
	  }

}
