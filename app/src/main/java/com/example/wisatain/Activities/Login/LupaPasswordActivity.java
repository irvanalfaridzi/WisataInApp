package com.example.wisatain.Activities.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.wisatain.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LupaPasswordActivity extends AppCompatActivity {

    @BindView(R.id.lpetNomorPonsel)
    EditText nomorponsel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
    }

    @OnClick(R.id.lpbtnGantiSandi)
    public void lanjut() {
        if (nomorponsel.toString() == null) {
            nomorponsel.setError("Masukan nomor ponsel");
        }else {
            Intent intent = new Intent(LupaPasswordActivity.this, VerifikasiSandiActivity.class);
            startActivity(intent);
        }

    }

}
