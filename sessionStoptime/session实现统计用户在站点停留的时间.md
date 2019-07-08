---
title: session实现统计用户在站点停留的时间
date: 2019-07-08 21:50:00
categories:
	- [JavaWeb]
    - [session]
tag:
	- 学习总结练手
author: 洪凯庆
---

session实现统计用户在站点停留的时间

<!--more-->

  session在网络中被称为会话。由于Http协议是一种无状态协议，也就是当一个客户向服务器发出请求时，服务器接收请求并返回响应后，连接结束，而服务器并不保存相关的信息。为了弥补这一缺点，HTTP协议提供了session。通过session可以在应用程序的web页面进行跳转，并保存用户状态，使整个用户会话一直存在下去，直到关闭浏览器。但是，如果一个会话中，客户端长时间不向服务器发送请求，session对象就会自动消失。这个时间取决于服务器，例如，Tomcat服务器默认为30分钟。不过这个时间可以通过编写程序进行修改。

使用session对象最常用的功能就是记录用户的状态。

**实例1：统计用户在站点停留的时间**

**解题思路：**

用户在站点的停留时间可以通过计算从session对象的创建到session对象的销毁之间的时间差来得到。再在用户退出站点时把用户的登录、离开和停留时间写进数据表中。

**关键技术：**

通过继承HttpSessionBindingListener接口监听会话的创建与销毁。继承该接口的类必须实现继承的抽象方法valueBound(HttpSessionBindingEvent)和valueUnbound(HttpSessionBindingEvent)。前者在向session中存入实现该接口的类和对象时触发，后者在从session移除该对象时触发；数据库用MySQL就好了。

**实现代码：**

在Java编程工具钟创建一个Dynamic web project的动态web项目，版本选2.5或2.4的，因为编码工具是eclipse-jee从3.0开始都是使用注解来完成server类的映射关系的，我还不会使用注解，所以只能使用2.5或2.4的，因为这些是使用XML来完成映射关系的，不用担心啦，你的编程工具会帮你搞好的，安心编码就是啦。对了，我用intellij idea的创建动态web项目也非常简单自行百度即可。。
![深度截图_选择区域_20190708095919.png][1]



之后点击finish即可
出现以下界面，界面上我写了个index.jsp页面，待会把代码贴上哈
![深度截图_选择区域_20190708213655.png][2]


