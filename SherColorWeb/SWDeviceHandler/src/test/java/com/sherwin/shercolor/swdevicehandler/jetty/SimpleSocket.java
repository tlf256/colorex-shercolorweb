package com.sherwin.shercolor.swdevicehandler.jetty;

//========================================================================
//Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
* Basic Echo Client Socket
*/
@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleSocket
{
private final CountDownLatch closeLatch;
@SuppressWarnings("unused")
private Session session;

public SimpleSocket()
{
    this.closeLatch = new CountDownLatch(1);
}

public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
{
    return this.closeLatch.await(duration,unit);
}

@OnWebSocketClose
public void onClose(int statusCode, String reason)
{
    System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
    this.session = null;
    this.closeLatch.countDown(); // trigger latch
}

@OnWebSocketConnect
public void onConnect(Session session)
{
    System.out.printf("Got connect: %s%n",session);
    this.session = session;
   try
    {
	   /* comment/uncomment these if you want to toggle  sending a message after connecting */
        Future<Void> fut;
		String json_tinter="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Bogus\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Simulator\",\"serial\":\"434344\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"W1\",\"shots\":32,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
		String json_coloreye="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"SpectroMessage\",\"command\":\"Config\", \"curve\":{\"curve\":[],\"curvePointCnt\":0},\"spectroConfig\":{\"model\":\"SWSimSpectro\",\"serial\":\"\",\"port\":\"USB\"}}";

		fut = session.getRemote().sendStringByFuture(json_tinter);
        fut.get(4,TimeUnit.SECONDS); // wait for send to complete.

      //  fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
        //fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
        

        session.close(StatusCode.NORMAL,"I'm done");
    }
    catch (Throwable t)
    {
        t.printStackTrace();
    }
}

@OnWebSocketMessage
public void onMessage(String msg)
{
    System.out.printf("Got msg: %s%n",msg);
}
}
