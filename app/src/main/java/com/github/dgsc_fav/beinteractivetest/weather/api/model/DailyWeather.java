
package com.github.dgsc_fav.beinteractivetest.weather.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DailyWeather {

    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("cnt")
    @Expose
    private long cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<com.github.dgsc_fav.beinteractivetest.weather.api.model.List> list = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public DailyWeather() {
    }

    /**
     * 
     * @param message
     * @param cnt
     * @param cod
     * @param list
     * @param city
     */
    public DailyWeather(City city, String cod, double message, long cnt, java.util.List<com.github.dgsc_fav.beinteractivetest.weather.api.model.List> list) {
        this.city = city;
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    /**
     * 
     * @return
     *     The city
     */
    public City getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    public DailyWeather withCity(City city) {
        this.city = city;
        return this;
    }

    /**
     * 
     * @return
     *     The cod
     */
    public String getCod() {
        return cod;
    }

    /**
     * 
     * @param cod
     *     The cod
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    public DailyWeather withCod(String cod) {
        this.cod = cod;
        return this;
    }

    /**
     * 
     * @return
     *     The message
     */
    public double getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(double message) {
        this.message = message;
    }

    public DailyWeather withMessage(double message) {
        this.message = message;
        return this;
    }

    /**
     * 
     * @return
     *     The cnt
     */
    public long getCnt() {
        return cnt;
    }

    /**
     * 
     * @param cnt
     *     The cnt
     */
    public void setCnt(long cnt) {
        this.cnt = cnt;
    }

    public DailyWeather withCnt(long cnt) {
        this.cnt = cnt;
        return this;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<com.github.dgsc_fav.beinteractivetest.weather.api.model.List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<com.github.dgsc_fav.beinteractivetest.weather.api.model.List> list) {
        this.list = list;
    }

    public DailyWeather withList(java.util.List<com.github.dgsc_fav.beinteractivetest.weather.api.model.List> list) {
        this.list = list;
        return this;
    }

}
