package com.sherwin.shercolor.swdevicehandler.jetty;

//import org.eclipse.jetty.http.HttpVersion;
//import org.eclipse.jetty.server.HttpConfiguration;
//import org.eclipse.jetty.server.HttpConnectionFactory;
//import org.eclipse.jetty.server.SecureRequestCustomizer;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
//import org.eclipse.jetty.server.SslConnectionFactory;
//import org.eclipse.jetty.server.handler.ContextHandler;
//import org.eclipse.jetty.server.handler.ContextHandlerCollection;
//import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

//import com.sherwin.shercolor.swdevicehandler.view.TheUserConsole;

@SuppressWarnings("serial")
public class TinterHandler extends WebSocketHandler {
   // private final static String HOST = "127.0.0.1";
   // private final static int WS_PORT = 80;
  //  private final static int WSS_PORT = 8443;
    
//	private static final int MAX_MESSAGE_SIZE = 60000;
    
    public TinterHandler(){
    	super();
    }
   
	
    @Override
    public void configure(WebSocketServletFactory factory) {
   // DJM it's here if we need it, but we don't need it. 	factory.getPolicy().setMaxTextMessageSize(MAX_MESSAGE_SIZE); // so I can download the ecal and the gdata all at once.
        factory.register(TinterListener.class);
    }
    
}