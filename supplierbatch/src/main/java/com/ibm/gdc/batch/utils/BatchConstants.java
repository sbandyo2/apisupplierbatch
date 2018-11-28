package com.ibm.gdc.batch.utils;

public class BatchConstants {

	public static final String POLL_DIR = "POLL_DIR";
	public static final String WORK_DIR = "WORK_DIR";
	public static final String DONE_DIR = "DONE_DIR";
	public static final String INVALID_DIR = "INVALID_DIR";
	public static final String FILE_NAME_FILTER = "FILE_NAME_FILTER";
	public static final String DELIMETER = ",";
	public static final String UNDERSCORE = "_";
	public static final String DOUBLE_BACKSLASH = "/";//it needs be changed for windows "\\";
	public static final String DOT = ".";
	public static final String FILE_EXT = "FILE_EXT";
	public static final String ZIP = ".zip";
	
	public static final String CLOUDANT_URI = "URL";
	public static final String CLOUDANT_USER = "USER";
	
	public static final String CLOUDANT_PASSWORD = "PWD"; 
	
	public static final String DB_UPDATE_SUCCESS = "SUCCESS";
	public static final String DB_UPDATE_ERROR = "ERROR";
	public static final String DB_RESULT_KEY = "RESULT";
	
	public static final String CSV = ".csv";
	public static final int BULK_LIMIT = 10000;
	public static final String DB_SEARCH_PATH = "_search/newSearch";
	public static final int DB_SEARCH_LIMIT = 200;
	public static final int DB_SINGLE_LIMIT = 5;
	public static final String DB_SEARCH_ERROR = "Please specify valid Doc ID";
	public static final int INDEX_ZERO = 0;
	public static final int THREAD_SLEEP_TIME = 100;
	public static final int THREAD__SEARCH_SLEEP_TIME = 100; //250
	public static final int TD_LIMIT = 2;
	public static final String EMAIL_AT = "@";
	public static final String SINGLE_BACK_SLASH = "/";
	public static final String DB_CONN_OBJ = "DB_CONN";
	public static final String SEARCH_RESULT_OBJECT = "PAGE_SEARCH_OBJ";
	
	public static final String CLOUDANT_SUM_TYPE = "sum";
	public static final String CLOUDANT_MAX_TYPE = "max";
	public static final String CLOUDANT_COUNT_TYPE = "count";
	public static final int SEQ_COUNTER = 1;
	
	
	public static final String ORIGINATING_SYSTEM_FP = "CSAFP";
	public static final String ORIGINATING_SYSTEM_TM = "CSATM";
	public static final String FILTER_RECORD_KEY_INDEX = "FILTER_RECORD_KEY_INDEX";
	public static final String _FETCH_KEY = "_FETCH_KEY";
	
	public static final String MAP_KEY_NAME = "MAP_KEY_NAME";
	public static final String MAP_KEY_INDEX = "MAP_KEY_INDEX";
	public static final String connection_properties ="_connection";
	
	public static final String monitor_app_type = "MONITOR";
	
	public static final String fp_app_type = "FP";
	
	public static final String PROP_DIR = "PROP_DIR";
	
	//datastore
     public static final String BATCH_TRACKER_DATASTORE = "batchtracker";
     public static final String BATCH_TRACKER_FILES = "batchattachments";
}
