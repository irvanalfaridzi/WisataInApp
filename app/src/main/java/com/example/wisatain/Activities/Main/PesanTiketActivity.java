package com.example.wisatain.Activities.Main;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PesanTiketActivity extends AppCompatActivity {

    @BindView(R.id.ptetJumlahTiket)
    EditText jumlahTiket;

    @BindView(R.id.ptetTanggalPesan)
    EditText tanggalPesan;

    @BindView(R.id.ptiGambarWisata)
    ImageView gambarWisata;

    @BindView(R.id.pttNamaWisata)
    TextView namaWisata;

    @BindView(R.id.pttKotaWisata)
    TextView wilayahWisata;

    @BindView(R.id.pttHargaTiketKeterangan)
    TextView hargaTiket;

    public DatePickerDialog.OnDateSetListener tglPesanSetListener;

    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;

    String getGambarWisata, getNamaWisata, getWilayahWisata, getHargaTiket;
    String wisataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tiket);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");

        Intent intent = getIntent();
        wisataKey = intent.getStringExtra("key");
        Log.d("wisataKey", "onCreate: " + wisataKey);
        tanggalPesan.setEnabled(false);

        loadData();
    }

    public void loadData() {

        mWisata.child(wisataKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                getGambarWisata = dataSnapshot.child("FotoWisataURL").getValue(String.class);
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getWilayahWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                getHargaTiket = dataSnapshot.child("HargaWisata").getValue(String.class);
                Log.d("keynamawisata", "onDataChange: " + key + " " + getNamaWisata);

                if (!key.isEmpty() && !getNamaWisata.isEmpty()) {
                    Glide.with(getBaseContext()).load(getGambarWisata).into(gambarWisata);
                    namaWisata.setText(getNamaWisata);
                    wilayahWisata.setText(getWilayahWisata);
                    hargaTiket.setText(getHargaTiket);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.ptBtnTanggalPesan)
    public void tanggalPesanBtn() {
        Calendar cal = Calendar.getInstance();
        int tahun1 = cal.get(Calendar.YEAR);
        int bulan1 = cal.get(Calendar.MONTH);
        int hari1 = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(PesanTiketActivity.this,
                android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                tglPesanSetListener, tahun1, bulan1, hari1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((android.R.color.transparent)));
        dialog.show();

        tglPesanSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                bulan = bulan + 1;
                Log.d("pttanggal", "onDateSet: dd/mm/yyyy: " + hari + "/" + bulan + "/" + tahun);

                String tanggal = hari + "/" + bulan + "/" + tahun;
                tanggalPesan.setText(tanggal);
            }
        };

    }

    @OnClick(R.id.ptBtnBeliTiket)
    public void beliTiket() {
        if (jumlahTiket.getText().toString().trim().isEmpty()) {
            jumlahTiket.setError("Masukan Jumlah Tiket");
            jumlahTiket.requestFocus();
            return;
        }
        if (tanggalPesan.getText().toString().trim().isEmpty()) {
            tanggalPesan.setError("Masukan Tanggal Pesan Tiket");
            tanggalPesan.requestFocus();
            return;
        }

        Intent intent = new Intent(PesanTiketActivity.this, TransaksiTiketActivity.class);
        intent.putExtra("tanggalPesanTiket", tanggalPesan.getText().toString());
        intent.putExtra("jumlahTiket", jumlahTiket.getText().toString());
        intent.putExtra("key", wisataKey);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
