package com.sherwin.shercolor.swdevicehandler.jetty;


import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;

import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.After;

import org.junit.Before;

import org.junit.Ignore;
import org.junit.Test;



public class SWDeviceManagerTest
{
    //private static Server server;
	private final static String HOST = "localhost";
	private final static int WS_PORT = 55216;
	private final static int WSS_PORT =55217;
	private final static String CERT_PW = "OBF:1lfm1j931jn31y7z1unn1wg21wfw1uob1y7z1jkf1j5z1lbw";
    private static URI serverUri;
    private SWDeviceManager swdevicemanager = new SWDeviceManager();
    SslContextFactory sslContextFactory = new SslContextFactory();
    
    @Before
    public  void startJetty() throws Exception
    {
       /* // Create Server
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(0); // auto-bind to available port
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        ServletHolder defaultServ = new ServletHolder("default", DefaultServlet.class);
        defaultServ.setInitParameter("resourceBase",System.getProperty("user.dir"));
        defaultServ.setInitParameter("dirAllowed","true");
        context.addServlet(defaultServ,"/");
        server.setHandler(context);

        // Start Server
        server.start();

        // Determine Base URI for Server
        String host = connector.getHost();
        if (host == null)
        {
            host = "localhost";
        }
        int port = connector.getLocalPort();
        serverUri = new URI(String.format("http://%s:%d/",host,port));
        */
    	getSslContextFactory();
    	swdevicemanager.StartServer();
    }
    private void getSslContextFactory(){
        
        sslContextFactory.setKeyStorePath("localhost.jks");
       // sslContextFactory.setKeyStorePassword("OBF:1vn21ugu1saj1v9i1v941sar1ugw1vo0"); // changeit
        sslContextFactory.setKeyStorePassword(CERT_PW); // changeit
    	//sslContextFactory.setTrustStorePath("sw.jks");
		//sslContextFactory.setTrustStorePassword(CERT_PW);
        try {
			sslContextFactory.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @After
    public  void stopJetty()
    {
        try
        {
        	swdevicemanager.getServer().stop();
            //server.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testWebSocketConnForTinter(){
    	 String destUri = "ws://"+HOST+":"+WS_PORT+"/tinter";
       

         WebSocketClient client = new WebSocketClient();
         SimpleSocket socket = new SimpleSocket();
         try
         {
             client.start();

             URI echoUri = new URI(destUri);
             ClientUpgradeRequest request = new ClientUpgradeRequest();
             client.connect(socket,echoUri,request);
             System.out.printf("Connecting to : %s%n",echoUri);

             // wait for closed socket connection.
             socket.awaitClose(15,TimeUnit.SECONDS);
         }
         catch (Throwable t)
         {
             t.printStackTrace();
         }
         finally
         {
             try
             {
                 client.stop();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
    }
    @Test
    public void testWebSocketSecureConnForTinter(){
    	
    	 String destUri = "wss://"+HOST+":"+WSS_PORT+"/tinter";
    	 //getSslContextFactory();

         WebSocketClient client = new WebSocketClient(sslContextFactory);
         SimpleSocket socket = new SimpleSocket();
         try
         {
        	
             client.start();

             URI echoUri = new URI(destUri);
             ClientUpgradeRequest request = new ClientUpgradeRequest();
             client.connect(socket,echoUri,request);
             System.out.printf("Connecting to : %s%n",echoUri);

             // wait for closed socket connection.
             socket.awaitClose(25,TimeUnit.SECONDS);
         }
         catch (Throwable t)
         {
             t.printStackTrace();
         }
         finally
         {
             try
             {
                 client.stop();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
    }
    //@Ignore("need to separate Init from Ci52 & Ci62 constructors") @Test
    @Test
    public void testWebSocketConnForColorEye(){
    	 String destUri = "ws://"+HOST+":"+WS_PORT+"/coloreye";
       

         WebSocketClient client = new WebSocketClient();
         SimpleSocket socket = new SimpleSocket();
         try
         {
             client.start();

             URI echoUri = new URI(destUri);
             ClientUpgradeRequest request = new ClientUpgradeRequest();
             client.connect(socket,echoUri,request);
             
             System.out.printf("Connecting to : %s%n",echoUri);

             // wait for closed socket connection.
             socket.awaitClose(15,TimeUnit.SECONDS);
         }
         catch (Throwable t)
         {
             t.printStackTrace();
         }
         finally
         {
             try
             {
                 client.stop();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
    } @Test
    public void testWebSocketSecureConnForColorEye(){
   	 String destUri = "wss://"+HOST+":"+WSS_PORT+"/coloreye";
      
/*
     SslContextFactory sslContextFactory = new SslContextFactory();
     sslContextFactory.setKeyStorePath("sw.p12");
     // sslContextFactory.setKeyStorePassword("OBF:1vn21ugu1saj1v9i1v941sar1ugw1vo0"); // changeit
      sslContextFactory.setKeyStorePassword(CERT_PW); // changeit
  	sslContextFactory.setTrustStorePath("sw.jks");
		sslContextFactory.setTrustStorePassword(CERT_PW);
*/
   	//getSslContextFactory();

     WebSocketClient client = new WebSocketClient(sslContextFactory);
        SimpleSocket socket = new SimpleSocket();
        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket,echoUri,request);
            System.out.printf("Connecting to : %s%n",echoUri);

            // wait for closed socket connection.
            socket.awaitClose(15,TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
   }
    @Test
    public void testWebSocketSecureConnForHttps(){
    	
    	 String destUri = "https://"+HOST+":"+WSS_PORT+"";
    	 //getSslContextFactory();
    	 /*
         SslContextFactory sslContextFactory = new SslContextFactory();
         sslContextFactory.setKeyStorePath("sw.p12");
         sslContextFactory.setKeyStorePassword(CERT_PW); // changeit
     	sslContextFactory.setTrustStorePath("sw.jks");
		sslContextFactory.setTrustStorePassword(CERT_PW);
*/
		 HttpClient client = new HttpClient(sslContextFactory); 
		 String responseString = null;
         //WebSocketClient client = new WebSocketClient(sslContextFactory);
         //SimpleSocket socket = new SimpleSocket();
         try
         {
        	// sslContextFactory.start();
        	 client.setFollowRedirects(false);
        	
             client.start();
             URI echoUri = new URI(destUri);
             ContentResponse response = client.GET(echoUri);
             System.out.printf("Connecting to : %s%n",echoUri);
             responseString = response.getContentAsString();
             System.out.println("HTTPS RESPONSE: " + responseString);
             
            
           
          
             // wait for closed socket connection.
             
         }
         catch (Throwable t)
         {
             t.printStackTrace();
         }
         finally
         {
             try
             {
                 client.stop();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
         assertTrue(responseString.contains("SW Device"));
       
         
    }
    //not used
    public void testGet() throws Exception
    {
        // Test GET
        HttpURLConnection http = (HttpURLConnection) serverUri.resolve("/").toURL().openConnection();
        http.connect();
        assertThat("Response Code", http.getResponseCode(), is(HttpStatus.OK_200));
    }
    
    
}