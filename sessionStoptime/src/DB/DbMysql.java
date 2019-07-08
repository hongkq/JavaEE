package DB;

import java.sql.*;

/**
 * @author hkq
 */
public class DbMysql {


    private Connection con;
    private Statement stm;
    private ResultSet rs;
    private String classname = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8";
    //用户名
    private String user = "root";
    //用户密码
    private String pwd = "123456";


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


    }
}
