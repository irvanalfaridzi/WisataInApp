package com.example.wisatain.Activities.Main;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.wisatain.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PesanTiketActivity extends AppCompatActivity {

    @BindView(R.id.ptetJumlahTiket)
    EditText jumlahTiket;

    @BindView(R.id.ptetTanggalPesan)
    EditText tanggalPesan;

    public DatePickerDialog.OnDateSetListener tglPesanSetListener;

    String wisataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tiket);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        wisataKey = intent.getStringExtra("key");
        tanggalPesan.setEnabled(false);

    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.ptBtnTanggalPesan)
    public void tanggalPesanBtn() {
        Calendar cal = Calendar.getInstance();
        int tahun1 = cal.get(Calendar.YEAR);
        int bulan1 = cal.get(Calendar.MONTH);
        int hari1 = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(PesanTiketActivity.this,
                android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                tglPesanSetListener, tahun1, bulan1, hari1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((android.R.color.transparent)));
        dialog.show();

        tglPesanSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahun, int bulan, int hari) {
                bulan = bulan + 1;
                Log.d("pttanggal", "onDateSet: dd/mm/yyyy: " + hari + "/" + bulan + "/" + tahun);

                String tanggal = hari + "/" + bulan + "/" + tahun;
                tanggalPesan.setText(tanggal);
            }
        };

    }

    @OnClick(R.id.ptBtnBeliTiket)
    public void beliTiket() {
        if (jumlahTiket.getText().toString().trim().isEmpty()) {
            jumlahTiket.setError("Masukan Jumlah Tiket");
            jumlahTiket.requestFocus();
            return;
        }
        if (tanggalPesan.getText().toString().trim().isEmpty()) {
            tanggalPesan.setError("Masukan Tanggal Pesan Tiket");
            tanggalPesan.requestFocus();
            return;
        }

        Intent intent = new Intent(PesanTiketActivity.this, TransaksiTiketActivity.class);
        intent.putExtra("tanggalPesanTiket", tanggalPesan.getText().toString());
        intent.putExtra("jumlahTiket", jumlahTiket.getText().toString());
        intent.putExtra("key", wisataKey);
        startActivity(intent);
    }

}