之后开始编码吧，创建一个数据库操作类DbMysql.java

    package DB;
    
    import java.sql.*;
    
    /**
     * @author hkq
     */
    public class DbMysql {


​    
        private Connection con;
        private Statement stm;
        private ResultSet rs;
        private String classname = "com.mysql.jdbc.Driver";
        private String url = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8";
        //用户名
        private String user = "root";
        //用户密码
        private String pwd = "123456";


​    
        public DbMysql() {
        }
    
        public Connection getCon() {
            if (con == null) {
                try {
                    Class.forName ( "com.mysql.jdbc.Driver" );
                    con = DriverManager.getConnection ( url , user , pwd );
                } catch (SQLException e) {
                    e.printStackTrace ( );
                } catch (ClassNotFoundException e) {
                    e.printStackTrace ( );
                }
            }
            return con;
        }
    
        public Statement getStm() {
            try {
                //获取数据库连接
                con = getCon ( );
                //获取stm对象
                stm = con.createStatement ( );
            } catch (Exception e) {
                e.printStackTrace ( System.err );
            }
            return stm;
        }
    
        public int dosql(String sql) {
            int i = -1;
            getStm ( );
            try {
                i = stm.executeUpdate ( sql );
            } catch (Exception e) {
                e.printStackTrace ( );
            }
            return i;
        }
    
        /***
         * 关闭数据库资源
         */
        public void closed() {
            try {
                if (rs != null) {
                    rs.close ( );
                    rs = null;
                }
                if (stm != null) {
                    stm.close ( );
                    stm = null;
                }
                if (con != null) {
                    con.close ( );
                    con = null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace ( );
            }


​    
        }
    }
再写个记录session创建到销毁计算时间的类，命名为StopTime.java,代码如下

    package com.kaiqing;
    
    import java.text.SimpleDateFormat;
    import java.util.Date;
    
    /**
     * @author hkq
     */
    public class StopTime {
        /***
         *  时
         */
        private int h = 0;
        /***
         * 分
         */
        private int m = 0;
        /***
         * 秒
         */
        private int s = 0;
        /***
         *创建时间
         */
        private Date start = null;
        /***
         *结束时间
         */
        private Date end = null;
        /***
         * 时间格式
         */
        private SimpleDateFormat format = new SimpleDateFormat ( "yyy-MMM-dd hh:mm:ss" );
    
        public StopTime() {
        }
    
        /***
         * 计算停留时间方法
         * @param end
         */
        public void counttime(Date end) {
            this.end = end;
            long howmuch = this.end.getTime ( ) - start.getTime ( );
            h = (int) (howmuch / 1000 / 60 / 60);
            howmuch = howmuch - h * 60 * 60 * 1000;
            m = (int) (howmuch / 1000 / 60);
            howmuch = howmuch - m * 60 * 1000;
            s = (int) (howmuch / 1000);
    
        }
    
        public String getTime() {
            String times = this.h + "小时" + this.m + "分钟" + this.s + "秒";
            return times;
        }
    
        public String getStart() {
            return format.format ( start );
        }
    
        public void setStart(Date start) {
            this.start = start;
        }
    
        public String getEnd() {
            return format.format ( end );
        }
    
    }
之后便是操作类UserLine.java,代码如下

    package com.kaiqing;
    
    import DB.DbMysql;
    
    import javax.servlet.http.HttpSessionBindingEvent;
    import java.util.Date;
    
    public class UserLine implements javax.servlet.http.HttpSessionBindingListener {
        private String userip;
        private String username;
        private StopTime stopTime = new StopTime ( );
        public UserLine(){
            username="";
        }
        @Override
        public void valueBound(HttpSessionBindingEvent arg0){
            /***
             * 记录创建时间
             */
            stopTime.setStart ( new Date (  ) );
            System.out.println ( this.userip+"于"+new Date() .toLocaleString ()+"上线" );
    
        }
        @Override
        public void valueUnbound(HttpSessionBindingEvent arg0){
            /***
             * 计算停留时间
             */
            stopTime.counttime(new Date (  ));
            System.out.println ( this.userip+"于"+new Date (  ).toLocaleString ()+"上线了" );
            /***
             * 将信息写入数据库中
             */
            writetime();
        }
        public void writetime(){
            String starting=stopTime.getStart ();
            String endtime=stopTime.getEnd ();
            String times=stopTime.getTime ();
    
            String sql="INSERT INTO tbstoptime VALUES('"+starting+"','"+endtime+"','"+times+"')";
    
            DbMysql db=new DbMysql ();
            db.dosql ( sql );
            db.closed();
        }
    
        public void setUserip(String userip) {
            this.userip=userip;
        }
    }
显示代码就是我们的jsp页面index.jsp代码如下：

    <%--
      Created by IntelliJ IDEA.
      User: hkq
      Date: 19-7-8
      Time: 上午10:05
      To change this template use File | Settings | File Templates.
    --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@page import="com.kaiqing.UserLine" %>
    
    <%
      //设置session的有效活动时间
    session.setMaxInactiveInterval ( 10 );
    //获取用户的ip地址
    String userip=request.getRemoteAddr ();
    //实例化一个监听对象
    UserLine userline=new UserLine ();
    //设置ip
    userline.setUserip(userip);
    //将实例化的监听对象类存入session对象中
    session.setAttribute ( "logonuser",userline );
    
    %>
    <html>
      <head>
        <title>$Title$</title>
      </head>
      <body>
      <table>
        <tr bgcolor="#d3d3d3" height="25">
          <td align="center">
            欢迎登录
    
          </td>
    
        </tr>
        <tr>
          <td>
            本实例用于统计用户在站点的停留时间<br>
            该时间是一个session对象从创建到结束的时间差<br>
            本示例应用了servlet事件监听中会话监听的方法<br>
            主要是通过继承HttpSessionBindingListener接口监听对HTTP会话操作
          </td>
        </tr>
      </table>
      </body>
    </html>

之后发布即可，因为是idea搭建的动态web项目，使用到的MySQL数据库连接的mysql-connector-java-5.1.47.jar需自行下载放在lib目录下即可，lib目录在web/WEB-INF下自行创建即可；
自动写入数据到数据库时，好像会报错

    String sql="INSERT INTO tbstoptime VALUES('"+starting+"','"+endtime+"','"+times+"')";

使用以下这句：

    String sql="INSERT INTO tbstoptime VALUES(starting,endtime,times)";
数据能写入数据库，但为空啊。
![深度截图_选择区域_20190708215441.png][3]


[1]: https://blog.gzcodestudio.com/usr/uploads/2019/07/1193690655.png
[2]: https://blog.gzcodestudio.com/usr/uploads/2019/07/4279120268.png
[3]: https://blog.gzcodestudio.com/usr/uploads/2019/07/3436511169.png
最后附上源码地址：<https://github.com/hongkq/JavaEE>

