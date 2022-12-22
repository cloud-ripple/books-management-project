package window;

import com.book.pojo.Book;
import com.book.utils.JdbcUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminFrame extends JFrame {
    //声明组件 //图书名称 //作者 //出版社 //价格
    public JLabel label1 = null; //图书名称
    public JLabel label2 = null; //作者
    public JLabel label3 = null; //出版社
    public JLabel label4 = null; //价格
    public JLabel label5 = null; //图书类型

    public JTextField field1 = null; //图书名称
    public JTextField field2 = null; //作者
    public JTextField field3 = null; //出版社
    public JTextField field4 = null; //价格

    JComboBox box = null;

    JButton button1 = null; //添加
    JButton button2 = null; //清空
    JButton button3 = null; //删除

    static DefaultTableModel dtm = null; //行和列
    JTable table = null;
    JScrollPane scrollPane = null;

    int id = 0; //定义图书编号

    Object[] comboBoxValues = null; //定义下拉框里的所有值
    Object itemValue = null; //定义下拉框中被选定的那一项值

    JPopupMenu popupMenu = null; //弹出菜单

    public AdminFrame() {
        this.setTitle("图书管理页面");
        this.setSize(1300, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        label1 = new JLabel("图书名称：");
        label1.setBounds(30, 10, 70, 30);
        field1 = new JTextField();
        field1.setBounds(110, 10, 210, 30);

        label2 = new JLabel("作者：");
        label2.setBounds(30, 80, 70, 30);
        field2 = new JTextField();
        field2.setBounds(110, 80, 210, 30);

        label3 = new JLabel("出版社：");
        label3.setBounds(30, 150, 70, 30);
        field3 = new JTextField();
        field3.setBounds(110, 150, 210, 30);

        label4 = new JLabel("价格：");
        label4.setBounds(30, 220, 70, 30);
        field4 = new JTextField();
        field4.setBounds(110, 220, 210, 30);

        label5 = new JLabel("图书类型：");
        label5.setBounds(30, 270, 70, 30);

        //调用getAllComboboxValue方法，用全局变量 Object[] comboBoxValues来接收所返回的下拉框里的所有项的值
        comboBoxValues = getAllComboboxValue();
        box = new JComboBox(comboBoxValues); //把所有项的值给到下拉框
        box.addItem("诗歌");
        box.addItem("白话文");
        box.addItem("玄幻");
        box.addItem("小说");

        //下拉框事件
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemValue = box.getSelectedItem(); //得到下拉框被选择这一项的值
                //System.out.println(itemValue);
            }
        });

        box.setBounds(110, 270, 210, 30);

        //按钮
        button1 = new JButton("添加");
        button2 = new JButton("清空");
        button3 = new JButton("删除");

        button1.setBounds(120, 340, 60, 30);
        button2.setBounds(260, 340, 60, 30);
        button3.setBounds(600, 670, 60, 30);

        //表格模型
        dtm = new DefaultTableModel();
        //表头 String[]
        String[] head = {"编号", "书名", "剩余数量", "作者", "出版社", "价格", "类型"};
        for (int i = 0; i < head.length; i++) {
            dtm.addColumn(head[i]);
        }
        //数据填充到表格模型里
        fillTable();
        //将表格模型交割table
        table = new JTable(dtm);
        //设置表格字体和颜色
//        table.setFont(new Font("楷体", Font.PLAIN, 18));
//        table.setForeground(Color.black);
        //设置表头字体和颜色
//        table.getTableHeader().setFont(new Font("楷体", Font.BOLD, 22));
//        table.getTableHeader().setBackground(Color.pink);
        //设置被选中行的背景
