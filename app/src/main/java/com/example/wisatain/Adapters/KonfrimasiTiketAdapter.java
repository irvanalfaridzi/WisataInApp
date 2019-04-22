package com.example.wisatain.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.Fragments.DetailKonfirmasiTiketFragment;
import com.example.wisatain.R;

public class KonfrimasiTiketAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView refTransaksi, namaUser, jumlahTiket, totalHarga, tanggalKunjungan;
    public ImageView gambarBukti;
    private Context context;

    public KonfrimasiTiketAdapter(@NonNull View itemView) {
        super(itemView);

        refTransaksi = itemView.findViewById(R.id.ikttRefTransaksi);
        namaUser = itemView.findViewById(R.id.ikttNamaUser);
        jumlahTiket = itemView.findViewById(R.id.ikttJumlahTiket);
        totalHarga = itemView.findViewById(R.id.ikttTotalHarga);
        tanggalKunjungan = itemView.findViewById(R.id.ikttTanggalKunjungan);
        gambarBukti = itemView.findViewById(R.id.iktiGambarBukti);
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ktFrameLayout, new DetailKonfirmasiTiketFragment()).commit();
    }
}
