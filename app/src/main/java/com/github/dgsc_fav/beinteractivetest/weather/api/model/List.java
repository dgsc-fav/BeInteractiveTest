
package com.github.dgsc_fav.beinteractivetest.weather.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class List {

    @SerializedName("dt")
    @Expose
    private long dt;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("pressure")
    @Expose
    private double pressure;
    @SerializedName("humidity")
    @Expose
    private long humidity;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private long deg;
    @SerializedName("clouds")
    @Expose
    private long clouds;
    @SerializedName("snow")
    @Expose
    private double snow;

    /**
     * No args constructor for use in serialization
     * 
     */
    public List() {
    }

    /**
     * 
     * @param clouds
     * @param dt
     * @param humidity
     * @param pressure
     * @param speed
     * @param snow
     * @param deg
     * @param weather
     * @param temp
     */
    public List(long dt, Temp temp, double pressure, long humidity, java.util.List<Weather> weather, double speed, long deg, long clouds, double snow) {
        this.dt = dt;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weather = weather;
        this.speed = speed;
        this.deg = deg;
        this.clouds = clouds;
        this.snow = snow;
    }

    /**
     * 
     * @return
     *     The dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * 
     * @param dt
     *     The dt
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    public List withDt(long dt) {
        this.dt = dt;
        return this;
    }

    /**
     * 
     * @return
     *     The temp
     */
    public Temp getTemp() {
        return temp;
    }

    /**
     * 
     * @param temp
     *     The temp
     */
    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public List withTemp(Temp temp) {
        this.temp = temp;
        return this;
    }

    /**
     * 
     * @return
     *     The pressure
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * 
     * @param pressure
     *     The pressure
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public List withPressure(double pressure) {
        this.pressure = pressure;
        return this;
    }

    /**
     * 
     * @return
     *     The humidity
     */
    public long getHumidity() {
        return humidity;
    }

    /**
     * 
     * @param humidity
     *     The humidity
     */
    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public List withHumidity(long humidity) {
        this.humidity = humidity;
        return this;
    }

    /**
     * 
     * @return
     *     The weather
     */
    public java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public List withWeather(java.util.List<Weather> weather) {
        this.weather = weather;
        return this;
    }

    /**
     * 
     * @return
     *     The speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public List withSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * 
     * @return
     *     The deg
     */
    public long getDeg() {
        return deg;
    }

    /**
     * 
     * @param deg
     *     The deg
     */
    public void setDeg(long deg) {
        this.deg = deg;
    }

    public List withDeg(long deg) {
        this.deg = deg;
        return this;
    }

    /**
     * 
     * @return
     *     The clouds
     */
    public long getClouds() {
        return clouds;
    }

    /**
     * 
     * @param clouds
     *     The clouds
     */
    public void setClouds(long clouds) {
        this.clouds = clouds;
    }

    public List withClouds(long clouds) {
        this.clouds = clouds;
        return this;
    }

    /**
     * 
     * @return
     *     The snow
     */
    public double getSnow() {
        return snow;
    }

    /**
     * 
     * @param snow
     *     The snow
     */
    public void setSnow(double snow) {
        this.snow = snow;
    }

    public List withSnow(double snow) {
        this.snow = snow;
        return this;
    }

}
