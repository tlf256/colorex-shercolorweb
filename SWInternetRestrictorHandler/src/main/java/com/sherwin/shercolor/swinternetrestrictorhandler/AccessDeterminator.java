package com.sherwin.shercolor.swinternetrestrictorhandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.logging.Logger;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.HttpString;

public class AccessDeterminator {
	private static Logger LOG = Logger.getLogger(AccessDeterminator.class.getName());

	public static boolean isSherwinNetworkAccess(HttpServerExchange request) {
		String ipAddr = getRemoteAddr(request);
		LOG.fine("remote ip list is " + ipAddr);
		logAllHeaders(request);
		return isSherwinInternalIP(ipAddr);
	}

	public static String getRemoteAddr(HttpServerExchange request) {
		String ipAddr = request.getSourceAddress().getAddress().toString();
		//the request source address has a slash in the front.  Let's replace slashes with spaces and trim.
		ipAddr = ipAddr.replace("/", " ").trim();
		LOG.fine("sourceaddress IP is " + ipAddr);
		HeaderValues headers = request.getRequestHeaders().get("X-FORWARDED-FOR");
		
		if (headers!=null) {

			for(String thisHeader: headers) {
				final String ips = thisHeader;
				LOG.fine("X-FORWARDED-FOR IPs are " + ips);
				if (ips != null) {
					String[] addrs = ips.split(",");
					ipAddr = addrs[addrs.length - 1].trim();
				}
	        }
		}
		return ipAddr;
	}

	/**
	 * Check an IP address to see if it inside the Sherwin network. This
	 * includes all RFC 1597 private IP ranges, as well as the 148.141.0.0/16
	 * class B range.
	 * 
	 * @param dottedQuad
	 * @return
	 */
	private static boolean isSherwinInternalIP(String dottedQuad) {
		assertDottedQuad(dottedQuad);
		return dottedQuad.startsWith("148.141.") || isPrivateIP(dottedQuad);
	}

	private static void assertDottedQuad(String dottedQuad) {
		if (dottedQuad == null)
			throw new IllegalArgumentException("IP address required");
		String numsAndDots = dottedQuad.replaceAll("[^0-9\\.]", "");
		if (!dottedQuad.equals(numsAndDots)) {
			throw new IllegalArgumentException("Invalid dotted-quad format for IP address: <" + dottedQuad + ">");
		}
	}

	/**
	 * Determine if a given address is an RFC 1597 private IP.
	 * 
	 * @param dottedQuad
	 * @return
	 * @throws IllegalArgumentException
	 *             if the IP address is not in dotted quad format
	 */
	private static boolean isPrivateIP(String dottedQuad) {
		boolean ret = false;
		if (dottedQuad.equals("127.0.0.1"))
			return true;

		try {
			InetAddress[] addrs = InetAddress.getAllByName(dottedQuad);
			if (addrs != null && addrs[0] != null) {
				byte[] octets = addrs[0].getAddress();
				if (10 == octets[0]) { // 10.---.---.-
					ret = true;
				} else if (intToByte(192) == octets[0] && intToByte(168) == octets[1]) { // 192.168.---.-
					ret = true;
				} else if (intToByte(172) == octets[0]) { // 172.{16-32}.---.-
					for (byte val = 16; val < 32; val++) {
						if (val == octets[1]) {
							ret = true;
							break;
						}
					}
				} else {
					ret = false;
				}
			}
		} catch (UnknownHostException e) {
			LOG.severe("Cannot lookup address: " + dottedQuad + ": " + e.getMessage());
		}
	
		LOG.fine("isPrivateIP(" + dottedQuad + ") ->" + ret);

		return ret;
	}

	/**
	 * Convert an integer to a signed byte. IP addresses are expressed as a
	 * quartet of unsigned bytes. Unsigned byte values > 127 won't fit into a
	 * signed byte, and since Java doesn't have unsigned types, they have to be
	 * transformed. You would not ordinarily use this to generate byte values
	 * intended for human consumption.
	 * 
	 * @param intValue
	 *            An integer such that 0 < intValue < 255
	 * @throws IllegalArgumentException
	 *             if intValue is less than 0 or greater than 255
	 */
	private static byte intToByte(int intValue) {
		if (intValue < 0 || intValue > 255)
			throw new IllegalArgumentException("Value must be between in the range 0 .. 255");
		if (intValue >= 0 && intValue < 128) {
			return (byte) intValue;
		} else if (intValue >= 128 && intValue < 256) {
			return (byte) (-1 * (~intValue + 1));
		} else {
			return (byte) intValue;
		}
	}

	private static void logAllHeaders(HttpServerExchange request) {
		//if (LOG.isLoggable(Level.FINE)) {
			HeaderMap requestHeaders = request.getRequestHeaders();
			if (requestHeaders != null) {
				Collection<HttpString> headerNames = requestHeaders.getHeaderNames();
				
				for(HttpString theHeaderName : headerNames) {
					HeaderValues headerNameVals = requestHeaders.get(theHeaderName);
					for(String thisVal : headerNameVals) {
						LOG.fine(String.format("%s: %s", theHeaderName, thisVal));
						//LOG.info(String.format("%s: %s", theHeaderName, thisVal));
					}
	
				}

			}
		//}
	}

}
