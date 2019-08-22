package com.github7.classloader;

import com.github7.controller.Controller;
import com.github7.controller.StaticController;
import com.github7.request.Request;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;


public class ClassLoaderTest {
    String resquestMess = "GET /hh.html?id=a&page=1 HTTP/1.1\r\nHost:www.baidu.com\r\nAccept:text/html\r\n\r\n";
    InputStream inputStream = new ByteArrayInputStream(resquestMess.getBytes());
    Request request = Request.parse(inputStream);
    Controller controller = null;
    public ClassLoaderTest() throws IOException {
    }

    @Test
    public void findClass() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> cla = new ClassLoader().findClass(request.getUrl());
        if (cla != null) {
            System.out.println(cla.getName());
            controller = (Controller) cla.newInstance();
        }else {
          Assert.assertNull(cla);
        }
    }


    String resquestMess1 = "GET /Index?id=a&page=1 HTTP/1.1\r\nHost:www.baidu.com\r\nAccept:text/html\r\n\r\n";
    InputStream inputStream1 = new ByteArrayInputStream(resquestMess1.getBytes());
    Request request1 = Request.parse(inputStream1);
    Controller controller1 = null;

    @Test
    public void findClassTrue() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> cla = new ClassLoader().findClass(request1.getUrl());
        if (cla != null) {
            System.out.println(cla.getName());
            controller1 = (Controller) cla.newInstance();
            Assert.assertNotNull(cla);
        }else {
        }
    }
}