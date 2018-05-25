using System;
using System.Collections.Generic;

using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;


#region FM_TYPES typedefs

using I8S = System.SByte;
using I8U = System.Byte;
using I16S = System.Int16;
using I16U = System.UInt16;
using I32S = System.Int32;

using FP64S = System.Double;
using CHAR = System.Char;
using FM_SAPI__HANDLE = System.Byte;
using FM_SAPI__CALLBACK = System.Delegate;
using IDD;



#endregion

using SWDeviceHandler.domain;
using System.IO.Ports;
using System.Diagnostics;
using System.Xml;
using System.Text.RegularExpressions;

namespace SWFluid
{

    public class FMTinter
    {
        static string xmlFilename = "c:\\Program Files (x86)\\Fluid Management\\FM_IDD\\FM_SYSTEM.XML";
        TinterMessage msg = new TinterMessage();
        Dispenser2 dispenser = new Dispenser2();
        static AutoResetEvent resetEventRaised = new AutoResetEvent(false);

        public TinterMessage Msg { get => msg; set => msg = value; }
        public Dispenser2 Dispenser { get => dispenser; set => dispenser = value; }


        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        private bool initRunning  {get; set;} = false;
        public FMTinter()
        {
            dispenser.OnPurgeAllDone += OnPurgeDone;
            dispenser.OnGetErrorDetailListDone += OnGetErrorDetailDone;
            dispenser.OnResetDone += OnResetDone;
            dispenser.OnShutdownDone += OnShutdownDone;
            dispenser.OnInitDone += OnInitDone;
            dispenser.OnInitProgress += OnInitProgress;
            dispenser.OnGetHeartbeatStatusDone += OnGetHeartbeatStatusDone;
            dispenser.OnSelfTestDone += OnSelfTestDone;
            dispenser.OnOpenNozzleDone += OnOpenNozzleDone;
            dispenser.OnCloseNozzleDone += OnCloseNozzleDone;
            dispenser.OnDispenseDone += OnDispenseDone;


            //status = l;
        }
        public void init()
        {
           
            if (!this.initRunning) // make sure we don't start the thread twice
            {
                Thread initThread = new Thread(InitThread);
                initThread.Start();
               // InitThread();
            }
            else
            {
                FMCommandCenter.LastDetect.errorMessage = "Detect already in progress.";
                FMCommandCenter.LastDetect.errorNumber = 1;

            }
        }
        private void InitThread()
        {
            /*
             *  a positive error number and status of one will cause the web page to remain in "Init Status" mode.
             *  if the error number goes negative or the message says: Initialization Done, the page will go 
             *  to init error or init complete mode.  When we get detect error codes, we want to iterate to the next
             *  serial port instead of returning that error to the webpage, so you see us checking for certain 
             *  error codes before allowing the Last Detect to be written.  The Last Detect is how we communicate with 
             *  the web page who is constantly checking it by sending "Init Status" commands over and over.
             */
            initRunning = true;
            log.Info("Begin init");
            FMCommandCenter.LastDetect.errorMessage = "Checking for tinter on Last known Serial Port";
            FMCommandCenter.LastDetect.errorNumber = 1;
            FMCommandCenter.LastDetect.status = 1;
            //try existing serial port
            startFMInit();

            bool timeout_did_not_occur = FMTinter.resetEventRaised.WaitOne(120000);

          
            if (timeout_did_not_occur == false || FMCommandCenter.LastDetect.errorNumber == -104 ||
               FMCommandCenter.LastDetect.errorNumber == -54 ||
               FMCommandCenter.LastDetect.errorNumber == -1 ||
               FMCommandCenter.LastDetect.errorNumber == -51 ||
                FMCommandCenter.LastDetect.errorNumber == -91

               )
            {
                if (GetPortTypeXML().Equals("SERIAL"))
                {
                    iterateSerialPorts();
                }
                else
                {
                    log.InfoFormat("Type {0}.  Do not iterate serial port: ", GetPortTypeXML());
                }
            }
            
            Debug.WriteLine(FMCommandCenter.LastDetect.errorMessage);
            initRunning = false;
            if(FMCommandCenter.LastDetect.errorNumber == -51)
            {
                FMCommandCenter.LastDetect.errorMessage = "Serial Port Locked up.  Please reboot your PC to finish detecting tinter.";
            }
            FMCommandCenter.LastDetect.status = 0;

        }
        public void iterateSerialPorts()
        {
            
                string[] ports = SerialPort.GetPortNames();

           

            // Display each port name to the console.
            foreach (string port in ports)
            {
                log.Info("Begin iterating serial port: " + port);
                FMCommandCenter.LastDetect.errorNumber = 1;
                FMCommandCenter.LastDetect.errorMessage = "Checking for tinter on Serial Port: " + port;
      
                Debug.WriteLine(port);
                updateXML(port);
                dispenser.StartInit(xmlFilename);
                FMTinter.resetEventRaised.Close();
                FMTinter.resetEventRaised = new AutoResetEvent(false);
                bool timeout_did_not_occur = FMTinter.resetEventRaised.WaitOne(120000);
              
                if(timeout_did_not_occur && FMCommandCenter.LastDetect.errorNumber != -104 &&
                   FMCommandCenter.LastDetect.errorNumber != -54 &&
                   FMCommandCenter.LastDetect.errorNumber != -91 &&
                   FMCommandCenter.LastDetect.errorNumber != -51
                   )
                {
                    break;
                }
            }
        }
        public void updateXML(string comPort) {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(xmlFilename);
            XmlNodeList nodeList = xmlDoc.GetElementsByTagName("FM_SYSTEM");

            XmlElement root = (XmlElement)nodeList[0];
            XmlNode node = root.SelectSingleNode("FM_DISPENSERS/FM_DISPENSER/COMMUNICATION");
            XmlNode item = node.Attributes.GetNamedItem("name");
            item.Value = comPort;


            xmlDoc.Save(xmlFilename);
        }
        public string GetPortTypeXML()
        {
            string portType = "";
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(xmlFilename);
            XmlNodeList nodeList = xmlDoc.GetElementsByTagName("FM_SYSTEM");

            XmlElement root = (XmlElement)nodeList[0];
            XmlNode node = root.SelectSingleNode("FM_DISPENSERS/FM_DISPENSER/COMMUNICATION");
            XmlNode item = node.Attributes.GetNamedItem("type");
            if (item != null)
            {
                portType = item.Value;
            }
            return portType;
        }
        public void getHeartBeat()
        {
            // dispenser.StartGetHeartbeatStatus();
            reset();
        }

