package com.example.wisatain.Activities.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.wisatain.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GantiSandiActivity extends AppCompatActivity {

    @BindView(R.id.gsetKataSandi)
    EditText katasandi;

    @BindView(R.id.gsetKonfirmasiKataSandi)
    EditText konfirmasikatasandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_sandi);
    }

    @OnClick(R.id.gsbtnGantiKataSandi)
    public void gantiKataSandi() {
        if (katasandi.toString() == null) {
            katasandi.setError("Masukan kata sandi");
        }else if (konfirmasikatasandi.toString() == null) {
            konfirmasikatasandi.setError("Masukan konfirmasi kata sandi");
        }else if (konfirmasikatasandi.toString() != katasandi.toString()) {
            konfirmasikatasandi.setError("Kata sandi tidak sama");
        }else {
            Intent intent = new Intent(GantiSandiActivity.this, VerifikasiSandiActivity.class);
            startActivity(intent);
        }

    }

}
