package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarWisataActivity extends AppCompatActivity {

    @BindView(R.id.dwetNamaWisata)
    EditText namaWisata;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    DatabaseReference mLocationRef;

    String getUID, getToko, getLocation;

    ArrayList<String> arrayLocation = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_wisata);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mLocationRef = mRef.child("Users");

        getUID = mUser.getUid();

    }

    @OnClick(R.id.dwBtnLanjut)
    public void cekLanjut() {
        if (namaWisata.getText().toString().trim().isEmpty()) {
            namaWisata.setError("Masukan Nama Wisata");
            namaWisata.requestFocus();
            return;
        }
        Intent intent = new Intent(DaftarWisataActivity.this, BiodataWisataActivity.class);
        intent.putExtra("namawisata", namaWisata.getText().toString().trim());
        startActivity(intent);

    }
}