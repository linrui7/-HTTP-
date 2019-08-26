
import com.github7.controller.Controller;
import com.github7.request.Request;
import com.github7.response.Response;

import java.io.IOException;
import java.sql.*;

public class ArticleController extends Controller {
    @Override
    public void doGet(Request request, Response response) throws IOException {
        this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        //从url 获取id
        String id = request.getRequestParam().get("id");
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String url = "jdbc:mysql://localhost:3307/blogs?useSSL=false";
        String sql = "select article_title,article_author,article_content from blog where article_id='" + id + "'";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "root", "aaaaaa");
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            response.println("</h6><a href='/ListController'>回到首页</a></h6>");
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                String content = resultSet.getString(3);
                response.println("<h3><center>" + title + "</center></h3>");
                response.println("<h5><center>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "作者：" + author + "</center></h5>");
                response.println(content);
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
                response.println(e.getMessage());
            }
        }

    }
}
