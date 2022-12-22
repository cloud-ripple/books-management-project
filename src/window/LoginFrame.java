package window;

import com.book.pojo.User;
import com.book.utils.JdbcUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginFrame {
    String name2 = null;
    String pwd2 = null;

    public LoginFrame() {

        JFrame frame = new JFrame("图书管理系统");
        frame.setSize(600, 500);

        frame.setLocationRelativeTo(null);
        // frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        // 把窗口关闭交给窗口监听器处理
        frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel(null);

        JLabel userLabel = new JLabel("用户名：");
        userLabel.setBounds(170, 140, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(22);
        userText.setBounds(240, 140, 170, 35);

        panel.add(userText);

        JLabel pwdLabel = new JLabel("密 码：");
        pwdLabel.setBounds(170, 200, 80, 25);
        panel.add(pwdLabel);

        JPasswordField pwdText = new JPasswordField(22);
        pwdText.setBounds(240, 200, 170, 35);
        panel.add(pwdText);

        JButton loginBtn = new JButton("登录");
        loginBtn.setBounds(240, 270, 60, 35);
        panel.add(loginBtn);

        JButton registerBtn = new JButton("注册");
        registerBtn.setBounds(340, 270, 60, 35);
        panel.add(registerBtn);

        // 登录按钮添加事件
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理事件
                // 得到文本框中的内容，并去空格，转为字符串
                String text1 = userText.getText().trim();
                String text2 = pwdText.getText().trim();
                // 调用isUserTable方法，并把输入框中的信息作为实参传过去，得到一个boolean值
                boolean bool = isUserTable(text1, text2);
                if (bool) { // 如果bool是true时
                    new UserFrame(); // 打开用户界面
                    System.out.println("登录成功..");
                    // 登录成功后关闭当前窗口
                    frame.setVisible(false);
                } else if (text1.length() == 0 || text2.length() == 0) {
                    JOptionPane.showMessageDialog(
                            loginBtn,
                            "用户名或密码不能为空，请输入！",
                            "注意",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                            loginBtn,
                            "你输入的用户名或密码有误，请重新输入！",
                            "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        // 注册按钮添加事件
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();
                frame.setVisible(false); // 设置当前窗口不可见
            }
        });

        // 窗口监听器，正在被关闭时，给出提示框
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "确认退出",
                        "提示",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);

    }

    // 得到数据库中所有用户的信息，并把每一个用户放到用户集合中，返回集合列表
    public List<User> findAllUsers() {
        ResultSet rs = JdbcUtils.getRs("select * from `user`");
        List<User> list = new ArrayList();
        User users = null;
        try {
            while (rs.next()) {
                users = new User();
                users.setUserName(rs.getString("user_name"));
                users.setPassword(rs.getString("pwd"));
                list.add(users);
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    // 判断输入框中的姓名和密码等数据，是否为用户表中的信息，返回true或false
    public boolean isUserTable(String userTxt, String pwd) {
        List<User> list = findAllUsers();
        boolean bools = false;
        System.out.println("集合里有多少个用户：" + list.size());
        User[] user = new User[list.size()];
        for (int i = 0; i < list.size(); i++) {
            // 循环遍历集合中的所有用户，放到用户数组里
            user[i] = list.get(i);
            // 判断数组中的每一个用户，只要有用户的姓名和密码信息与输入框中的数据相同，则返回true
            if (userTxt.equals(user[i].getUserName()) && pwd.equals(user[i].getPassword())) {
                bools = true; // 如果输入框中姓名、密码等信息与数据库中用户表的某一个用户信息相同
            }
            // System.out.println(user[i].getUserName() + " " + user[i].getPassword());
        }
        return bools;
    }
}
