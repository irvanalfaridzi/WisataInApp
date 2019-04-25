package com.example.wisatain.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisatain.Activities.Main.MainActivity;
import com.example.wisatain.Activities.Main.ResultPencarianActivity;
import com.example.wisatain.Activities.Main.WisataMapsActivity;
import com.example.wisatain.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCariFragment extends Fragment {

    @BindView(R.id.cardPedesaan)
    CardView cPedesaan;

    @BindView(R.id.cardKuliner)
    CardView cKuliner;

    @BindView(R.id.cardPantai)
    CardView cPantai;

    @BindView(R.id.cardPerkebunan)
    CardView cPerkebunan;

    String kategoriCari;

    public MainCariFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_cari, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Kategori");

        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        clickCard();

        return view;
    }

    public void clickCard() {

        cPedesaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriCari = "Pedesaan";
                Intent intent = new Intent(getActivity(), ResultPencarianActivity.class);
                intent.putExtra("kategori", kategoriCari);
                startActivity(intent);
            }
        });
        cKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriCari = "Kuliner";
                Intent intent = new Intent(getActivity(), ResultPencarianActivity.class);
                intent.putExtra("kategori", kategoriCari);
                startActivity(intent);
            }
        });
        cPantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriCari = "Pantai";
                Intent intent = new Intent(getActivity(), ResultPencarianActivity.class);
                intent.putExtra("kategori", kategoriCari);
                startActivity(intent);
            }
        });
        cPerkebunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategoriCari = "Perkebunan";
                Intent intent = new Intent(getActivity(), ResultPencarianActivity.class);
                intent.putExtra("kategori", kategoriCari);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.mfBtnMaps)
    public void maps() {
        Intent intent = new Intent(getActivity(), WisataMapsActivity.class);
        startActivity(intent);
    }

}
