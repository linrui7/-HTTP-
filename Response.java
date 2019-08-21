package com.github7.response;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Response {
    public static Response start(OutputStream outputStream) throws IOException {
        Response response = new Response();
        response.setServer();
        response.setOutputStream(outputStream);
        response.setContentType("text/html");
        response.setDate();
        return response;
    }

    //保存输出流
    private byte[] resByteArray = new byte[8192];
    private OutputStream outputStream;

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private int contentLength = 0;
    //保存以下信息Content-Type：Content-Lenth:Data：Server：
    private Map<String, String> headers = new HashMap<>();

    //将默认状态置为200 ok
    private State state = State.OK;

    public void setState(State state) {
        this.state = state;
    }

    public void setServer() {
        headers.put("Server", "LR/1.0");
    }


    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType + ";charset=utf-8");
    }

    public void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E，dd MMM yyyy HH:mm:ss z");
        headers.put("Date", dateFormat.format(new Date()));
    }

    //将信息写入到响应输出流中的方法。
    public void write(byte[] bytes, int off, int len) throws IOException {
        if (contentLength + len > 8192) {
            throw new IOException("超过请求头最大长度");
        }
        //将写入的东西先保存在自己的byte数组中
        System.arraycopy(bytes, off, resByteArray, contentLength, len);
        contentLength += len;
    }

    public void write(byte[] bytes, int len) throws IOException {
        write(bytes, 0, len);
    }

    public void write(byte[] bytes) throws IOException {
        write(bytes, bytes.length);
    }

    //格式化响应
    public void print(String string, Object... args) throws IOException {
        write(new Formatter().format(string, args).toString().getBytes("utf-8"));
    }

    public void println(Object o) throws IOException {
        print("%s%n", o.toString());
    }

    public void flush() throws IOException {
        headers.put("Content-Length", String.valueOf(contentLength));
        sendResponseLine();
        sendResponseHeads();
        outputStream.write(resByteArray, 0, contentLength);
    }

    //将响应行加入到输出流中
    public void sendResponseLine() throws IOException {
        String responseLine = String.format("HTTP/1.0 %d %s \r\n", state.getCode(), state.getReason());
        outputStream.write(responseLine.getBytes());
    }

    //将响应头加入到输出流中
    public void sendResponseHeads() throws IOException {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String header = String.format("%s:%s\r\n", entry.getKey(), entry.getValue());
            outputStream.write(header.getBytes("utf-8"));
        }
        outputStream.write("\r\n".getBytes("UTF-8"));
    }

    @Override
    public String toString() {
        return "Response{" +
                " headers=" + headers +
                ", state=" + state +
                '}';
    }
}
