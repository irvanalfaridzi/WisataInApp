package com.example.wisatain.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.ProfilWisataUserActivity;
import com.example.wisatain.R;

public class RekomendasiAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView namaWisata, wilayahWisata, keteranganRatingText;
    public ImageView gambarWisata, keteranganRatingImg;
    public Button lihat;
    public String key;

    public RekomendasiAdapter(@NonNull View itemView) {
        super(itemView);

        namaWisata =itemView.findViewById(R.id.irkJudulWisata);
        wilayahWisata = itemView.findViewById(R.id.irkKotaWisata);
        gambarWisata = itemView.findViewById(R.id.irkGambarWisata);
        keteranganRatingText = itemView.findViewById(R.id.irtStarKeterangan);
        keteranganRatingImg = itemView.findViewById(R.id.irStar);
        lihat = itemView.findViewById(R.id.irkbtnLihat);

        lihat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(itemView.getContext(), ProfilWisataUserActivity.class);
        intent.putExtra("intent", key);
        itemView.getContext().startActivity(intent);

    }
}
