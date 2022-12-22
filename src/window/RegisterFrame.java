package window;

import com.book.utils.JdbcUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame {
    public static void main(String[] args) {
        new RegisterFrame();
    }
    JTextField userText = null;
    JPasswordField pwdText1 = null;
    JPasswordField pwdText2 = null;
    String userName = null;
    String pwd1 = null;
    String pwd2 = null;
    JButton signBtn = null;
    JButton returnBtn = null;

    public RegisterFrame() {
        JFrame frame = new JFrame("用户注册页面");
        frame.setSize(600, 500);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);

        JLabel userLabel = new JLabel("输入用户名：");
        userLabel.setBounds(160, 90, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(22);
        userText.setBounds(250, 90, 170, 35);

        panel.add(userText);

        JLabel pwdLabel1 = new JLabel("设置新密码：");
        pwdLabel1.setBounds(160, 150, 80, 25);
        panel.add(pwdLabel1);

        pwdText1 = new JPasswordField(22);
        pwdText1.setBounds(250, 150, 170, 35);
        panel.add(pwdText1);

        JLabel pwdLabel2 = new JLabel("确认新密码：");
        pwdLabel2.setBounds(160, 210, 80, 25);
        panel.add(pwdLabel2);

        pwdText2 = new JPasswordField(22);
        pwdText2.setBounds(250, 210, 170, 35);
        panel.add(pwdText2);

        signBtn = new JButton("注册");
        signBtn.setBounds(200, 260, 80, 35);
        panel.add(signBtn);

        returnBtn = new JButton("重新登录");
        returnBtn.setBounds(340, 260, 90, 35);
        panel.add(returnBtn);
        //注册按钮
        signBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取注册信息
                userName = userText.getText().trim();
                pwd1 = pwdText1.getText().trim();
                pwd2 = pwdText2.getText().trim();
                //判断两次输入的密码是否一样
                if (pwd1.equals(pwd2)) {
                    int i = addUser(); //如果两次密码一样，再调用添加用户方法，并用i借书返回值
                    if (i > 0) { //说明sql语句添加成功
                        JOptionPane.showMessageDialog(
                                panel,
                                "注册成功！",
                                "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearText(); //清空注册信息
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            panel,
                            "两次输入的密码不一致，请重新输入！",
                            "注意",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //返回登录按钮事件
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
                frame.setVisible(false);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
    //获取注册信息,并注册用户
    public int addUser() {

        int i = JdbcUtils.update("insert into `user` values (?,?)", new Object[]{
             userName, pwd1
        });
        return i;
    }
    //清空注册信息
    public void clearText() {
        userText.setText("");
        pwdText1.setText("");
        pwdText2.setText("");
    }
}

