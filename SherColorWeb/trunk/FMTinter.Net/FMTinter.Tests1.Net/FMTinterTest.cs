using NUnit.Framework;
using SWDeviceHandler.domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace SWFluid
{
    [TestFixture]
    public class FMTinterTest
    {
        FMCommandCenter cc = new FMCommandCenter();
        ManualResetEvent eventRaised = new ManualResetEvent(false);
      
        ManualResetEvent detailRaised = new ManualResetEvent(false);
        int TIMEOUT = 50000;
       [SetUp]
        public void TestInit()
        {



            cc = new FMCommandCenter();
            FMCommandCenter.LastDetect = new SWDeviceHandler.domain.TinterMessage();
            FMCommandCenter.LastDetect.command = "Init";

            // cc.Tinter.Dispenser.OnGetExtendedErrorDone  += (sender, e,e2,e3,e4,e5) => { detailRaised.Set(); };
            cc.Tinter.Dispenser.OnInitDone += (sender, e) => { eventRaised.Set(); };





            cc.Tinter.init();

            eventRaised.WaitOne(TIMEOUT); //wait for init baby!!!
            eventRaised.Reset();
           // Assert.IsTrue(FMCommandCenter.LastDetect.commandRC == 0);

            // Assert.Equals(0, FMCommandCenter.LastDetect.commandRC);
        }
        [Test]
        public void TestGetHeartBeat()
        {
            ManualResetEvent hbRaised = new ManualResetEvent(false);
            cc.Tinter.getHeartBeat();
            hbRaised.WaitOne(TIMEOUT);
            Console.WriteLine("Hello");
        }
        [Test]
        public void TestReset()
        {
            ManualResetEvent resetRaised = new ManualResetEvent(false);
            cc.Tinter.reset();
            resetRaised.WaitOne(TIMEOUT);
            Console.WriteLine("Hello");
        }
        [Test]
        public void TestPurge()
        {
            ManualResetEvent purgeRaised = new ManualResetEvent(false);
            
            if (cc.Tinter.Dispenser != null)
            {
               // cc.Tinter.Dispenser.OnPurgeAllDone -= cc.Tinter.OnPurgeDone;
               cc.Tinter.Dispenser.OnPurgeAllDone += (e1, e2) => { purgeRaised.Set(); };
                cc.Tinter.Dispenser.OnGetErrorDetailListDone += (e1, e2, e3) => { detailRaised.Set(); };
                cc.Tinter.Msg = new TinterMessage();
                cc.Tinter.purgeAll();
                purgeRaised.WaitOne(TIMEOUT); //wait for init baby!!!
                detailRaised.WaitOne(TIMEOUT);
               Console.WriteLine("PurgeAllCompleted");
            }
        }

        private void Dispenser_OnGetErrorDetailListDone(int i32sResultCode, string sErrrorString, List<IDD.ErrorDetail> oErrorDetailList)
        {
            throw new NotImplementedException();
        }

        [Test]
        public void TestDispense()
        {
            ManualResetEvent dispenseRaised = new ManualResetEvent(false);
            cc.Tinter.Dispenser.OnDispenseDone += (rc1, rc2, rc3) => { dispenseRaised.Set(); };
            TinterMessage msg = cc.Tinter.Msg;
            Colorant colorant = new Colorant();
            colorant.shots = 1;
            colorant.uom = 1;
            colorant.code = "R3";
            msg.shotList = new List<Colorant>();
            msg.shotList.Add(colorant);
            cc.Tinter.dispense();
            dispenseRaised.WaitOne(TIMEOUT);
            Console.WriteLine("DispenseComplete");

        }   
    }
}