//        table.setSelectionBackground(Color.lightGray);
        //将表格放入滚动面板
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(350, 10, 880, 650);


        //添加按钮事件
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取文本框和下拉框的值 //图书名称 作者 出版社 价格
                String text1 = field1.getText(); //图书名称 去空格
                String text2 = field2.getText(); //作者
                String text3 = field3.getText(); //出版社
                String text4 = field4.getText(); //价格
                Object price = (text4); //把价格这个字符串转换成double类型
                //新建一个Object[]类型的数组
                //依次把 图书名称、作者、出版社、价格、以及在下拉框中被选定的值全部放到数组fieldTexts里
                Object[] fieldTexts = {text1, text2, text3, price, itemValue};

                if (text1.length() == 0 || text2.length() == 0 || text3.length() == 0 || text4.length() == 0) {
                    JOptionPane.showMessageDialog(
                    button1,
                            "请在所有文本框内输入完整信息！",
                            "注意",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    //调用该类中的 addBooks方法，并传入一个Object[]类型的实参数组fieldTexts
                    //用int类型i来接收，返回受影响的记录(元组)个数
                    int i = addBooks(fieldTexts);
                    //如果返回值大于0，说明sql语句执行成功，即已经向表中插入了数据，此时先清空原先表格，然后在填充新数据刷新
                    if (i > 0) {
                        JOptionPane.showMessageDialog(
                                button2,
                                "图书添加成功",
                                "提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearTable();
                        fillTable();
                    }
                }
            }
        });

        //清空按钮事件
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //点击清空按钮时，把文本框里的内容变成空字符串
                field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
            }
        });

        //删除按钮事件
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        scrollPane,
                        "确认删除所选图书吗？",
                        "提示",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    //调用删除图书方法 deleteBooksById，当点击删除按钮时就执行方法
                    int i = deleteBooksById(id); //把选中的id传过去
                    //调用deleteBooksById方法后，用i来接收返回值，即有多少行受影响
                    if (i > 0) {
                        clearTable();
                        fillTable();
                    }
                }

            }
        });

        //表格添加一个鼠标监听事件，当用户用鼠标对表格进行操作时，执行mousePressed鼠标按压点击方法
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //super.mousePressed(e);
                //得到选中的行数
                int row = table.getSelectedRow();
                //表格的行和列都从下标0开始
                //System.out.println("当前在表格中选中的是第"+ row + "行");
                //调用getValueAt方法，得到第row行，第column列的值,
                //Object valueAt = table.getValueAt(row, 0); //得到你选中的行的第1列的值
                //System.out.println(valueAt);
                if(row >= 0) { //当已经选中一行的时候才能获得图书ID，才可以右键弹出菜单
                    id = (int) table.getValueAt(row, 0); //id是全局变量，强制转换为int类型
                }
            }
        });

        this.add(label1);
        this.add(field1);
        this.add(label2);
        this.add(field2);
        this.add(label3);
        this.add(field3);
        this.add(label4);
        this.add(field4);
        this.add(label5);
        this.add(box);
        this.add(button1);
        this.add(button2);
        this.add(button3);

        this.add(scrollPane);


        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    //查询图书数据,并获取
    public List<Book> findAllBook() {
        ResultSet rs = JdbcUtils.getRs("select * from tb_book");
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
        return list; //null
    }

    //把获取到的图书信息填充到表格
    public void fillTable() {
        List<Book> books = findAllBook();
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            dtm.addRow(new Object[]{
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

    //清空表格数据方法，并刷新
    public void clearTable() {
        dtm.getDataVector().clear(); //清空表格
        dtm.fireTableDataChanged(); //刷新数据

    }

    //删除图书方法，根据图书编号id来删除所选的书籍信息 , 返回返回有多少行数据受影响，int值
    public int deleteBooksById(int id) { //形参id，接收传入选中的id
        //调用工具类的update方法来实现删除书籍，并得到返回值，返回的是数据库表执行sql语句后受影响的行数
        int i = JdbcUtils.update("delete from tb_book where bookId=?", new Object[]{id});
        return i;
    }

    //获取下拉框里每项的所有值，并返回一个Object[]数组
    public Object[] getAllComboboxValue() {
        List<String> list = new ArrayList<>();
        Object[] strs = null;
        ResultSet rs = JdbcUtils.getRs("select distinct bookType from tb_book");
        try {
            while (rs.next()) {
                String type = rs.getString("bookType");
                list.add(type);
                strs = list.toArray();
            }
            return strs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return strs;
    }

    //添加图书方法,用 obj接收一个Object[]类型的实参数组，并返回受影响的行数int值
    public int addBooks(Object[] obj) {
        //调用工具类中的update增删改查方法，返回受影响的记录(元组)个数，并用int值 i 来接收
        //如果返回值大于0，说明sql语句执行成功，即已经向表中插入了数据
        int i = JdbcUtils.update(
                "insert into tb_book (bookName,bookAuthor,bookPublish,bookPrice,bookType) values (?,?,?,?,?)",
                obj); //此处的 obj 为调用方法时参入的实参数组
        return i;
    }

}
