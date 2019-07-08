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
