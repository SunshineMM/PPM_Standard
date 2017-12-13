package com.example.npttest.entity;

/**
 * Created by liuji on 2017/9/1.
 */

public class Prese {

    /**
     * pnum : 京B1FL39
     * ctype : 2
     * cdtp : 4
     * itime : 1503632287
     * iurl :
     * sid :
     */

    private String pnum;
    private int ctype;
    private int cdtp;
    private long itime;
    private String iurl;
    private String sid;

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getCdtp() {
        return cdtp;
    }

    public void setCdtp(int cdtp) {
        this.cdtp = cdtp;
    }

    public long getItime() {
        return itime;
    }

    public void setItime(long itime) {
        this.itime = itime;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
