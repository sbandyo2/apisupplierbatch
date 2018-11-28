package com.ibm.gdc.batch.dbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.gdc.batch.bean.SuppPartneringInfo;
import com.ibm.gdc.batch.utils.BatchConstants;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.dto.SupplierDto;


public class DBUtils {

	/**
	 * @param suppPartneringInfo
	 * @throws BatchException
	 * Load fresh data
	 */
	public static int loadSupppartNumber(List<SuppPartneringInfo> suppPartneringInfo ) throws BatchException{
		PreparedStatement stmt = null;
		String qry = "";
		Connection connectionObj = null;
		List<SuppPartneringInfo> suppPartneringInfoFinal  = null;
		try {
			//clear the previous data for fresh upload
			cleanData();
			
			//get connection for Monitor application
			connectionObj = ConnectionManager.getConnection(BatchConstants.monitor_app_type);
			long rowCount = Long.valueOf("1");
			
				
			//Parse list to remove duplicate entry
			Map<String,SuppPartneringInfo> suppPartneringInfoMap = new HashMap<String, SuppPartneringInfo>();
			for(SuppPartneringInfo supplier:suppPartneringInfo) {
				suppPartneringInfoMap.put(supplier.getLocationID(), supplier);
			}
			suppPartneringInfoFinal = new ArrayList<SuppPartneringInfo>(suppPartneringInfoMap.values());

			//TODO: perform the insertion logic here
			qry = "INSERT INTO MONITOR.TSUPP_PARTNERING_INFO"
					+ "(SUPP_PARTNER_ID, VendorID, LocationID, Name, City, Street, PostalCode, Region, Country, Phone, Fax, EmailAddress) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
			
			stmt = connectionObj.prepareStatement(qry);
			
			Iterator<SuppPartneringInfo> iterator = suppPartneringInfoFinal.iterator();
			while(iterator.hasNext()) {
				SuppPartneringInfo info = iterator.next();
				
				
				stmt.setLong(1, Long.valueOf("" + rowCount));
				stmt.setString(2, info.getVendorID());
				stmt.setString(3, info.getLocationID());
				stmt.setString(4, info.getName());
				stmt.setString(5, info.getCity());
				stmt.setString(6, info.getStreet());
				stmt.setString(7, info.getPostalCode());
				stmt.setString(8, info.getRegion());
				stmt.setString(9, info.getCountry());
				stmt.setString(10, info.getPhone());
				stmt.setString(11, info.getFax());
				stmt.setString(12, info.getEmailAddress());
				
				//add to the batch
				stmt.addBatch();
				
				rowCount++;
			}	
			
			stmt.executeBatch();
			connectionObj.commit();
		} catch (SQLException e) {
			throw new BatchException(e);
		} finally {
			// close the connection resource such as result set and prepared statement
			ConnectionManager.closeConnectionResources(null, stmt);
			//close connection
			ConnectionManager.closeConnection(connectionObj);
		}

		return  (suppPartneringInfoFinal!=null && !suppPartneringInfoFinal.isEmpty()?suppPartneringInfoFinal.size() : 0);
	}

	/**
	 * @param connectionObj
	 * @throws BatchException
	 * Delete old data 
	 */
	private static void cleanData() throws BatchException {
		Statement stmt = null;
		String qry = "";	
		Connection connectionObj = null;
		
		try {
			connectionObj = ConnectionManager.getConnection(BatchConstants.monitor_app_type);
			
			qry = "DELETE FROM MONITOR.TSUPP_PARTNERING_INFO";
			stmt = connectionObj.createStatement();
			
			stmt.executeUpdate(qry);
			
			connectionObj.commit();
		} catch (SQLException e) {
			throw new BatchException(e);
		} finally {
			// close the connection resource such as result set and prepared statement
			try {
				if(stmt !=null)
					stmt.close();
				
				ConnectionManager.closeConnection(connectionObj);
			} catch (SQLException e) {
				throw new BatchException(e);
			}
			
			
		}
	}


	/**
	 * @param suppPartneringInfo
	 * @throws BatchException
	 * Select all data
	 */
	public static Map<String,SupplierDto> SelectAllSupplierData(Connection connectionObj) throws BatchException{
		PreparedStatement stmt = null;
		String qry = "";
		ResultSet rs = null;
		Map<String,SupplierDto> resultMap = new HashMap<String, SupplierDto>();

		try {
			qry = "SELECT * FROM MONITOR.TSUPP_PARTNERING_INFO";
			stmt = connectionObj.prepareStatement(qry);
			rs = stmt.executeQuery();
			while(rs.next()) {
				SupplierDto dto = new SupplierDto();
				dto.setSuppPartnerId(rs.getLong("SUPP_PARTNER_ID"));
				dto.setLocationId(rs.getString("LocationID"));
				dto.setVendorId(rs.getString("VendorID"));
				resultMap.put(rs.getString("LocationID"),dto);
			}
		} catch (SQLException e) {
			throw new BatchException(e);
		} finally {
			// close the connection resource such as result set and prepared statement
			ConnectionManager.closeConnectionResources(null, stmt);
		}
		return resultMap;
	}
}
