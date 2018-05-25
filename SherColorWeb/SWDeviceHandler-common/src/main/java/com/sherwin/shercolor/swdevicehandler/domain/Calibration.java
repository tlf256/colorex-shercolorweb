package com.sherwin.shercolor.swdevicehandler.domain;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class Calibration {
	String filename;
	byte[] data;
	final String UPLOADDIR="./uploads/";
	static Logger logger = LogManager.getLogger(Calibration.class);

// src = src folder  dest = backup folder
	public void BackupXMLFiles(File src, File dest) {
	    // checks
	    if(src==null || dest==null){
	    	logger.error("src  or dest folders are null");
	    	   return;
	    }
	     
	    if(!src.isDirectory()){
	    	logger.error("src ["+src.getName()+"]  is not a directory");
	    	return;
	    }
	    if(dest.exists()){
	        if(!dest.isDirectory()){
		    	logger.error("dest ["+dest.getName()+"]  is not a directory");
	            return;
	        }
	    } else {
	        dest.mkdir();
	    }

	    if(src.listFiles()==null || src.listFiles().length==0){
	    	logger.error("No files in src ["+src.getName()+"]"  );
	    	return;
	    }

	    for(File file: src.listFiles()){
	        File fileDest = new File(dest, file.getName());
	       // logger.error("Copying files from src ["+src.getName()+"] to dest ["+dest.getName()+"]");
	        if(!file.isDirectory() && file.getName().endsWith(".XML")){
	            try {
	                Files.copy(file.toPath(), fileDest.toPath(),StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	            	logger.error("stacktrace",e);
	            }
	        }
	    }
	}
	public void copyFolder(File src, File dest) {
	    // checks
	    if(src==null || dest==null){
	    	logger.error("src  or dest folders are null");
	    	   return;
	    }
	     
	    if(!src.isDirectory()){
	    	logger.error("src ["+src.getName()+"]  is not a directory");
	    	return;
	    }
	    if(dest.exists()){
	        if(!dest.isDirectory()){
		    	logger.error("dest ["+dest.getName()+"]  is not a directory");
	            return;
	        }
	    } else {
	        dest.mkdir();
	    }

	    if(src.listFiles()==null || src.listFiles().length==0){
	    	logger.error("No files in src ["+src.getName()+"]"  );
	    	return;
	    }

	    for(File file: src.listFiles()){
	        File fileDest = new File(dest, file.getName());
	       // logger.error("Copying files from src ["+src.getName()+"] to dest ["+dest.getName()+"]");
	        if(file.isDirectory()){
	            copyFolder(file, fileDest);
	        }else{	         
	            try {
	                Files.copy(file.toPath(), fileDest.toPath(),StandardCopyOption.REPLACE_EXISTING);
	            } catch (Exception e) {
	            	logger.error("stacktrace",e);
	            }
	        }
	    }
	}
	
	/* Function: unzip
	 * Purpose: takes data buffer that contains a zip file and unzips it without ever saving the zipfile itself
	 */
	public String unzip(String OUTDIR){
		String errorString = null;
		if(getData()!=null){
			byte[] buffer =new byte[1024];

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(getData()));
			ZipEntry zipEntry;
			try {
				zipEntry = zis.getNextEntry();

				while(zipEntry != null){
					String fileName = zipEntry.getName();
					//2018-01-11 BKP validate the fileNames best we can.  Confirm it does NOT in .EXE or .BAT.
					if (fileName.toUpperCase().endsWith(".EXE") || fileName.toUpperCase().endsWith(".BAT") ) {
						throw new Exception("Invalid calibration file name");
					}
					File newFile = new File(OUTDIR + fileName);
					//2018-01-11 BKP create an even newer file using the canonical method.
					//      This will provide some security by 
					File newNewFile = newFile.getCanonicalFile();
					System.out.println("in Unzip, newNewfilename is " + newNewFile.getName());
					logger.error("in Unzip, newNewfilename is " + newNewFile.getName());
					FileOutputStream fos = new FileOutputStream(newNewFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
				zis.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				errorString = e.getMessage();
				logger.error("stacktrace",e);
			}
		}
	
		return errorString;
	}


	public void ZipDirNonRecursive(ZipOutputStream zos,String dirtozip) throws IOException{
		File[] children = new File(dirtozip).listFiles();
		for (File childFile : children) {
			if(!childFile.isDirectory()  && ! childFile.getName().endsWith("NTX")){
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(dirtozip+childFile.getName());
				zos.putNextEntry(new ZipEntry(childFile.getName()));
				int length;
				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}
				zos.closeEntry();
				fis.close();
			}
		}
	}

	public  void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parentDirectoryName) throws Exception {
		if (fileToZip == null || !fileToZip.exists()) {
			return;
		}

		String zipEntryName = fileToZip.getName();
		if (parentDirectoryName!=null && !parentDirectoryName.isEmpty()) {
			zipEntryName = parentDirectoryName + "/" + fileToZip.getName();
		}

		if (fileToZip.isDirectory()) {
			logger.error("+" + zipEntryName);
			for (File file : fileToZip.listFiles()) {
				addDirToZipArchive(zos, file, zipEntryName);
			}
		} else {
			logger.error("   " + zipEntryName);
			byte[] buffer = new byte[1024];
			FileInputStream fis = new FileInputStream(fileToZip);
			zos.putNextEntry(new ZipEntry(zipEntryName));
			int length;
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}
			zos.closeEntry();
			fis.close();
		}
	}

	
		
	//getters and setters
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
