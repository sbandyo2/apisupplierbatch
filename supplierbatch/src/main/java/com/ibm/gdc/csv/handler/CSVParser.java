package com.ibm.gdc.csv.handler;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.ibm.gdc.batch.utils.BatchConstants;
import com.ibm.gdc.batch.utils.BatchException;

public class CSVParser {

	Logger logger = Logger.getLogger(CSVParser.class);
	
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';
	public static final String CLOUMNS = "CLOUMNS";
	public static final String ROW = "ROW";
	

	
	/**
	 * @param cvsLine
	 * @return
	 */
	public  List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	/**
	 * @param cvsLine
	 * @param separators
	 * @return
	 */
	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	/**
	 * This will parse a line from CSV file
	 * @param cvsLine
	 * @param separators
	 * @param customQuote
	 * @return
	 */
	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<String>();

		if (cvsLine == null || cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		cvsLine.split(BatchConstants.DELIMETER);
		char[] chars = cvsLine.toCharArray();
		int charIndex = 0;
		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					//Allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					//Allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						
						// removing the double quotes in case the first value is
						// empty
						if(charIndex>0 && chars[charIndex-1]!= ','){
							curVal.append('"');
						}
						
					}

					//double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					//ignore LF characters
					continue;
				} else if (ch == '\n') {
					//the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}
			
			charIndex++;

		}

		result.add(curVal.toString());

		return result;
	}

	
	/**
	 * Method will parse list of CSV file and add them in the MAP
	 * Map key will be CSV file name.
	 * @param fileList
	 * @return
	 * @throws BatchException 
	 */
	public Map<String, Map<String,List<String>>> parseCSVFiles(List<File> fileList) throws BatchException {
		//File name will be the key
		Map<String, Map<String,List<String>>> fileMap = new LinkedHashMap<String, Map<String,List<String>>>();
		//COLUMN and ROW + row number will be the key
		Map<String,List<String>> rowsMap = null;

		try{
			//Scan each CSV and generate MAP
			for(File file : fileList) {
				Scanner scanner = new Scanner(file);
				int fileRow = 0;
				
				// initializing the map before each file processing
				rowsMap = new LinkedHashMap<String, List<String>>();
				
				while (scanner.hasNext()) {
					if(fileRow==1) {
						//List contain all the columns name in CSV
						//This will the 1st entry of the rowsMap
						List<String> column = parseLine(scanner.nextLine());
						rowsMap.put(CLOUMNS, column);
					}else if(fileRow>1) {
						int dataRow = fileRow - 1;
						//List contain single row data
						List<String> row = parseLine(scanner.nextLine());
						String rowNumber = ROW + dataRow;
						//Map key example: ROW1, ROW2
						rowsMap.put(rowNumber, row);
					}else{
						//First row represent character encoding. 
						parseLine(scanner.nextLine());
					}   
					fileRow++;
				}
				scanner.close();
				//populate map
				fileMap.put(file.getName(), rowsMap);
			}
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new BatchException(e.getMessage());
		}
		return fileMap;
	}
	
	/**
	 * Map contain key as file name and value as list of row lists which contain map of
	 * column key and corresponding row value.
	 * @param fileMap
	 * @return
	 */
	
	public  Map<String, List<List<Map<String, String>>>> prepareRowDataPairs(Map<String, Map<String,List<String>>> fileMap) {
		//file list
		Map<String, List<List<Map<String, String>>>> csvFileRowMap = new LinkedHashMap<String, List<List<Map<String, String>>>>();
		for(Map.Entry<String, Map<String, List<String>>>  entry : fileMap.entrySet()) {
			//Column list
			List<String> columnNameList = null;
			//file data row list
			List<List<Map<String, String>>> csvRowList = new LinkedList<List<Map<String, String>>>();
			for(Map.Entry<String,List<String>> entryy : entry.getValue().entrySet()) {

				if(entryy.getKey().equals(CLOUMNS)) {
					columnNameList = entryy.getValue();
				}else {
					//Row list
					List<String> rowValueList = entryy.getValue();
					//row pair list
					List<Map<String, String>> rowPairList = new LinkedList<Map<String, String>>();
					for(int i = 0; i < rowValueList.size(); i++) {
						//pair map
						Map<String, String> pairMap = new LinkedHashMap<String, String>();
						pairMap.put(columnNameList.get(i), rowValueList.get(i));
						(rowPairList).add(pairMap);
					}	
					csvRowList.add(rowPairList);
				}
			}
			csvFileRowMap.put(entry.getKey(), csvRowList);
		}
		return csvFileRowMap;
	}

}
