package com.github7.controller;

import com.github7.request.Request;
import com.github7.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticController extends Controller {
    private static final String HOME = System.getenv("LR_HOME");
    //保存ContentType  支持三种类型用HashMap先保存起来
    private final Map<String, String> Content_TYPE = new HashMap<String, String>() {
        {
            put("html", "text/html");
        }
    };

    @Override
    public void doPost(Request request, Response response) throws IOException {
        this.doGet(request, response);
    }

    @Override
    public void doGet(Request request, Response response) throws IOException {
        //1.获取文件地址 request.getUrl()得到的是/hh.html
        String filename = getFileName(request.getUrl());
        //2.开始读取文件内容
        System.out.println(filename);
        InputStream inputStream = new FileInputStream(filename);
        //缓冲流 一次读取1024字节内容
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            response.write(buf, 0, len);
        }
        //3.获取类型content-type，根据文件名的后缀
        String suffix = getSuffix(filename);
        String contype = getContentType(suffix);
        //然后设置响应头里面的contenttype
        response.setContentType(contype);
    }

    private String getContentType(String suffix) {
        String contentType = Content_TYPE.get(suffix);
        if (contentType == null) {
            //说明没有这种类型的文件
            contentType = "text/html";
        }
        return contentType;
    }

    private String getSuffix(String filename) {
        //找到最后一个点，点后面的就是文件类型
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        //返回文件名后缀
        return filename.substring(index + 1);
    }

    private String getFileName(String url) {
        if (url.equals("/")) {
            url = "/index.html";
        }
        return "F:\\\\httpProject\\webapps\\"+ url.replace("/", File.separator);
    }
}
