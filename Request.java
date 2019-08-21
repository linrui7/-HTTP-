package com.github7.request;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Request {
    String url;
    String protocol;
    String method;
    Map<String, String> headers = new HashMap<>();
    Map<String, String> requestParam = new HashMap<>();

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    public void setRequestParam(String key, String value) {
        this.requestParam.put(key, value);
    }

    public void setMethod(String method) throws IOException {
        this.method = method.toUpperCase();
        if (this.method.equals("POST") || this.method.equals("GET")) {
            return;
        }
        throw new IOException("不支持的方法");
    }

    public void setUrl(String url) throws UnsupportedEncodingException {
        this.url = URLDecoder.decode(url, "UTF-8");
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getRequestParam() {
        return requestParam;
    }

    //请求方法
    public static Request parse(InputStream inputStream) throws IOException {
        //传入浏览器请求的字节流，Server解析
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Request request = new Request();
        //设置请求行参数
        praseRequestLine(bufferedReader, request);
        //设置请求头参数
        praseRequestHeader(bufferedReader, request);
        return request;
    }


    //请求第一行 请求行：方法 请求URL HTTP版本号
    //GET /aaa.html?id=1&password=ppp&name=10 HTTP/1.1

    private static void praseRequestLine(BufferedReader bufferedReader, Request request) throws IOException {
        String firstLine = bufferedReader.readLine();
        //第一行参数是以" "进行分割。然后得到请求方法，请求参数，请求版本号
        //比如请求行为GET /aaa.html?id=1&name=10 HTTP/1.1
        String[] fragments = firstLine.split(" ");
        //fragmens至少存在三个参数
        if (fragments.length < 3) {
            throw new IOException("错误的请求行，参数不够");
        }
        //第一个参数是请求方式
        String method = fragments[0];
        request.setMethod(method);
        //第二个参数是总的url请求参数
        String urlAll = fragments[1];
        //第三个参数是版本号
        String protocol = fragments[2];
        request.setProtocol(protocol);
        //获取url请求页面，是urlAll里面的
        String[] qFragmens = urlAll.split("\\?");
        request.setUrl(qFragmens[0]);
        //获取请求的参数 qFragmens里面的第二个参数
        //id=1 & name=10
        if (qFragmens.length > 1) {
            String[] mess = qFragmens[1].split("&");
            for (int i = 0; i < mess.length; i++) {
                String[] keyvalue = mess[i].split("=");
                String key = keyvalue[0];
                String value = "";
                if (keyvalue.length > 1) {
                    value = URLDecoder.decode(keyvalue[1], "utf-8");
                }
                //将获取到的键值对添加进去
                request.setRequestParam(key, value);
            }
        }

    }

    //请求第二部分，直到遇到空行结束
    // 请求头有很多信息如下：
    // Host：www.baidu.com
    // Accept:text/html
    private static void praseRequestHeader(BufferedReader bufferedReader, Request request) throws IOException {
        String secondLine;
        //将请求头的信息全部保存到HashMap中。
        while ((secondLine = bufferedReader.readLine()) != null && secondLine.trim().length() != 0) {
            String[] keyValue = secondLine.split(":");
            //分割之后可能会出现空格。所以要去掉空格
            String key = keyValue[0];
            String value = "";
            if (keyValue.length > 1) {
                value = keyValue[1].trim();
            }
            //将信息头保存到请求头里面
            request.setHeaders(key, value);
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "请求页面='" + url + '\'' +
                ", 版本号='" + protocol + '\'' +
                ", 请求方法='" + method + '\'' +
                ", 请求头=" + headers +
                ", 请求行参数=" + requestParam +
                '}';
    }
}
