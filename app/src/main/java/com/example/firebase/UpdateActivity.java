package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.database.Mahasiswa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etTitle, etDesc;
    private Button button;
    private String key;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    Mahasiswa mahasiswa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        init();
        getData(key);
    }
    private void init(){
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        button = findViewById(R.id.btn_submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Mahasiswa.class.getSimpleName());
        mahasiswa = new Mahasiswa();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        button.setOnClickListener(this);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
    }
    private void getData(String key){
        if(key != null){
            databaseReference.child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        mahasiswa = task.getResult().getValue(Mahasiswa.class);
                        etTitle.setText(mahasiswa.getNama());
                        etDesc.setText(mahasiswa.getNIM());
                    }
                }
            });
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Required");
            result = false;
        } else {
            etTitle.setError(null);
        }
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            etDesc.setError("Required");
            result = false;
        } else {
            etDesc.setError(null);

        }
        return result;
    }

    private void submitData(String key){
        if(!validateForm()){
            return;
        }
        if(key != null){
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();
            String uid = firebaseUser.getUid();
            Mahasiswa baru = new Mahasiswa(uid, title, desc);
            databaseReference.child(key).setValue(baru).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Intent intent = new Intent(UpdateActivity.this, InsertNoteActivity.class);
                    startActivity(intent);
                    Toast.makeText(UpdateActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateActivity.this, "Update Gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                submitData(key);
                break;
        }
    }
}