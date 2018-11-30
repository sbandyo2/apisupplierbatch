package com.ibm.gdc.entry;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.batch.utils.FileProcessor;
import com.ibm.gdc.batch.utils.GDBatchUtils;
import com.ibm.gdc.csv.handler.CSVDataTransformer;
import com.ibm.gdc.csv.handler.CSVParser;
import com.ibm.gdc.data.handler.DataPreparer;
import com.ibm.vo.BatchTrackerVO;
import com.ibm.dao.BatchTrackerDAO;
import com.ibm.daoImpl.BatchTrackerDAOImpl;

public class EntryPoint {

	private static Logger logger = Logger.getLogger(EntryPoint.class);

	private static final String LOCATION_ID = "LocationID";

	public static void main(String[] args) {

		FileProcessor fileProcessor = null;
		List<File> filesForProcessing = null;
		CSVParser csvParser = null;
		Map<String, Map<String, List<String>>> csvRawMap = null;
		Map<String, List<List<Map<String, String>>>> csvCollection = null;
		CSVDataTransformer csvDataTransformer = null;
		Map<String, List<Object[]>> transformedList = null;
		BatchTrackerDAO batchDAO = null;
		List<String> indexColumn = null;
		String fileName = "";

		DataPreparer dataPreparer = null;
		List<Map<Object,Object>> suppPartneringInfos = null;
		long processedCount = 0;

		try {

			logger.info("Processing Started "+ GDBatchUtils.getCurrentTimestampToString() );

			fileProcessor = new FileProcessor();
			filesForProcessing = fileProcessor.pollFilesForProcessing();

			// do not proceed if there is no file present in the poll directory
			if (filesForProcessing == null || filesForProcessing.isEmpty())
				return;

			for(File file:filesForProcessing) {
				List<File> fileForProcessing = new ArrayList<File>();
				fileForProcessing.add(file);
				fileName = file.getName();
				//parse the csv after polling
				csvParser = new CSVParser();

				csvRawMap = csvParser.parseCSVFiles(fileForProcessing);
				csvCollection = csvParser.prepareRowDataPairs(csvRawMap);

				csvDataTransformer = new CSVDataTransformer();
				transformedList = csvDataTransformer.transformCSVData(csvCollection, fileForProcessing);

				dataPreparer = new DataPreparer();

				suppPartneringInfos = dataPreparer.prepareSuppPartnerInfor(transformedList);

				batchDAO = new BatchTrackerDAOImpl();
				indexColumn = new ArrayList<String>();
				indexColumn.add(LOCATION_ID);

				batchDAO.bulkInsert(suppPartneringInfos, indexColumn);

				// Save attachment
				processedCount = GDBatchUtils.archiveAndDelete(fileForProcessing);

				//Save tracking info
				saveTrakInfo(batchDAO,"SUCCESS",fileName,processedCount);

				logger.info("File " + file.getName() +" processed");
			}	

			logger.info("Processing Done"+ GDBatchUtils.getCurrentTimestampToString() + " for record (s) :"+suppPartneringInfos.size() );

		} catch (BatchException e) {
			logger.error(e.getMessage());
			try {
				if(filesForProcessing!=null && !filesForProcessing.isEmpty())
					//Save tracking info
					GDBatchUtils.archiveAndDeleteForError(filesForProcessing);
				saveTrakInfo(batchDAO,"FAILURE",fileName,processedCount);
			} catch (BatchException e1) {
				logger.error(e.getMessage());
			}
		}catch (Exception e) {
			//catching system exceptions if any
			logger.error(e.getMessage());
		}

	}

	/**
	 * @param status
	 */
	private static void saveTrakInfo(BatchTrackerDAO batchDAO, String status, String fileName, long count) {
		BatchTrackerVO batchTrackerVO = null;
		
		
		try {
			batchTrackerVO = new BatchTrackerVO();
			batchTrackerVO.setFileName(fileName);
			batchTrackerVO.setRecordCount(count);
			batchTrackerVO.setStatus(status);
			batchTrackerVO.setDate(GDBatchUtils.getDate());
			batchTrackerVO.setDateTs(Instant.now().toString());	
			
			batchDAO.saveTrackingInfo(batchTrackerVO);
		}catch(BatchException be) {
			logger.error(be.getMessage());
		}
		
	}
}
