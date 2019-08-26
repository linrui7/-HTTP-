package com.github7.server;

import com.github7.classloader.MyClassLoader;
import com.github7.controller.Controller;
import com.github7.controller.StaticController;
import com.github7.request.Request;
import com.github7.response.Response;
import com.github7.response.State;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public final Controller staticController = new StaticController();

    public Server() throws DocumentException {
    }

    public static void main(String[] args) throws IOException, DocumentException {
        Server server = new Server();
        server.run(1234);
    }

    public void run(int port) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        //监听端口
        ServerSocket serverSocket = new ServerSocket(port);
        // 查看端口netstat -ano
        while (true) {

            Socket socket = serverSocket.accept();
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //初始化
                        Request request = Request.parse(socket.getInputStream());
                        Response response = Response.start(socket.getOutputStream());
                        //查找请求url是否存在
                        String filelocation = request.getUrl();
                        String filename = "";
                        if (filelocation.equals("/")) {
                            filename = "F:\\\\httpProject\\webapps\\index.html";
                        }
                        filename = "F:\\\\httpProject\\webapps\\" + filelocation.replace("/", File.separator);
                        //根据 URL 的不同，用不用的 controller 去处理
                        File file = new File(filename);
                        Controller controller = null;
                        if (file.exists()) {
                            System.out.println("静态controller");
                            controller = staticController;
                        } else {
                            System.out.println("动态controller");
                            Class<?> cla = null;
                            String name = request.getUrl().replace("/", "");
                            if (name.equals("article")) {
                                cla = new MyClassLoader().loadClass("ArticleController");
                            } else if (name.equals("postArticle")) {
                                cla = new MyClassLoader().loadClass("PostController");
                            } else {
                                cla = new MyClassLoader().loadClass(name);
                            }
                            if (cla != null) {
                                System.out.println(cla.getName());
                                controller = (Controller) cla.newInstance();
                            }
                        }
                        //动态也找不到 返回错误
                        if (controller == null) {
                            response.setState(State.NOT_FOUND);
                            response.println("<h1>" + State.NOT_FOUND.getCode() + "    " + State.NOT_FOUND.getReason() + " 页面没有找到</h1>");
                        } else {
                            if (request.getMethod().equals("GET")) {
                                controller.doGet(request, response);
                            } else if (request.getMethod().equals("POST")) {
                                controller.doPost(request, response);

                            } else {
                                //不支持的方法
                                response.setState(State.METHOD_NOT_ALLOWED);
                                response.println(State.METHOD_NOT_ALLOWED.getReason());
                            }
                        }
                        response.flush();
                        System.out.println(request.toString());
                        System.out.println(response.toString());
                        System.out.println();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
