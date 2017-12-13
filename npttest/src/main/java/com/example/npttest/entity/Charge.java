package com.example.npttest.entity;

/**
 * Created by liuji on 2017/9/29.
 */

public class Charge {

    /**
     * cnum :
     * pnum : 冀F579R4
     * itime : 1509454890
     * ctime : 1509455087
     * nmon : 200
     * rmon : 200
     * pmon : 0
     * smon : 0
     * dmon : 0
     * back : 0
     * preson : 0
     * ptype : 0
     * cdtp : 临时车
     * ctype : 小型车
     * iurl : 4b5633bc64c4a78c1ff62f7a6cb7d173.jpg
     * ourl : 4b5633bc64c4a78c1ff62f7a6cb7d173.jpg
     */

    private String cnum;
    private String pnum;
    private long itime;
    private long ctime;
    private double nmon;
    private double rmon;
    private double pmon;
    private double smon;
    private double dmon;
    private int back;
    private int preson;
    private String ptype;
    private String cdtp;
    private String ctype;
    private String iurl;
    private String ourl;

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

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public double getNmon() {
        return nmon;
    }

    public void setNmon(double nmon) {
        this.nmon = nmon;
    }

    public double getRmon() {
        return rmon;
    }

    public void setRmon(double rmon) {
        this.rmon = rmon;
    }

    public double getPmon() {
        return pmon;
    }

    public void setPmon(double pmon) {
        this.pmon = pmon;
    }

    public double getSmon() {
        return smon;
    }

    public void setSmon(double smon) {
        this.smon = smon;
    }

    public double getDmon() {
        return dmon;
    }

    public void setDmon(double dmon) {
        this.dmon = dmon;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getPreson() {
        return preson;
    }

    public void setPreson(int preson) {
        this.preson = preson;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getCdtp() {
        return cdtp;
    }

    public void setCdtp(String cdtp) {
        this.cdtp = cdtp;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public String getOurl() {
        return ourl;
    }

    public void setOurl(String ourl) {
        this.ourl = ourl;
    }
}
