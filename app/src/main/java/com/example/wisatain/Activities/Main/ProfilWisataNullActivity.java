package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.wisatain.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilWisataNullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_wisata_null);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.pwnBtnDaftarWisata)
    public void daftarWisata() {
        Intent intent = new Intent(ProfilWisataNullActivity.this, DaftarWisataActivity.class);
        startActivity(intent);
    }
}
