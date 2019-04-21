package com.example.wisatain.Activities.Login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wisatain.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BiodataAkunActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 100;
    @BindView(R.id.biaetNamaLengkap)
    EditText namalengkap;

    @BindView(R.id.biaetNomorPonsel)
    EditText nomorponsel;

    @BindView(R.id.biaetTanggalLahir)
    EditText tanggallahir;

    @BindView(R.id.biaetAlamat)
    EditText alamat;

    @BindView(R.id.biaiFotoProfil)
    ImageView fotoprofil;

    @BindView(R.id.biaProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.biaProgressBar2)
    ProgressBar progressBar2;

    public DatePickerDialog.OnDateSetListener tglLahirSetListener;

    public static final String TAG = "BiodataAkunActivity";

    public FirebaseAuth mAuth;


    public StorageReference mStorageRef;

    public static final int CHOOSE_IMAGE = 101;

    Uri uriGambarProfil;

    Uri fotoProfileURL;

    UploadTask uploadTask;

    String intentNamaLengkap;
    String intentEmail;
    String intentKataSandi;

    String statusUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata_akun);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        intentNamaLengkap = intent.getStringExtra("namalengkap");
        intentEmail = intent.getStringExtra("email");
        intentKataSandi = intent.getStringExtra("katasandi");

        statusUser = "wisatawan";

        tanggallahir.setEnabled(false);
        namalengkap.setText(intentNamaLengkap);
        nomorponsel.setText("081");
        alamat.setText("test");

        fotoprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();
            }
        });
    }

    private void saveData() {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(intentEmail, intentKataSandi).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (namalengkap.toString() != null && fotoProfileURL != null) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(namalengkap.getText().toString())
                                    .setPhotoUri(Uri.parse(fotoProfileURL.toString()))
                                    .build();

                            user.updateProfile(profile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(BiodataAkunActivity.this, "Foto telah diinputkan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(BiodataAkunActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(BiodataAkunActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }

                        if (task.isSuccessful()) {
                            user akun = new user(
                                    namalengkap.getText().toString(),
                                    intentEmail,
                                    nomorponsel.getText().toString(),
                                    tanggallahir.getText().toString(),
                                    alamat.getText().toString(),
                                    statusUser,
                                    fotoProfileURL.toString()
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(akun).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(BiodataAkunActivity.this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BiodataAkunActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Intent intent = new Intent(BiodataAkunActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            uriGambarProfil = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriGambarProfil);
                fotoprofil.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Susah", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebaseStorage() {

        final StorageReference fotoProfilRef = mStorageRef.child("fotoprofil/" + System.currentTimeMillis() + ".jpg");

        if (uriGambarProfil != null) {
            progressBar2.setVisibility(View.VISIBLE);
            findViewById(R.id.biabtnBuatAkun).setEnabled(false);
            uploadTask = fotoProfilRef.putFile(uriGambarProfil);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Toast.makeText(BiodataAkunActivity.this, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                    return fotoProfilRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressBar2.setVisibility(View.GONE);
                        findViewById(R.id.biabtnBuatAkun).setEnabled(true);
                        fotoProfileURL = task.getResult();
                    } else {
                        progressBar2.setVisibility(View.GONE);
                        findViewById(R.id.biabtnBuatAkun).setEnabled(true);
                    }
                }
            });
        }
    }

    private void pilihGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    public class user {
        public String Nama, Email, NomorTelepon, TanggalLahir, Alamat, Status, FotoURL;

        public user() {

        }

        public user(String nama, String email, String nomorTelepon, String tanggalLahir, String alamat, String status, String fotoURL) {
            Nama = nama;
            Email = email;
            NomorTelepon = nomorTelepon;
            TanggalLahir = tanggalLahir;
            Alamat = alamat;
            Status = status;
            FotoURL = fotoURL;
        }
    }

    @OnClick(R.id.biabtnBuatAkun)
    public void buatAkun() {
        if (namalengkap.getText().toString().trim().isEmpty()) {
            namalengkap.setError("Tolong masukan nama lengkap anda");
            namalengkap.requestFocus();
            return;
        }
        if (nomorponsel.getText().toString().trim().isEmpty() || nomorponsel.toString().length() < 10) {
            nomorponsel.setError("Tolong masukan nomor ponsel anda");
            nomorponsel.requestFocus();
            return;
        }
        if (tanggallahir.getText().toString().trim().isEmpty()) {
            tanggallahir.setError("Tolong masukan tanggal lahir anda");
            tanggallahir.requestFocus();
            return;
        }
        if (alamat.getText().toString().trim().isEmpty()) {
            alamat.setError("Tolong masukan alamat anda");
            alamat.requestFocus();
            return;
        }
        if (fotoprofil.getDrawable() == null) {
            Toast.makeText(this, "Tolong Masukan Gambar Profile", Toast.LENGTH_SHORT).show();
            fotoprofil.requestFocus();
            return;
        }

        saveData();
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.biaBtnTanggalLahir)
    public void showTanggal() {
        Calendar cal = Calendar.getInstance();
        int tahun1 = cal.get(Calendar.YEAR);
        int bulan1 = cal.get(Calendar.MONTH);
        int hari1 = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(BiodataAkunActivity.this,
                android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                tglLahirSetListener, tahun1, bulan1, hari1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((android.R.color.transparent)));
        dialog.show();

        tglLahirSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int hari, int bulan, int tahun) {
                bulan = bulan + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + hari + "/" + bulan + "/" + tahun);

                String tanggal = hari + "/" + bulan + "/" + tahun;
                tanggallahir.setText(tanggal);
            }
        };
    }

}