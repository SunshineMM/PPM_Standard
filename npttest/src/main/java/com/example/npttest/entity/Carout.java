package com.example.npttest.entity;

/**
 * Created by liuji on 2017/8/26.
 */

public class Carout {

    /**
     * cnum :
     * pnum : 赣F16712
     * etime : 1504007156
     * eurl :
     * sid :
     * ctype : 小型车
     * cdtp : 临时车
     */

    private String cnum;
    private String pnum;
    private long etime;
    private String eurl;
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

    public long getEtime() {
        return etime;
    }

    public void setEtime(long etime) {
        this.etime = etime;
    }

    public String getEurl() {
        return eurl;
    }

    public void setEurl(String eurl) {
        this.eurl = eurl;
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
