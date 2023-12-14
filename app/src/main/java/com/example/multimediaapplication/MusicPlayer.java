package com.example.multimediaapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MusicPlayer extends AppCompatActivity implements  LocationListener {

    private MediaPlayer mediaPlayer;
    private int[] musicFiles = {R.raw.song1, R.raw.song2, R.raw.song3};
    private int currentSongIndex = 0;

    private Button playButton;
    private Button pauseButton;
    private Button nextButton;
    private ListView musicListView;
    private TextView speedTextView;

    private static final int MIN_SPEED = 10;
    private static final int MAX_SPEED = 200;
    private static final float SPEED_THRESHOLD = 15.0f;
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        requestLocationPermissions();


        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);
        musicListView = findViewById(R.id.musicListView);
        speedTextView = findViewById(R.id.speedTextView);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.music_titles));
        musicListView.setAdapter(adapter);


        mediaPlayer = MediaPlayer.create(this, musicFiles[currentSongIndex]);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseSong();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });


        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSongIndex = position;
                playSelectedSong();
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();
            }
        });

        checkLocationPermissions();


        startLocationUpdates();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                startLocationUpdates();
            } else {

            }
        }
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


        String speedText = "Hız: " + speed + " m/s";
        speedTextView.setText(speedText);


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





    private void playSong() {
        mediaPlayer.start();
    }


    private void pauseSong() {
        mediaPlayer.pause();
    }


    private void nextSong() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        currentSongIndex++;
        if (currentSongIndex >= musicFiles.length) {
            currentSongIndex = 0;
        }
        playSelectedSong();
    }


    private void playSelectedSong() {
        mediaPlayer = MediaPlayer.create(this, musicFiles[currentSongIndex]);
        mediaPlayer.start();
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }
}
