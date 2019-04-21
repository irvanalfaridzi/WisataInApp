package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wisatain.Items.Users;
import com.example.wisatain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseUser mUser;

    ArrayList<Users> namaku = new ArrayList<>();

    @BindView(R.id.epetNama)
    EditText nama;

    @BindView(R.id.epetNomorTelepon)
    EditText nomorTelepon;

    @BindView(R.id.epetTanggalLahir)
    EditText tanggalLahir;

    @BindView(R.id.epetAlamat)
    EditText alamat;

    String getUID;
    String getNama, getEmail, getNomorTelepon, getTanggalLahir, getAlamat, getStatus, getFotoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getUID = mUser.getUid();

        loadDataUser();
    }

    private void loadDataUser() {

        if (mUser != null) {

            FirebaseDatabase.getInstance().getReference().child("Users").child(getUID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            getNama = dataSnapshot.child("Nama").getValue(String.class);
                            getEmail = dataSnapshot.child("Email").getValue(String.class);
                            getNomorTelepon = dataSnapshot.child("NomorTelepon").getValue(String.class);
                            getTanggalLahir = dataSnapshot.child("TanggalLahir").getValue(String.class);
                            getAlamat = dataSnapshot.child("Alamat").getValue(String.class);
                            getStatus = dataSnapshot.child("Status").getValue(String.class);
                            getFotoURL = dataSnapshot.child("FotoURL").getValue(String.class);

                            Log.d("userkunama", "onDataChange: " + getNama + " " + getEmail + " " + getNomorTelepon + " "
                                    + getTanggalLahir + " " + getAlamat + " " + getStatus + " " + getFotoURL);

                            nama.setText(getNama.toString());
                            nomorTelepon.setText(getNomorTelepon.toString());
                            tanggalLahir.setText(getTanggalLahir.toString());
                            alamat.setText(getAlamat.toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            Toast.makeText(this, "User Null", Toast.LENGTH_SHORT).show();
        }

    }

    public void saveDataUser() {
        Users users = new Users(
                nama.getText().toString().trim(),
                getEmail.toString(),
                nomorTelepon.getText().toString().trim(),
                tanggalLahir.getText().toString().trim(),
                alamat.getText().toString().trim(),
                getStatus.toString(),
                getFotoURL.toString()
        );

        FirebaseDatabase.getInstance().getReference().child("Users").child(getUID)
                .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error Input Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.epbtnSave)
    public void saveData() {
        saveDataUser();
    }

}
