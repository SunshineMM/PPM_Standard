package com.example.npttest.entity;

/**
 * Created by liuji on 2017/8/26.
 */

public class Carinto {

    /**
     * cnum :
     * pnum : 豫B94111
     * itime : 1504007853
     * iurl :
     * sid :
     * ctype : 中型车
     * cdtp : 临时车
     */
    private String cnum;
    private String pnum;
    private String iurl;
    private long itime;
    private String sid;
    private String ctype;
    private String cdtp;

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
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

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCdtp() {
        return cdtp;
    }

    public void setCdtp(String cdtp) {
        this.cdtp = cdtp;
    }
}
