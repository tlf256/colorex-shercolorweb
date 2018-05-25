package com.sherwin.shercolor.common.dao;
//Done 10/21/2014
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.sherwin.shercolor.common.dao.CustWebEcalDao;
import com.sherwin.shercolor.common.domain.CustWebEcal;
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
    @TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
    @Transactional
    public class CustWebEcalDaoTest {
    // 05/04/2015 - Checked after new DE exception implemented.
        
        
        
        @Autowired
        private CustWebEcalDao target;
        String home = System.getProperty("user.home");
        final String UPLOADDIR="/SWUploads/";
        final String UPLOADDIREXT="/SWUploads/CCE_COROBD600_434344_20170912_1131.zip";
        final String LITERALDIR = "SWUploads";
        final String filename ="CCE_COROBD600_434344_20170912_1131.zip";
        CustWebEcal ecal = new CustWebEcal();
        Path path = Paths.get(home + UPLOADDIR + filename);
        @Before
        /*
         * ensure that we have a copy of the ecal to upload before we go and delete it in 
         * the testEcalUpload method.
         */
        public void createLocalDir(){
         
            if(new File(home + UPLOADDIREXT).exists()) {
                //System.out.println(LITERALDIR + " found, did not write");
            }
            else if(new File(home + UPLOADDIR).exists()){
                writeFile(LITERALDIR, filename);
                System.out.println(LITERALDIR + " found, wrote " + filename);
            }
            else {
                new File(home + UPLOADDIR).mkdir();
                writeFile(LITERALDIR, filename);
                System.out.println(LITERALDIR + " missing, created dir and wrote " + filename);
            }
        }
        public void writeFile(final String LITERALDIR, final String filename) {
          
            String str = "";
            try {
                CustWebEcal ecal = target.selectEcal(filename);
                str = new String(ecal.getData());
                }
            catch(Exception e){
                System.out.println(e.getMessage() + e.getCause());
                }
            if(!str.isEmpty() && str != null) {
                try {
                    PrintWriter out = new PrintWriter(home + "/" + LITERALDIR + "/" + filename);
                    System.out.println("out = " + out.toString() );
                    out.println(str);
                    out.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
    
        @Test
        public void testCreate() {
            assertNotNull(target);
        }
            
    
        @Test
        public void testEcalUpload(){
          
            System.out.println("path = " + path.toString());
            byte[] data=null;
            try {
                data = Files.readAllBytes(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String[] arr = filename.split("_");
            String time = arr[4].substring(0, 4);
            ecal.setCustomerid("TEST");
            ecal.setColorantid(arr[0]);
            ecal.setTintermodel(arr[1]);
            ecal.setTinterserial(arr[2]);
            ecal.setFilename(filename);
            ecal.setUploaddate(arr[3]);
            ecal.setUploadtime(time);
            ecal.setData(data);
            int rc = target.deleteEcal(filename);
            if(rc==0) System.out.println("No Ecals to delete");
            rc = target.uploadEcal(ecal);
            assertEquals(0,rc);  //upload must work for everything else to work.
        }
        @Test
        public void testGetEcalList(){
          
            String[] arr = filename.split("_");
        
            String customerid = "TEST";
            String colorantid = arr[0];
            String tintermodel = arr[1];
            String tinterserial = arr[2];
            List<CustWebEcal>  ecalList = target.getEcalList(customerid, colorantid, tintermodel, tinterserial);
            assertNotNull(ecalList.get(0));
            
        }
        @Test
        public void getEcalsByCustomer(){
            String customerid = "TEST";
            
        
            List<CustWebEcal>  ecalList = target.getEcalList(customerid);
            assertNotNull(ecalList.get(0));
        }
        @Test
        public void testGetEcalTemplate(){
         
            String[] arr = filename.split("_");
        
            String customerid = "DEFAULT";
            String colorantid = arr[0];
            String tintermodel = arr[1];
        
            List<CustWebEcal>  ecalList = target.getEcalTemplate(colorantid, tintermodel);
            //assertNotNull(ecalList);
            assertEquals(1,ecalList.size());
            
        }
        @Test
        public void testGetEcalListAnyColorantID(){
          
            String[] arr = filename.split("_");
        
            String customerid = "DEFAULT";
            String colorantid = null;
            String tintermodel = arr[1];
            String tinterserial = arr[2];
        
            List<CustWebEcal>  ecalList = target.getEcalList(customerid, colorantid, tintermodel,null);
            assertNotNull(ecalList);
            
        }
        @Test
        public void testSelectEcal(){
            
            CustWebEcal ecal = target.selectEcal(filename);
            assertNotNull(ecal);
        }
        @Test
        public void testSelectGData(){
            final String[] colorantId_arr ={"CCE","BAC","808","844"};
            for(String colorantId:colorantId_arr){
            CustWebEcal ecal = target.selectGData(colorantId);
            System.out.println(ecal.getFilename());
            assertNotNull(ecal);
            }
        }
        @Test
        public void test(){
            Calendar cal = Calendar.getInstance();
            final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            final DateFormat ttt = new SimpleDateFormat("HHmm");
            System.out.println(sdf.format(cal.getTime()));
            System.out.println(ttt.format(cal.getTime()));
        }
        
          public byte[] readFile(File f) {
                ByteArrayOutputStream bos = null;
                try {
                    
                    FileInputStream fis = new FileInputStream(f);
                    byte[] buffer = new byte[1024];
                    bos = new ByteArrayOutputStream();
                    for (int len; (len = fis.read(buffer)) != -1;) {
                        bos.write(buffer, 0, len);
                    }
                    fis.close();
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e2) {
                    System.err.println(e2.getMessage());
                }
                return bos != null ? bos.toByteArray() : null;
            }
        @Ignore @Test //for uploading files, but can be ignored when not in use.
        public void testiterateTemplateFiles(){
            String myDirectoryPath="/templates/";
            Calendar cal = Calendar.getInstance();
            final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            final DateFormat ttt = new SimpleDateFormat("HHmm");
            String date = (sdf.format(cal.getTime()));
            String time = (ttt.format(cal.getTime()));
             File dir = new File(myDirectoryPath);
              File[] directoryListing = dir.listFiles();
              if (directoryListing != null) {
                for (File file : directoryListing) {
                    CustWebEcal ecal = new CustWebEcal();
                    String[] arr = file.getName().split("_");
                    ecal.setColorantid(arr[0]);
                    ecal.setCustomerid("DEFAULT");
                    ecal.setFilename(file.getName());
                    String arr1 = arr[1];
                    String[] arr2 = arr1.split("\\.");
                    String model = arr2[0];
                    ecal.setTintermodel(model);
                    ecal.setTinterserial(null);
                    ecal.setUploaddate(date);
                    ecal.setUploadtime(time);
                    ecal.setData(readFile(file));
                    System.out.println(file.getName());
                    try{
                        //target.deleteEcal(file.getName());
                    //    target.uploadEcal(ecal);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    
                }
              }
        }
        @Ignore  @Test //for uploading files, but can be ignored when not in use.
        public void testiterateGdataTemplateFiles(){
            String myDirectoryPath="/gdatatemplates/";
            Calendar cal = Calendar.getInstance();
            final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            final DateFormat ttt = new SimpleDateFormat("HHmm");
            String date = (sdf.format(cal.getTime()));
            String time = (ttt.format(cal.getTime()));
             File dir = new File(myDirectoryPath);
              File[] directoryListing = dir.listFiles();
              if (directoryListing != null) {
                for (File file : directoryListing) {
                    CustWebEcal ecal = new CustWebEcal();
                    String[] arr = file.getName().split("_");
                    ecal.setColorantid(arr[0]);
                    ecal.setCustomerid("GDATA");
                    ecal.setFilename(file.getName());
                    
                    ecal.setTintermodel("GDATA");
                    ecal.setTinterserial(null);
                    ecal.setUploaddate(date);
                    ecal.setUploadtime(time);
                    ecal.setData(readFile(file));
                    System.out.println(file.getName());
                    try{
                        //target.deleteEcal(file.getName());
                        //target.uploadEcal(ecal);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    
                }
              }
        }
       @Ignore  @Test //for uploading files, but can be ignored when not in use.
        public void testiterateFMTemplateFiles(){
            String myDirectoryPath="/fmtemplates/";
            Calendar cal = Calendar.getInstance();
            final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            final DateFormat ttt = new SimpleDateFormat("HHmm");
            String date = (sdf.format(cal.getTime()));
            String time = (ttt.format(cal.getTime()));
             File dir = new File(myDirectoryPath);
              File[] directoryListing = dir.listFiles();
              if (directoryListing != null) {
                for (File file : directoryListing) {
                    CustWebEcal ecal = new CustWebEcal();
                    String[] arr = file.getName().split("_");
                    ecal.setColorantid(arr[0]);
                    ecal.setCustomerid("DEFAULT");
                
                    String arr1 = arr[2];
                    String[] arr2 = arr1.split("\\.");
                    String model = arr[1] + arr2[0];
                    ecal.setTintermodel(model);
                    ecal.setTinterserial(null);
                    String new_filename =ecal.getColorantid()+"_"+ ecal.getTintermodel() +".zip"; 
                    ecal.setFilename(new_filename);
                    ecal.setUploaddate(date);
                    ecal.setUploadtime(time);
                    ecal.setData(readFile(file));
                    System.out.print(model + ":");
                    System.out.println(new_filename);
                    try{
                        target.deleteEcal(new_filename);
                        target.uploadEcal(ecal);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    
                }
              }
        }
}