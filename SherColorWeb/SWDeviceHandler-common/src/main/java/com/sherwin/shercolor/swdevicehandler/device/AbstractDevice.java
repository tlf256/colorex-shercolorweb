package com.sherwin.shercolor.swdevicehandler.device;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.sherwin.shercolor.swdevicehandler.domain.Message;


//Place for all common functions, similar to gti.c

public abstract class  AbstractDevice implements AbstractInterface {

	static Logger abstractlogger = LogManager.getLogger(AbstractDevice.class);
	final BlockingQueue<Message> toDeviceQueue =  new LinkedBlockingQueue<Message>();
	final BlockingQueue<Message> fromDeviceQueue =  new LinkedBlockingQueue<Message>();
	private boolean toQRunning = false;  // flag to stop Queue from running.
	private boolean fromQRunning = false;  // flag to stop Queue from running.	
	//colorant level object, etc.

	public BlockingQueue<Message> getToDeviceQueue() {
		return toDeviceQueue;
	}



	public BlockingQueue<Message> getFromDeviceQueue() {
		return fromDeviceQueue;
	}



	public boolean isToQRunning() {
		return toQRunning;
	}



	public void setToQRunning(boolean toQRunning) {
		this.toQRunning = toQRunning;
	}



	public boolean isFromQRunning() {
		return fromQRunning;
	}



	public void setFromQRunning(boolean fromQRunning) {
		this.fromQRunning = fromQRunning;
	}



	public void init(){
		ReadToDeviceQueueThread readToQ = new ReadToDeviceQueueThread();
		if(!this.isToQRunning()){
			readToQ.start();
		}
		else{
			abstractlogger.error("You attempted to init the ToQ Reader for " + this.getClass().getName() + " but it is already running");
			System.out.println("You attempted to init the ToQ Reader for " + this.getClass().getName() + " but it is already running");
		}
	

		/*
		ReadQueueService service = new ReadQueueService();
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
	class  ReadToDeviceQueueThread extends Thread{


		/* public ReadToDeviceQueueThread(){
			 //constructor function
		 }
		 */
		public void run() {
			//do this when thread executes
			setToQRunning(true);
			while (isToQRunning()){
				try{
					Message m = getToDeviceQueue().take();
					UpdateConsole(m);
					ProcessQueueItem(m);//send item to actual tinter or coloreye.
					//Block the thread for a short time, but be sure
					//to check the InterruptedException for cancellation
					//Block the thread for a short time, but be sure
					//to check the InterruptedException for cancellation

					Thread.sleep(100);
				} catch (InterruptedException ex) {
					abstractlogger.info(ex.getMessage());
				}
			}
		}



	}
	/*
	class ReadQueueService extends Service<Void> {
		//private StringProperty url = new SimpleStringProperty();
		protected Task<Void> createTask() {
			return new Task<Void>() {

				@Override protected Void call() throws Exception {
					while (true){

						Message m = toDeviceQueue.take();
						UpdateConsole(m);
						ProcessQueueItem(m);//send item to actual tinter or coloreye.
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
	//default processqueueitem meant to be overridden
	protected void ProcessQueueItem(Message m){
		if(m != null){
			System.out.println("Sending " + m + " to device" );


		}
	}
}
