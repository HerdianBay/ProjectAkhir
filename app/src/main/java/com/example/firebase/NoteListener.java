package com.example.firebase;

import android.widget.ImageButton;

import com.example.firebase.database.Mahasiswa;

public interface NoteListener {
    void onClickUbah(Mahasiswa mahasiswa, ImageButton imageButton);
    void onClickHapus(Mahasiswa mahasiswa, ImageButton imageButton);
}
