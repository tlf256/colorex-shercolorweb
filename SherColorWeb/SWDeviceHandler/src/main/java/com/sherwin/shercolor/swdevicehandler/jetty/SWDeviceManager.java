package com.sherwin.shercolor.swdevicehandler.jetty;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.sherwin.shercolor.swdevicehandler.device.FMTinter;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.view.TheUserConsole;

import javafx.application.Platform;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class SWDeviceManager {
	static Logger logger = LogManager.getLogger(SWDeviceManager.class);

	private final static String HOST = "127.0.0.1";
	private final static int WS_PORT = 55216;
	private final static int WSS_PORT =55217;

	private  TheUserConsole TheUserConsole=null;
	Server server ;


	public TheUserConsole getTinterConsole() {
		return TheUserConsole;
	}


	public void setTinterConsole(TheUserConsole tinterconsole) {
		this.TheUserConsole = tinterconsole;
	}

	//public SWDeviceManager(){
	public void StartServer(){
		try {
			server = new Server();
			WsContextBuilder context_builder = new WsContextBuilder(WSS_PORT);

			server.setHandler(context_builder.getContexts());
			// HTTP connector
			// The first server connector we create is the one for http/ws, passing in
			// the http configuration we configured above so it can get things like
			// the output buffer size, etc. We also set the port (8080) and
			// configure an idle timeout.

			ServerConnector wsConnector = new ServerConnector(server,
					new HttpConnectionFactory(context_builder.getHttp_config()));
			wsConnector.setHost(HOST);
			wsConnector.setPort(WS_PORT);
			server.addConnector(wsConnector);

			ServerConnector wssConnector = new ServerConnector(server,
					new SslConnectionFactory(context_builder.getSslContextFactory(), HttpVersion.HTTP_1_1.asString()),
					new HttpConnectionFactory(context_builder.getHttps_config()));

			wssConnector.setHost(HOST);
			wssConnector.setPort(WSS_PORT);
			server.addConnector(wssConnector);

			
			server.start();
			//moved to main server.join(); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		boolean consoleOn = true;
		Configuration configuration = Configuration.ReadFromDisk(null);
		//2017-11-30 BKP - need to check if configuration is null (no tinter or no tinter yet configured.)
		if (configuration!=null && configuration.getModel()!=null) {
			if(configuration.getModel().contains("FM") || configuration.getModel().contains("IFC")){
				FMTinter.StartFMSocketServer();
			}
		}

		for(String arg:args){
			if(arg.contains("console")){
				if(arg.contains("no") || arg.contains("off")){
					consoleOn=false;
				}
			}
		}
		if(consoleOn){
			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(TheUserConsole.class);
				}
			}.start();
		}
		SWDeviceManager swdevicemanager = new SWDeviceManager();
		swdevicemanager.StartServer();
		swdevicemanager.getServer().join(); // this keeps the main program from finishing while the server thread is running!

	}


	public Server getServer() {
		return server;
	}


	public void setServer(Server server) {
		this.server = server;
	}



}