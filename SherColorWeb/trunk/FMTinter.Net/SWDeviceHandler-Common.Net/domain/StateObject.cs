using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace SWDeviceHandler.domain
{
    // State object for reading client data asynchronously  
    public class StateObject
    {
       
        private const int _BufferSize = 1024;
        private byte[] _buffer = new byte[_BufferSize];

        // Client  socket.  
        public Socket workSocket { get; set; } = null;
        // Size of receive buffer.  
        public static int BufferSize
        {
            get
            {
                return _BufferSize;
            }
        }
        // Receive buffer.  
        public byte[] buffer
        {
            get
            {
                return _buffer;
            }
            set
            {
                _buffer = value;
            }
        }

        // Received data string.  
        public StringBuilder sb { get; set; } = new StringBuilder();
    }
}

