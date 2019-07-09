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
