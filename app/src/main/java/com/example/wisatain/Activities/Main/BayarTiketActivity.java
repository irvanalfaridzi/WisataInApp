package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BayarTiketActivity extends AppCompatActivity {

    @BindView(R.id.bttMetodePembayaran)
    TextView metodePembayaran;

    @BindView(R.id.bttRefTransaksiKeterangan)
    TextView refTransaksi;

    @BindView(R.id.bttNamaUserKeterangan)
    TextView namaUser;

    @BindView(R.id.bttTanggalTransaksiKeterangan)
    TextView tanggalTransaksi;

    @BindView(R.id.bttJumlahTiketKeterangan)
    TextView jumlahTiket;

    @BindView(R.id.bttTanggalPenggunaanKeterangan)
    TextView tanggalPenggunaan;

    @BindView(R.id.bttTotalhargaKeterangan)
    TextView totalHarga;

    @BindView(R.id.imgIndomaret)
    ImageView logoImage;

    String intentTanggalPesanTiket, intentJumlahTiket, metodeBayar, wisataKey;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    DatabaseReference mTiket;

    public String getUID, getNamaUser;
    public String getCurrentDateandTime, setrefTransaksi;
    public String getNamaWisata, getLokasiWisata, getWilayahWisata, getHargaTiket;

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

        getUID = mUser.getUid();

        Intent getintent = getIntent();
        intentTanggalPesanTiket = getintent.getStringExtra("tanggalPesanTiket");
        intentJumlahTiket = getintent.getStringExtra("jumlahTiket");
        metodeBayar = getintent.getStringExtra("bayar");
        wisataKey = getintent.getStringExtra("key");

        if (metodeBayar.equals("indomaret")) {
            Glide.with(getBaseContext()).load(R.drawable.logoindomart).into(logoImage);
            metodePembayaran.setText("INDOMARET");
        } else if (metodeBayar.equals("bca")) {
            Glide.with(getBaseContext()).load(R.drawable.bca).into(logoImage);
            metodePembayaran.setText("BCA");
        } else if (metodeBayar.equals("bni")) {
            Glide.with(getBaseContext()).load(R.drawable.bni).into(logoImage);
            metodePembayaran.setText("BNI");
        }
        Log.d("cekkey", "onCreate: " + wisataKey);

        loadData();

    }

    public void loadData() {

        mUsers.child(getUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNamaUser = dataSnapshot.child("Nama").getValue(String.class);

                mWisata.child(wisataKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getKey();
                        getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                        getLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                        getWilayahWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                        getHargaTiket = dataSnapshot.child("HargaWisata").getValue(String.class);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        getCurrentDateandTime = simpleDateFormat.format(new Date());

                        setrefTransaksi = getNamaWisata + "/" + getWilayahWisata + "/" + intentJumlahTiket + "/" + System.currentTimeMillis() + "/";

                        Log.d("test123", "saveData: " + getHargaTiket + " " + intentJumlahTiket);

                        refTransaksi.setText(setrefTransaksi);
                        namaUser.setText(getNamaUser);
                        tanggalTransaksi.setText(getCurrentDateandTime);
                        jumlahTiket.setText(intentJumlahTiket);
                        tanggalPenggunaan.setText(intentTanggalPesanTiket);
                        totalHarga.setText(String.valueOf(Integer.parseInt(getHargaTiket)*Integer.parseInt(intentJumlahTiket)));

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

        Tiket tiket = new Tiket(
                setrefTransaksi,
                getCurrentDateandTime,
                wisataKey,
                getNamaWisata,
                getLokasiWisata,
                getWilayahWisata,
                intentJumlahTiket,
                intentTanggalPesanTiket,
                String.valueOf(Integer.parseInt(getHargaTiket)*Integer.parseInt(intentJumlahTiket)),
                "Menunggu Konfirmasi"
        );

        final String pushKey = mDatabase.getReference("Tiket").push().getKey();
        mUsers.child(getUID).child("Tiket").child("MenungguKonfirmasi").child(pushKey).setValue(tiket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(BayarTiketActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    mDatabase.getReference().child("Tiket").child(pushKey).child("UIDUser").setValue(getUID);
                    mDatabase.getReference().child("Tiket").child(pushKey).child("TiketStatus").setValue("Telah Konfirmasi");
                    finish();
                }
            }
        });

    }

    public class Tiket {
        public String RefTransaksi, TanggalPembelian, WisataID, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga, TiketStatus;

        public Tiket(String refTransaksi, String tanggalPembelian, String wisataID, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga, String tiketStatus) {
            RefTransaksi = refTransaksi;
            TanggalPembelian = tanggalPembelian;
            WisataID = wisataID;
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
