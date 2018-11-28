package com.ibm.gdc.data.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ibm.gdc.batch.bean.SuppPartneringInfo;
import com.ibm.gdc.batch.utils.GDBatchUtils;

public class DataPreparer {

	Logger logger = Logger.getLogger(DataPreparer.class);

	public List<SuppPartneringInfo> prepareSuppPartnerInfor(Map<String, List<Object[]>> csvtransformedDataMap){
		
		Set<String> set = null;
		Iterator<String> it = null;
		List<SuppPartneringInfo> infos = null;
		SuppPartneringInfo info = null;
		String key = null;
		List<Object[]> objs = null;
		
		infos = new ArrayList<SuppPartneringInfo>();
		
		if(csvtransformedDataMap!=null && !csvtransformedDataMap.isEmpty()){
			set = csvtransformedDataMap.keySet();
			it = set.iterator();
			
			while(it.hasNext()){
				key = it.next();
				objs = csvtransformedDataMap.get(key);
				
				if(objs!=null && !objs.isEmpty()){
					for(Object[] obj : objs){
						info = new SuppPartneringInfo();
						if (obj[0] != null)
							info.setVendorID(String.valueOf(obj[0]));
						
						if (obj[1] != null)
							info.setLocationID(String.valueOf(obj[1]));
						
						if (obj[2] != null)
							info.setName(String.valueOf(obj[2]));
						
						if (obj[3] != null)
							info.setCity(String.valueOf(obj[3]));
						
						if (obj[4] != null)
							info.setStreet(String.valueOf(obj[4]));
						
						if (obj[5] != null)
							info.setPostalCode(String.valueOf(obj[5]));
						
						if (obj[6] != null)
							info.setRegion(String.valueOf(obj[6]));
						
						if (obj[7] != null)
							info.setCountry(String.valueOf(obj[7]));
						
						if (obj[8] != null)
							info.setPhone(String.valueOf(obj[8]));
						
						if (obj[9] != null)
							info.setFax(String.valueOf(obj[9]));
						
						if (obj[10] != null)
							info.setEmailAddress(String.valueOf(obj[10]));
						
						infos.add(info);
					}
				}
			}
		}
		return infos;
	}
}
