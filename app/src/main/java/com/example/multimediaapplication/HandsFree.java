package com.example.multimediaapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.multimediaapplication.API.Api;
import com.example.multimediaapplication.API.Cevap;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HandsFree extends AppCompatActivity implements LocationListener  {
    private TextView songTitleTextView;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton backButton;
    private ImageView imageViewGif;
    private ImageButton nextButton;
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0;
    private boolean isMusicPlaying = false;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 110;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "9d430cc655ba7e6f2e171ce57f0e91ff";
    private TextView textHavaDurumuBilgisi;
    private TextView textHavaDurumuBilgisi2;
    private TextView textHavaDurumuBilgisi3;
    private Retrofit retrofit;

    private Api apiService;
    private ImageView iv_mic;
    private TextView tv_Speech_to_text;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private boolean isListening = false;
    private RecyclerView recyclerView;
    private ContactAdapter2 contactAdapter;
    static final int REQUEST_CODE_CALL_PHONE = 102;

    private ImageView imageViewPhone;
    private static final int REQUEST_CODE_READ_CONTACTS = 101;
    private ArrayList<ContactItem2> contacts;
    private static final int MIN_SPEED = 10;
    private static final int MAX_SPEED = 200;
    private int[] musicFiles = {
            R.raw.song1,
            R.raw.song2,

    };
    private String[] songs = {
            "Aman Güzel Yavaş Yürü",
            "Cuma"

             };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hands_free);
        iv_mic = findViewById(R.id.iv_mic);
        tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);
        songTitleTextView = findViewById(R.id.songTitleTextView);
        backButton = findViewById(R.id.backButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);
        textHavaDurumuBilgisi = findViewById(R.id.textHavaDurumuBilgisi);
        textHavaDurumuBilgisi2 = findViewById(R.id.textHavaDurumuBilgisi2);
        textHavaDurumuBilgisi3 = findViewById(R.id.textHavaDurumuBilgisi3);
        imageViewGif=findViewById(R.id.imageViewGif);
        Resources res = getResources();
        String[] songs = res.getStringArray(R.array.music_titles);
        String songTitle = songs[0];
        songTitleTextView.setText(songTitle);
        mediaPlayer = MediaPlayer.create(this, musicFiles[currentSongIndex]);
        songTitleTextView.setText(songs[currentSongIndex]);

        checkLocationPermissions();
        requestLocationPermissions();

        startLocationUpdates();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex > 0) {
                    currentSongIndex--;
                } else {
                    currentSongIndex = musicFiles.length - 1;
                }
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), musicFiles[currentSongIndex]);
                mediaPlayer.start();
                songTitleTextView.setText(songs[currentSongIndex]);

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSongIndex < musicFiles.length - 1) {
                    currentSongIndex++;
                } else {
                    currentSongIndex = 0;
                }
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), musicFiles[currentSongIndex]);
                mediaPlayer.start();
                songTitleTextView.setText(songs[currentSongIndex]);

            }

        });

        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hava durumunu görmek için: Hava durumu Isparta");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast.makeText(HandsFree.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (!isListening) {
                    startListening();
                } else {
                    stopListening();
                }
            }
        });


        contacts = new ArrayList<>();

        rehberVerileriniCek();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter2(contacts);
        recyclerView.setAdapter(contactAdapter);
        imageViewPhone = findViewById(R.id.imageViewPhone);

        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(HandsFree.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    String phoneNumber = "123456789";

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));

                    startActivity(callIntent);
                } else {

                    ActivityCompat.requestPermissions(HandsFree.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                }
            }
        });
    }
    private void rehberVerileriniCek() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        } else {

            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    if (nameColumnIndex >= 0 && phoneNumberColumnIndex >= 0) {
                        String name = cursor.getString(nameColumnIndex);
                        String phoneNumber = cursor.getString(phoneNumberColumnIndex);

                        if (!phoneNumber.isEmpty()) {

                            ContactItem2 contact = new ContactItem2(name, phoneNumber);
                            contacts.add(contact);
                        }
                    }
                }
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                rehberVerileriniCek();

                contactAdapter.setContacts(contacts);
            } else {

            }
        }
    }
    private boolean isMusicPlayingBeforeListening = false;
    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hava durumunu görmek için: Hava durumu Isparta");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isMusicPlayingBeforeListening = true;
            } else {
                isMusicPlayingBeforeListening = false;
            }

        } catch (Exception e) {
            Toast.makeText(HandsFree.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopListening() {
        if (isMusicPlayingBeforeListening) {
            mediaPlayer.start();
            isMusicPlayingBeforeListening = false;
        }
    }



    private void playMusic() {
        if (!isMusicPlaying) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), musicFiles[currentSongIndex]);
            mediaPlayer.start();
            songTitleTextView.setText(songs[currentSongIndex]);
            isMusicPlaying = true;
        }
    }

    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isMusicPlaying = false;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && result.size() > 0) {
                    String spokenText = result.get(0).toLowerCase();
                    if (spokenText.contains("müzik") || spokenText.contains("çal") || spokenText.contains("şarkı")) {

                        if (!mediaPlayer.isPlaying() || !isMusicPlayingBeforeListening) {
                            pauseMusic();
                        }
                    }

                    Log.d("SPOKEN_TEXT", "Spoken Text: " + spokenText);

                    if (spokenText.contains("müzik şarkı çal")) {
                        Log.d("COMMAND", "Play Music Command Detected");
                        playMusic();
                    } else if (spokenText.contains("hava durumu")) {
                        Log.d("COMMAND", "Weather Command Detected");

                        String cityName = extractCityName(spokenText);

                        getWeatherData(cityName);
                    } else if (spokenText.contains("müzik") || spokenText.contains("çal") || spokenText.contains("şarkı")) {
                        Log.d("COMMAND", "Play Music Command Detected");
                        playMusic();
                    } else if (spokenText.contains("telefon")|| spokenText.contains("ara") || spokenText.contains("rehber")) {
                        Log.d("COMMAND", "Phone Command Detected");

                        Intent phoneIntent = new Intent(this, TelefonActivity.class);
                        startActivity(phoneIntent);
                    }else {
                        Log.d("COMMAND", "Unknown Command");

                    }
                }
            }
        }
    }

    private String extractCityName(String spokenText) {

        String[] words = spokenText.split("hava durumu");
        if (words.length > 1) {

            String cityName = words[1].trim();

            return cityName;
        }
        return "";
    }
    private void getWeatherData(String cityName) {
        Log.d("API_REQUEST", "Requested City: " + cityName);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(Api.class);

        Call<Cevap> cevapCall = apiService.getWeather(cityName, API_KEY, "tr", "metric");
        cevapCall.enqueue(new Callback<Cevap>() {
            @Override
            public void onResponse(Call<Cevap> call, Response<Cevap> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());
                if (response.isSuccessful()) {
                    Cevap cevap = response.body();
                    if (cevap != null) {
                        String weatherInfo =  "\n " + cevap.getName();
                        String weatherInfo2 =  "\n " + cevap.getMain().getTemp() + "°C" ;
                        String weatherInfo3 =  "\n " + cevap.getWeather()[0].getMainn() ;


                        textHavaDurumuBilgisi.setText(weatherInfo);
                        textHavaDurumuBilgisi2.setText(weatherInfo2);
                        textHavaDurumuBilgisi3.setText(weatherInfo3);
                        showGifBasedOnWeather(cevap.getWeather()[0].getMainn());
                    } else {
                        // Boş yanıt
                        textHavaDurumuBilgisi.setText("Hava durumu verileri alınamadı.");
                        textHavaDurumuBilgisi2.setText("Hava durumu verileri alınamadı.");
                        textHavaDurumuBilgisi3.setText("Hava durumu verileri alınamadı.");
                    }
                } else {
                    // Başarısız yanıt
                    textHavaDurumuBilgisi.setText("Hava durumu verileri alınamadı.");
                    textHavaDurumuBilgisi2.setText("Hava durumu verileri alınamadı.");
                    textHavaDurumuBilgisi3.setText("Hava durumu verileri alınamadı.");
                }
            }

            private void showGifBasedOnWeather(String weatherCondition) {
                ImageView imageViewGif = findViewById(R.id.imageViewGif);

                int gifResource;

                if ("Clear".equalsIgnoreCase(weatherCondition)) {
                    gifResource = R.drawable.gunesli;
                }

                else if ("Snow".equalsIgnoreCase(weatherCondition)) {
                    gifResource = R.drawable.karli;
                } else if ("Rain".equalsIgnoreCase(weatherCondition) || "Drizzle".equalsIgnoreCase(weatherCondition)) {
                    gifResource = R.drawable.yagmurlu;
                } else if ("Thunderstorm".equalsIgnoreCase(weatherCondition)|| "Tornado".equalsIgnoreCase(weatherCondition)|| "Squall".equalsIgnoreCase(weatherCondition)) {
                    gifResource = R.drawable.firtinali;
                } else if ("Clouds".equalsIgnoreCase(weatherCondition)|| "Mist".equalsIgnoreCase(weatherCondition)|| "Smoke".equalsIgnoreCase(weatherCondition)|| "Haze".equalsIgnoreCase(weatherCondition)|| "Dust".equalsIgnoreCase(weatherCondition)|| "Fog".equalsIgnoreCase(weatherCondition)|| "Sand".equalsIgnoreCase(weatherCondition)|| "Dust".equalsIgnoreCase(weatherCondition)) {
                    gifResource = R.drawable.bulutlu;
                }  else {

                    gifResource = R.drawable.gunesli;
                }
                Glide.with(getApplicationContext())
                        .load(gifResource)
                        .into(new DrawableImageViewTarget(imageViewGif) {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);


                                if (resource instanceof GifDrawable) {
                                    GifDrawable gifDrawable = (GifDrawable) resource;
                                    gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
                                    gifDrawable.start();
                                }
                            }
                        });


                imageViewGif.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Cevap> call, Throwable t) {

                Log.e("API_ERROR", "API Request Failed: " + t.getMessage());
                t.printStackTrace();
                textHavaDurumuBilgisi.setText("Hava durumu verileri alınamadı.");
            }
        });

    }

    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1F, (LocationListener) this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        float speed = location.getSpeed();
        speed = Math.max(MIN_SPEED, Math.min(MAX_SPEED, speed));

        float volume = (speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED);

        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationUpdates();
        }
    }






}
