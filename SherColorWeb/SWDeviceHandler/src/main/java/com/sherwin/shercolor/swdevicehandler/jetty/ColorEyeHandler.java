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



@SuppressWarnings("serial")
public class ColorEyeHandler extends WebSocketHandler {
//    private final static String HOST = "127.0.0.1";
//    private final static int WS_PORT = 80;
//    private final static int WSS_PORT = 8443;
    

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(ColorEyeListener.class);
    }
    
}