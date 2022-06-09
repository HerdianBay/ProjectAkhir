package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.firebase.adapter.MahasiswaAdapter;
import com.example.firebase.database.Mahasiswa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RVview extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private Button button;
    private MahasiswaAdapter mahasiswaAdapter;
    private ArrayList<Mahasiswa> arrayList = new ArrayList<Mahasiswa>();
    private FirebaseAuth mAuth;
    private Mahasiswa mahasiswa;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        init();
        data();
        setRecycle();
    }
    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
//        button = findViewById(R.id.button);
//        button.setOnClickListener(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Mahasiswa.class.getSimpleName());
        mAuth = FirebaseAuth.getInstance();
    }
    private void setRecycle(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mahasiswaAdapter = new MahasiswaAdapter(arrayList, getApplicationContext(), listener, firebaseUser);
        recyclerView.setAdapter(mahasiswaAdapter);
    }
    private NoteListener listener = new NoteListener() {
        @Override
        public void onClickUbah(Mahasiswa mahasiswa, ImageButton imageButton) {
            Mahasiswa select = new Mahasiswa();
            select = mahasiswa;
            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
            intent.putExtra("key", select.getKey());
            startActivity(intent);
        }
        @Override
        public void onClickHapus(Mahasiswa mahasiswa, ImageButton imageButton) {
                Mahasiswa select = new Mahasiswa();
                select = mahasiswa;
                deleteData(select.getKey());
        }
    };
    private void deleteData(String key){
        databaseReference.child(key).removeValue().addOnSuccessListener(sucess -> {
            Toast.makeText(getApplicationContext(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
            arrayList.clear();
            data();
        }).addOnFailureListener(error -> {
            Toast.makeText(getApplicationContext(), "Gagal Dihapus", Toast.LENGTH_SHORT).show();
        });
    }
    private void data(){
        databaseReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    mahasiswa = dataSnapshot.getValue(Mahasiswa.class);
                    mahasiswa.setKey(dataSnapshot.getKey());
                    arrayList.add(mahasiswa);
                }
                mahasiswaAdapter.notifyDataSetChanged();
                setRecycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.button:
//                logOut();
//                break;
//        }
    }
    private void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}