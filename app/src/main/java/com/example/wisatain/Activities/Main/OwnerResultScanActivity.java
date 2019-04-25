package com.example.wisatain.Activities.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class OwnerResultScanActivity extends AppCompatActivity {

    @BindView(R.id.owsiGambarQRCode)
    ImageView gambarQRCode;

    @BindView(R.id.owstKeteranganTiket)
    TextView keteranganTiket;

    @BindView(R.id.owstRefTransaksiKeterangan)
    TextView refTransaksi;

    @BindView(R.id.owsNamaUserKeterangan)
    TextView namaUser;

    @BindView(R.id.owstTanggalTransaksiKeterangan)
    TextView tanggalTransaksi;

    @BindView(R.id.owstNamaWisataKeterangan)
    TextView namaWisata;

    @BindView(R.id.owstLokasiWisataKeterangan)
    TextView lokasiWisata;

    @BindView(R.id.owstJumlahTiketKeterangan)
    TextView jumlahTiket;

    @BindView(R.id.owstTanggalPenggunaanKeterangan)
    TextView tanggalPenggunaan;

    @BindView(R.id.owstTotalhargaKeterangan)
    TextView totalHarga;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;

    String getUID;

    String getQRCode;
    String getKey, getKeteranganTiket, getRefTransaksi, getUIDUser, getNamaUser, getTanggalTransaksi, getNamaWisata, getLokasiWisata, getJumlahTiket, getTanggalPenggunaan, getTotalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_result_scan);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        getQRCode = getintent.getStringExtra("scan");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users").child(getUID);
        mTiket = mUsers.child("Tiket");

        if (getQRCode == null) {
            namaWisata.setText(null);
            lokasiWisata.setText(null);
            keteranganTiket.setText(null);
        } else {
//            loadData();
        }

    }

    public void loadData() {

        mTiket.child(getQRCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getKey = dataSnapshot.getKey();
                getKeteranganTiket = dataSnapshot.child("TiketStatus").getValue(String.class);
                getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                getUIDUser = dataSnapshot.child("UIDUser").getValue(String.class);
                getTanggalTransaksi = dataSnapshot.child("TanggalPembelian").getValue(String.class);
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getLokasiWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                getJumlahTiket = dataSnapshot.child("Jumlah").getValue(String.class);
                getTanggalPenggunaan = dataSnapshot.child("TanggalKunjungan").getValue(String.class);
                getTotalHarga = dataSnapshot.child("TotalHarga").getValue(String.class);

                mUsers.child(getUIDUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotU) {
                        getNamaUser = dataSnapshotU.child("Nama").getValue(String.class);

                        if (getKey != null && getNamaUser != null) {

                            if (getKeteranganTiket.equals("Siap Digunakan")) {
                                Glide.with(getBaseContext()).load(R.drawable.ic_check_green_100dp).into(gambarQRCode);
                                keteranganTiket.setText("Tiket Valid!");
                                keteranganTiket.setTextColor(Color.GREEN);
                            } else {
                                Glide.with(getBaseContext()).load(R.drawable.ic_clear_red_100dp).into(gambarQRCode);
                                keteranganTiket.setText("Tiket Tidak Valid!");
                                keteranganTiket.setTextColor(Color.RED);
                            }
                            refTransaksi.setText(getRefTransaksi);
                            namaUser.setText(getNamaUser);
                            tanggalTransaksi.setText(getTanggalTransaksi);
                            namaWisata.setText(getNamaWisata);
                            lokasiWisata.setText(getLokasiWisata);
                            jumlahTiket.setText(getJumlahTiket);
                            tanggalPenggunaan.setText(getTanggalPenggunaan);
                            totalHarga.setText(getTotalHarga);

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

    }

    public void saveData() {

        mTiket.child(getQRCode).child("TiketStatus").setValue("Sudah Digunakan");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "Membatalkan", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(OwnerResultScanActivity.this, OwnerResultScanActivity.class);
                intent.putExtra("scan", intentResult.getContents());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.owsBtnScan)
    public void scanTiket() {
//        saveData();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getQRCode != null) {
//            saveData();
        }
        Intent intent = new Intent(OwnerResultScanActivity.this, ProfilWisataOwnerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
