package com.sherwin.shercolor.swinternetrestrictorhandler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternetRestrictorHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(InternetRestrictorHandler.class.getName());
    
    private Set<String> externalContexts = new HashSet<String>(Arrays.asList("/CustomerSherColorWeb"));

    private HttpHandler next;

    public InternetRestrictorHandler(HttpHandler next) {
        LOGGER.setLevel(Level.WARNING);
    	this.next = next;
    }
    

    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        LOGGER.info("handleRequest() is called");
        LOGGER.info("requestPath = " + exchange.getRequestPath());
        LOGGER.info("sourceSocketAddress = " + exchange.getSourceAddress());
        LOGGER.info("sourceAddress = " + exchange.getSourceAddress().getAddress());
        LOGGER.info("sourceHostAddress = " + exchange.getSourceAddress().getAddress().getHostAddress());
//        LOGGER.info("destAddress = " + exchange.getDestinationAddress());
//        HeaderValues hvals = exchange.getRequestHeaders().get("X-FORWARDED-FOR");
//        for(String thisVal: hvals) {
//        	LOGGER.info("A header value  = " + thisVal);
//        }
        
		if (AccessDeterminator.isSherwinNetworkAccess(exchange)) {
			LOGGER.info("Request is Sherwin Network Access");
			//exchange.REQUEST_ATTRIBUTES.cast(arg0)
			//exchange.getResponseHeaders()put("SHERWIN_NETWORK_ACCESS", "true");
			next.handleRequest(exchange);
		} else if (isExternalAppRequest(exchange)) {
			//request.setAttribute("SHERWIN_NETWORK_ACCESS", false);
			LOGGER.info("Request is External App Request");
			next.handleRequest(exchange);
		} else {
			LOGGER.warning("Request is Unauthorized");
			unauthorized(exchange);
		}
        
        
        
    }
    
	private boolean isExternalAppRequest(HttpServerExchange exchange) {
		return externalContexts.contains(getContextPath(exchange));
	}

	private String getContextPath(HttpServerExchange exchange) {
		//String contextPath = exchange.getRequestPath();
		LOGGER.info("in getContextPath");
		String contextPath = "";
		if ("".equals(contextPath)) {
			//contextPath = exchange.getRequest().getContextPath();
			String url = exchange.getRequestURL();
			LOGGER.info("url = " + url);
			String[] parts = url.split("/");
			// 0: http:, 1: '', 2:server, 3: context, 4: more path ...
			if (parts.length > 3) {
				contextPath = "/" + parts[3];
			}
		}
		LOGGER.info("contextPath = " + contextPath);
		return contextPath;
	}

	private void unauthorized(HttpServerExchange exchange) throws IOException {
		String message = "Accessed refused to " + exchange.getSourceAddress() + " for app " + getContextPath(exchange);
		LOGGER.severe(message);
		System.out.println(message);
		exchange.setStatusCode(403);
		//response.sendError(403, "Access to this content is denied.");
	}

}
