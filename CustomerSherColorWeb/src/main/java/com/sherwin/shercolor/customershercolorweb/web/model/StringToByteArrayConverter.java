package com.sherwin.shercolor.customershercolorweb.web.model;


import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class StringToByteArrayConverter extends StrutsTypeConverter{

	@Override
	/* Ecal upload file POSTs as an array of strings and struts does not know how to convert it to a byte array without this function.
	 * (non-Javadoc)
	 * @see org.apache.struts2.util.StrutsTypeConverter#convertFromString(java.util.Map, java.lang.String[], java.lang.Class)
	 */
	public Object convertFromString(Map context, String[] value, Class arg2) {
		int i = 0;
		if(value !=null){
			int len = value.length;
			if(len > 0){
				byte[] byte_arr = new byte[len];
				System.out.println("Length of data is: " + value.length);
				for(i=0; i < len; i++ ){
					byte_arr[i] = Byte.parseByte(value[i]);
				}
				return byte_arr;
			}
		}
		System.out.println("No data received");
		return null;

	}

	@Override
	public String convertToString(Map context, Object value) {
		try {            
			return new String((byte[]) value, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}

