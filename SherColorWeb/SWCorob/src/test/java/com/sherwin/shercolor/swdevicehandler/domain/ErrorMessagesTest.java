package com.sherwin.shercolor.swdevicehandler.domain;


import java.io.FileWriter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;


import org.junit.Ignore;
import org.junit.Test;



public class ErrorMessagesTest {
	ErrorMessages errorMessages = new ErrorMessages();
	
	/*
	 * Transfer error messages from HashMap to Resource file
	 */
@Ignore @Test
public void MapToFile(){
	String path = "/swdevicehandler/SWCorobErrors_en_US.properties";
	

	try {
		
		FileWriter writer = new FileWriter(path);
		PrintWriter pWriter = new PrintWriter(writer);
		ErrorMessages errorMessages = new ErrorMessages();
		HashMap<Long, String> rcmap = errorMessages.getRC_map();
		HashMap<Long, String> map = errorMessages.getMap();
		rcmap.putAll(map);
		for(Entry<Long, String> entry:new TreeMap<Long, String>(rcmap).entrySet()){
			pWriter.printf("%x=%s\n",entry.getKey(),entry.getValue());
		}
		/*
		for(Entry<Long, String> mentry:new TreeMap<Long, String>(map).entrySet()){
			pWriter.printf("%d=%s\n",mentry.getKey(),mentry.getValue());
		}
		*/
		pWriter.close();
		writer.close();
	} 
	catch(Exception ex){
		ex.printStackTrace();
	}
}
@Test
public void TestgetInternationalErrorMessage(){
	String msg = errorMessages.getInternationalErrorMessage(-1);
	System.out.println(msg);
}
}
