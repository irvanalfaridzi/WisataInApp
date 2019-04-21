package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTiketActivity extends AppCompatActivity {

    @BindView(R.id.dtiGambarQRCode)
    ImageView qrCode;

    @BindView(R.id.dttRefTransaksiKeterangan)
    TextView refTransaksi;

    @BindView(R.id.dttTanggalTransaksiKeterangan)
    TextView tanggalTransaksi;

    @BindView(R.id.dttNamaWisataKeterangan)
    TextView namaWisata;

    @BindView(R.id.dttLokasiWisataKeterangan)
    TextView lokasiWisata;

    @BindView(R.id.dttJumlahTiketKeterangan)
    TextView jumlahTiket;

    @BindView(R.id.dttTanggalPenggunaanKeterangan)
    TextView tanggalPenggunaan;

    @BindView(R.id.dttTotalhargaKeterangan)
    TextView totalHarga;

    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;

    String intentTiketKey;
    String getRefTransaksi, getTanggalTransaksi, getNamaWisata, getLokasiwisata, getJumlahTiket, getTanggalPenggunaan, getTotalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tiket);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        intentTiketKey = getintent.getStringExtra("key");

        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");

        loadData();
    }

    public void loadData() {

        mTiket.child(intentTiketKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                getTanggalTransaksi = dataSnapshot.child("TanggalPembelian").getValue(String.class);
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getLokasiwisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                getJumlahTiket = dataSnapshot.child("Jumlah").getValue(String.class);
                getTanggalPenggunaan = dataSnapshot.child("TanggalKunjungan").getValue(String.class);
                getTotalHarga = dataSnapshot.child("TotalHarga").getValue(String.class);
                Log.d("dtcek", "onDataChange: " + getRefTransaksi + " " + getNamaWisata);

                if (!getNamaWisata.isEmpty() && !getRefTransaksi.isEmpty()) {
                    refTransaksi.setText(getRefTransaksi);
                    tanggalTransaksi.setText(getTanggalTransaksi);
                    namaWisata.setText(getNamaWisata);
                    lokasiWisata.setText(getLokasiwisata);
                    jumlahTiket.setText(getJumlahTiket);
                    tanggalPenggunaan.setText(getTanggalPenggunaan);
                    totalHarga.setText(getTotalHarga);

                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(intentTiketKey, BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