        public void reset()
        {
            dispenser.StartReset();
          
        }
        public void shutdown()
        {
              dispenser.StartShutdown();
        }
        private void startFMInit()
        {  
            dispenser.StartInit(xmlFilename);
        }
        
        public void purgeAll()
        {
           
          
            dispenser.StartPurgeAll(0.001);
        }
        public void pulseTest()
        {
           
            dispenser.StartSelfTest();

        }
        

        public I32S openNozzle()
        {

            return dispenser.SetNozzle(true);
        }
        public I32S closeNozzle()
        {

            return dispenser.SetNozzle(false);
            
        }
        public I32S agitateOn()
        {
            
            return dispenser.SetAgitation(true);
        }
        public I32S agitateOff()
        {
            return dispenser.SetAgitation(false);
        }
        public I32S recirculateOn()
        {

            return dispenser.SetRecirculation(true);
        }
        public I32S recirculateOff()
        {
            return dispenser.SetRecirculation(false);
        }
        public float OunceToLiter(double oz)
        {
            return (float) (oz * 29.57353 / 1000.0);
        }

        public double DispenseAmt(double shots, int uom)
        {          
            double amt = 0.0;
            if (uom == 0)
            {
                uom = 128;
            }
            amt = (double)(shots / (double)uom);
            return amt;
        }
        public void SetAllFull()
        {
            for (int i = 0; i < 16; i++)
            {
                Canister2 can = dispenser.GetCanisterByIndex(i);
                if (can != null)
                {
                    double max = can.MaximumLevel;
                    can.CurrentLevel = max;
                }
            }
        }

