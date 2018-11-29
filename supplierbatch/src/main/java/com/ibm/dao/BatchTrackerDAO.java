package com.ibm.dao;

import java.util.List;
import java.util.Map;

import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.vo.BatchTrackerVO;

public interface BatchTrackerDAO extends BaseDAO {

	/**
	 * @param fileName
	 * @throws BatchException
	 */
	public void saveAttachment(String fileName,StringBuffer filecontent) throws BatchException;
	
	
	/**
	 * @param supplierData
	 * @throws BatchException
	 */
	public void bulkInsert(List<Map<Object,Object>> supplierData,List<String> colNames) throws BatchException;
	
	
	/**
	 * @param batchTrackerVO
	 * @throws BatchException
	 */
	public void saveTrackingInfo(BatchTrackerVO batchTrackerVO) throws BatchException;
}
