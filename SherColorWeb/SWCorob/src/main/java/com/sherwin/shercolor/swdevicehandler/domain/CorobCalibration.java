package com.sherwin.shercolor.swdevicehandler.domain;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.zip.ZipOutputStream;

public class CorobCalibration extends Calibration{
	//String filename;
	//byte[] data;
	final String COROBDIR="/local/";
	final String COROBBAKDIR="/localbak/";
	
	
	public CorobCalibration(){

	}
	public  CorobCalibration(Calibration c){
		setFilename(c.getFilename());
		setData(c.getData());
	
	}

	public String toDisk(){
		String errorString=null;
		//make a backup first
		File src = new File(COROBDIR);
		if(!src.exists())src.mkdir();
		
		File dest = new File(COROBBAKDIR);
		if(!dest.exists()) dest.mkdir();
		copyFolder(src,dest);
		errorString = unzip(COROBDIR);

		return errorString;
	}
	public String fromDisk(){
		String errorString = null;
		File upDir = new File(UPLOADDIR);
		if(!upDir.exists()){
			upDir.mkdir();
		}
		if(upDir.exists()){
			try{
				FileOutputStream fos;
				fos = new FileOutputStream(UPLOADDIR + getFilename());

				ZipOutputStream zos = new ZipOutputStream(fos);
				ZipDirNonRecursive(zos, COROBDIR);
				zos.flush();
				fos.flush();
				zos.close();
				fos.close();
			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				errorString = e.getMessage();
				System.out.println(errorString);
			}		
		}


		try{	 

			System.out.println("Retrieving cal from file: " + UPLOADDIR + getFilename());
			Path path = Paths.get(UPLOADDIR + getFilename());
			setData(Files.readAllBytes(path));
		} catch (IOException e) {
			errorString = e.getMessage();
		} catch (Exception e){
			errorString = e.getMessage();
		}
		return errorString;
	}
	


	

	
}
