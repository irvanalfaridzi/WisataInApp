package com.example.wisatain.Activities.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.wisatain.R;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifikasiSandiActivity extends AppCompatActivity {

    @BindView(R.id.vsetVerifikasiKode)
    EditText verifikasikode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_sandi);
    }

    @OnClick(R.id.vsbtnGantiKataSandi)
    public void verifikasiKode() {
        if (verifikasikode.toString() == null) {
            verifikasikode.setError("Masukan kode verifikasi");
        }else {
            Intent intent = new Intent(VerifikasiSandiActivity.this, GantiSandiActivity.class);
            startActivity(intent);
        }

    }

}
