package com.sherwin.shercolor.swdevicehandler.domain;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class GData {
	String colorantId;
	byte[] data;
	
	final String GDATADIR="/gdata/";

	public GData(){

	}
	
	public String toDisk(){
		String errorString=null;
		File dir = new File(GDATADIR);
		if(!dir.exists()){
			dir.mkdir();
		}
		
		errorString = unzip();

		return errorString;
	}
	
	
	/*
	 * Function: unzip
	 * Purpose: takes data buffer that contains a zip file and unzips it without ever saving the zipfile itself
	 */
	public String unzip(){
		String errorString = null;
		if(getData()!=null){
			byte[] buffer =new byte[1024];

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(getData()));
			ZipEntry zipEntry;
			try {
				zipEntry = zis.getNextEntry();

				while(zipEntry != null){
					String fileName = zipEntry.getName();
					
					File newFile = new File(GDATADIR + fileName);
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
				zis.close();

			} catch (IOException | NullPointerException e) {
				// TODO Auto-generated catch block
				errorString = e.getMessage();
				e.printStackTrace();
			} catch (Exception ex) {
				errorString = ex.getMessage();
				ex.printStackTrace();
			}
		}
	
		return errorString;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getColorantId() {
		return colorantId;
	}

	public void setColorantId(String colorantId) {
		this.colorantId = colorantId;
	}
	

}
