package window;

import com.book.pojo.User;
import com.book.utils.JdbcUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountFrame {
    JFrame accountFrame = null;
    JPopupMenu popupMenu = null;
    JMenuItem Item1 = null;
    JMenuItem Item2 = null;
    Object name = null; //根据被选择的用户名来更换账号
    DefaultTableModel dtm = null;
    JTable table = null;
    JScrollPane scrollPane = null;

    public static void main(String[] args) {
        new AccountFrame();
    }

    public AccountFrame() {
        accountFrame = new JFrame("账号管理页面");
        accountFrame.setSize(450, 450);
        accountFrame.setLayout(null);
        accountFrame.setLocationRelativeTo(null);
        accountFrame.setDefaultCloseOperation(accountFrame.EXIT_ON_CLOSE);

        dtm = new DefaultTableModel();

        String[] head = {"用户名", "密码"};
        for (int i = 0; i < head.length; i++) {
            dtm.addColumn(head[i]);
        }
        table = new JTable(dtm);
        fillTable();
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, accountFrame.getWidth(), accountFrame.getHeight());
        accountFrame.add(scrollPane);

        accountFrame.setVisible(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                //得到被选择行的用户名
                name = table.getValueAt(row, 0);
                if (e.isMetaDown()) {
                    popupMenu = new JPopupMenu();
                    Item1 = new JMenuItem("用此账号登录");
                    Item2 = new JMenuItem("刷新");
                    popupMenu.add(Item1);
                    popupMenu.add(Item2);
                    popupMenu.setPopupSize(120, 65);
                    popupMenu.show(scrollPane, e.getX() + 50, e.getY());
                }
            }
        });
    }

    //得到用户信息
    public List<User> findAllUsers() {
        ResultSet rs = JdbcUtils.getRs("select * from `user`");
        List<User> list = new ArrayList();
        User users = null;
        try {
            while(rs.next()) {
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

    //把用户填充到表格
    public void fillTable() {
        List<User> list = findAllUsers();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            dtm.addRow(new Object[] {
                    user.getUserName(),
                    user.getPassword()
            });
        }
    }

    //清空表格
    public void clearTable() {
        dtm.getDataVector().clear(); //清空表格
        dtm.fireTableDataChanged(); //刷新数据
    }
}
