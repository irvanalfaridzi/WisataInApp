package com.example.wisatain.Activities.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wisatain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuatAkunActivity extends AppCompatActivity {

    @BindView(R.id.baetNamaLengkap)
    EditText namalengkap;

    @BindView(R.id.baetEmail)
    EditText email;

    @BindView(R.id.baetKataSandi)
    EditText katasandi;

    @BindView(R.id.baetKonfirmasiSandi)
    EditText konfirmasisandi;

    @BindView(R.id.baProgressBar)
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_akun);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        namalengkap.setText("testtest");
        email.setText("test@test.test");
        katasandi.setText("testtest");
        konfirmasisandi.setText("testtest");
    }

    private void saveMetaData() {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.fetchProvidersForEmail(email.getText().toString()).addOnCompleteListener(
                new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (task.getResult().getProviders().isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Email belum terdaftar",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BuatAkunActivity.this, BiodataAkunActivity.class);
                            intent.putExtra("namalengkap", namalengkap.getText().toString());
                            intent.putExtra("email", email.getText().toString());
                            intent.putExtra("katasandi", konfirmasisandi.getText().toString());
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Email telah terdaftar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @OnClick(R.id.babtnBuatAkun)
    public void buatAkun(View view) {
        if (namalengkap.getText().toString().trim().isEmpty()) {
            namalengkap.setError("Tolong masukan nama akun anda");
            namalengkap.requestFocus();
            return;
        } if (email.getText().toString().trim().isEmpty()){
            email.setError("Tolong masukan email anda");
            email.requestFocus();
            return;
        } if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("Tolong masukan email anda dengan benar");
            email.requestFocus();
            return;
        } if (katasandi.getText().toString().trim().isEmpty()) {
            katasandi.setError("Tolong masukan kata sandi anda");
            katasandi.requestFocus();
            return;
        } if (katasandi.getText().toString().trim().length() < 8) {
            katasandi.setError("Kata sandi minimal terdiri dari 8 karakter");
            katasandi.requestFocus();
            return;
        } if (konfirmasisandi.getText().toString().trim().isEmpty()) {
            konfirmasisandi.setError("Tolong masukan konfirmasi kata sandi anda");
            konfirmasisandi.requestFocus();
            return;
        } if (!(katasandi.getText().toString().equals(konfirmasisandi.getText().toString().trim()))) {
            konfirmasisandi.setError("Kata sandi anda tidak cocok");
            konfirmasisandi.requestFocus();
            return;
        }

        saveMetaData();
    }

}
