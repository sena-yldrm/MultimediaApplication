package com.example.multimediaapplication;

import androidx.appcompat.app.AppCompatActivity;

public class ContactItem2 extends AppCompatActivity {

    private String isim;
    private String telefonNumarasi;

    public ContactItem2(String isim, String telefonNumarasi) {
        this.isim = isim;
        this.telefonNumarasi = telefonNumarasi;
    }

    public String getIsim() {
        return isim;
    }

    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }
}