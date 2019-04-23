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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    DatabaseReference mTiketBelumScan;

    String intentTiketKey;
    public String getUID, getWisata;
    public String getUIDUser, getNamaUser, getRefTransaksi, getTanggalPembelian, getNamaWisata, getLokasiWisata, getWilayahWisata, getJumlah, getTanggalKunjungan, getTotalHarga, getBuktiPembayaranURL, getTiketStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konfirmasi);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        intentTiketKey = getintent.getStringExtra("key");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users").child(getUID);
        mTiket = mDatabase.getReference().child("Wisata");
        mTiketBelumScan = mDatabase.getReference().child("Users");

        loadData();
    }

    public void loadData() {

        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getWisata = dataSnapshot.child("Wisata").getValue(String.class);

                mTiket.child(getWisata).child("Tiket").child("TelahKonfirmasi").child(intentTiketKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        getUIDUser = dataSnapshot.child("UIDUser").getValue(String.class);
                        getNamaUser = dataSnapshot.child("NamaUser").getValue(String.class);
                        getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                        getTanggalPembelian = dataSnapshot.child("TanggalPembelian").getValue(String.class);
                        getNamaWisata = getWisata;
                        getLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                        getWilayahWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                        getJumlah = dataSnapshot.child("Jumlah").getValue(String.class);
                        getTanggalKunjungan = dataSnapshot.child("TanggalKunjunga").getValue(String.class);
                        getTotalHarga = dataSnapshot.child("TotalHarga").getValue(String.class);
                        getBuktiPembayaranURL = dataSnapshot.child("BuktiPembayaranURL").getValue(String.class);
                        getTiketStatus = dataSnapshot.child("TiketStatus").getValue(String.class);

                        Glide.with(getBaseContext()).load(getBuktiPembayaranURL).into(buktiGambar);
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

        loadData();

        inputTiket tiket = new inputTiket(
                getUIDUser,
                getNamaUser,
                getRefTransaksi,
                getTanggalPembelian,
                getNamaWisata,
                getLokasiWisata,
                getWilayahWisata,
                getJumlah,
                getTanggalKunjungan,
                getTotalHarga,
                getBuktiPembayaranURL,
                "Belum Digunakan"
        );

        mTiketBelumScan.child(getUIDUser).child("Tiket").child("BelumDigunakan").child(intentTiketKey).setValue(tiket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(DetailKonfirmasiActivity.this, ProfilWisataOwnerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    mTiket.child(getWisata).child("Tiket").child("TelahKonfirmasi").child(intentTiketKey).removeValue();
                } else {

                }
            }
        });

    }

    public class inputTiket {

        public String UIDUser, NamaUser, RefTransaksi, TanggalPembelian, NamaWisata, LokasiWisata, WilayahWisata, Jumlah, TanggalKunjungan, TotalHarga, BuktiPembayaranURL, TiketStatus;

        public inputTiket(String UIDUser, String namaUser, String refTransaksi, String tanggalPembelian, String namaWisata, String lokasiWisata, String wilayahWisata, String jumlah, String tanggalKunjungan, String totalHarga, String buktiPembayaranURL, String tiketStatus) {
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

    @OnClick(R.id.dkBtnKonfirmasi)
    public void konfirmasi() {
        saveData();
    }

}
