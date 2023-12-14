package com.example.multimediaapplication;

public class Contact {
    private String isim;
    private String telefonNumarasi;

    public Contact(String isim, String telefonNumarasi) {
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
