package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class KonfirmasiTiketActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;

    String getKey, getUIDUser, getRefTransaksi, getJumlahTiket, getTotalHarga, getBuktiURL, getTanggalKunjungan;

    ArrayList<String> arrayTiketKey = new ArrayList<>();
    ArrayList<String> arrayNamaUser = new ArrayList<>();
    ArrayList<String> arrayUIDUser = new ArrayList<>();
    ArrayList<String> arrayRefTransaksi = new ArrayList<>();
    ArrayList<String> arrayJumlahTiket = new ArrayList<>();
    ArrayList<String> arrayTotalHarga = new ArrayList<>();
    ArrayList<String> arrayBuktiURL = new ArrayList<>();
    ArrayList<String> arrayTanggalKunjungan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_tiket);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");
    }

    public void loadData() {

        mTiket.child("MenungguKonfirmasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getKey = ds.getKey();
                    getUIDUser = ds.child("UIDUser").getValue(String.class);
                    getRefTransaksi = ds.child("RefTransaksi").getValue(String.class);
                    getJumlahTiket = ds.child("Jumlah").getValue(String.class);
                    getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                    getBuktiURL = ds.child("GambarBuktiURL").getValue(String.class);
                    getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);

                    arrayTiketKey.add(getKey);
                    arrayUIDUser.add(getUIDUser);
                    arrayRefTransaksi.add(getRefTransaksi);
                    arrayJumlahTiket.add(getJumlahTiket);
                    arrayTotalHarga.add(getTotalHarga);
                    arrayBuktiURL.add(getBuktiURL);
                    arrayTanggalKunjungan.add(getTanggalKunjungan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
