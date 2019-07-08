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
