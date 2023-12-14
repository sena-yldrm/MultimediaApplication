
package com.example.multimediaapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NormalSurusMod extends AppCompatActivity{
    private Button btnMusic;
    private Button btnKonum;
    private ImageView ivhava;
    private ImageView ivmusic;
    private Button btnHava;
    private ImageView ivtelefon;
    private ImageView ivHarita;


    private TextClock textClock;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_surus_mod);
        ivhava =findViewById(R.id.imgHava);
        ivmusic = findViewById(R.id.imgMusic);
        ivtelefon = findViewById(R.id.imgTelefon);
        ivHarita=findViewById(R.id.imgHarita);



        ivhava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NormalSurusMod.this, HavaDurumuActivity.class);
                startActivity(intent);
            }
        });

        ivtelefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NormalSurusMod.this, TelefonActivity.class);
                startActivity(intent);
            }
        });



        ivmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NormalSurusMod.this, MusicPlayer.class);
                startActivity(intent);
            }
        });
        ivHarita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NormalSurusMod.this, Harita.class);
                startActivity(intent);
            }
        });

    }




    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }
}
