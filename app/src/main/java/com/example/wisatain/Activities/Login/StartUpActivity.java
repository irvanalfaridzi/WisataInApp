package com.example.wisatain.Activities.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class StartUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase mDatabase;
    DatabaseReference mUsers;

    String getUID;
    public String getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        getUID = sharedPreferences.getString("UID", "");
        Log.d("asdasd", "onCreate: " + getUID);

        if (getUID != null) {
            mUsers.child(getUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getEmail = dataSnapshot.child("Email").getValue(String.class);
                    getPassword = dataSnapshot.child("Password").getValue(String.class);

                    Log.d("emailpass", "onDataChange: " + getEmail + " " + getPassword);

                    if (getPassword != null && getPassword != null) {

                        mAuth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } else {

                        Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}
