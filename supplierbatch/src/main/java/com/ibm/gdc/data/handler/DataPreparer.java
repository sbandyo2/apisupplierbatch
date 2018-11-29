package com.ibm.gdc.data.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class DataPreparer {

	Logger logger = Logger.getLogger(DataPreparer.class);
	
	private static final String VENDOR_ID = "VendorID";
	private static final String LOCATION_ID = "LocationID";
	private static final String NAME = "Name";
	private static final String CITY = "City";
	private static final String STREET = "Street";
	private static final String POSTAL_CODE = "PostalCode";
	private static final String REGION = "Region";
	private static final String COUNTRY = "Country";
	private static final String PHONE = "Phone";
	private static final String FAX = "Fax";
	private static final String EMAIL_ADDRESS = "EmailAddress";

	public List<Map<Object,Object>> prepareSuppPartnerInfor(Map<String, List<Object[]>> csvtransformedDataMap){
		
		Set<String> set = null;
		Iterator<String> it = null;
		List<Map<Object,Object>> dataList = null;
		Map<Object,Object> rowDataMap = null;
		String key = null;
		List<Object[]> objs = null;
		
		dataList = new ArrayList<Map<Object,Object>>();
		
		if(csvtransformedDataMap!=null && !csvtransformedDataMap.isEmpty()){
			set = csvtransformedDataMap.keySet();
			it = set.iterator();
			
			while(it.hasNext()){
				key = it.next();
				objs = csvtransformedDataMap.get(key);
				
				if(objs!=null && !objs.isEmpty()){
					for(Object[] obj : objs){
						rowDataMap = new LinkedHashMap<Object, Object>();
						
						if (obj[0] != null)
							rowDataMap.put(VENDOR_ID, String.valueOf(obj[0]).trim());
						
						if (obj[1] != null)
							rowDataMap.put(LOCATION_ID, String.valueOf(obj[1]).trim());
						
						if (obj[2] != null)
							rowDataMap.put(NAME, String.valueOf(obj[2]).trim());
						
						if (obj[3] != null)
							rowDataMap.put(CITY, String.valueOf(obj[3]).trim());
						
						if (obj[4] != null)
							rowDataMap.put(STREET, String.valueOf(obj[4]).trim());
						
						if (obj[5] != null)
							rowDataMap.put(POSTAL_CODE, String.valueOf(obj[5]).trim());
						
						if (obj[6] != null)
							rowDataMap.put(REGION, String.valueOf(obj[6]).trim());
						
						if (obj[7] != null)
							rowDataMap.put(COUNTRY, String.valueOf(obj[7]).trim());
						
						if (obj[8] != null)
							rowDataMap.put(PHONE, String.valueOf(obj[8]).trim());
						
						if (obj[9] != null)
							rowDataMap.put(FAX, String.valueOf(obj[9]).trim());
						
						if (obj[10] != null)
							rowDataMap.put(EMAIL_ADDRESS, String.valueOf(obj[10]).trim());
						
						dataList.add(rowDataMap);
					}
				}
			}
		}
		return dataList;
	}
}
