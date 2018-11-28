package com.ibm.dao;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.JsonObject;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.vo.BaseVO;

public interface BaseDAO {

	
	/**
	 * @param dataStore
	 * @param docId
	 * @return
	 * @throws BatchException
	 */
	public Object findById(String dataStore,String docId) throws BatchException;
	/**
	 * @param dataStore
	 * @param obj
	 * @throws BatchException
	 */
	public void save(String dataStore, Object obj) throws BatchException;

	
	/**
	 * @param docId
	 * @param dataStore
	 * @param baseVO
	 * @return
	 * @throws BatchException
	 */
	public JsonObject doUpdate(String docId, String dataStore,BaseVO baseVO) throws BatchException;

	
	/**
	 * @param docId
	 * @param dataStore
	 * @param baseVO
	 * @throws BatchException
	 */
	public void delete(String docId, String dataStore,BaseVO baseVO) throws BatchException;

	
	/**
	 * @param datastore
	 * @param param
	 * @throws BatchException
	 */
	public void getSearchResults(Object datastore, String param) throws BatchException;
	
	/**
	 * @param datastore
	 * @param viewName
	 * @param viewIndex
	 * @return
	 * @throws BatchException
	 */
	public BigDecimal getMaxValue(String datastore, String viewName, String viewIndex) throws BatchException;
	
	
	/**
	 * @param datastore
	 * @param viewName
	 * @param viewIndex
	 * @return
	 * @throws BatchException
	 */
	public BigDecimal getSum(String datastore, String viewName, String viewIndex) throws BatchException;
	
	
	/**
	 * @param datastore
	 * @param viewName
	 * @param viewIndex
	 * @return
	 * @throws BatchException
	 */
	public BigDecimal getCount(String datastore, String viewName, String viewIndex) throws BatchException;
	
	/**
	 * @param datastore
	 * @return
	 * @throws BatchException
	 */
	public List<? extends Object> getAllRecords(String datastore) throws BatchException;
	
	
	
	/**
	 * @param datastore
	 * @param viewName
	 * @param viewIndex
	 * @param param
	 * @return
	 * @throws BatchException
	 */
	public BigDecimal getMaxWithParam(String datastore, String viewName, String viewIndex,Object[] param) throws BatchException;
	
	
	
	/**
	 * @param datastore
	 * @param param
	 * @param list
	 * @throws BatchException
	 */
	public  void getSearchWithoutScan(Object datastore, String param, List<Object> list) throws BatchException;
	
	
	/**
	 * @param datastore
	 * @param param
	 * @param colNames
	 * @throws BatchException
	 */
	public  void getSearchResultsWithOutPojo(Object datastore, String param,List<String> colNames) throws BatchException;
	
	
}
