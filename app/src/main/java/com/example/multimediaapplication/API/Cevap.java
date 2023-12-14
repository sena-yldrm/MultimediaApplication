package com.example.multimediaapplication.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cevap {


    @SerializedName("weather")
    @Expose
    private Weather[] weather;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("main")
    @Expose
    private Main main;

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @SerializedName("wind")
    @Expose
    private Wind wind;


    public class Weather {
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("main")
        @Expose
        private String main;


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMainn() {return main;}

        public void setMainn(String main) {this.main = main;}




    }

    public class Main {
        @SerializedName("temp")
        @Expose
        private Double temp;
        @SerializedName("feels_like")
        @Expose
        private Double feelsLike;
        @SerializedName("temp_min")
        @Expose
        private Double tempMin;
        @SerializedName("temp_max")
        @Expose
        private Double tempMax;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(Double feelsLike) {
            this.feelsLike = feelsLike;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public void setTempMin(Double tempMin) {
            this.tempMin = tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }
    }
    public class Wind {
        @SerializedName("speed")
        @Expose
        private Double speed;

        public Double getSpeed() {
            return speed;
        }
        public void setSpeed(Double speed) {
            this.speed = speed;
        }
    }

}