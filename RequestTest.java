package com.github7.request;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestTest {

    String resquestMess = "GET /hh.html?id=a&page=1 HTTP/1.1\r\nHost:www.baidu.com\r\nAccept:text/html\r\n\r\n";
    InputStream inputStream = new ByteArrayInputStream(resquestMess.getBytes());
    Request request = Request.parse(inputStream);

    public RequestTest() throws IOException {
    }

    @Test
    public void setMethod() {
        try {
            request.setMethod("AAA");
        } catch (IOException e) {
            Assert.assertEquals("不支持的方法", e.getMessage());
        }
    }

    @Test
    public void getUrl() {
        Assert.assertEquals("/hh.html", request.getUrl());
    }

    @Test
    public void getProtocol() {
        Assert.assertEquals("HTTP/1.1", request.getProtocol());
    }

    @Test
    public void getMethod() {
        Assert.assertEquals("GET", request.method);
    }

    @Test
    public void getHeaders() {
        System.out.println(request.getHeaders());
    }

    @Test
    public void getRequestParam() {
        System.out.println(request.getRequestParam());
    }


    String resquestMess1 = "GET /hh.html";
    InputStream inputStream1 = new ByteArrayInputStream(resquestMess1.getBytes());

    @Test
    public void prase() {
        try {
            Request.parse(inputStream1);
        } catch (IOException e) {
            Assert.assertEquals("错误的请求行，参数不够", e.getMessage());
        }
    }
}