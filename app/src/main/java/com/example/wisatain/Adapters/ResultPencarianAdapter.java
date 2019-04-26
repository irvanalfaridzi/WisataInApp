package com.example.wisatain.Adapters;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.ProfilWisataUserActivity;
import com.example.wisatain.R;

public class ResultPencarianAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView namaWisata, wilayahWisata;
    public ImageView gambarWisata;
    public Button lihat;
    public String wisataKey;

    public ResultPencarianAdapter(@NonNull View itemView) {
        super(itemView);

        namaWisata = itemView.findViewById(R.id.iwtJudulWisata);
        wilayahWisata = itemView.findViewById(R.id.iwtKotaWisata);
        gambarWisata = itemView.findViewById(R.id.iwiGambarWisata);
        lihat = itemView.findViewById(R.id.btnLihat);

        lihat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(itemView.getContext(), ProfilWisataUserActivity.class);
        intent.putExtra("intent", wisataKey);
        itemView.getContext().startActivity(intent);
    }
}