        public int dispense()
        {
            int rc = 0;
 
            if (dispenser.DispenseJob != null)
            {
                dispenser.DispenseJob.Clear();
            }

            SetAllFull();
             
            if (Msg.shotList != null)
            {
                foreach (Colorant colorant in Msg.shotList) {
                    double amt = DispenseAmt(colorant.shots, colorant.uom);
                    AddIngredient(colorant.code, IDD.TAmountUnit.unitLiter, OunceToLiter(amt));
                }
                rc = dispenser.StartDispense();
            }
            return rc;
        }

        // This will be called whenever the list changes:
        private void UpdateDetailStatus(int i32sResultCode, string sErrorString, List<ErrorDetail> oErrorDetailList)
        {
       
         
            foreach (ErrorDetail detail in oErrorDetailList){
                Msg.errorNumber = -3084;
                Msg.errorMessage = "";
                Error e = new Error();
                e.num = detail.ErrorCode;
                e.message = detail.ErrorText;
                Debug.WriteLine("New Error Detail: code {0} string {1}", e.num, e.message);
                log.InfoFormat("code {0} string {1}", e.num, e.message);

                Msg.errorList.Add(e);
            }
            log.InfoFormat("msg code {0} string {1}", Msg.errorNumber, Msg.errorMessage);
            FMCommandCenter.Respond(Msg);
        }
      
