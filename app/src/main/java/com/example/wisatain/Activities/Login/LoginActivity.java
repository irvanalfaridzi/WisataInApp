package com.example.wisatain.Activities.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wisatain.Activities.Main.MainActivity;
import com.example.wisatain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.letEmail)
    EditText email;

    @BindView(R.id.letKataSandi)
    EditText katasandi;

    @BindView(R.id.lProgressBar)
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mref;

    String getUID;
    String getUser, getStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        email.setText("test@test.test");
        katasandi.setText("testtest");
    }

    public void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.lbtnLogin).setEnabled(false);
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), katasandi.getText().toString().trim()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            findViewById(R.id.lbtnLogin).setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Selamat Datang", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);

                        } else {
                            findViewById(R.id.lbtnLogin).setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @OnClick(R.id.lbtnLogin)
    public void login() {
        if (email.getText().toString().trim().isEmpty()) {
            email.setError("Masukan username");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.setError("Tolong masukan email dengan benar");
            email.requestFocus();
            return;
        }
        if (katasandi.getText().toString().trim().isEmpty()) {
            katasandi.setError("Masukan password");
            katasandi.requestFocus();
            return;
        }
        if (katasandi.getText().toString().trim().length() < 8) {
            katasandi.setError("Kata sandi minimal terdiri dari 8 karakter");
            katasandi.requestFocus();
            return;
        }

        loadData();
    }

    @OnClick(R.id.lbtnCrtAccount)
    public void createAccount() {
        Intent intent = new Intent(LoginActivity.this, BuatAkunActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.ltLupPassword)
//    public void lupaPassword() {
//        Intent intent = new Intent(LoginActivity.this, LupaPasswordActivity.class);
//        startActivity(intent);
//    }

}
