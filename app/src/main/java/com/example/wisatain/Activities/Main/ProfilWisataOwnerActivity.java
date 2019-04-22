package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilWisataOwnerActivity extends AppCompatActivity {

    @BindView(R.id.pwotNamaOwner)
    TextView namaOwner;

    @BindView(R.id.pwoiGambarOwner)
    ImageView gambarOwner;

    @BindView(R.id.pwotHargaTiketKeterangan)
    TextView hargaTiket;

    @BindView(R.id.pwotNamaWisata)
    TextView namaWisata;

    @BindView(R.id.pwoiGambarWisata)
    ImageView gambarWisata;

    @BindView(R.id.pwotNamadanKotaWisata)
    TextView namadanKotaWisata;

    @BindView(R.id.pwotDeskripsiWisata)
    TextView deskripsiWisata;

    @BindView(R.id.pwotJamOperasional)
    TextView jamOperasional;

    @BindView(R.id.pwotCocokUntukKeterangan)
    TextView cocokUntuk;

    @BindView(R.id.pwotDetailLokasiKeterangan)
    TextView detailLokasiWisata;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;

    String getNamaUser, getGambarUserURL, getWisataUser;
    String key;
    String getHargaTiket, getNamaWisata, getKotaWisata, getGambarWisataURL, getDeskripsiWisata, getJamOperasional, getKategoriWisata, getLokasiWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_wisata_owner);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");
        mUsers = mDatabase.getReference().child("Users");

        loadData();

    }

    public void loadData() {

        mUsers.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNamaUser = dataSnapshot.child("Nama").getValue(String.class);
                getGambarUserURL = dataSnapshot.child("FotoURL").getValue(String.class);
                getWisataUser = dataSnapshot.child("Wisata").getValue(String.class);
                Log.d("getWisataUser1", "loadData: " + getWisataUser);

                mWisata.child(getWisataUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        key = dataSnapshot.getKey();
                        getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                        getHargaTiket = dataSnapshot.child("HargaWisata").getValue(String.class);
                        getGambarWisataURL = dataSnapshot.child("FotoWisataURL").getValue(String.class);
                        getDeskripsiWisata = dataSnapshot.child("DeskripsiWisata").getValue(String.class);
                        getKotaWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                        getJamOperasional = dataSnapshot.child("JamOperasional").getValue(String.class);
                        getKategoriWisata = dataSnapshot.child("KategoriWisata").getValue(String.class);
                        getLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                        Log.d("getWisataUser4", "onDataChange: " + key + " " + getJamOperasional);

                        if (getNamaUser != null && getNamaWisata != null) {

                            namaOwner.setText(getNamaUser);
                            Glide.with(getApplicationContext()).load(getGambarUserURL).into(gambarOwner);
                            if (getHargaTiket.toString().equals("0")) {
                                hargaTiket.setText("Gratis");
                            } else {
                                hargaTiket.setText("Rp" + getHargaTiket);
                            }
                            namaWisata.setText(getNamaWisata);
                            Glide.with(getApplicationContext()).load(getGambarWisataURL).into(gambarWisata);
                            namadanKotaWisata.setText(getNamaWisata + ", " + getKotaWisata);
                            deskripsiWisata.setText(getDeskripsiWisata);
                            jamOperasional.setText("Jam Operasional : " + getJamOperasional);
                            cocokUntuk.setText(getKategoriWisata);
                            detailLokasiWisata.setText(getLokasiWisata);

                        }

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

        Log.d("getWisataUser2", "loadData: " + getWisataUser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Membatalkan", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProfilWisataOwnerActivity.this, OwnerResultScanActivity.class);
                intent.putExtra("scan", intentResult.getContents());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.pwoBtnScan)
    public void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @OnClick(R.id.pwoBtnKonfirmasiTiket)
    public void konfirmasiTiket() {
        Intent intent = new Intent(ProfilWisataOwnerActivity.this, KonfirmasiTiketActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.pwoBtnEditWisata)
    public void editWisata() {
        Intent intent = new Intent(ProfilWisataOwnerActivity.this, EditWisataActivity.class);
        startActivity(intent);
    }
}
