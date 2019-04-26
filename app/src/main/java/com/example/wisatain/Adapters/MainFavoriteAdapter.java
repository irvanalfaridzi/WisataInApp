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

public class MainFavoriteAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView judul, kota;
    public ImageView foto;
    public String key;
    public Button lihat;

    public MainFavoriteAdapter(@NonNull View itemView) {
        super(itemView);

        judul = itemView.findViewById(R.id.iwtJudulWisata);
        kota = itemView.findViewById(R.id.iwtKotaWisata);
        foto = itemView.findViewById(R.id.iwiGambarWisata);
        lihat = itemView.findViewById(R.id.btnLihat);
        lihat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(itemView.getContext(), ProfilWisataUserActivity.class);
        intent.putExtra("intent", key);
        itemView.getContext().startActivity(intent);

    }

}
