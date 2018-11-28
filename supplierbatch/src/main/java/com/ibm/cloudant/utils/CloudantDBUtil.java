package com.ibm.cloudant.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.ibm.gdc.batch.utils.BatchConstants;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.batch.utils.GDBatchUtils;

public class CloudantDBUtil {
 
	
	/**
	 * Get the cloudant client
	 * 
	 * @return
	 * @throws BatchException
	 */
	public CloudantClient getCloudantContext() throws BatchException {

		CloudantClient cloudantObj = null;
		try {
			cloudantObj = ClientBuilder.url(new URL(GDBatchUtils.getConnectionConfig(BatchConstants.CLOUDANT_URI)))
					.username(GDBatchUtils.getConnectionConfig(BatchConstants.CLOUDANT_USER))
					.password(GDBatchUtils.getConnectionConfig(BatchConstants.CLOUDANT_PASSWORD)).build();

		} catch (MalformedURLException e) {
			throw new BatchException(e.getMessage());
		} catch (IOException e) {
			throw new BatchException(e.getMessage());
		}

		
		return cloudantObj;
	}

	
	/**
	 * Shut down the client builder
	 * 
	 * @return
	 * @throws BatchException
	 */
	public void closeConnection(CloudantClient cloudantClient) throws BatchException {

		try {
			if(cloudantClient!=null)
				cloudantClient.shutdown();

		}  catch (Exception e) {
			throw new BatchException(e.getMessage());
		}

		
		return ;
	}
	
	/**
	 * Check if Cloudant DB exist
	 * @param dbName
	 * @return
	 */
	public boolean isCloudantDBExist( String dbName,CloudantClient cloudantObj) {
		boolean isExist = false;

		// Get a List of all the databases this Cloudant account
		List<String> databases = cloudantObj.getAllDbs();
		for (String db : databases) {
			if(dbName.equalsIgnoreCase(db)){
				isExist = true;
				break;
			}
		}

		return isExist;
	}
	
	/**
	 * @param dbName
	 * @return
	 */
	public Database getDB(String dbName,CloudantClient cloudantObj){
		
		return cloudantObj.database(dbName, false);
	}
	
	
	/**
	 * @param dbName
	 */
	public void createDB(String dbName,CloudantClient cloudantObj){
		
		cloudantObj.createDB(dbName);
	}
}
