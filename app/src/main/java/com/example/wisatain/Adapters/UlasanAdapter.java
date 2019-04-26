package com.example.wisatain.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatain.R;

public class UlasanAdapter extends RecyclerView.ViewHolder {

    public ImageView GambarUser, Star1, Star2, Star3, Star4, Star5;
    public TextView NamaUser, IsiUlasan;

    public UlasanAdapter(@NonNull View itemView) {
        super(itemView);

        GambarUser = itemView.findViewById(R.id.iuiGambarUser);
        Star1 = itemView.findViewById(R.id.iuStar1);
        Star2 = itemView.findViewById(R.id.iuStar2);
        Star3 = itemView.findViewById(R.id.iuStar3);
        Star4 = itemView.findViewById(R.id.iuStar4);
        Star5 = itemView.findViewById(R.id.iuStar5);
        NamaUser = itemView.findViewById(R.id.iutNamaUser);
        IsiUlasan = itemView.findViewById(R.id.iutUlasan);

    }
}
