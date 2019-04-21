package com.example.wisatain.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.DetailTiketActivity;
import com.example.wisatain.R;


public class MainTiketAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView namaWisata, wilayahWisata, tanggalKunjungan, jumlahTiket, totalHarga;
    public String tiketKey;

    public MainTiketAdapter(@NonNull View itemView) {
        super(itemView);

        namaWisata = itemView.findViewById(R.id.ittNamaWisata);
        wilayahWisata = itemView.findViewById(R.id.ittWilayahWisata);
        tanggalKunjungan = itemView.findViewById(R.id.ittTanggalKunjungan);
        jumlahTiket = itemView.findViewById(R.id.ittJumlahTiket);
        totalHarga = itemView.findViewById(R.id.ittTotalHarga);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(itemView.getContext(), DetailTiketActivity.class);
        intent.putExtra("key", tiketKey);
        itemView.getContext().startActivity(intent);
    }
}