        private void UpdateStatus(int i32sResultCode, string sErrorString)
        {
            //set initial response in case no error/no error in error List.
            Msg.errorNumber = i32sResultCode;
            Msg.errorMessage = sErrorString;

            dispenser.StartGetErrorDetailList();
        }
       /* private void OldUpdateStatus(int i32sResultCode, string sErrorString)
        {
            I32S i32sExtendedErrorNum;
            string sExtendedErrorString;
            Msg.commandRC = i32sResultCode;
            if (i32sResultCode < 0)
            {
                ParseExtendedError(sErrorString, out i32sExtendedErrorNum, out sExtendedErrorString);
                Msg.errorNumber = i32sExtendedErrorNum;
                Msg.errorMessage = sExtendedErrorString;
            }
            else
            {
                Msg.errorNumber = i32sResultCode;
                Msg.errorMessage = sErrorString;
            }
           
          
            //  MainWindow.main.Status = text;
            log.InfoFormat("code {0} string {1}", i32sResultCode,sErrorString );
           
            FMCommandCenter.Respond(Msg);
        }
        */
        private void OnSelfTestDone(int i32sResultCode, string sErrorString)
        {
            Debug.WriteLine("Self Test Done.");
            UpdateStatus(i32sResultCode,sErrorString);

        }
        private void OnOpenNozzleDone(int i32sResultCode, string sErrorString)
        {
            Debug.WriteLine("Nozzle Open Done.");
            UpdateStatus(i32sResultCode, sErrorString);

        }
        private void OnCloseNozzleDone(int i32sResultCode, string sErrorString)
        {
            Debug.WriteLine("Nozzle Close Done.");
            UpdateStatus(i32sResultCode, sErrorString);

        }
       public void OnInitProgress(I32S i32sBoard, I32S i32sDevice, I32S i32sPercent, I32S i32sAction, string sText)
        {
            FMCommandCenter.LastDetect.command = "Init";
            FMCommandCenter.LastDetect.errorNumber = i32sPercent;
            FMCommandCenter.LastDetect.errorMessage = sText;
            Debug.WriteLine(sText);
          //user will query the last detect errornumber and message
          //  UpdateStatus(i32sPercent, sText);
        }
            public void OnInitDone(int i32sResultCode, string sErrorString)
            //we will not send a response back because user will query the last detect for the string Initialization Done.
            {
            I32S i32sExtendedErrorNum = 0;
            string sExtendedErrorString;
            //dispenser.OnGetExtendedErrorDone += OnInitGetExtendedErrorDone;
            //I32S rc = dispenser.StartGetExtendedError();
            log.Info("Init Done rc:" + i32sResultCode + " message:" + sErrorString);
            if (i32sResultCode < 0)
            {
                ParseExtendedError(sErrorString,out i32sExtendedErrorNum,out sExtendedErrorString);
                FMCommandCenter.LastDetect.command = "Init";
                FMCommandCenter.InitComplete = true;
                FMCommandCenter.LastDetect.errorNumber = i32sExtendedErrorNum;
                FMCommandCenter.LastDetect.errorMessage = sExtendedErrorString;
                string date = DateTime.Now.ToString("yyyyMMdd");
                FMCommandCenter.LastDetect.lastInitDate = Int32.Parse(date);
                log.Info("Init Done extended rc:" + i32sExtendedErrorNum + " extended message:" + sExtendedErrorString);
                resetEventRaised.Set();
            }
            else
            {
                FMCommandCenter.LastDetect.command = "Init";
                FMCommandCenter.InitComplete = true;
                FMCommandCenter.LastDetect.errorNumber = i32sResultCode;
                FMCommandCenter.LastDetect.errorMessage = sErrorString;
                string date = DateTime.Now.ToString("yyyyMMdd");
                FMCommandCenter.LastDetect.lastInitDate = Int32.Parse(date);
                log.Info("Init Done extended rc:" + i32sResultCode + " error message:" + sErrorString);
                resetEventRaised.Set();
            }


            // UpdateStatus(i32sResultCode, sErrorString);

            /*
            if (i32sResultCode >= Dispenser2.IDD_RESULT__OK) {
                if (dispenser.IsInitialized)
                {
                    UpdateStatus(i32sResultCode, sErrorString);
                }
                else
                {
                    MessageBox.Show("Init Warning: " + sErrorString + "(" + i32sResultCode + ")" );
                }

            }
            else
            {
                MessageBox.Show("Init Error: " + sErrorString + "(" + i32sResultCode + ")" );
            }
            */

        }
        private void ParseExtendedError(string errorString, out I32S errorNumber, out string errorStringOut)
        {
            
            bool numberFound = false;
            string[] words = errorString.Split(' ');
            errorNumber = -1;
            string outString = "";
            log.Info(errorString);
            foreach (string word in words)
            {
                Debug.Write(word);
                if(Regex.IsMatch(word, @"\d:")) // if it is a number, it must be the error number
                {
                    string num = word.Remove(word.Length-1);
                    int.TryParse(num, out errorNumber);
                    numberFound = true;

                }
                else if (numberFound && !word.Contains("["))
                {
                    if(outString.Length > 0)
                    {
                        outString += " ";//space between words
                    }
                    outString = outString + word;
                }
                else if (word.Contains("["))
                {
                    break;
                }
            }
            errorStringOut = outString;
        }
        /*
        public void OnInitGetExtendedErrorDone(short i16sErrorCode, int i32sExtendedError, short i16sState, short i16SubsState, short i16sTask, string sErrorString)
        {
            {
                FMCommandCenter.LastDetect.command = "Init";
                FMCommandCenter.InitComplete = true;
                FMCommandCenter.LastDetect.commandRC = i16sErrorCode;
                FMCommandCenter.LastDetect.errorNumber = i32sExtendedError;
                FMCommandCenter.LastDetect.errorMessage = sErrorString;
                string date = DateTime.Now.ToString("yyyyMMdd");
                FMCommandCenter.LastDetect.lastInitDate = Int32.Parse(date);
                resetEventRaised.Set();
            }
        }
        */
        public void OnGetErrorDetailDone(int i32sResultCode, string sErrorString, List<ErrorDetail> oErrorDetailList)   
        {
            Debug.WriteLine("geterrordetail: code {0} extended  string {1} ", i32sResultCode,  sErrorString);
            log.InfoFormat("getextended: code {0} extended   string {1} ", i32sResultCode,  sErrorString);
            UpdateDetailStatus(i32sResultCode, sErrorString, oErrorDetailList);
        }
        /*
        public void OnEventGetExtendedErrorDone(short i16sErrorCode, int i32sExtendedError, short i16sState, short i16SubsState, short i16sTask, string sErrorString)
        {
            Debug.WriteLine("getextended: code {0} extended {1} state {2} substate {3} task {4} string {5} ", i16sErrorCode, i16sState, i16SubsState, i16sTask, sErrorString);
           log.InfoFormat("getextended: code {0} extended {1} state {2} substate {3} task {4} string {5} ", i16sErrorCode, i16sState, i16SubsState, i16sTask, sErrorString);

            //UpdateStatus(i32sResultCode, sErrorString);
        }
        */
        private void OnGetHeartbeatStatusDone(int i32sResultCode, string sErrorString, THeartbeatStatus tHeartbeatStatus)

