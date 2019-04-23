package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.example.wisatain.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransaksiTiketActivity extends AppCompatActivity {

    @BindView(R.id.cvIndomaret)
    ImageView indomaret;

    @BindView(R.id.cvBCA)
    ImageView bca;

    @BindView(R.id.cvBNI)
    ImageView bni;

    String bayar;

    String intentTanggalPesanTiket, intentJumlahTiket, wisataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_tiket);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        intentTanggalPesanTiket = getintent.getStringExtra("tanggalPesanTiket");
        intentJumlahTiket = getintent.getStringExtra("jumlahTiket");
        wisataKey = getintent.getStringExtra("key");

        indomaret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar = "indomaret";
                Intent intent = new Intent(TransaksiTiketActivity.this, BayarTiketActivity.class);
                intent.putExtra("tanggalPesanTiket", intentTanggalPesanTiket);
                intent.putExtra("jumlahTiket", intentJumlahTiket);
                intent.putExtra("bayar", bayar);
                intent.putExtra("key", wisataKey);
                startActivity(intent);
            }
        });

        bca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar = "bca";
                Intent intent = new Intent(TransaksiTiketActivity.this, BayarTiketActivity.class);
                intent.putExtra("tanggalPesanTiket", intentTanggalPesanTiket);
                intent.putExtra("jumlahTiket", intentJumlahTiket);
                intent.putExtra("bayar", bayar);
                intent.putExtra("key", wisataKey);
                startActivity(intent);
            }
        });

        bni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar = "bni";
                Intent intent = new Intent(TransaksiTiketActivity.this, BayarTiketActivity.class);
                intent.putExtra("tanggalPesanTiket", intentTanggalPesanTiket);
                intent.putExtra("jumlahTiket", intentJumlahTiket);
                intent.putExtra("bayar", bayar);
                intent.putExtra("key", wisataKey);
                startActivity(intent);
            }
        });

    }
}
