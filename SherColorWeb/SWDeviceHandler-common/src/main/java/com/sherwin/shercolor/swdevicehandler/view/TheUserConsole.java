package com.sherwin.shercolor.swdevicehandler.view;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TheUserConsole extends Application {
	// private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	 private static final Logger LOG = LogManager.getLogger(TheUserConsole.class);
	 private static  TextArea tinterstatus=null;
	 private static  TextArea coloreyestatus=null;
	 private final static BlockingQueue<TinterMessage> tinterConsoleOutputQueue= new LinkedBlockingQueue<>();
  	 private final static BlockingQueue<SpectroMessage> coloreyeConsoleOutputQueue= new LinkedBlockingQueue<>();
   	 //final static BlockingQueue<String> tinterConsoleOutputQueue= new LinkedBlockingQueue<>();
 	// final static BlockingQueue<String> coloreyeConsoleOutputQueue= new LinkedBlockingQueue<>();
  	 private static TinterMessage lastTinterMessage=null;
  	 private static SpectroMessage lastSpectroMessage=null;
  	 public static boolean isConsoleStarted(){
  		 if(getTinterstatus() !=null ){
  			 return true;
  		 }
  		 else{
  			 return false;
  		 }
  	 }
  	 public static void AppendTinterConsole(TinterMessage m){

  		 if(m != null){
  			 setLastTinterMessage(m);
  			 String str = m.getCommand();
  			 if(str !=null){
  				 TheUserConsole.getTinterstatus().appendText(str+"\n");
  			 }
  		 }
  	 }
	 public static void AppendTinterConsole(String str){

  			 if(str !=null){
  				 TheUserConsole.getTinterstatus().appendText(str+"\n");
  			 }
  	 }
	 
	 public static void AppendSpectroConsole(SpectroMessage m){

  		 if(m != null){
  			 setLastSpectroMessage(m);
  			 String str = m.getCommand();
  			 if(str !=null){
  				 TheUserConsole.getColoreyestatus().appendText(str+"\n");
  			 }
  		 }
  	 }
	 public static void AppendSpectroConsole(String str){

  			 if(str !=null){
  				 TheUserConsole.getColoreyestatus().appendText(str+"\n");
  			 }
  	 }
	 
	 @Override
	    public void start(Stage primaryStage) {
		 tinterstatus=  new TextArea();
		 coloreyestatus=new TextArea();
		 Button dispense_btn = new Button();
	        dispense_btn.setText("Send Response");
	        dispense_btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                //startScheduledExecutorService();
	            	try {
	            		TinterMessage msg = getLastTinterMessage();
	            		msg.setErrorMessage("User Console button click");
	            		getTinterconsoleoutputqueue().put(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
						LOG.warn(e.getCause());
					}
	            	System.out.println("Dispense Complete");
	            }
	        });
	       
	        Button coloreye_btn = new Button();
	        coloreye_btn.setText("ColorEye Reading");
	        coloreye_btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                //startScheduledExecutorService();
	            	try {
	            		SpectroMessage msg = getLastSpectroMessage();
	            		msg.setErrorMessage("User Console button click");
	            		getColoreyeconsoleoutputqueue().put(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
						LOG.warn(e.getCause());
					}
	            	System.out.println("Color Eye Read");
	            }
	        });
	        Label l1 = new Label("TinterStatus");
	        l1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        Label l2 = new Label("ColorEye Status");
	        l2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
	        VBox vBox = new VBox();
	        vBox.getChildren().addAll(l1,tinterstatus,dispense_btn,l2,coloreyestatus,coloreye_btn);
	         
	        StackPane root = new StackPane();
	        root.getChildren().add(vBox);
	    
	        Scene scene = new Scene(root, 600, 650);
	         
	        primaryStage.setTitle("Tinter Simulator");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
	
		System.exit(0);
	}
	private static TextArea getTinterstatus() {
		return tinterstatus;
	}
	
	private static TextArea getColoreyestatus() {
		return coloreyestatus;
	}
	public static BlockingQueue<TinterMessage> getTinterconsoleoutputqueue() {
		return tinterConsoleOutputQueue;
	}
	public static BlockingQueue<SpectroMessage> getColoreyeconsoleoutputqueue() {
		return coloreyeConsoleOutputQueue;
	}
	public static TinterMessage getLastTinterMessage() {
		return lastTinterMessage;
	}
	public static void setLastTinterMessage(TinterMessage lastTinterMessage) {
		TheUserConsole.lastTinterMessage = lastTinterMessage;
	}
	public static SpectroMessage getLastSpectroMessage() {
		return lastSpectroMessage;
	}
	public static void setLastSpectroMessage(SpectroMessage lastSpectroMessage) {
		TheUserConsole.lastSpectroMessage = lastSpectroMessage;
	}
	
	
	
	 
	
	 
	 
}
