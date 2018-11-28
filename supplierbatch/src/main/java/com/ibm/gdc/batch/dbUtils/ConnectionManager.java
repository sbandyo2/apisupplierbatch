package com.ibm.gdc.batch.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.batch.utils.GDBatchUtils;

public class ConnectionManager {

	  private static String delimeter = "/";
	  private static String colon = ":";
	  
	/**
	 * @return
	 * @throws BatchException
	 */
	public static Connection getConnection(String appType) throws BatchException {
		Connection connnectionObj = null;
		String url = null;
		String port = "";
		String dbInstance = "";
		GDBatchUtils gdUtils = null;
		try {
			gdUtils = new GDBatchUtils();
			
			Class.forName(DBConstants.DB_DRIVER);
			//form the url
			port = gdUtils.getConnectionPropertiesValue(DBConstants.DB_PORT,appType);
			dbInstance = gdUtils.getConnectionPropertiesValue(DBConstants.DB_NAME,appType);
			
			url = DBConstants.DB_URL_PREFIX + gdUtils.getConnectionPropertiesValue(DBConstants.DB_SERVER,appType)
																	+ colon + port + delimeter + dbInstance;
			
			// Create the connection using the IBM Data Server Driver for JDBC
			// and SQLJ
			connnectionObj = DriverManager.getConnection(url, gdUtils.getConnectionPropertiesValue(DBConstants.DB_USER,appType), 
					gdUtils.getConnectionPropertiesValue(DBConstants.DB_PWD,appType));
			// Commit changes manually
			connnectionObj.setAutoCommit(false);

		} catch (ClassNotFoundException e) {
			throw new BatchException(e.getMessage());
		} catch (SQLException e) {
			throw new BatchException(e.getMessage());
		}

		return connnectionObj;
	}
	

	
	/**
	 * @param rs
	 * @param stmt
	 * @throws BatchException
	 */
	public static void closeConnectionResources(ResultSet rs,PreparedStatement stmt) throws BatchException {
		
		try {
			//close the result set
			if (rs != null)
				rs.close();
			
			// close the statement object
			if (stmt != null)
				stmt.close();
				
		} catch (SQLException e) {
			throw new BatchException(e.getMessage());
		}
	}
	/**
	 * @param connectionObj
	 * @throws BatchException
	 */
	public static void closeConnection(Connection connectionObj) throws BatchException {

		try {
			
			// commit and close the connection close the connection
			if (connectionObj != null){
				connectionObj.commit();
				connectionObj.close();
			}
				
		} catch (SQLException e) {
			throw new BatchException(e.getMessage());
		}
	}
	    
}
