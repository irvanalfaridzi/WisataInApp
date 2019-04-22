package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatain.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatain.Activities.Login.BiodataAkunActivity.PICK_IMAGE_REQUEST;

public class DetailTiketActivity extends AppCompatActivity {

    @BindView(R.id.dtiGambarActivity)
    ImageView gambarActivity;

    @BindView(R.id.dttRefTransaksiKeterangan)
    TextView refTransaksi;

    @BindView(R.id.dttTanggalTransaksiKeterangan)
    TextView tanggalTransaksi;

    @BindView(R.id.dttNamaWisataKeterangan)
    TextView namaWisata;

    @BindView(R.id.dttLokasiWisataKeterangan)
    TextView lokasiWisata;

    @BindView(R.id.dttJumlahTiketKeterangan)
    TextView jumlahTiket;

    @BindView(R.id.dttTanggalPenggunaanKeterangan)
    TextView tanggalPenggunaan;

    @BindView(R.id.dttTotalhargaKeterangan)
    TextView totalHarga;

    @BindView(R.id.dttStatusTiketKeterangan)
    TextView statusTiket;

    @BindView(R.id.dtProgressBar)
    ProgressBar progressBar;

    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    StorageReference mBuktiRef;

    Uri uriBuktiPembayaran;
    Uri buktiPembayaranURL;

    String intentTiketKey, intentTiketStatus;
    String getRefTransaksi, getTanggalTransaksi, getNamaWisata, getLokasiwisata, getJumlahTiket, getTanggalPenggunaan, getTotalHarga, getStatusTiket;

    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tiket);
        ButterKnife.bind(this);

        Intent getintent = getIntent();
        intentTiketKey = getintent.getStringExtra("key");
        intentTiketStatus = getintent.getStringExtra("status");

        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");

        if (intentTiketStatus.equals("Menunggu Konfirmasi")) {
            gambarActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });
            menungguKonfirmasi();
        } else {

        }

    }

    public void menungguKonfirmasi() {

        mTiket.child("MenungguKonfirmasi").child(intentTiketKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                getRefTransaksi = dataSnapshot.child("RefTransaksi").getValue(String.class);
                getTanggalTransaksi = dataSnapshot.child("TanggalPembelian").getValue(String.class);
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getLokasiwisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                getJumlahTiket = dataSnapshot.child("Jumlah").getValue(String.class);
                getTanggalPenggunaan = dataSnapshot.child("TanggalKunjungan").getValue(String.class);
                getTotalHarga = dataSnapshot.child("TotalHarga").getValue(String.class);
                getStatusTiket = dataSnapshot.child("TiketStatus").getValue(String.class);
                Log.d("dtcek", "onDataChange: " + getRefTransaksi + " " + getNamaWisata);

                if (!getNamaWisata.isEmpty() && !getRefTransaksi.isEmpty()) {
                    refTransaksi.setText(getRefTransaksi);
                    tanggalTransaksi.setText(getTanggalTransaksi);
                    namaWisata.setText(getNamaWisata);
                    lokasiWisata.setText(getLokasiwisata);
                    jumlahTiket.setText(getJumlahTiket);
                    tanggalPenggunaan.setText(getTanggalPenggunaan);
                    totalHarga.setText(getTotalHarga);
                    statusTiket.setText(getStatusTiket);

                    Glide.with(getBaseContext()).load(R.drawable.ic_add_a_photo_black_128dp).into(gambarActivity);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sementara() {

        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(intentTiketKey, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            gambarActivity.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void saveData() {

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "pilih gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            uriBuktiPembayaran = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriBuktiPembayaran);
                gambarActivity.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        mBuktiRef = FirebaseStorage.getInstance().getReference().child("Buktipembayaran/" + System.currentTimeMillis() + ".jpg");
        if (uriBuktiPembayaran != null) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.bwBtnSave).setEnabled(false);
            uploadTask = mBuktiRef.putFile(uriBuktiPembayaran);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Toast.makeText(DetailTiketActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                    return mBuktiRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.bwBtnSave).setEnabled(true);
                        buktiPembayaranURL = task.getResult();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.bwBtnSave).setEnabled(true);
                    }
                }
            });
        }
    }

}
