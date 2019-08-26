package com.github7.response;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ResponseTest {

    OutputStream outputStream = new ByteArrayOutputStream();
    Response response;

    @Test
    public void start() throws IOException {
      response= Response.start(outputStream);
      response.flush();
    }

    @Test
    public void start2() throws IOException {
        response= Response.start(outputStream);
        response.setContentType("text/html");
        response.println("<h1>张三<h1>");
        response.setState(State.INTERNAL_SERVER_ERROR);
        response.flush();
        System.out.println(outputStream);
    }

}