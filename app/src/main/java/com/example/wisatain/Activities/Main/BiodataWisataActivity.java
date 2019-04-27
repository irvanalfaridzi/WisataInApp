package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.wisatain.Activities.Login.LoginActivity;
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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BiodataWisataActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 201;

    @BindView(R.id.bwiGambarWisata)
    ImageView fotoWisata;

    @BindView(R.id.bwetNamaWisata)
    EditText namaWisata;

    @BindView(R.id.cardPedesaan)
    CardView cPedesaan;

    @BindView(R.id.cardKuliner)
    CardView cKuliner;

    @BindView(R.id.cardPantai)
    CardView cPantai;

    @BindView(R.id.cardPerkebunan)
    CardView cPerkebunan;

    @BindView(R.id.tgBtnPedesaan)
    ToggleButton tPedesaan;

    @BindView(R.id.tgBtnKuliner)
    ToggleButton tKuliner;

    @BindView(R.id.tgBtnPantai)
    ToggleButton tPantai;

    @BindView(R.id.tgBtnPerkebunan)
    ToggleButton tPerkebunan;

    @BindView(R.id.bwetLokasi)
    EditText lokasiWisata;

    @BindView(R.id.bwetKota)
    EditText kotaWisata;

    @BindView(R.id.bwetNomorTelepon)
    EditText nomorTelepon;

    @BindView(R.id.bwetJamOperasionalBuka)
    EditText jamBukaWisata;

    @BindView(R.id.bwetJamOperasionalTutup)
    EditText jamTutupWisata;

    @BindView(R.id.bwetHarga)
    EditText hargaWisata;

    @BindView(R.id.bwetDeskripsi)
    EditText deskripsiWisata;

    @BindView(R.id.bwProgressBar)
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    StorageReference mStorage;
    StorageReference mFotoRef;

    Uri uriFotoWisata;
    Uri fotoWisataURL;

    String kategoriWisata;
    String getNamaWisata;
    String getUID, getNama, getNomorTelepon;
    String verifikasi;
    String setStatus, status;

    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata_wisata);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users");

        kategoriWisata = "";
        pilihKategori();

        getUID = mUser.getUid();
        Toast.makeText(this, getUID, Toast.LENGTH_SHORT).show();
        getNama = mUser.getDisplayName();

        status = "Pemilik Wisata";

        verifikasi = "n";

        mRef = mDatabase.getReference().child("Users").child(getUID);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getNomorTelepon = dataSnapshot.child("NomorTelepon").getValue(String.class);
                nomorTelepon.setText(getNomorTelepon);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BiodataWisataActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        getNamaWisata = intent.getStringExtra("namawisata");

        namaWisata.setText(getNamaWisata);
        lokasiWisata.setText("Babatan Pilang XIV f1 nomor 6");
        kotaWisata.setText("surabaya");
        jamBukaWisata.setText("16.00");
        jamTutupWisata.setText("17.00");
        hargaWisata.setText("0");
        deskripsiWisata.setText("Luar Biasa");

        fotoWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    public void saveData(){

        final wisata wisataAkun = new wisata(
                namaWisata.getText().toString().trim(),
                getUID,
                kategoriWisata,
                lokasiWisata.getText().toString().trim(),
                kotaWisata.getText().toString().trim(),
                nomorTelepon.getText().toString().trim(),
                jamBukaWisata.getText().toString() + " - " + jamTutupWisata.getText().toString().trim(),
                hargaWisata.getText().toString().trim(),
                deskripsiWisata.getText().toString().trim(),
                fotoWisataURL.toString(),
                verifikasi
        );

        final String key = mDatabase.getReference("wisata").push().getKey();
        FirebaseDatabase.getInstance().getReference("Wisata").child(key).setValue(wisataAkun)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user userWisata = new user(key);
                            mUsers.child(getUID).child("Wisata").setValue(key);
                            Toast.makeText(BiodataWisataActivity.this, "Wisata telah dibuat", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("Users").child(getUID).child("Status").setValue(status);
                            mDatabase.getReference().child("Kategori").child(kategoriWisata).child(key).setValue(wisataAkun);
                            Intent intent = new Intent(BiodataWisataActivity.this, ProfilWisataOwnerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mDatabase.getReference().child("Wisata").child(key).child("Ulasan").child("TotalRating").setValue("0");
                            mDatabase.getReference().child("Wisata").child(key).child("Ulasan").child("RataRataRating").setValue("0");
                            mDatabase.getReference().child("Wisata").child(key).child("Ulasan").child("JumlahRating").setValue("0");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BiodataWisataActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            uriFotoWisata = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFotoWisata);
                fotoWisata.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        mFotoRef = FirebaseStorage.getInstance().getReference().child("fotowisata/" +System.currentTimeMillis() +".jpg");
        if (uriFotoWisata != null) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.bwBtnSave).setEnabled(false);
            uploadTask = mFotoRef.putFile(uriFotoWisata);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Toast.makeText(BiodataWisataActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                    return mFotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.bwBtnSave).setEnabled(true);
                        fotoWisataURL = task.getResult();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        findViewById(R.id.bwBtnSave).setEnabled(true);
                    }
                }
            });
        }
    }

    public class wisata {
        public String NamaWisata, UIDUser, KategoriWisata, LokasiWisata, WilayahWisata, NomorTeleponWisata, JamOperasional, HargaWisata, DeskripsiWisata, FotoWisataURL, Terverifikasi;

        public wisata(String namaWisata, String UIDUser, String kategoriWisata, String lokasiWisata, String wilayahWisata, String nomorTeleponWisata, String jamOperasional, String hargaWisata, String deskripsiWisata, String fotoWisataURL, String terverifikasi) {
            NamaWisata = namaWisata;
            this.UIDUser = UIDUser;
            KategoriWisata = kategoriWisata;
            LokasiWisata = lokasiWisata;
            WilayahWisata = wilayahWisata;
            NomorTeleponWisata = nomorTeleponWisata;
            JamOperasional = jamOperasional;
            HargaWisata = hargaWisata;
            DeskripsiWisata = deskripsiWisata;
            FotoWisataURL = fotoWisataURL;
            Terverifikasi = terverifikasi;
        }
    }

    public class user {

        public  String Wisata;

        public user(String wisata) {
            Wisata = wisata;
        }
    }

    public void pilihKategori() {
        tPedesaan.setChecked(false);
        tKuliner.setChecked(false);
        tPantai.setChecked(false);
        tPerkebunan.setChecked(false);
        tPedesaan.setBackgroundDrawable(null);
        tKuliner.setBackgroundDrawable(null);
        tPantai.setBackgroundDrawable(null);
        tPerkebunan.setBackgroundDrawable(null);

        cPedesaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tPedesaan.isChecked()) {
                    tPedesaan.setChecked(true);
                    tPedesaan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_green_50dp));
                    kategoriWisata = "Pedesaan";
                    cKuliner.setEnabled(false);
                    cPantai.setEnabled(false);
                    cPerkebunan.setEnabled(false);
                } else {
                    tPedesaan.setChecked(false);
                    tPedesaan.setBackgroundDrawable(null);
                    kategoriWisata = "";
                    cKuliner.setEnabled(true);
                    cPantai.setEnabled(true);
                    cPerkebunan.setEnabled(true);
                }
            }
        });

        cKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tKuliner.isChecked()) {
                    tKuliner.setChecked(true);
                    tKuliner.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_green_50dp));
                    kategoriWisata = "Kuliner";
                    cPedesaan.setEnabled(false);
                    cPantai.setEnabled(false);
                    cPerkebunan.setEnabled(false);
                } else {
                    tKuliner.setChecked(false);
                    tKuliner.setBackgroundDrawable(null);
                    kategoriWisata = "";
                    cPedesaan.setEnabled(true);
                    cPantai.setEnabled(true);
                    cPerkebunan.setEnabled(true);
                }
            }
        });

        cPantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tPantai.isChecked()) {
                    tPantai.setChecked(true);
                    tPantai.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_green_50dp));
                    kategoriWisata = "Pantai";
                    cPedesaan.setEnabled(false);
                    cKuliner.setEnabled(false);
                    cPerkebunan.setEnabled(false);
                } else {
                    tPantai.setChecked(false);
                    tPantai.setBackgroundDrawable(null);
                    kategoriWisata = "";
                    cPedesaan.setEnabled(true);
                    cKuliner.setEnabled(true);
                    cPerkebunan.setEnabled(true);
                }
            }
        });

        cPerkebunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tPerkebunan.isChecked()) {
                    tPerkebunan.setChecked(true);
                    tPerkebunan.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_check_green_50dp));
                    kategoriWisata = "Perkebunan";
                    cPedesaan.setEnabled(false);
                    cKuliner.setEnabled(false);
                    cPantai.setEnabled(false);
                } else {
                    tPerkebunan.setChecked(false);
                    tPerkebunan.setBackgroundDrawable(null);
                    kategoriWisata = "";
                    cPedesaan.setEnabled(true);
                    cKuliner.setEnabled(true);
                    cPantai.setEnabled(true);
                }
            }
        });

    }

    @OnClick(R.id.bwBtnSave)
    public void saveWisata() {
        if (namaWisata.getText().toString().isEmpty()) {
            namaWisata.setError("Masukan Nama Wisata");
            namaWisata.requestFocus();
            return;
        }
        if (kategoriWisata.isEmpty()) {
            Toast.makeText(this, "Pilih Kategori Wisata", Toast.LENGTH_SHORT).show();
            return;
        }
        if (lokasiWisata.getText().toString().isEmpty()) {
            lokasiWisata.setError("Masukan Lokasi Wisata");
            lokasiWisata.requestFocus();
            return;
        }
        if (kotaWisata.getText().toString().isEmpty()) {
            kotaWisata.setError("Masukan wilayah wisata");
            kotaWisata.requestFocus();
            return;
        }
        if (nomorTelepon.getText().toString().isEmpty()) {
            nomorTelepon.setError("Masukan Nomor Telepon Wisata");
            nomorTelepon.requestFocus();
            return;
        }
        if (jamBukaWisata.getText().toString().isEmpty()) {
            jamBukaWisata.setError("Masukan Jam Buka Wisata");
            jamBukaWisata.requestFocus();
            return;
        }
        if (jamTutupWisata.getText().toString().isEmpty()) {
            jamTutupWisata.setError("Masukan Jam Tutup Wisata");
            jamTutupWisata.requestFocus();
            return;
        }
        if (hargaWisata.getText().toString().isEmpty()) {
            hargaWisata.setError("Masukan Harga Wisata");
            hargaWisata.requestFocus();
            return;
        }
        if (deskripsiWisata.getText().toString().isEmpty()) {
            deskripsiWisata.setError("Masukan Deskripsi Wisata");
            deskripsiWisata.requestFocus();
            return;
        }
        if (uriFotoWisata == null) {
            Toast.makeText(this, "Masukan Gambar Wisata", Toast.LENGTH_SHORT).show();
            return;
        }

        saveData();
    }

}
