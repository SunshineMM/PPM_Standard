package com.example.npttest.entity;

/**
 * Created by liuji on 2017/10/16.
 */

public class Querynum {

    /**
     * cnum : 5582347
     * pnum : 苏FFF123
     * ctype : 2
     * cdtp : 2
     * itime : 1508116711
     * ptime : 0
     * pmon : 0
     * iurl :
     * sid : Qdg8AFyjKuP0VIKY
     * spare : null
     */
    private String cnum;
    private String pnum;
    private int ctype;
    private int cdtp;
    private long itime;
    private int ptime;
    private int pmon;
    private String iurl;
    private String sid;
    private Object spare;

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

    public int getPtime() {
        return ptime;
    }

    public void setPtime(int ptime) {
        this.ptime = ptime;
    }

    public int getPmon() {
        return pmon;
    }

    public void setPmon(int pmon) {
        this.pmon = pmon;
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

    public Object getSpare() {
        return spare;
    }

    public void setSpare(Object spare) {
        this.spare = spare;
    }
}
