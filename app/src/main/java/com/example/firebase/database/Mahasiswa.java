package com.example.firebase.database;

public class Mahasiswa {
    private String key;
    private String nama;
    private String NIM;
    private String uid;

    public Mahasiswa() {
    }

    public Mahasiswa(String uid, String nama, String NIM) {
        this.uid = uid;
        this.nama = nama;
        this.NIM = NIM;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNamae(String nama) {
        this.nama = nama;
    }

    public String getNIM() {
        return NIM;
    }

    public void setNIM(String NIM) {
        this.NIM = NIM;
    }
}
