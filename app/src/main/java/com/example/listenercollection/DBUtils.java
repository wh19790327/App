package com.example.listenercollection;

import android.util.Log;

import com.example.listenercollection.constants.GlobalVars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";
    //这里是MySQL的用户名
    private static String user = "root";
    //这里是我的MySQL密码，本文作了隐藏处理，
    private static String password = "A123@456";

    public static Connection getConn() {
        Connection connection = null;
        try {
            Class.forName(driver);
//数据的IP地址，本文中的地址不是我的真实地址，请换为你的真实IP地址。
            String ip = "106.12.104.17";
            String port = "3306";
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + "db_demo?useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url, user, password);
            Log.e("数据库连接", "成功!");
        } catch (Exception e) {
            Log.e("数据库连接", "失败!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void getData() {
        String sql = "SELECT * FROM tb_flag WHERE packageName = 'com.example.listenercollection'";
        try {
            ResultSet rs = getConn().createStatement().executeQuery(sql);
            while (rs.next()) {
                // 通过字段检索
                int flag = rs.getInt("flag");
                GlobalVars.setAuthority(flag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
