package com.sherwin.shercolor.swdevicehandler.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SimCalibration extends Calibration {
	//String filename;
	//byte[] data;
	final String SIMDIR="./simcal/";
	final String SIMBAKDIR="./simcalbak/";

	public SimCalibration(){
		
	}
	
	public SimCalibration(Calibration c){
		setFilename(c.getFilename());
		setData(c.getData());
	}
	
	public String toDisk(){
		String errorString=null;
		//make a backup first
		File src = new File(SIMDIR);
		if(!src.exists())src.mkdir();
		
		File dest = new File(SIMBAKDIR);
		if(!dest.exists()) dest.mkdir();
		copyFolder(src,dest);
		errorString = unzip(SIMDIR);

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
				ZipDirNonRecursive(zos, SIMDIR);
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
					//2018-01-11 BKP validate the fileNames best we can.  Confirm it does NOT in .EXE or .BAT.
					if (fileName.toUpperCase().endsWith(".EXE") || fileName.toUpperCase().endsWith(".BAT") ) {
						throw new Exception("Invalid calibration file name");
					}


					File newFile = new File(SIMDIR + fileName);
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

			} catch (IOException | NullPointerException e) {
				// TODO Auto-generated catch block
				errorString = e.getMessage();
				e.printStackTrace();
				logger.error(errorString);
				logger.error("stacktrace", e);
			} catch (Exception ex) {
				// catch errors other than IOException and NullPointerException
				errorString = ex.getMessage();
				logger.error(errorString);
				logger.error("stacktrace", ex);
			}
		}
	
		return errorString;
	}


	public void ZipDirNonRecursive(ZipOutputStream zos,String dirtozip) throws IOException{
		File[] children = new File(dirtozip).listFiles();
		for (File childFile : children) {
			if(!childFile.isDirectory()){
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
			System.out.println("+" + zipEntryName);
			for (File file : fileToZip.listFiles()) {
				addDirToZipArchive(zos, file, zipEntryName);
			}
		} else {
			System.out.println("   " + zipEntryName);
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


}
