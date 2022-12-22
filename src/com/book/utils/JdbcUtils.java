package com.book.utils;

import java.sql.*;
//JDBC工具类
public class JdbcUtils {
    static String driver = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/library";
    static Connection conn = null;
    static PreparedStatement pstat = null;
    static ResultSet rs = null;

    //专门用来获取连接对象，只有有了连接对象，才可能会有 PreparedStatement，ResultSet
    public static Connection getConn() {
        try {
            //注册驱动
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "root", "twb");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn; //null
    }

    //查询所有 不带参数 select * from student;
    public static ResultSet getRs(String sql) {
        try {
            conn = getConn();
            pstat = conn.prepareStatement(sql);
            rs = pstat.executeQuery();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    public static ResultSet getRs(String sql,Object[] params) {
        try {
            conn = getConn();
            //select * from tb_book where bookId=?
            pstat = conn.prepareStatement(sql);
            //给问号赋值
            for (int i = 0; i < params.length; i++) {
                pstat.setObject(i+1, params[i]);
            }
            rs = pstat.executeQuery();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    //增删改的方法 调用该方法时，通过传入的sql语句执行，返回有多少行数据受影响，int值
    public static int update(String sql,Object[] params) {

        try {
            conn = getConn();
            //delete from student where bookId=?
            pstat = conn.prepareStatement(sql);
            //给问号赋值
            for (int i = 0; i < params.length; i++) {
                pstat.setObject(i+1, params[i]);
            }
            int i  = pstat.executeUpdate(); //返回受影响数据的行数，int值
            return i;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int update(String sql) {
        try {
            conn = getConn();
            pstat = conn.prepareStatement(sql);
            int i  = pstat.executeUpdate(); //返回受影响数据的行数，int值
            return i;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
    public static void close(ResultSet rs, PreparedStatement pstat, Connection conn) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(pstat != null) {
            try {
                pstat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        conn=getConn();
        System.out.println(conn);
    }
 }
