using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NUnit.Framework;

namespace SWFluid
{
    [TestFixture]
    class FMTinterTest2
    {
        FMCommandCenter cc = new FMCommandCenter();
        [Test]
        public void testIterateSerialPorts()
        {
            FMCommandCenter.LastDetect = new SWDeviceHandler.domain.TinterMessage();
            FMCommandCenter.LastDetect.command = "StartDriver";
            cc.init();
        }
    }
}
