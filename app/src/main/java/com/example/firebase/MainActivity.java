package com.example.firebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etEmail;
    private EditText etPass;
    private Button btnMasuk;
    private TextView btnDaftar;
    private SignInButton btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_pass);
        btnMasuk = findViewById(R.id.btn_masuk);
        btnDaftar = findViewById(R.id.btn_daftar);
        btnGoogle = findViewById(R.id.btn_google);
        mAuth = FirebaseAuth.getInstance();

        allowLoginWithGoogle();

        btnMasuk.setOnClickListener(this);
        btnDaftar.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                login(etEmail.getText().toString(), etPass.getText().toString());
                break;
            case R.id.btn_daftar:
                signUp(etEmail.getText().toString(), etPass.getText().toString());
                break;
            case R.id.btn_google:
                loginGoogle();
                break;
        }
    }

    private void allowLoginWithGoogle(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("188809952858-qu4b2nsusnl0uqdv2obtes86uhgbrql9.apps.googleusercontent.com").requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void loginGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            }catch (ApiException e){
                Log.w(TAG, "Google Sign In Gagal", e);
            }
        }
    }

    private void handleSignInResult(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnSuccessListener(this, authResult -> {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }).addOnFailureListener(this, e -> Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show());
    }

    public void signUp(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                                } else {
// If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
    }

    public void login(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
// If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            result = false;
        } else {
            etEmail.setError(null);
        }
        if (TextUtils.isEmpty(etPass.getText().toString())) {
            etPass.setError("Required");
            result = false;
        } else {
            etPass.setError(null);
        }
        return result;
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, InsertNoteActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Log In First", Toast.LENGTH_SHORT).show();
        }
    }
}