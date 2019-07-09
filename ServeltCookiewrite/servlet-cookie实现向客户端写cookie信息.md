---
title: servlet cookie实现向客户端写cookie信息
date: 2019-07-09 11:00:00
categories:
	- [JavaWeb]
    - [servlet]
    - [Cookie]
tag:
	- 学习总结练手
author: 洪凯庆
---

<!--more-->

servlet**基础

  servlet是Java web应用中最核心的组件，也是web服务器组件，它是一个中间控制层，负责处理客户端提交过来的请求数据以及调用业务逻辑对象，根据业务逻辑来控制页面转发。

cookie**

  在互联网中，cookie是小段的文本信息，在网络服务器上生成，并发送给浏览器。通过使用cookie可以标识用户身份，记录用户名和密码，跟踪重复用户。浏览器将cookie以key/value的形式保存到客户机的某个指定目录中。

基本概念就是这样，下面我想**实现一个小功能**，利用servlet cookie实现向客户端写cookie信息，即是在登录页面实现用户输入用户名和密码并提交到servlet中，在servlet中将用户名添加到cookie对象中，然后关闭浏览器，在重新访问用户登录页时，用户名的文本框会显示上一次输入的用户名信息

**解题思路**：

  应用在Servlet API中提供的cookie类即可实现；用户把表单信息提交给servlet后，在servlet中获取用户请求的信息并添加到cookie对象中，再通过HttpServletResponse对象把cookie信息返回给客户端，然后在JSP页面中通过request内置对象来获取客户端的cookie信息。 
  在JSP中使用request对象获取的是一个cookie对象的数组，需要在循环中遍历所有cookie对象，并通过cookie对象的getName()方法查找所有cookie对象的名称，然后根据找到的cookie名称获得cookie对象中的值。

**实现步骤**：
  首先打开我们的JavaWeb编程工具eclipse或idea，我用的就是idea，创建一个动态web项目，因为这个小功能没有用到其他的jar包，所以不必使用maven搭建项目；下面开始编码工作：

**代码实现**：
因为需使用表单提交用户名和密码到servlet处理，所以新建一个index.jsp,代码如下：

    <%--
      Created by IntelliJ IDEA.
      User: hkq
      Date: 19-7-9
      Time: 上午10:08
      To change this template use File | Settings | File Templates.
    --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%
        //用于保存从cookie中读取的用户名
        String userName = null;
        //获取客户端的所有cookie
        Cookie cookieArr[]=request.getCookies ();
        if (cookieArr!=null&&cookieArr.length>0){
            for (Cookie c:cookieArr){
                //如果Cookie中有一个名为userName的cookie
                if (c.getName ().equals ( "userName" )){
                    //将字符串解码，获得此cookie的值
                    userName=java.net.URLDecoder.decode ( c.getValue () );
                }
            }
        }
    %>
    <html>
    <head>
        <title>$Title$</title>
    </head>
    <body>
    <form action="CookieServlet" method="post">
        <table align="center">
            <tr>
                <td>用户名：</td>
                <td><input type="text" name="name" value="<%if (userName!=null){out.print ( userName );}%>"></td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="password" name="pwd"></td>
            </tr>
            <tr>
    
                <td colspan="2"><input type="submit" value="登录"></td>
            </tr>
    
        </table>
    
    </form>
    </body>
    </html>
之后创建servlet处理类CookieServlet.java,代码如下：

    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    
    /**
     * @author hkq
     */
    @WebServlet(name = "CookieServlet")
    public class CookieServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
    
        }
    
        @Override
        protected void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
          request.setCharacterEncoding ( "UTF-8" );
            /***
             * 获得用户名
             */
          String name=request.getParameter ( "name" );
            /***
             * 将用户名进行格式编码
             */
          name=java.net.URLEncoder.encode ( name,"UTF-8" );
            /***
             * 创建一个cookie对象，并将用户名保存到cookie对象中
             */
            Cookie nameCookie=new Cookie ( "userName",name );
            /***
             * 设置Cookie的过期时间，单位为秒
             */
            nameCookie.setMaxAge ( 60 );
            /***
             * 通过response的addCookie()方法将此Cookie对象保存到客户端浏览器的cookie中
             */
            response.addCookie ( nameCookie );
            request.getRequestDispatcher ( "success.jsp" ).forward ( request,response );
        }
    }
需要配置一下web.xml文件

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
             version="4.0">
        <servlet>
            <servlet-name>CookieServlet</servlet-name>
            <servlet-class>CookieServlet</servlet-class>
        </servlet>
        <servlet-mapping>
            <servlet-name>CookieServlet</servlet-name>
            <url-pattern>/CookieServlet</url-pattern>
        </servlet-mapping>
    </web-app>

运行即可，页面比较糙，就不把页面push上来了。。。
这是提供源码的demo

