package com.ibm.vo;

public class BatchTrackerVO extends BaseVO{

	private String fileName;
	private Long recordCount;
	private String status;
	private String date; //date yyyy-mm-dd format
	private String dateTs; //getCurrent timestamp
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the recordCount
	 */
	public Long getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the dateTs
	 */
	public String getDateTs() {
		return dateTs;
	}
	/**
	 * @param dateTs the dateTs to set
	 */
	public void setDateTs(String dateTs) {
		this.dateTs = dateTs;
	}
	
	
}
