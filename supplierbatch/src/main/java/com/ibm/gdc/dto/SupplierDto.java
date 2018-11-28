package com.ibm.gdc.dto;

public class SupplierDto {
	
	private long suppPartnerId;
	private String vendorId;
	private String locationId;
	/**
	 * @return the suppPartnerId
	 */
	public long getSuppPartnerId() {
		return suppPartnerId;
	}
	/**
	 * @param suppPartnerId the suppPartnerId to set
	 */
	public void setSuppPartnerId(long suppPartnerId) {
		this.suppPartnerId = suppPartnerId;
	}
	/**
	 * @return the vendorId
	 */
	public String getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

}
