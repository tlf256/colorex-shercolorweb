package com.sherwin.shercolor.swdevicehandler.jetty;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;


import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sherwin.shercolor.swdevicehandler.device.AbstractTinterImpl;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.Message;

import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;





public class TinterListener implements WebSocketListener {
	private static final Logger LOG = Log.getLogger(TinterListener.class);

	private Integer testme=0;
	private String mName;
	private Session mSession;
	private String tinterClass;
	private boolean tinterListenerStarted=false;
	private boolean consoleListenerStarted=false;
	Configuration configuration = Configuration.ReadFromDisk(null);
	private boolean configTinterFlag = false;
	//private  TheUserConsole tinterConsole=new TheUserConsole();

	//final private  SimTinter sim = new SimTinter();
	private AbstractTinterImpl tinter=null;

	/*
	 *  getTinter
	 *  - Instantiate tinter from className retrieved from config
	 */
	
	private AbstractTinterImpl getTinter(String className){
		System.out.println("Get Tinter num times: " + testme);
		if (tinter==null){
			try {
				System.out.println("New tinter instance for  " + className + testme);
				testme++;
				if(this.isConfigTinterFlag()){
					if(className.contains("FMTinter")){
					Constructor c =  Class.forName(className).getConstructor(boolean.class);
					tinter = (AbstractTinterImpl)c.newInstance(false);		
					}
					else{
						tinter = (AbstractTinterImpl) Class.forName(className).newInstance();
					}
						
					
				}
				else{
					tinter = (AbstractTinterImpl) Class.forName(className).newInstance();
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					getmSession().getRemote().sendString("Exception instantiating class " + className);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return this.tinter;
	}
	public AbstractTinterImpl getCurrentTinter(){
		return this.tinter;
	}
	public void initTinterQueueListeners(){
		if(this.isTinterListenerStarted()==false){
			getTinter(getTinterClass()).init();//init to queue
			setTinterListenerStarted(true);


			//init from queue
			ReadFromDeviceQueueThread readFromQ = new ReadFromDeviceQueueThread();		
			readFromQ.setMyQueue(getTinter(getTinterClass()).getFromDeviceQueue());			
			readFromQ.start();			
			
			String junkstring2 = getSWDeviceHandlerVersion();
			/*
			ReadQueueService service = new ReadQueueService();
			service.setMyQueue(getTinter(getTinterClass()).getFromDeviceQueue());
			service.setOnCancelled(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent t) {
					System.out.println("done:" + t.getSource().getValue());
				}
			});
			service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent t) {
					System.out.println("done:" + t.getSource().getValue());
				}
			});
			service.start();
			 */	
		}
	}
	/*
     * Use this function to change devices.  Stop the Queue threads here, set this.tinter to null.
      instantiate new device, run initTinterQueueListeners
	*/
	public void stopTinterQueueListeners(){
	//set the stop flags for the to and from Queues
		if(getCurrentTinter() != null){
			getCurrentTinter().setFromQRunning(false);
			getCurrentTinter().setToQRunning(false);
		}
	}
	public void initConsoleQueueListener(){

		/* turned off because there is no need to send something back through the console, and it makes
		// unit testing difficult.
		if(this.isConsoleListenerStarted()==false){
			ReadQueueService service = new ReadQueueService();
			service.setMyQueue(TheUserConsole.getTinterconsoleoutputqueue());

			service.setOnCancelled(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent t) {
					System.out.println("done:" + t.getSource().getValue());
				}
			});
			service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent t) {
					System.out.println("done:" + t.getSource().getValue());
				}
			});
			service.start();
		}
		 */
	}  


	private void disconnect() {
		if (mSession != null && mSession.isOpen()) {
			mSession.close();
		}

		//mSession = null;
	}

	@Override
	public void onWebSocketConnect(Session session) {

		LOG.info("onWebSocketConnect: {}", session.getRemoteAddress());
		initConsoleQueueListener();

		System.out.println("WebSocket Connect from" + session.getRemoteAddress()); 


		mSession = session;

	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		LOG.info("onWebSocketClose: {} - {}", statusCode, reason);
		disconnect();
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		LOG.warn("onWebSocketError", cause);
		disconnect();
	}

	@Override
	public void onWebSocketText(String json) {

		LOG.info("onWebSocketText: {}", json);

		if(json != null){
			/* DJM  No more echo back to websocket. 
			 try {
				getmSession().getRemote().sendString(this.getClass().getSimpleName() +" received" + json + " from " + getmSession().getRemoteAddress());
			} catch (IOException e1) {
				LOG.warn("Send received string error:", e1.getMessage());
			}
			 */

			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(json).getAsJsonObject();
			String messageName = new String();
			try{
				messageName = (String)(obj.get("messageName").getAsString());
			}
			catch(Exception e){
				e.printStackTrace();
				try {
					getmSession().getRemote().sendString("Exception with Parsing Message Name");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if(messageName.equalsIgnoreCase("TinterMessage")){
				String tintermodel = null;
			
				if(getConfiguration() != null){
					tintermodel = getConfiguration().getModel();  // set model based on file
				}


				TinterMessage msg = new Gson().fromJson(json,TinterMessage.class);
				//  getSim().getTinterConsole().getTotinter().setCommand("In Progress");
				//send connection to tinter console
				if(msg != null){
					if(msg.getConfiguration() != null && msg.getConfiguration().getModel() != null){
						tintermodel = msg.getConfiguration().getModel();  //set model based on configuration of tinter message.
						this.setConfigTinterFlag(true);
						setConfiguration(msg.getConfiguration());
					}
					else{
						this.setConfigTinterFlag(false);
					}
					LOG.info("TinterModel is: " + tintermodel);
					System.out.println("TinterModel is: " + tintermodel);
					if(tintermodel != null){
						if(tintermodel.contains("COROB") || tintermodel.contains("Corob")){
							setTinterClass("com.sherwin.shercolor.swdevicehandler.device.CorobTinter"); 
						}
						else if(tintermodel.startsWith("IFC") ||
								tintermodel.startsWith("FM")){
							setTinterClass("com.sherwin.shercolor.swdevicehandler.device.FMTinter"); 	
						}
						else{
							setTinterClass("com.sherwin.shercolor.swdevicehandler.device.SimTinter"); 	
						}
						initTinterQueueListeners();

						try {
							getTinter(getTinterClass()).getToDeviceQueue().put(msg);

							System.out.println("TinterListener command received from:" + getmSession().getRemoteAddress());
						} catch (InterruptedException e) {
							LOG.warn("Queueput Error", e.getCause());
						}
					}
					else{
						msg.setErrorNumber(-22);
						msg.setErrorMessage("Tinter not configured. Please configure tinter.");
						LOG.warn("Tinter not configured");
						System.out.println("ERROR: Tinter not configured");
						json = new Gson().toJson(msg);
						try {
							getmSession().getRemote().sendString(json );
						} catch (IOException e) {
							LOG.warn("Send Error", e.getCause());
							e.printStackTrace();
						}
					}
				}
				//    WaitForComplete(str);
			}
		}

		// disconnect();
	}

	@Override
	public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
		LOG.info("onWebSocketBinary");
		disconnect();
	}
	
	public String getSWDeviceHandlerVersion() {
    	String result = "";
    	String line = "";
    	//Get file from resources folder
    	InputStream in = getClass().getResourceAsStream("/version.txt"); 
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    	try  {

    	    while ((line = reader.readLine()) != null)
    	    {
    	        result=line;
    	    }

    		reader.close();
    		LOG.info("SWDeviceHandler version " + result);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	return result;
	}
	
	/*
    private void WaitForComplete(String command){
    	boolean inProgress=true;
    	int i=0;
    	while(inProgress){
    		try {
    			getmSession().getRemote().sendString("In progress " + command );
    			String s = TinterListener.getListenerqueue().poll();
    			if(s != null){
    				getmSession().getRemote().sendString("Status: " + s );
    				inProgress=!inProgress;
    			}
    			else{
    				Thread.sleep(1000);
    			}
    		} catch (IOException ex) {
    			LOG.warn(ex);
    		} catch (InterruptedException ex) {
    			// TODO Auto-generated catch block
    			LOG.warn(ex);
    		}
    		i++;
    	}
    	try {
    		getmSession().getRemote().sendString(command + " Complete " );
    	} catch (IOException ex) {

    		LOG.warn(ex);

    	}
    }
	 */
	/*
    public void ProcessListenerThread(Session session, String str) {
    	Thread t = new Thread(){
    		boolean inProgress=true;
    		int i=0;
    		@Override
    		public 	void run(){
    			while(inProgress){
    				try {



    					getmSession().getRemote().sendString("In progress " + str );
    					String s = TinterListener.getListenerqueue().poll();
    					if(s != null){
    						getmSession().getRemote().sendString("Status: " + s );
    						inProgress=!inProgress;
    					}
    					else{
    						Thread.sleep(1000);
    					}
    				} catch (IOException ex) {
    					LOG.warn(ex);
    				} catch (InterruptedException ex) {
    					// TODO Auto-generated catch block
    					LOG.warn(ex);
    				}
    				i++;
    			}
    			try {
    				session.getRemote().sendString(str + " Complete " );
    			} catch (IOException ex) {

    				LOG.warn(ex);

    			}
    		}	};
    }
	 */
	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Session getmSession() {
		return mSession;
	}

	public void setmSession(Session mSession) {
		this.mSession = mSession;
	}



	public String getTinterClass() {
		return tinterClass;
	}
	public void setTinterClass(String tinterClass) {
		this.tinterClass = tinterClass;
	}





	public boolean isTinterListenerStarted() {
		return tinterListenerStarted;
	}
	public void setTinterListenerStarted(boolean tinterListenerStarted) {
		this.tinterListenerStarted = tinterListenerStarted;
	}





	public boolean isConsoleListenerStarted() {
		return consoleListenerStarted;
	}
	public void setConsoleListenerStarted(boolean consoleListenerStarted) {
		this.consoleListenerStarted = consoleListenerStarted;
	}



	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}



	public boolean isConfigTinterFlag() {
		return configTinterFlag;
	}
	public void setConfigTinterFlag(boolean configTinterFlag) {
		this.configTinterFlag = configTinterFlag;
	}



	class  ReadFromDeviceQueueThread extends Thread{
		private BlockingQueue<? extends Message> myQueue=null;

		/* public ReadFromDeviceQueueThread(){
			 //constructor function
		 }
		 */
		public void run() {
			//do this when thread executes
			TinterMessage msg=null;
			if(getCurrentTinter()!=null){
				getCurrentTinter().setFromQRunning(true);
				while (getCurrentTinter().isFromQRunning()){
					try {
						msg =  (TinterMessage) getMyQueue().take();
						if(msg != null){
							String json = new Gson().toJson(msg);
							getmSession().getRemote().sendString(json );
							//System.out.println(json +" sent from console to webpage");
						}

						//Block the thread for a short time, but be sure
						//to check the InterruptedException for cancellation

						Thread.sleep(100);
					} catch (InterruptedException | IOException ex) {
						LOG.info(ex.getMessage());
					}
				}
			}
			else{
				LOG.warn("You attempted to start the FromQ Thread when no tinter is defined");
			}
		}

		public BlockingQueue<? extends Message> getMyQueue() {
			return myQueue;
		}

		public void setMyQueue(BlockingQueue<? extends Message> blockingQueue) {
			this.myQueue = blockingQueue;
		}
	}
	/*
	class ReadQueueService extends Service<Void> {
		//private StringProperty url = new SimpleStringProperty();
		private BlockingQueue<? extends Message> myQueue=null;

		public BlockingQueue<? extends Message> getMyQueue() {
			return myQueue;
		}

		public void setMyQueue(BlockingQueue<? extends Message> blockingQueue) {
			this.myQueue = blockingQueue;
		}

		protected Task<Void> createTask() {
			return new Task<Void>() {

				TinterMessage msg=null;
				@Override protected Void call() throws Exception {

					while (true){

						msg =  (TinterMessage) getMyQueue().take();
						if(msg != null){
							String json = new Gson().toJson(msg);
							getmSession().getRemote().sendString(json );
							//System.out.println(json +" sent from console to webpage");
						}

						//Block the thread for a short time, but be sure
						//to check the InterruptedException for cancellation
						try {
							Thread.sleep(100);
						} catch (InterruptedException interrupted) {
							if (isCancelled()) {
								updateMessage("Cancelled");
								break;
							}
						}
					}
					return null;


				}
			};


		}
	}
	 */
}