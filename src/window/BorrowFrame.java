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

public class BorrowFrame extends JFrame {
    JFrame borrowFrame = null;
    Object name = null; //根据被选择的图书名来借阅
    DefaultTableModel dtm = null;
    JTable table = null;
    JScrollPane scrollPane = null;
    JPopupMenu popupMenu = null;
    JMenuItem Item1 = null;
    JMenuItem Item2 = null;

    public BorrowFrame() {
        borrowFrame = new JFrame("我的借书空间");
        borrowFrame.setSize(450, 450);
        borrowFrame.setLayout(null);
        borrowFrame.setLocationRelativeTo(null);
        borrowFrame.setDefaultCloseOperation(borrowFrame.DISPOSE_ON_CLOSE);

        dtm = new DefaultTableModel();

        String[] head = {"书名", "作者", "图书类型"};
        for (int i = 0; i < head.length; i++) {
            dtm.addColumn(head[i]);
        }
        fillTable();
        table = new JTable(dtm);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 0, borrowFrame.getWidth(), borrowFrame.getHeight());
        borrowFrame.add(scrollPane);

        popupMenu = new JPopupMenu();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row >= 0) {
                    name =  table.getValueAt(row, 0);
                    //只有当选中一行的时候，才可以得到图书名，才可以右键弹出菜单
                    if (e.isMetaDown()) { //判断是否为右键
                        popupMenu = new JPopupMenu();
                        //添加子菜单
                        Item1 = new JMenuItem("退还图书");
                        Item2 = new JMenuItem("刷新");
                        popupMenu.add(Item1);
                        popupMenu.add(Item2);
                        popupMenu.setPopupSize(70, 60);
                        popupMenu.show(e.getComponent(), e.getX() + 60, e.getY());
                        //退还图书菜单项添加事件
                        Item1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int num = JdbcUtils.update("delete from tb_borrow where bookName='" + name + "'");
                                if(num > 0) { //说图书退还成功
                                    //调用方法，增加原来表格图书剩余数量
                                    increaseBookNum(name); //根据书名
                                    JOptionPane.showMessageDialog(scrollPane,
                                            "图书退还成功",
                                            "提示",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    clearTable();
                                    fillTable();
                                }
                            }
                        });
                        //刷新菜单项添加事件
                        Item2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                clearTable();
                                fillTable();
                            }
                        });
                    }
                }

            }
        });
        borrowFrame.setVisible(true);
    }

    //查询借书表的图书信息
    public List<Book> findAllBooks() {
        List<Book> list = new ArrayList();
        Book books = null;
        ResultSet rs = JdbcUtils.getRs("select * from tb_borrow");
        try {
            while (rs.next()) {
                books = new Book();
                books.setBookName(rs.getString("bookName"));
                books.setBookAuthor(rs.getString("bookAuthor"));
                books.setBookType(rs.getString("bookType"));
                list.add(books);
            }
            return list;
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    //填充表格
    public void fillTable() {
        List<Book> list = findAllBooks();
        for (int i = 0; i < list.size(); i++) {
            Book book = list.get(i);
            dtm.addRow(new Object[]{
                    book.getBookName(),
                    book.getBookAuthor(),
                    book.getBookType()
            });
        }
    }
    //当你在我的借书空间点击退还图书时，让表格中图书剩余数量加一，根据书名
    public int increaseBookNum(Object name) {
        int i = JdbcUtils.update("update tb_book" +
                " set bookNumber = bookNumber + 1" +
                " where bookName = '" + name + "'");
        return i;
    }

    public void clearTable() {
        dtm.getDataVector().clear(); //清空表格
        dtm.fireTableDataChanged(); //刷新数据
    }
}


