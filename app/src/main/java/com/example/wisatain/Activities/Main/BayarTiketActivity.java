package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BayarTiketActivity extends AppCompatActivity {

    @BindView(R.id.bttMetodePembayaran)
    TextView metodePembayaran;

    String intentTanggalPesanTiket, intentJumlahTiket, metodeBayar, wisataKey;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    DatabaseReference mTiket;

    String getUID;
    String getNamaWisata, getLokasiWisata, getWilayahWisata, getHargaTiket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar_tiket);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users");
        mWisata = mDatabase.getReference().child("Wisata");
        mTiket = mDatabase.getReference().child("Tiket");

        getUID = mUser.getUid();

        Intent getintent = getIntent();
        intentTanggalPesanTiket = getintent.getStringExtra("tanggalPesanTiket");
        intentJumlahTiket = getintent.getStringExtra("jumlahTiket");
        metodeBayar = getintent.getStringExtra("bayar");
        wisataKey = getintent.getStringExtra("key");

        if (metodeBayar.equals("indomaret")) {
            metodePembayaran.setText("INDOMARET");
        } else if (metodeBayar.equals("bca")) {
            metodePembayaran.setText("BCA");
        } else if (metodeBayar.equals("bni")) {
            metodePembayaran.setText("BNI");
        }

    }

    public void loadData() {

    }

    public void saveData() {

        mWisata.child(wisataKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                getWilayahWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                getHargaTiket = dataSnapshot.child("HargaWisata").getValue(String.class);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String getCurrentDateandTime = simpleDateFormat.format(new Date());

                loadData();

                final String refTransaksi = getNamaWisata + "/" + getWilayahWisata + "/" + intentJumlahTiket + "/" + System.currentTimeMillis() + "/";

                Log.d("test123", "saveData: " + getHargaTiket + " " + intentJumlahTiket);

                Tiket tiket = new Tiket(
                        getUID,
                        refTransaksi,
                        getCurrentDateandTime,
                        getNamaWisata,
                        getLokasiWisata,
                        getWilayahWisata,
                        intentJumlahTiket,
                        intentTanggalPesanTiket,
                        String.valueOf(Integer.parseInt(getHargaTiket)*Integer.parseInt(intentJumlahTiket)),
                        "Menunggu Konfirmasi"
                );

                final String pushKey = mDatabase.getReference("Tiket").push().getKey();
                mTiket.child("MenungguKonfirmasi").child(pushKey).setValue(tiket).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(BayarTiketActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BayarTiketActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class Tiket {
        public String UIDUser, RefTransaksi, TanggalPembelian, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga, TiketStatus;

        public Tiket(String UIDUser, String refTransaksi, String tanggalPembelian, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga, String tiketStatus) {
            this.UIDUser = UIDUser;
            RefTransaksi = refTransaksi;
            TanggalPembelian = tanggalPembelian;
            NamaWisata = namaWisata;
            LokasiWisata = lokasiWisata;
            WilayahWisata = wilayahWisata;
            Jumlah = jumlah;
            TanggalKunjungan = tanggalKunjungan;
            TotalHarga = totalHarga;
            TiketStatus = tiketStatus;
        }
    }

    @OnClick(R.id.btBtnBayar)
    public void bayarTiket() {
        saveData();
    }

}
