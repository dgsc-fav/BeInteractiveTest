
package com.github.dgsc_fav.beinteractivetest.weather.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temp {

    @SerializedName("day")
    @Expose
    private double day;
    @SerializedName("min")
    @Expose
    private double min;
    @SerializedName("max")
    @Expose
    private double max;
    @SerializedName("night")
    @Expose
    private double night;
    @SerializedName("eve")
    @Expose
    private double eve;
    @SerializedName("morn")
    @Expose
    private double morn;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Temp() {
    }

    /**
     * 
     * @param min
     * @param eve
     * @param max
     * @param morn
     * @param night
     * @param day
     */
    public Temp(double day, double min, double max, double night, double eve, double morn) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    /**
     * 
     * @return
     *     The day
     */
    public double getDay() {
        return day;
    }

    /**
     * 
     * @param day
     *     The day
     */
    public void setDay(double day) {
        this.day = day;
    }

    public Temp withDay(double day) {
        this.day = day;
        return this;
    }

    /**
     * 
     * @return
     *     The min
     */
    public double getMin() {
        return min;
    }

    /**
     * 
     * @param min
     *     The min
     */
    public void setMin(double min) {
        this.min = min;
    }

    public Temp withMin(double min) {
        this.min = min;
        return this;
    }

    /**
     * 
     * @return
     *     The max
     */
    public double getMax() {
        return max;
    }

    /**
     * 
     * @param max
     *     The max
     */
    public void setMax(double max) {
        this.max = max;
    }

    public Temp withMax(double max) {
        this.max = max;
        return this;
    }

    /**
     * 
     * @return
     *     The night
     */
    public double getNight() {
        return night;
    }

    /**
     * 
     * @param night
     *     The night
     */
    public void setNight(double night) {
        this.night = night;
    }

    public Temp withNight(double night) {
        this.night = night;
        return this;
    }

    /**
     * 
     * @return
     *     The eve
     */
    public double getEve() {
        return eve;
    }

    /**
     * 
     * @param eve
     *     The eve
     */
    public void setEve(double eve) {
        this.eve = eve;
    }

    public Temp withEve(double eve) {
        this.eve = eve;
        return this;
    }

    /**
     * 
     * @return
     *     The morn
     */
    public double getMorn() {
        return morn;
    }

    /**
     * 
     * @param morn
     *     The morn
     */
    public void setMorn(double morn) {
        this.morn = morn;
    }

    public Temp withMorn(double morn) {
        this.morn = morn;
        return this;
    }

}
