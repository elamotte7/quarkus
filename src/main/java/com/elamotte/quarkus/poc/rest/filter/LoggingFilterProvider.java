package com.elamotte.quarkus.poc.rest.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class LoggingFilterProvider implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilterProvider.class);

    @Override
    public void filter(ContainerRequestContext requestContext)  throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nUser: ").append(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown"
                : requestContext.getSecurityContext().getUserPrincipal());
        sb.append("\n - Path: ").append(requestContext.getUriInfo().getPath());
        sb.append("\n - Header: ").append(requestContext.getHeaders());
        sb.append("\n - Entity: ").append(getEntityBody(requestContext));
        System.out.println("HTTP REQUEST : " + sb.toString());
    }


    private String getEntityBody(ContainerRequestContext requestContext)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = requestContext.getEntityStream();

        final StringBuilder b = new StringBuilder();
        try
        {
            byte[] buf = new byte[1024];

            int numRead;
            while ( (numRead = in.read(buf) ) >= 0) {
                out.write(buf, 0, numRead);
            }

            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0)
            {
                b.append("").append("\n");
            }
            else
            {
                b.append(new String(requestEntity)).append("\n");
            }
            requestContext.setEntityStream( new ByteArrayInputStream(requestEntity) );

        } catch (IOException ex) {
            //Handle logging error
        }
        return b.toString();
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nHeader: ").append(responseContext.getHeaders());
        sb.append("\n - Entity: ").append(responseContext.getEntity());
        System.out.println("HTTP RESPONSE : " + sb.toString());
    }
}