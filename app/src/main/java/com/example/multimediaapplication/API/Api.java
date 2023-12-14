package com.example.multimediaapplication.API;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
public interface Api {
    @GET("weather")
    Call<Cevap> getWeather(@Query("q") String cityName, @Query("appid") String apiKey, @Query("lang") String lang,@Query("units") String units);
    // POST method
    @FormUrlEncoded
    @POST("weather")
    Call<Void> postMethod(@Field("param1") String param1, @Field("param2") String param2);

    // PUT method
    @FormUrlEncoded
    @PUT("weather/1")
    Call<Void> putMethod(@Query("1") int id, @Field("param1") String param1, @Field("param2") String param2);

    // DELETE method
    @DELETE("weather/1")
    Call<Void> deleteMethod(@Query("1") int id);
}

