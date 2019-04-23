package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
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

public class DetailKonfirmasiActivity extends AppCompatActivity {

    @BindView(R.id.dkiGambarBukti)
    ImageView buktiGambar;

    @BindView(R.id.dktRefTransaksi)
    TextView refTransaksi;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mUsers;
    DatabaseReference mTiket;

    String intentWisataKey;
    public String getUID, getWisata;
    String getRefTransaksi, getBuktiURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konfirmasi);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        intentWisataKey = getintent.getStringExtra("key");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users").child(getUID);
        mTiket = mDatabase.getReference().child("Wisata");

        loadData();
    }

    public void loadData() {

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getWisata = dataSnapshot.child("Wisata").getValue(String.class);

                mTiket.child(getWisata).child("Tiket").child("TelahKonfirmasi").child(intentWisataKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                        getBuktiURL = dataSnapshot.child("BuktiPembayaranURL").getValue(String.class);

                        Glide.with(getBaseContext()).load(getBuktiURL).into(buktiGambar);
                        refTransaksi.setText("Ref. Transaksi : " + getRefTransaksi);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveData() {

    }

    @OnClick(R.id.dkBtnKonfirmasi)
    public void konfirmasi() {
        saveData();
    }

}
