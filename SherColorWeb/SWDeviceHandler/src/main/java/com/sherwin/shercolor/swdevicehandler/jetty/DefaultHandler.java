package com.sherwin.shercolor.swdevicehandler.jetty;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class DefaultHandler extends AbstractHandler
{
    private String greeting=null;
    private String body=null;

    public DefaultHandler()
    {
        this("You have successfully installed the SW Device Manager");
    }

    public DefaultHandler( String greeting )
    {
        this(greeting, null);
    }

    public DefaultHandler( String greeting, String body )
    {
        this.greeting = greeting;
        this.body = body;
    }

    @Override
    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
       // out.println("<img id='shercolorimg' src='shercolor-lg.jpg'  style='width: 33rem;'/>");
        out.println("<h1>Sherwin Williams SherColor</h1><br>");
        out.println("<h3>" + greeting + "</h3>");
        if (body != null)
        {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}
