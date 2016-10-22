
package com.github.dgsc_fav.beinteractivetest.weather.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private long sunrise;
    @SerializedName("sunset")
    @Expose
    private long sunset;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sys() {
    }

    /**
     * 
     * @param message
     * @param sunset
     * @param sunrise
     * @param country
     */
    public Sys(double message, String country, long sunrise, long sunset) {
        this.message = message;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
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

    public Sys withMessage(double message) {
        this.message = message;
        return this;
    }

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public Sys withCountry(String country) {
        this.country = country;
        return this;
    }

    /**
     * 
     * @return
     *     The sunrise
     */
    public long getSunrise() {
        return sunrise;
    }

    /**
     * 
     * @param sunrise
     *     The sunrise
     */
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public Sys withSunrise(long sunrise) {
        this.sunrise = sunrise;
        return this;
    }

    /**
     * 
     * @return
     *     The sunset
     */
    public long getSunset() {
        return sunset;
    }

    /**
     * 
     * @param sunset
     *     The sunset
     */
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public Sys withSunset(long sunset) {
        this.sunset = sunset;
        return this;
    }

}
