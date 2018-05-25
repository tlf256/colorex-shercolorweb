package com.sherwin.shercolor.swdevicehandler.jetty;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import com.sherwin.shercolor.swdevicehandler.jetty.DefaultHandler;

public class WsContextBuilder {

	public WsContextBuilder(int wss_port){
		this.WSS_PORT = wss_port;
	}
	private  int WSS_PORT = 0;
	SslContextFactory sslContextFactory = new SslContextFactory();
	HttpConfiguration http_config = new HttpConfiguration();
	ContextHandlerCollection contexts = new ContextHandlerCollection();
	HttpConfiguration https_config = null;
	/* obfuscated password done with 
	 * java -cp C:\Users\djm301\.m2\repository1\org\eclipse\jetty\jetty-util\9.4.5.v20170502\jetty-util-9.4.5.v20170502.jar org.eclipse.jetty.util.security.Password ?
	 */
	private final static String CERT_PW = "OBF:1lfm1j931jn31y7z1unn1wg21wfw1uob1y7z1jkf1j5z1lbw";

	public void buildContexts(){

		ContextHandler maincontext = new ContextHandler("/");
		maincontext.setContextPath("/");
		maincontext.setHandler(new DefaultHandler());

		ContextHandler tcontext = new ContextHandler("/tinter");
		tcontext.setAllowNullPathInfo(true); //prevent 302 error from redirect /tinter to /tinter/
		tcontext.setHandler(new TinterHandler());


		ContextHandler contextCE = new ContextHandler("/coloreye");
		contextCE.setHandler(new ColorEyeHandler());
		contextCE.setAllowNullPathInfo(true);

		
   /* ContextHandler contextV = new ContextHandler("/");
    contextV.setVirtualHosts(new String[] { "127.0.0.2" });
    contextV.setHandler(new DefaultHandler());
*/		

		contexts.setHandlers(new Handler[] { maincontext,tcontext,contextCE   // contextFR, contextIT,              contextV
		});



		http_config.setSecureScheme("https");
		http_config.setSecurePort(WSS_PORT);
		//http_config.setOutputBufferSize(32768);
		//http_config.setRequestHeaderSize(8192);
		//http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);
		//    
		https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		
	/* keystore and self-signed certificate creation (not storing password here, when you run replace ***** with password)
	 * 
	 * keytool -genkeypair -keystore localhost.jks  -dname "CN=Sherwin Williams,OU=TAG,O=SherColor, L=Unknown, ST=Unknown, C=Unknown" -keypass ***** -storepass ***** -keyalg RSA  -ext SAN=dns:localhost,ip:127.0.0.1 -validity 10000 -alias SherColor
     *
     */
	/* export certificate (not storing password here, when you run replace ***** with password)
          keytool -exportcert -alias SherColor -keystore localhost.jks -file localhost.crt -storepass *****
	 */
	/* import certificate into "Trusted Root Authority in your browser
	 * 
	 */
		 
		


		sslContextFactory.setKeyStorePath("localhost.jks");
		sslContextFactory.setKeyStorePassword(CERT_PW); // changeit
		
		//sslContextFactory.setTrustStorePath("sw.jks");
		//sslContextFactory.setTrustStorePassword(CERT_PW);


	}


	public ContextHandlerCollection getContexts() {
		if(contexts.getHandlers()==null){
			this.buildContexts();
		}
		return contexts;
	}


	public void setContexts(ContextHandlerCollection contexts) {
		this.contexts = contexts;
	}


	public SslContextFactory getSslContextFactory() {
		return sslContextFactory;
	}


	public void setSslContextFactory(SslContextFactory sslContextFactory) {
		this.sslContextFactory = sslContextFactory;
	}


	public HttpConfiguration getHttp_config() {
		return http_config;
	}


	public void setHttp_config(HttpConfiguration http_config) {
		this.http_config = http_config;
	}


	public HttpConfiguration getHttps_config() {
		return https_config;
	}


	public void setHttps_config(HttpConfiguration https_config) {
		this.https_config = https_config;
	}


}
