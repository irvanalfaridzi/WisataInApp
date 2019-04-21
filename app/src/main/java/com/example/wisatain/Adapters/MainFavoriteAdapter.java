package com.example.wisatain.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.ProfilWisataUserActivity;
import com.example.wisatain.R;

public class MainFavoriteAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView judul, kota;
    public ImageView foto;
    public String key;

    public MainFavoriteAdapter(@NonNull View itemView) {
        super(itemView);

        judul = itemView.findViewById(R.id.iftJudulWisata);
        kota = itemView.findViewById(R.id.iftKotaWisata);
        foto = itemView.findViewById(R.id.ifiGambarWisata);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(itemView.getContext(), ProfilWisataUserActivity.class);
        intent.putExtra("intent", key);
        itemView.getContext().startActivity(intent);

    }

}
