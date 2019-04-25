package com.example.wisatain.Activities.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public String getUID;
    public String getUIDUserTiket, getTiketStatus;

    String getQRCode;
    public String getKey, getKeteranganTiket, getRefTransaksi, getUIDUser, getNamaUser, getTanggalTransaksi, getNamaWisata, getLokasiWisata, getWilayahWisata, getJumlahTiket, getTanggalPenggunaan, getTotalHarga, getBuktiURL;

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
        Log.d("uidusertiketqr", "onDataChange: " + getQRCode);

        if (getQRCode == null) {
            namaWisata.setText(null);
            lokasiWisata.setText(null);
            keteranganTiket.setText(null);
        } else {
            loadData();
        }

    }

    public void loadData() {

        mDatabase.getReference().child("Tiket").child(getQRCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getUIDUserTiket = dataSnapshot.child("UIDUser").getValue(String.class);
                getTiketStatus = dataSnapshot.child("TiketStatus").getValue(String.class);

                Log.d("uidusertiket", "onDataChange: " + getUIDUserTiket);

                mDatabase.getReference().child("Users").child(getUIDUserTiket).child("Tiket").child("BelumDigunakan").child(getQRCode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getKey = dataSnapshot.getKey();
                        getKeteranganTiket = dataSnapshot.child("TiketStatus").getValue(String.class);
                        getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                        getUIDUser = dataSnapshot.child("UIDUser").getValue(String.class);
                        getTanggalTransaksi = dataSnapshot.child("TanggalPembelian").getValue(String.class);
                        getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                        getWilayahWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                        getLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                        getJumlahTiket = dataSnapshot.child("Jumlah").getValue(String.class);
                        getTanggalPenggunaan = dataSnapshot.child("TanggalKunjungan").getValue(String.class);
                        getTotalHarga = dataSnapshot.child("TotalHarga").getValue(String.class);
                        getBuktiURL = dataSnapshot.child("BuktiPembayaranURL").getValue(String.class);
                        Log.d("keyketerangan", "onDataChange: " + getKey + " " + getKeteranganTiket + " " + getRefTransaksi);

                        if (getKey != null) {

                            if (getTiketStatus.equals("Belum Digunakan")) {
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

        sudahTiket tiket = new sudahTiket(
                getUIDUserTiket,
                getNamaUser,
                getRefTransaksi,
                getTanggalTransaksi,
                getNamaWisata,
                getLokasiWisata,
                getWilayahWisata,
                getJumlahTiket,
                getTanggalPenggunaan,
                getTotalHarga,
                getBuktiURL,
                "Sudah Digunakan"
        );

        mDatabase.getReference().child("Users").child(getUIDUserTiket).child("Tiket").child("SudahDigunakan").child(getQRCode).setValue(tiket).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            mDatabase.getReference().child("Tiket").child(getQRCode).child("TiketStatus").setValue("Sudah Digunakan");
                            mDatabase.getReference().child("Users").child(getUIDUser).child("Tiket").child("BelumDigunakan").child(getQRCode).setValue(null);

                        } else {
                            Toast.makeText(OwnerResultScanActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.owsBtnScan)
    public void scanTiket() {
        if (getQRCode != null) {
            saveData();
        }
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public class sudahTiket {

        public String UIDUser, NamaUser, RefTransaksi, TanggalPembelian, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga, BuktiPembayaranURL, TiketStatus;

        public sudahTiket(String UIDUser, String namaUser, String refTransaksi, String tanggalPembelian, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga, String buktiPembayaranURL, String tiketStatus) {
            this.UIDUser = UIDUser;
            NamaUser = namaUser;
            RefTransaksi = refTransaksi;
            TanggalPembelian = tanggalPembelian;
            NamaWisata = namaWisata;
            LokasiWisata = lokasiWisata;
            WilayahWisata = wilayahWisata;
            Jumlah = jumlah;
            TanggalKunjungan = tanggalKunjungan;
            TotalHarga = totalHarga;
            BuktiPembayaranURL = buktiPembayaranURL;
            TiketStatus = tiketStatus;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getQRCode != null) {
            saveData();
        }
        Intent intent = new Intent(OwnerResultScanActivity.this, ProfilWisataOwnerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