        {
            I32S rc = (I32S)tHeartbeatStatus;
            Debug.WriteLine(rc + sErrorString);
            UpdateStatus(rc, sErrorString);
        }
        public void OnShutdownDone(I32S i32sResultCode, string sErrorString)
        {

            UpdateStatus(i32sResultCode, sErrorString);
        }
        public void OnResetDone(I32S i32sResultCode, string sErrorString)
        {
            Debug.WriteLine("Reset Done" + i32sResultCode + sErrorString);
            resetEventRaised.Set();
            UpdateStatus(i32sResultCode, sErrorString);
        }
        public void OnPurgeDone(I32S i32sResultCode, string sErrorString)
        {
            Debug.WriteLine("Purge Done");
            try
            {
               
               
                UpdateStatus(i32sResultCode, sErrorString);
            }
            catch (Exception ex)
            {
                LogError("PurgeAll" + ": Error:[" + ex.Message + "]");
            }
            /*
            if (i32sResultCode == Dispenser2.IDD_RESULT__OK)
            {
                MessageBox.Show("Purge Complete " + sErrorString + "(" + i32sResultCode + ")");
            }
            else if (i32sResultCode > Dispenser2.IDD_RESULT__OK)
            {
                MessageBox.Show("Purge Warning " + sErrorString + "(" + i32sResultCode + ")");
            }
            else
            {
                MessageBox.Show("Purge Error " + sErrorString + "(" + i32sResultCode + ")");
            }
            */
        }
        private void OnDispenseDone(I32S i32sResultCode, string sErrorString, string sErrorSource)
        {
            UpdateStatus(i32sResultCode, sErrorString);
            /*
            if (i32sResultCode == Dispenser2.IDD_RESULT__OK)
            {
                MessageBox.Show("Dispense Complete " + sErrorString + "(" + i32sResultCode + ") Source= " + sErrorSource);
            }
            else if (i32sResultCode > Dispenser2.IDD_RESULT__OK)
            {
                MessageBox.Show("Dispense Warning " + sErrorString + "(" + i32sResultCode + ") Source= " + sErrorSource);
            }
            else
            {
                MessageBox.Show("Dispense Error " + sErrorString + "(" + i32sResultCode + ") Source= " + sErrorSource);
            }
            Canister2 can1 = dispenser.GetCanisterById("1");
            double level1 = can1.CurrentLevel;

            String newLevel = "Level After Dispense: " + level1.ToString();
            UpdateStatus(newLevel);
            */
        }

        private void AddIngredient(string stID, IDD.TAmountUnit eUnit, float fAmount)
        {
            //Keep an action string for debugging.
            string stAction = "";


            //Attempt to add an ingredient to the DispenseJob
            IDD.Ingredient2 oIngredientNew = null;
            try
            {
                stAction = "New Ingredient";
                //Use the parent to try and get a new ingredient.
                oIngredientNew = dispenser.DispenseJob.AddIngredient();
            }
            catch (Exception ex)
            {
                LogError(stAction + ": Error:[" + ex.Message + "]");
            }
            //Falure by the DispenseJob to add an ingredient will result in a null
            //  ingredient object reference being returned.
            if (oIngredientNew != null)
            {
                //Fill in the details for the added ingredient
                stAction = "Ingredient Config";
                oIngredientNew.ComponentId = stID;
                oIngredientNew.AmountUnit = eUnit;
                oIngredientNew.PreferredAmount = fAmount;
                oIngredientNew.MinimumAmount = fAmount;
                oIngredientNew.MaximumAmount = fAmount;
                //Update the local representation
                stAction = "Ingredient Display";
               // int iRow = this.dgIngredients.Rows.Add();
               // this.dgIngredients.Rows[iRow].Cells["ID"].Value = stID;
                //this.dgIngredients.Rows[iRow].Cells["Units"].Value = eUnit;
                //this.dgIngredients.Rows[iRow].Cells["Amount"].Value = fAmount;
            }
            else
            {
               LogError("Add ingredient not accepted.");
            }
            //Update ingredient count display
          //  this.datCount.Text = dispenser.DispenseJob.IngredientCount.ToString();
        }
        private void LogError(string text)
        {
            Debug.WriteLine(text);
            log.Error(text);
        }
    }
}
