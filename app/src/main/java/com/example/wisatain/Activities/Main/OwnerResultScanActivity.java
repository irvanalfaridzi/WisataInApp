package com.example.wisatain.Activities.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;

    String getQRCode;
    String getKey, getKeteranganTiket, getRefTransaksi, getUIDUser, getNamaUser, getTanggalTransaksi, getNamaWisata, getLokasiWisata, getJumlahTiket, getTanggalPenggunaan, getTotalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_result_scan);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        getQRCode = getintent.getStringExtra("scan");

        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");
        mUsers = mDatabase.getReference().child("Users");

        loadData();

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
}
