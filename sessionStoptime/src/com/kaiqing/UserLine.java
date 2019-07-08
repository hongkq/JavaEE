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
