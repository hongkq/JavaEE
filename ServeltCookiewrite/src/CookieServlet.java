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
