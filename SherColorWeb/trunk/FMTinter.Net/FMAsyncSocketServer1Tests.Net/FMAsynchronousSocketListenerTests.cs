using Microsoft.VisualStudio.TestTools.UnitTesting;
using SWDeviceHandler;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SWDeviceHandler.domain;


namespace SWDeviceHandler.Tests
{
    [TestClass()]
    public class FMAsynchronousSocketListenerTests
    {
        FMAsynchronousSocketListener server = new FMAsynchronousSocketListener();
        [TestMethod()]
        public void DecodeMessageandSendTest()
        {
         
            String json = @"{'id':'dc62e861-5d47-4df3-9b21-5bef2c707888','messageName':'TinterMessage','command':'PurgeAll','status':0,'javaMessage':'','commandRC':0,'errorNumber':0,'errorSeverity':0,'errorMessage':0,'errorList':null}";
            TinterMessage resp = server.DecodeMessageandSend(json);
            Assert.AreEqual("PurgeAll", resp.command);

        }
       
    }
}