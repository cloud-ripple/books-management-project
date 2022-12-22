# books-management-project
基于Java Swing开发图书管理案例

项目部署
1. 导入相关依赖

该项目所用到的jar包依赖，已经放在src\com\book\lib目录下了，直接添加该目录作为项目引用即可，也可以直接导入jar包

![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/1.png)
![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/2.png)



2. 连接数据库

先下载library.sql文件，执行该文件中的sql语句后，会自动创建好数据库library，从而得到表和数据。
可直接查看表中的用户信息和图书信息

然后在JDBC工具类中修改自己的本地MySQL用户名和密码

![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/4.png)

3. 运行

主程序入口如图，找到RunMe.java源文件

![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/3.png)

4. 注册、登录

进入普通用户页面，点击--->关于--->管理员密钥，输入 twb 点击确定即可进入管理员界面，进行图书信息的添加，修改

![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/5.png)

管理界面如下

![image](https://github.com/Tianbobo520/books-management-project/blob/master/iamges/6.png)


5. 想要对该项目有更多了解，请阅读 “图书管理系统实验报告.doc”文件，更多功能和需求尚未完善，敬请期待....



