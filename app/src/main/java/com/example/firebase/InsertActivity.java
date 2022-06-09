package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.database.Mahasiswa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private EditText et_title, et_description;
    private Button btn_submit;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    Mahasiswa mahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();
    }

    private void init(){
        textView = findViewById(R.id.textView);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        btn_submit = findViewById(R.id.btn_submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Mahasiswa.class.getSimpleName());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        btn_submit.setOnClickListener(this);
    }
    public void submitData(){
        if (!validateForm()){
            return;
        }
        String title = et_title.getText().toString();
        String desc = et_description.getText().toString();
        String uid = firebaseUser.getUid();
        Mahasiswa baru = new Mahasiswa(uid, title, desc);
        databaseReference.push().setValue(baru).addOnSuccessListener(this, new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertActivity.this, "Add data", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InsertActivity.this, InsertNoteActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(et_title.getText().toString())) {
            et_title.setError("Required");
            result = false;
        } else {
            et_title.setError(null);
        }
        if (TextUtils.isEmpty(et_description.getText().toString())) {
            et_description.setError("Required");
            result = false;
        } else {
            et_description.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                submitData();
                break;
        }
    }
}