/*
   Author:linrui
   Date:2019/8/26
   Content:
*/

import com.github7.controller.Controller;
import com.github7.request.Request;
import com.github7.response.Response;
import com.github7.response.State;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class PostController extends Controller {
    @Override
    public void doGet(Request request, Response response) throws IOException {
        this.doPost(request, response);
    }
    @Override
    public void doPost(Request request, Response response) throws IOException {
        //获取url的id 和内容
        String id = UUID.randomUUID().toString();//随机ID
        String title = request.getRequestParam().get("title");
        String author = request.getRequestParam().get("author");
        String content = request.getRequestParam().get("content");
        if(title.length()==0){
            response.println("<center>");
            response.println("输入标题</br>");
            response.println("</h4><a href='/post.html'>点击返回发表文章</a></h4></center>");
            response.println("</center>");
            return;
        }
        if(author.length()==0){
            response.println("<center>");
            response.println("对不起，未输入作者姓名</br>");
            response.println("</h4><a href='/post.html'>点击返回发表文章</a></h4></center>");
            response.println("/<center>");
            return;
        }
        if(content.length()==0){
            response.println("<center>");
            response.println("未输入内容</br>");
            response.println("</h4><a href='/post.html'>点击返回发表文章</a></h4></center>");
            response.println("</center>");
            return;
        }
        //然后将所有内容存储到数据库中
        //连接到数据库
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        String url = "jdbc:mysql://localhost:3307/blogs?useSSL=false";
        String sql = "insert into blog values(?,?,?,?)";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "aaaaaa");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            preparedStatement.setString(4, content);
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //重定向首页
        response.setState(State.Temporarily_Moved);
        System.out.println("进入到PostController");
        response.setHeaders("Location", "/article?id=" + id);
    }
}
