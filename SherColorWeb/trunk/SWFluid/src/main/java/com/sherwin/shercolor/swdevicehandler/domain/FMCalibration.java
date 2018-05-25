package com.sherwin.shercolor.swdevicehandler.domain;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FMCalibration extends Calibration{
	//String filename;
	//byte[] data;

	final String FMDIR="c:/Program Files (x86)/Fluid Management/FM_IDD/";
	final String FMBAKDIR="c:/Program Files (x86)/Fluid Management/FM_IDD/bak";
	final String FMFILE="FM_SYSTEM.XML";

	public FMCalibration(){
		
	}
	public  FMCalibration(Calibration c){
		setFilename(c.getFilename());
		setData(c.getData());
	}
	/*
	private void setData(char[] chars){
		ByteBuffer bb = Charset.forName("UTF-8").encode(CharBuffer.wrap(chars));
		byte[] b = new byte[bb.remaining()];
		bb.get(b);
		setData(b);
	}
*/
	
	
	/*  items for using FM_SYSTEM.XML not zipped 
	public String toDisk(){
		String errorString=null;
		//make a backup first
		File src = new File(FMDIR);
		File dest = new File(FMBAKDIR);
		copyFolder(src,dest);
		if(getFilename().contains("FM") || getFilename().contains("fm")){
			try{	 

				System.out.println("Saving cal to file: " + FMDIR + getFilename());
				Path path = Paths.get(FMDIR + FMFILE);

				Files.write(path, data);

			} catch (IOException e) {
				errorString = e.getMessage();
			} catch (Exception e){
				errorString = e.getMessage();
			}
		}
		else{
			errorString = "Filename does not contain 'FM'";
		}
		return errorString;
	}
	
	public String fromDisk(){
		String errorString = null;
		try{	 

			System.out.println("Saving cal to file: " + FMDIR + FMFILE);
			Path path = Paths.get(FMDIR + FMFILE);
			setData(Files.readAllBytes(path));
			this.setFilename(FMFILE);


		} catch (IOException e) {
			errorString = e.getMessage();
		} catch (Exception e){
			errorString = e.getMessage();
		}
		return errorString;
	}
	*/
	
	public String toDisk(){
		String errorString=null;
		//make a backup first
		File src = new File(FMDIR);
		File dest = new File(FMBAKDIR);
		BackupXMLFiles(src,dest);
		errorString = unzip(FMDIR);

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
				ZipXMLFilesinDir(zos, FMDIR);
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

	
	public void ZipXMLFilesinDir(ZipOutputStream zos,String dirtozip) throws IOException{
		File[] children = new File(dirtozip).listFiles();
		for (File childFile : children) {
			if(!childFile.isDirectory() && childFile.getName().contentEquals("FM_SYSTEM.XML")){
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(dirtozip+"/"+childFile.getName());
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
