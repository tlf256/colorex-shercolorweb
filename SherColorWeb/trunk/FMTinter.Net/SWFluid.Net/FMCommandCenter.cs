using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SWDeviceHandler.domain;
using System.Threading;


namespace SWFluid
{
    public class FMCommandCenter
    {
        Queue<TinterMessage> deviceQueue = new Queue<TinterMessage>();
        private static Queue<TinterMessage> responseQueue = new Queue<TinterMessage>();
        private bool deviceQRunning = false;  // flag to stop Queue from running.
        private bool responseQRunning = false;
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        public static bool InitComplete { get; set; } = false;
        public static TinterMessage LastDetect { get; set; }
        public Queue<TinterMessage> DeviceQueue { get => deviceQueue; set => deviceQueue = value; }
        public static Queue<TinterMessage> ResponseQueue { get => responseQueue; set => responseQueue = value; }
        
        private FMTinter tinter = new FMTinter();
        public FMTinter Tinter { get => tinter; set => tinter = value; }

        public bool isDeviceQRunning()
        {
            return deviceQRunning;
        }


        public void setDeviceQRunning(bool toQRunning)
        {
            this.deviceQRunning = toQRunning;
        }


        public bool isResponseQRunning()
        {
            return responseQRunning;
        }



        public void setResponseQRunning(bool responseQRunning)
        {
            this.responseQRunning = responseQRunning;
        }
        public void init()
        {
            InitComplete = false;
            Thread readDeviceQ = new Thread(ReadDeviceQueueThread);
            if (!this.isDeviceQRunning()) // make sure we don't start the thread twice
            {
                readDeviceQ.Start();
                Tinter.Msg.command="StartDriver";
                LastDetect = CloneObject.Clone(Tinter.Msg);  //reset the lastDetect status;

                Tinter.init();
            }
            else
            {
                log.Error("You attempted to init the ToQ Reader for " + this.GetType().Name + " but it is already running");
                Console.WriteLine("You attempted to init the ToQ Reader for " + this.GetType().Name + " but it is already running");
            }
        }
        public void Quit()
        {
            log.Info("Received poison pill");
            this.setResponseQRunning(false);
            this.setDeviceQRunning(false);
            System.Environment.Exit(1);
            System.Environment.FailFast("shutting down");
        }
        protected void ProcessQueueItem(TinterMessage msg)
        {
            
            log.InfoFormat("Received item in FM queue. Command = {0}",msg.command);
            if (msg != null && msg.messageName != null)
            {
                switch (msg.messageName)
                {
                    case "TinterMessage":


                        String command = msg.command;

                        if (command != null)
                        {
                            /*		Configuration new_config = msg.getConfiguration();
                            if(new_config !=null  && new_config.getCanisterLayout() != null){
                                try{
                                    this.setConfiguration(new Configuration(new_config));
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                             */
                            tinter.Msg = msg;  // pass the message to the FMTinter class
                            Int32 rc = 0;
                            switch (command)
                            {

                                case "GetHeartBeat":
                                    tinter.getHeartBeat();
                                    break;
                                case "Quit": // the poison pill
                                    Quit();
                                    break;
                                case "Agitate":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    tinter.agitateOn();
                                    Thread.Sleep(30000);
                                    rc = tinter.agitateOff();
                                    msg.errorNumber=rc;
                                    msg.errorMessage="";
                                    Respond(msg);
                                    log.Info("rc=" + msg.errorNumber + " " + msg.errorMessage);
                                    break;

                                case "CloseNozzle":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    rc = tinter.closeNozzle();
                                    msg.errorNumber=rc;
                                    msg.errorMessage="";
                                   
                                    log.Info("rc=" + msg.errorNumber + " " + msg.errorMessage);
                                    break;
                                case "Detect":
                                case "Init":// this is for an initialize command not startup
                                    LastDetect = msg; 
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    tinter.init();
                                    log.Info("rc=" + msg.errorNumber + " " + msg.errorMessage);
                                    break;
                                case "DetectStatus":
                                case "InitStatus":
                                    LastDetect.id = msg.id;
                                    LastDetect.command = "InitStatus";
                                      Respond(LastDetect);  //status of last detect/init command
                                    break;
                                case "Dispense":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    rc = tinter.dispense();
                                    if (rc < 0)
                                    {
                                        msg.errorNumber = rc;
                                        Respond(msg);
                                    }
                                    else
                                    {
                                        Console.WriteLine("Dispense good.");
                                    }
                                        break;
                                case "OpenNozzle":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    tinter.openNozzle();
                                    msg.errorNumber=rc;
                                    msg.errorMessage="";
                                  
                                    log.Info("rc=" + msg.errorNumber + " " + msg.errorMessage);
                                    break;
                                case "PurgeAll":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    tinter.purgeAll();
                                    break;
                                case "Recirculate":
                                    log.Info(this.GetType().Name + " Sending " + command + " to device");
                                    tinter.recirculateOn();
                                    Thread.Sleep(30000);
                                    rc = tinter.recirculateOff();
                                    msg.errorNumber = rc;
                                    msg.errorMessage = "";
                                    Respond(msg);
                                    log.Info("rc=" + msg.errorNumber + " " + msg.errorMessage);
                                    break;
                                default:
                                    msg.errorMessage=command + " not recognized as a valid tinter command.";
                                    msg.errorNumber=-1005;
                                    Respond(msg);
                                    break;
                            }
                        }

                        log.Info("Process Queue Item...Message Name: " + msg.messageName + msg.command + " commmand started");
                        log.Info("Process Queue Item...Message Name: " + msg.messageName + msg.command + " command started");
                        break;

                    default:
                        msg.errorMessage="Message Name: " + msg.messageName + " not recognized";
                        log.Error("Message Name: " + msg.messageName + " not recognized");
                        msg.messageName = "TinterMessage";
                        Respond(msg);
                        break;
                }
            }
            else
            {
                msg.messageName = "TinterMessage";
                msg.errorMessage="Message or message name was null";
                log.Error("Message or message name was null");
                Respond(msg);
            }
        }

        public static void Respond(TinterMessage respmsg)
        {
            if (respmsg.command != "StartDriver") // startup is saved in last detect, 
                                                   // and is not started by a request from the user, rather, it starts automatically when the program starts!
                                                   // therefore ,do not send a response since nobody is waiting for it.
            {
                ResponseQueue.Enqueue(respmsg);
            }
            else
            {
                Console.WriteLine("StartDriver.  Not sending message back.");
            }
        }

        private void ReadDeviceQueueThread()
        {
            TinterMessage m = null;
            //do this when thread executes
            setDeviceQRunning(true);
            while (isDeviceQRunning())
            {
                try
                {
                    if(DeviceQueue.Count > 0) m = DeviceQueue.Peek();  //peek so we can check command and let InitStatus in.
                    if (DeviceQueue.Count > 0 && (InitComplete || (m!=null && m.command.Contains("InitStatus"))))
                    {
                         m = DeviceQueue.Dequeue();

                        ProcessQueueItem(m);//send item to actual tinter.
                    }
                                        //Block the thread for a short time, but be sure
                                        //to check the InterruptedException for cancellation
                                        //Block the thread for a short time, but be sure
                                        //to check the InterruptedException for cancellation

                    Thread.Sleep(100);
                }
                catch (Exception ex)
                {
                    m.errorMessage = ex.Message;

                    Respond(m);
                    log.Error(ex.Message);
                }
            }
        }



    }

}
