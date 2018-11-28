package com.ibm.daoImpl;

import java.util.List;
import java.util.Map;

import com.ibm.dao.BatchTrackerDAO;
import com.ibm.gdc.batch.utils.BatchConstants;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.vo.BatchTrackerVO;

public class BatchTrackerDAOImpl extends BaseDAOImpl implements BatchTrackerDAO {

	public void saveAttachment(String fileName,StringBuffer filecontent) throws BatchException {
		saveAttachment(BatchConstants.BATCH_TRACKER_FILES, fileName, filecontent);	
	}

	public void bulkInsert(List<Map<Object, Object>> supplierData)
			throws BatchException {
		
		//delete the db
		deleteDB(BatchConstants.SUPPLIER_INFO_DATASTORE);
		
		//persist the data
		saveWithDB(BatchConstants.SUPPLIER_INFO_DATASTORE, supplierData);
		
	}

	public void saveTrackingInfo(BatchTrackerVO batchTrackerVO)
			throws BatchException {
		
		save(BatchConstants.BATCH_TRACKER_DATASTORE, batchTrackerVO);
		
	}

	@Override
	public Class<? extends Object> getPojoClass() {
		
		return BatchTrackerVO.class;
	}

}
