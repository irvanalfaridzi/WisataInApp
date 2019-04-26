package com.example.wisatain.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.DetailKonfirmasiActivity;
import com.example.wisatain.R;

public class KonfirmasiTiketAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView refTransaksi, namaUser, jumlahTiket, totalHarga, tanggalKunjungan;
    public ImageView gambarBukti;
    public String key;
    public ConstraintLayout constraintLayout;
    private Context context;

    public KonfirmasiTiketAdapter(@NonNull View itemView) {
        super(itemView);

        refTransaksi = itemView.findViewById(R.id.ikttRefTransaksi);
        namaUser = itemView.findViewById(R.id.ikttNamaUser);
        jumlahTiket = itemView.findViewById(R.id.ikttJumlahTiket);
        totalHarga = itemView.findViewById(R.id.ikttTotalHarga);
        tanggalKunjungan = itemView.findViewById(R.id.ikttTanggalKunjungan);
        gambarBukti = itemView.findViewById(R.id.iktiGambarBukti);
        constraintLayout = itemView.findViewById(R.id.ktCoonstraint);

        constraintLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(itemView.getContext(), DetailKonfirmasiActivity.class);
        intent.putExtra("key", key);
        itemView.getContext().startActivity(intent);

    }
}
