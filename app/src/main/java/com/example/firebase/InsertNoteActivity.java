package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.firebase.database.Mahasiswa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvEmail;
    private TextView tvUid;
    private TextView displayName;
    private Button btnKeluar;
    private Button btnLihatData;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private EditText etTitle;
    private EditText etDesc;
    private Button btnInsert;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Intent intent;
    Mahasiswa mahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        tvEmail = findViewById(R.id.tv_email);
        btnKeluar = findViewById(R.id.btn_keluar);
        displayName = findViewById(R.id.displayName);
        mAuth = FirebaseAuth.getInstance();
        btnKeluar.setOnClickListener(this);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        btnInsert = findViewById(R.id.insertData);
//        btnSubmit = findViewById(R.id.btn_submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Mahasiswa.class.getSimpleName());
        mahasiswa = new Mahasiswa();
        btnLihatData = findViewById(R.id.btnLihat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();

        btnInsert.setOnClickListener(this);
//        btnSubmit.setOnClickListener(this);
        btnLihatData.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null){
//            displayName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
//            tvUid.setText(firebaseUser.getUid());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_keluar:
                logOut();
                break;
            case R.id.insertData:
                insertData();
                break;
            case R.id.btnLihat:
                lihatData();
                break;
        }
    }
    public void lihatData(){
        Intent intent = new Intent(this, RVview.class);
        startActivity(intent);
    }
    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(InsertNoteActivity.this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK); //makesure user cant go back
        startActivity(intent);
    }
    private void insertData(){
        Intent intent = new Intent(this, InsertActivity.class);
        startActivity(intent);
    }
//    public void submitData(){
//        if (!validateForm()){
//            return;
//        }
//        String title = etTitle.getText().toString();
//        String desc = etDesc.getText().toString();
//        Note baru = new Note(title, desc);
//        databaseReference.push().setValue(baru).addOnSuccessListener(this, new OnSuccessListener<Void>()
//        {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(InsertNoteActivity.this, "Add data", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(this, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(InsertNoteActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private boolean validateForm() {
//        boolean result = true;
//        if (TextUtils.isEmpty(etTitle.getText().toString())) {
//            etTitle.setError("Required");
//            result = false;
//        } else {
//            etTitle.setError(null);
//        }
//        if (TextUtils.isEmpty(etDesc.getText().toString())) {
//            etDesc.setError("Required");
//            result = false;
//        } else {
//            etDesc.setError(null);
//        }
//        return result;
//    }
}