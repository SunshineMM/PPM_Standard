package com.example.npttest.entity;

/**
 * Created by liuji on 2017/10/25.
 */

public class Weather {

    /**
     * week : 星期一
     * templow : 18
     * weather : 多云
     * temphigh : 25
     * img : 1
     * winddirect : 东北风
     * windpower : 3-4 级
     */

    private String week;
    private String templow;
    private String weather;
    private String temphigh;
    private String img;
    private String winddirect;
    private String windpower;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemplow() {
        return templow;
    }

    public void setTemplow(String templow) {
        this.templow = templow;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemphigh() {
        return temphigh;
    }

    public void setTemphigh(String temphigh) {
        this.temphigh = temphigh;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWinddirect() {
        return winddirect;
    }

    public void setWinddirect(String winddirect) {
        this.winddirect = winddirect;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }
}
