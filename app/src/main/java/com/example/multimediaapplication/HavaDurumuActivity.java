package com.example.multimediaapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.multimediaapplication.API.Api;
import com.example.multimediaapplication.API.Cevap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HavaDurumuActivity extends AppCompatActivity {
    private EditText editSehir;
    private Button btnHavaDurumunuGetir;
    private ImageView imageViewGif;
    private TextView textHavaDurumuBilgisi;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "9d430cc655ba7e6f2e171ce57f0e91ff";

    private Retrofit retrofit;
    private Cevap cevap ;
    private Call<Cevap> cevapCall;
    private Api apiService;

    private void init() {
        btnHavaDurumunuGetir = findViewById(R.id.btnHavaDurumunuGetir);
        editSehir = findViewById(R.id.editSehir);
        textHavaDurumuBilgisi = findViewById(R.id.textHavaDurumuBilgisi);
        imageViewGif=findViewById(R.id.imageViewGif);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hava_durumu2);
        init();

        Glide.with(HavaDurumuActivity.this)
                .asGif()
                .load(R.drawable.senavideo)
                .placeholder(R.drawable.senavideo)
                .error(R.drawable.senavideo)
                .into(imageViewGif);
        btnHavaDurumunuGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRetrofitSettings();
            }
        });
    }

    private void setRetrofitSettings() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(Api.class);

        String cityName = editSehir.getText().toString();
        Call<Cevap> cevapCall = apiService.getWeather(cityName, API_KEY,"tr","metric");
        cevapCall.enqueue(new Callback<Cevap>() {
            @Override
            public void onResponse(Call<Cevap> call, Response<Cevap> response) {
                if (response.isSuccessful()) {
                    cevap = response.body();
                    if (cevap != null) {
                        textHavaDurumuBilgisi.setText("\n Sıcaklık: " + cevap.getMain().getTemp()  + "°C");
                        textHavaDurumuBilgisi.append("\n Hissedilen Sıcaklık: " + cevap.getMain().getFeelsLike()  + "°C");
                        textHavaDurumuBilgisi.append("\n Hissedilen Maksimum Sıcaklık: " + cevap.getMain().getTempMax()  + "°C");
                        textHavaDurumuBilgisi.append("\n Hissedilen Minimum Sıcaklık: " + cevap.getMain().getTempMin()  + "°C");
                        textHavaDurumuBilgisi.append("\n Rüzgar Hızı: " + cevap.getWind().getSpeed() );
                        textHavaDurumuBilgisi.append("\n Açık/Kapalı: " + cevap.getWeather()[0].getDescription());

                    } else {
                        textHavaDurumuBilgisi.setText("Hava durumu verileri alınamadı.");
                    }
                }



            }

            @Override
            public void onFailure(Call<Cevap> call, Throwable t) {
                textHavaDurumuBilgisi.setText("Hava durumu verileri alınamadı.");
                System.out.println(t.toString());
            }
        });
    }

}

