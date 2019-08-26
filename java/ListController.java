
import com.github7.controller.Controller;
import com.github7.request.Request;
import com.github7.response.Response;

import java.io.IOException;
import java.sql.*;

public class ListController extends Controller {
    @Override
    public void doGet(Request request, Response response) throws IOException {
        this.doPost(request, response);
    }
    @Override
    public void doPost(Request request, Response response) throws IOException {

        response.println("<center><h1>所有文章</h1>");
        response.println("</h4><a href='/post.html'>发表文章</a></h4></center>");
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String url = "jdbc:mysql://localhost:3307/blogs?useSSL=false";
        String sql = "select article_id,article_title,article_author from blog";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "aaaaaa");
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String title = resultSet.getString(2);
                String author=resultSet.getString(3);
                response.println("<li><a href='/article?id=" + id + "'>" + title + "</a>"+"&nbsp;&nbsp;&nbsp;作者:"+author+"</li>");

            }

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
