package com.book.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        Connection conn = JdbcUtils.getConn();
        System.out.println(conn);

        ResultSet rs = JdbcUtils.getRs("select * from tb_book");
        try {
            while(rs.next()) {
                String bookId = rs.getString("bookId");
                System.out.println(bookId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("=========");
        ResultSet resultSet = JdbcUtils.getRs("select * from tb_book where bookId=? or bookAuthor=?",new Object[]{1,"忘语"});
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("bookAuthor"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("=========");
        int i = JdbcUtils.update("delete from tb_book where bookId=?", new Object[]{3});
        System.out.println(i);
    }
}
