using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SWDeviceHandler.domain
{
    public class Colorant
    {
        public String code { get; set; }
        public int shots { get; set; } // colorant shot array
        public int uom { get; set; }// colorant shot uom array
        public int position { get; set; }// colorant pump position array
    }
}
