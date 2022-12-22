package window;

import com.book.pojo.Book;
import com.book.utils.JdbcUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {
    public JMenuBar menuBar = null;
    public JMenu menu1 = null;
    public JMenu menu2 = null;
    public JMenuItem menuItem1 = null;
    public JMenuItem menuItem2 = null;
    public JMenuItem menuItem3 = null;
    public JMenuItem menuItem4 = null;
    public JMenuItem menuItem5 = null;
    public JLabel label1 = null;
    public JTextField field = null;
    JButton button1 = null;
    JButton button2 = null;
    DefaultTableModel dtm = null;
    JTable table = null;
    JScrollPane scrollPane = null;
    Object name = null; //根据被选择的图书名来借阅

    public UserFrame() {
        this.setSize(1000, 800);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setTitle("图书管理用户界面");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        //菜单栏
        menuBar = new JMenuBar();
        menuBar.setSize(1000, 20);
        menu1 = new JMenu("用户");
        menu2 = new JMenu("关于");
        menuItem1 = new JMenuItem("我的借书空间");
        menuItem2 = new JMenuItem("更换账号");
        menuItem3 = new JMenuItem("退出");
        menuItem4 = new JMenuItem("管理员密钥");
        menuItem5 = new JMenuItem("帮助");

        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);
        menu2.add(menuItem4);
        menu2.add(menuItem5);

        menuBar.add(menu1);
        menuBar.add(menu2);
        this.add(menuBar);

        label1 = new JLabel("请输入关键字：");
        label1.setBounds(80, 30, 130, 35);
        field = new JTextField();
        field.setBounds(180, 30, 270, 27);
        button1 = new JButton("查询图书");
        //button1.setBorderPainted(false);
        button1.setBounds(460, 30, 90, 30);
        button2 = new JButton("借阅此书");
        button2.setBounds(460, 690, 90, 30);
        this.add(label1);
        this.add(field);
        this.add(button1);
        this.add(button2);

        dtm = new DefaultTableModel();
        String[] head = {"编号","书名", "剩余数量", "作者","出版社","价格","图书类型"};
        for (int i = 0; i < head.length; i++) {
            dtm.addColumn(head[i]);
        }
        fillTable();
        table = new JTable(dtm);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 80, 850, 590);
        this.add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int i = table.getSelectedRow();
                 //只有选中行的时候
                name = table.getValueAt(i, 1);

                //得到书名之后，根据书名来借阅
                //System.out.println(name);
            }
        });

        //查询按钮添加事件
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(field.getText());
                clearTable();
                fillTable2(); //把根据输入文本框内容来查询到的图书信息，填充到表格
            }
        });
        //借阅按钮事件
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //，且表格中的行处于被选中状态时，才会让数量减一，并给出提示借阅成功
                if(table.isRowSelected(table.getSelectedRow())) {
                    int i = borrowBooks(name); //根据图书名来借阅，用i来接收受影响的行数据
                    reduceBookNum(); //调用方法，让表格中剩余数量减一
                    //清空表格，填充数据
                    clearTable();
                    fillTable();
                    if (i > 0) { //说明sql语句执行成功，即借书成功，只有i > 0
                        //弹出提示
                        JOptionPane.showMessageDialog(
                                scrollPane,
                                "图书借阅成功",
                                "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        //我的借书空间菜单项添加事件
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BorrowFrame();
            }
        });

        //管理员密钥菜单项添加事件
        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog(scrollPane,
                        "请输入密钥，进入管理员页面！"
                        );
                if("twb".equals(password)) {
                    new AdminFrame(); //打开管理员窗口页面
                    setVisible(false); //设置当前窗口不可见
                }
            }
        });

        setVisible(true);
    }
    //查询所有图书信息
    public List<Book> findAllBooks() {
        String sql2 = "select * from tb_book";
        ResultSet rs = JdbcUtils.getRs(sql2);
        List<Book> list = new ArrayList<>();
        Book book = null;
        try {
            while (rs.next()) {
                book = new Book();
                book.setBookId(rs.getInt("bookId"));
                book.setBookName(rs.getString("bookName"));
                book.setBookNumber(rs.getInt("bookNumber"));
                book.setBookAuthor(rs.getString("bookAuthor"));
                book.setBookPublish(rs.getString("bookPublish"));
                book.setBookPrice(rs.getDouble("bookPrice"));
                book.setBookType(rs.getString("bookType"));
                list.add(book);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //查询到的所有图书信息填充到表格
    public void fillTable() {
        List<Book> list = findAllBooks();
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            dtm.addRow(new Object[] {
                    book.getBookId(),
                    book.getBookName(),
                    book.getBookNumber(),
                    book.getBookAuthor(),
                    book.getBookPublish(),
                    book.getBookPrice(),
                    book.getBookType()
            });
        }
    }
    //根据输入文本框内容来查询图书，模糊查询
    public List<Book> searchAllBooks() {
        ResultSet rs = JdbcUtils.getRs(
                "select * from tb_book " +
                    "where bookName like '%" + field.getText() + "%'"
                 + " or bookAuthor like '%" + field.getText() + "%'"
                 + " or bookType like '%" + field.getText() + "%'"
        );
        // "select * from tb_book where bookName like \"%\" ? \"%\" ",new Object[]{field.getText()}
        List<Book> list = new ArrayList<>();
        Book book = null;
        try {
            while (rs.next()) {
                book = new Book();
                book.setBookId(rs.getInt("bookId"));
                book.setBookName(rs.getString("bookName"));
                book.setBookNumber(rs.getInt("bookNumber"));
                book.setBookAuthor(rs.getString("bookAuthor"));
                book.setBookPublish(rs.getString("bookPublish"));
                book.setBookPrice(rs.getDouble("bookPrice"));
                book.setBookType(rs.getString("bookType"));
                list.add(book);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //把根据输入文本框内容来查询到的图书信息，填充到表格
    public void fillTable2() {
        List<Book> list = searchAllBooks();
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            dtm.addRow(new Object[] {
                    book.getBookId(),
                    book.getBookName(),
                    book.getBookNumber(),
                    book.getBookAuthor(),
                    book.getBookPublish(),
                    book.getBookPrice(),
                    book.getBookType()
            });
        }
    }
    //借阅图书
    public int borrowBooks(Object name) {
        //返回一个int,表示有多少行数据受影响
        int i = JdbcUtils.update(
                "INSERT INTO tb_borrow (bookName,bookAuthor,bookType)" +
                " SELECT DISTINCT bookName,bookAuthor,bookType" +
                        " FROM tb_book WHERE bookName='" + name + "'"
                );
        return i;
    }
    //当用户点击借阅后，图书剩余数量减一，根据书名
    public int reduceBookNum() {
        int i = JdbcUtils.update("update tb_book" +
                " set bookNumber = bookNumber - 1" +
                " where bookName = '" + name + "'");
        return i; //返回有多少受影响的数据行
    }

    //清空表格数据方法，并刷新
    public void clearTable() {
        dtm.getDataVector().clear(); //清空表格
        dtm.fireTableDataChanged(); //刷新数据
    }

    public static void main(String[] args) {

        UserFrame userFrame = new UserFrame();
    }

}
