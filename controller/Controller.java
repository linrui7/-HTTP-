package com.github7.controller;

import com.github7.request.Request;
import com.github7.response.Response;
import com.github7.response.State;

import java.io.IOException;

public class Controller {
    public void doGet(Request request, Response response) throws IOException {
        if (request.getProtocol().endsWith("1.0")) {
            response.setState(State.METHOD_NOT_ALLOWED);
            response.println("版本号不支持，仅支持1.1");
        } else {
            response.setState(State.BAD_REQUEST);
            response.println("请求错误，仅支持HTTP/1.1");
        }
    }

    public void doPost(Request request, Response response) throws IOException {
        this.doGet(request, response);
    }
}
