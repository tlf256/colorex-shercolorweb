using System;
using System.ComponentModel;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SWDeviceHandler.domain;
using SWFluid;
using Newtonsoft.Json;

namespace SWDeviceHandler
{
    // State object for reading client data asynchronously  


    public class FMAsynchronousSocketListener
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        // Thread signal.  
        public static ManualResetEvent allDone = new ManualResetEvent(false);

        Socket handler;
        FMCommandCenter commandCenter = new FMCommandCenter();
        

        public Socket Handler { get => handler; set => handler = value; }

        private bool tinterListenerStarted = false;
        public bool isTinterListenerStarted()
        {
            return tinterListenerStarted;
        }
        public void setTinterListenerStarted(bool tinterListenerStarted)
        {
            this.tinterListenerStarted = tinterListenerStarted;
        }
        public FMAsynchronousSocketListener()
        {
        }

        public void StartListening()
        {
            // Data buffer for incoming data.  
            byte[] bytes = new Byte[1024];

            // Establish the local endpoint for the socket.  
            // The DNS name of the computer  
            // running the listener is "host.contoso.com".  
            IPAddress ipAddress = IPAddress.Loopback;
            IPEndPoint localEndPoint = new IPEndPoint(ipAddress,55218);
            log.Info("Starting Listener...");
            // Create a TCP/IP socket.  
            Socket listener = new Socket(AddressFamily.InterNetwork,
                SocketType.Stream, ProtocolType.Tcp);
            InitTinterQueueThreads();
            // Bind the socket to the local endpoint and listen for incoming connections.  
            try
            {
                listener.Bind(localEndPoint);
                listener.Listen(100);

                while (true)
                {
                    // Set the event to nonsignaled state.  
                    allDone.Reset();

                    // Start an asynchronous socket to listen for connections.  
                    log.Info("Waiting for a connection...");
                    listener.BeginAccept(
                        new AsyncCallback(AcceptCallback),
                        listener);

                    // Wait until a connection is made before continuing.  
                    allDone.WaitOne();
                }

            }
            catch (Exception e)
            {
                log.Error(e.ToString());
            }

            

        }

        public  void AcceptCallback(IAsyncResult ar)
        {
            // Signal the main thread to continue.  
             allDone.Set();
           
            log.Info("A new connection is made at: " + DateTime.Now.ToShortTimeString() );
            // Get the socket that handles the client request.  
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);

            // Create the state object.  
            StateObject state = new StateObject();
            state.workSocket = handler;
            handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                new AsyncCallback(ReadCallback), state);
        }

        public  void ReadCallback(IAsyncResult ar)
        {
            String content = String.Empty;
            Boolean clientClosed = false;

            // Retrieve the state object and the handler socket  
            // from the asynchronous state object.  
            StateObject state = (StateObject)ar.AsyncState;
            Handler = state.workSocket;
            int bytesRead = 0;
            // Read data from the client socket.   
            try
            {
                bytesRead = Handler.EndReceive(ar);
            }
            catch(SocketException e)
            {
                log.Error(e.Message);
                clientClosed = true;
                allDone.Set();
            }

            if (bytesRead > 0)
            {
                // There  might be more data, so store the data received so far.  
                state.sb.Append(Encoding.ASCII.GetString(
                    state.buffer, 0, bytesRead));

                // Check for end-of-file tag. If it is not there, read   
                // more data.  
                content = state.sb.ToString();
               
                if (content.IndexOf("}") > -1)  //end of json message
                {
                    // All the data has been read from the   
                    // client. Display it on the console.  
                    log.InfoFormat("Read {0} bytes from socket. \n Data : {1}",
                        content.Length, content);
                    state.sb =  new StringBuilder();
                    // Send the data to the tinter. 
                    DecodeMessageandSend(content);
                }
                if (!clientClosed)
                {
                    // Get ready to get more and more and more 
                    Handler.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0,
                    new AsyncCallback(ReadCallback), state);
                }
            }
            else
            {
                log.Info("Read 0 bytes from socket. Begin receive again ");
            }
        }
        protected virtual void JsonErrorHandler(object sender, Newtonsoft.Json.Serialization.ErrorEventArgs e)
        {
            log.Error( e.ErrorContext);
        }
        public TinterMessage DecodeMessageandSend(string json)
        {
                       
            Console.WriteLine(json);
            TinterMessage msg = null;
            msg = TinterMessage.fromJSON(json);
            if (msg != null)
            {
                log.InfoFormat(" Enqueuing {0} command", msg.command);
                commandCenter.DeviceQueue.Enqueue(msg);
            }
            return msg;
        }
        /************************************************************************************************/
        private  void SendSocketResponse( String data)
        {
            data = data + "\r\n";
            // Convert the string data to byte data using ASCII encoding.  
            byte[] byteData = Encoding.ASCII.GetBytes(data);
            Console.WriteLine("Sending SocketResponse:");
            log.InfoFormat("Sending SocketResponse {0}  to client.", data);
            Console.WriteLine(data);

            // Begin sending the data to the remote device.  
            handler.BeginSend(byteData, 0, byteData.Length, 0,
                new AsyncCallback(SendCallback), Handler);
        }

        private  void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.  
                Socket handler = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.  
                int bytesSent = 0;
                bytesSent = handler.EndSend(ar);
                log.InfoFormat("Sent {0} bytes to client.", bytesSent);

                // handler.Shutdown(SocketShutdown.Both);
               //  handler.Close();

            }
            catch (Exception e)
            {
                log.Error(e.ToString());
            }
        }
        public void InitTinterQueueThreads()
        {
            if (this.isTinterListenerStarted() == false)
            {
                commandCenter.init();//init to queue
                setTinterListenerStarted(true);


                //init from queue
                Thread readFromQ = new Thread(ReadFromDeviceQueueThread);
               
                readFromQ.Start();
               
            }
        }
      
        private void ReadFromDeviceQueueThread()
        {
            //do this when thread executes
            TinterMessage msg = null;
            string json = "";
            if (commandCenter != null)
            {
                commandCenter.setResponseQRunning(true);
                while (Thread.CurrentThread.IsAlive && commandCenter.isResponseQRunning())
                {
                    try
                    {
                        if (FMCommandCenter.ResponseQueue.Count > 0)
                        {
                            msg = (TinterMessage)FMCommandCenter.ResponseQueue.Dequeue();
                            if (msg != null)
                            {
                                json = msg.toJSON();
                                log.Info("myJSONResp is: " + json);
                            }

                                SendSocketResponse(json);

                        }

                        //Block the thread for a short time, but be sure
                        //to check the InterruptedException for cancellation
                        if (Thread.CurrentThread.IsAlive)
                        {
                            Thread.Sleep(100);
                        }
                    }
                    catch (Exception ex)
                    {
                        log.Error(ex.Message);
                    }
                }
            }
            else
            {
                log.Warn("You attempted to start the FromQ Thread when no tinter is defined");
            }
        }
                /*
                 * Use this function to change devices.  Stop the Queue threads here, set this.tinter to null.
                  instantiate new device, run initTinterQueueListeners
                */
        public void stopTinterQueueListeners()
        {
            //set the stop flags for the to and from Queues
            if (commandCenter != null)
            {
                commandCenter.setResponseQRunning(false);
                commandCenter.setDeviceQRunning(false);
            }
        }
        public static int Main(String[] args)
        {
            FMAsynchronousSocketListener listener = new FMAsynchronousSocketListener();
        
            listener.StartListening();
            return 0;
        }

    }
}