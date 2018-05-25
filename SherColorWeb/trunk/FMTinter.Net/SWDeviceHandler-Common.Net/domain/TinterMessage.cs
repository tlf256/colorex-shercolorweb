using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SWDeviceHandler.domain;
using Newtonsoft.Json;


namespace SWDeviceHandler.domain
{
    public class TinterMessage : Message
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        public String command { get; set; }                    //command to execute
                                                                                       /*String colorantSystem;                   // Colorant system in use
                                                                                       String model;                     // tinter model
                                                                                       String serial;                    // tinter serial
                                                                                       List<Canister> canisterLayout =  new ArrayList<Canister>(); //colorant assignments
                                                                                       */


        public List<Colorant> shotList { get; set; }


        /* 
         String clrntCode;
         short clrntShots;   // colorant shot array
         short clrntUom;   // colorant shot uom array
         short clrntPumpPos;   // colorant pump position array

         */

        public long status { get; set; }                       // Status Code, 0 = successful completion 
                                                        // 1 = command still in process
                                                        // 2 = Fail - no color specified
                                                        // 3 = Bad UOM or Can't talk to GTI or Command not recognized
                                                        // Anything else will be type of status returned from GTI as
                                                        //      returned by GetRespRec 
                                                        //   String companyModel;              // model with the company name prepended (if not a sherwin store) 11/1/04 TAH
                                                        //   String DispenserFware;            // Dispenser Firmware version
                                                        //	String posVersion; //char posVersion[7];
                                                        //	String tinterDriverVersion; //char tinterDriverVersion[10];   String DispenserDrvVer;           // Dispenser driver version
                                                        //   String GTIVer;                    // GTI version
                                                        //    String PCModel;                   // PC model number
                                                        //    String PCSN;                      // PC serial number
                                                        //    long lLastCommand;                  // If the current command is a get response, 
                                                        // this is the command
                                                        // which was executing before the get response
                                                        //    long lMisc;                         // Miscellaneous data that needs to be passed
                                                        //      i.e. GTI transaction ID
        public String javaMessage { get; set; }                 //exception, file not found, etc


        public long commandRC { get; set; }                   //initial return code from Dsp command


        public long errorNumber { get; set; }                  // Actual GTI+Driver response code
        public short errorSeverity { get; set; }
        public String errorMessage { get; set; }
        public List<Error> errorList { get; set; }
        public long lastInitDate { get; set; }  //optional field to send back last detect date.


        public TinterMessage()
        {
            shotList= new List<Colorant>();
            errorList= new List<Error>();
        }
        protected virtual void JsonErrorHandler(object sender, Newtonsoft.Json.Serialization.ErrorEventArgs e)
        {
            log.Error(e.ErrorContext);
        }
        public static void StaticJsonErrorHandler(object sender, Newtonsoft.Json.Serialization.ErrorEventArgs e)
        {
            log.Error(e.ErrorContext);
        }
        public static TinterMessage fromJSON(string json)
        {
            TinterMessage msg = null;
            if (!string.IsNullOrEmpty(json))
            {
                var settings = new JsonSerializerSettings
                {
                    Error = (sender, args) =>
                    {
                        log.Error(args.ErrorContext.Error.Message);
                        if (System.Diagnostics.Debugger.IsAttached)
                        {
                            System.Diagnostics.Debugger.Break();
                        }
                    }
                };
                settings.NullValueHandling = NullValueHandling.Ignore;
                try
                {
                    settings.Error += StaticJsonErrorHandler;
                    msg = JsonConvert.DeserializeObject<TinterMessage>(json, settings);
                }
                catch (Exception ex)
                {
                    log.Error(ex.StackTrace);
                }
            }
            return msg;
        }
        public string toJSON()
        {
            string json = "";
           
                var settings = new JsonSerializerSettings
                {
                    Error = (sender, args) =>
                    {
                        log.Error(args.ErrorContext.Error.Message);

                    }
                };
                settings.NullValueHandling = NullValueHandling.Ignore;
                try
                {
                    settings.Error += JsonErrorHandler;
                    json = JsonConvert.SerializeObject(this, settings);

                }
                catch (Exception ex)
                {
                    log.Error(ex.StackTrace);
                }


                //System.out.println(json +" sent from console to webpage");
            
            return json;
        }

        /*
        //getters, setters from java that we use from time to time.
        public String getCommand()
        {
            return command;
        }
        public void setCommand(String command)
        {
            this.command = command;
        }


        public List<Colorant> getShotList()
        {
            return shotList;
        }
        public void setShotList(List<Colorant> shotList)
        {
            this.shotList = shotList;
        }
        public long getStatus()
        {
            return status;
        }
        public void setStatus(long status)
        {
            this.status = status;
        }
        public String getJavaMessage()
        {
            return javaMessage;
        }
        public void setJavaMessage(String javaMessage)
        {
            this.javaMessage = javaMessage;
        }
        public long getCommandRC()
        {
            return commandRC;
        }
        public void setCommandRC(long commandRC)
        {
            this.commandRC = commandRC;
        }
        public long getErrorNumber()
        {
            return errorNumber;
        }
        public void setErrorNumber(long errorNumber)
        {
            this.errorNumber = errorNumber;
        }
        public short getErrorSeverity()
        {
            return errorSeverity;
        }
        public void setErrorSeverity(short errorSeverity)
        {
            this.errorSeverity = errorSeverity;
        }
        public String getErrorMessage()
        {
            return errorMessage;
        }
        public void setErrorMessage(String errorMessage)
        {
            this.errorMessage = errorMessage;
        }
        public List<Error> getErrorList()
        {
            return errorList;
        }
        public void setErrorList(List<Error> errorList)
        {
            this.errorList = errorList;
        }
        public long getLastInitDate()
        {
            return lastInitDate;
        }
        public void setLastInitDate(long lastInitDate)
        {
            this.lastInitDate = lastInitDate;
        }
        */
    }
}
