package com.ibm.gdc.csv.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.gdc.batch.utils.BatchConstants;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.batch.utils.GDBatchUtils;

public class CSVDataTransformer {

	Logger logger = Logger.getLogger(CSVDataTransformer.class);
	
	private Map<String, List<Object[]>> dataMap = null;

	private List<Object[]>   filteredRowList = null;
	
	
	
	/**
	 * This method transforms the CSV data based on the fetch configurations and
	 * stores only the column which needs to be fetched in the collection
	 * 
	 * @param csvData
	 * @param fileList
	 * @return
	 * @throws BatchException 
	 */
	public Map<String, List<Object[]>> transformCSVData(Map<String, List<List<Map<String, String>>>> csvData,List<File> fileList) throws BatchException {
		
		Object[] rowDataCollection = null;
		String lookUpKey = null;
		String[] keyArray = null;
		String colKey = null;
		String[] colKeyArray = null;
		List<List<Map<String, String>>> keyMapData = null;
		List<Map<String, String>> rowData = null;
		String processFileName = null;
		List<Object[]> dataList = null;
		int objectIndex=0;
		
		try {
			lookUpKey = GDBatchUtils.getValue(BatchConstants.FILE_NAME_FILTER);
			
			for(File file : fileList){
			
				if (!GDBatchUtils.isNullOrEmpty(lookUpKey)) {
					
					keyArray = lookUpKey.split(BatchConstants.DELIMETER);

					for (String key : keyArray) {
						processFileName = file.getName();

						if(processFileName.contains(key)){
								
							colKey = GDBatchUtils.getValue(key + BatchConstants._FETCH_KEY);
							
							// initialize
							if(dataMap==null)
								dataMap = new LinkedHashMap<String, List<Object[]>>();
							
							dataList = new ArrayList<Object[]>();
							
							if(colKey!=null && colKey.contains(BatchConstants.DELIMETER)){
								
								colKeyArray = colKey.split(BatchConstants.DELIMETER);
								
								// get the data for the key
								keyMapData = csvData.get(processFileName);
								
									// get each row data
									if (keyMapData != null && !keyMapData.isEmpty()) {
										
										for(int rowIndex=0;rowIndex<keyMapData.size();rowIndex++){
											rowData = keyMapData.get(rowIndex);
											
											//initialize the object pointer for storing the data
											objectIndex = 0;
											rowDataCollection = new Object[colKeyArray.length];
											
											for (Map<String, String> row : rowData) {
												String mapKey = row.keySet().iterator().next();
												
												requiredColumn:
												for (String columnName : colKeyArray) {
													if(columnName.trim().equalsIgnoreCase(mapKey)){

														rowDataCollection[objectIndex]=row.get(columnName);
														objectIndex++;
														
														break requiredColumn;
													}
												}
											}
											
											dataList.add(rowDataCollection);
										}
										
									}
									
									
							}
							
							if (dataList != null && !dataList.isEmpty())
								dataMap.put(processFileName, dataList);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BatchException(e.getMessage());
		}

		return dataMap;
	}
	
	
	/**
	 * This method removes an item of an object array, this is required to
	 * remove the mapping column information which will be there in all the
	 * files to be parsed
	 * 
	 * @param index
	 * @param obj
	 * @return
	 */
	public Object[] removeObjectItem(int index,Object[] obj){
		ArrayList<Object> list = null;
		Object[] data = null;
		
		list = new ArrayList<Object>(Arrays.asList(obj));
		list.remove(index);
		
		data = new String[list.size()];
		list.toArray(data);
		
		return data;
	}

	/**
	 * @return the filteredRowList
	 */
	public List<Object[]> getFilteredRowList() {
		return filteredRowList;
	}

	/**
	 * @param filteredRowList the filteredRowList to set
	 */
	public void setFilteredRowList(List<Object[]> filteredRowList) {
		this.filteredRowList = filteredRowList;
	}
	
	

}
