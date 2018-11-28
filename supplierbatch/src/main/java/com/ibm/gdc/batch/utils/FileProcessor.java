package com.ibm.gdc.batch.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileProcessor {

	Logger logger = Logger.getLogger(FileProcessor.class);


	/**
	 * This method polls the acceptable files form the polling directory and
	 * then moves them to working directory for further processing. It returns
	 * the list of files for processing for the next module
	 * 
	 * @return
	 * @throws BatchException 
	 */
	public List<File> pollFilesForProcessing() throws BatchException {
		String pollDir = null;
		String workDir = null;
		String invalidDir = null;
		String lookUpFile = null;
		String[] filterArray = null;
		List<File> files = null;
		String ts = null;
		String fName = null;
		String copyFileName = null;
		String extension = null;
		List<File> fileListForProcessing = null;
		File fileToProcess = null;

		try {
			//get the directories from configuration
			pollDir = GDBatchUtils.getValue(BatchConstants.POLL_DIR);
			workDir = GDBatchUtils.getValue(BatchConstants.WORK_DIR);
			invalidDir= GDBatchUtils.getValue(BatchConstants.INVALID_DIR);
			
			lookUpFile = GDBatchUtils.getValue(BatchConstants.FILE_NAME_FILTER);
			extension = GDBatchUtils.getValue(BatchConstants.FILE_EXT);
			
			fileListForProcessing = new ArrayList<File>();
			
			// filter the file which are acceptable for the batch based on the
			// configuration
			if (!GDBatchUtils.isNullOrEmpty(lookUpFile)) {
				
				filterArray = lookUpFile.split(BatchConstants.DELIMETER);
				for (String filterStr : filterArray) {
					files = GDBatchUtils.listFiles(pollDir);
					for (File filterFile : files) {
						fName = filterFile.getName();
						if (fName.contains(filterStr)) {
							
							//move only the files with csv extension 
							if(fName.contains(extension)){
								copyFileName = fName.substring(0,fName.indexOf(BatchConstants.DOT+ extension));
							
								//get the file formatted time stamp
							ts = GDBatchUtils.formatTimeStamp(GDBatchUtils.getCurrentTimestampToString(),
														BatchConstants.UNDERSCORE);
							
							// generate the copy name with the time stamp to
							// mark the processing of the file
							copyFileName = copyFileName+BatchConstants.UNDERSCORE
									+ ts + BatchConstants.DOT + extension;
							
							//create directory for the first time if it does not exists
							GDBatchUtils.makeDir(workDir);
							
							GDBatchUtils.copyAndDeleteFile(pollDir + BatchConstants.DOUBLE_BACKSLASH + fName,
													workDir + BatchConstants.DOUBLE_BACKSLASH + copyFileName);
							
							//mark it for processing for the next module
							fileToProcess = new File(workDir + BatchConstants.DOUBLE_BACKSLASH + copyFileName);
							
							fileListForProcessing.add(fileToProcess);
							
							}else{
								//create directory for the first time if it does not exists
								GDBatchUtils.makeDir(invalidDir);
								
								// move the Invalid or unacceptable files to
								// Invalid Directory location
								GDBatchUtils.copyAndDeleteFile(pollDir + BatchConstants.DOUBLE_BACKSLASH + fName,
										invalidDir + BatchConstants.DOUBLE_BACKSLASH + fName);
							}

						}
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BatchException(e.getMessage());
			
		} catch (BatchException e) {
			logger.error(e.getMessage());
			throw new BatchException(e.getMessage());
		}
		
		return fileListForProcessing;
	}

	

	public void moveFilesToDoneInDir() {

	}
}
