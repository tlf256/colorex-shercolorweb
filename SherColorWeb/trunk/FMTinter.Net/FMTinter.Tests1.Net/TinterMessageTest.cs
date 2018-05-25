using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SWDeviceHandler.domain;

namespace FMTinter.Tests1
{
    [TestFixture]
    public class TinterMessageTest
    { 

    [Test]
    public void TestfromJSON()
    {
        String json = @"{'id':'dc62e861-5d47-4df3-9b21-5bef2c707888','messageName':'TinterMessage','command':'PurgeAll','status':0,'javaMessage':'','commandRC':0,'errorNumber':0,'errorSeverity':0,'errorMessage':0,'errorList':null}";
            TinterMessage msg = TinterMessage.fromJSON(json);
            Assert.True(msg.command.Contains("PurgeAll"));
    }
    [Test]
        public void TesttoJSON()
        {
            TinterMessage msg = new TinterMessage();
            Error e = new Error();
            e.num = 12345;
            e.message = "Tinter Malfunction Test";
            Console.WriteLine("New Error Detail: code {0} string {1}", e.num, e.message);
            msg.errorList.Add(e);
            msg.command = "PurgeAll";
            string json = msg.toJSON();
            Console.WriteLine(json);
            Assert.True(json.Contains("Malfunction"));
        }
    }
}
