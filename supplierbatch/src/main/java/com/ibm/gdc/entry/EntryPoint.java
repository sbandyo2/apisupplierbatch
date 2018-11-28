package com.ibm.gdc.entry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.gdc.batch.bean.SuppPartneringInfo;
import com.ibm.gdc.batch.dbUtils.DBUtils;
import com.ibm.gdc.batch.utils.BatchException;
import com.ibm.gdc.batch.utils.FileProcessor;
import com.ibm.gdc.batch.utils.GDBatchUtils;
import com.ibm.gdc.csv.handler.CSVDataTransformer;
import com.ibm.gdc.csv.handler.CSVParser;
import com.ibm.gdc.data.handler.DataPreparer;

public class EntryPoint {

	private static Logger logger = Logger.getLogger(EntryPoint.class);
	
	public static void main(String[] args) {

		FileProcessor fileProcessor = null;
		List<File> filesForProcessing = null;
		CSVParser csvParser = null;
		Map<String, Map<String, List<String>>> csvRawMap = null;
		Map<String, List<List<Map<String, String>>>> csvCollection = null;
		CSVDataTransformer csvDataTransformer = null;
		Map<String, List<Object[]>> transformedList = null;
		
		DataPreparer dataPreparer = null;
		List<SuppPartneringInfo> suppPartneringInfos = null;
		int processedCount = 0;
		
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
				//parse the csv after polling
				csvParser = new CSVParser();

				csvRawMap = csvParser.parseCSVFiles(fileForProcessing);
				csvCollection = csvParser.prepareRowDataPairs(csvRawMap);
				
				csvDataTransformer = new CSVDataTransformer();
				transformedList = csvDataTransformer.transformCSVData(csvCollection, fileForProcessing);

				dataPreparer = new DataPreparer();
				suppPartneringInfos = dataPreparer.prepareSuppPartnerInfor(transformedList);
				
				processedCount = DBUtils.loadSupppartNumber(suppPartneringInfos);
				
				// copy the files to done directory after the records are processed
				GDBatchUtils.archiveAndDelete(fileForProcessing);
				
				logger.info("File " + file.getName() +" processed");
			}	
			
			logger.info("Processing Done"+ GDBatchUtils.getCurrentTimestampToString() + " for record (s) :"+processedCount );
			
		} catch (BatchException e) {
			logger.error(e.getMessage());
			try {
				if(filesForProcessing!=null && !filesForProcessing.isEmpty())
					GDBatchUtils.archiveAndDeleteForError(filesForProcessing);
			} catch (BatchException e1) {
				logger.error(e.getMessage());
			}
		}catch (Exception e) {
			//catching system exceptions if any
			logger.error(e.getMessage());
		}

	}
}
