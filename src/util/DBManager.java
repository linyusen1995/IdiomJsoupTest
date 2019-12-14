package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBManager {
    private static final String url = "jdbc:mysql://localhost:3306/idiomdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String name = "com.mysql.cj.jdbc.Driver";
    private static final String username = "root";
    private static final String password = "root";
    public Connection connection = null;
    public PreparedStatement preparedStatement = null;

    public DBManager(String sql) {
        try {
            Class.forName(name);
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
            this.preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
