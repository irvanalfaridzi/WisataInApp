package com.example.wisatain.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatain.Activities.Login.LoginActivity;
import com.example.wisatain.Activities.Main.EditProfileActivity;
import com.example.wisatain.Activities.Main.MainActivity;
import com.example.wisatain.Activities.Main.ProfilWisataNullActivity;
import com.example.wisatain.Activities.Main.ProfilWisataOwnerActivity;
import com.example.wisatain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainAkunFragment extends Fragment {

    @BindView(R.id.mpFotoProfil)
    ImageView fotoProfil;

    @BindView(R.id.mpNamaProfil)
    TextView namaProfil;

    @BindView(R.id.mpProgressBar)
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mUsers;

    String getUID;
    String getStatus;

    public MainAkunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_akun, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Profil");

        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users");

        getUID = mUser.getUid();

        progressBar.setVisibility(View.VISIBLE);

        loadData();
        return view;
    }

    private void loadData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            progressBar.setVisibility(View.GONE);
            if (user.getDisplayName() != null && user.getPhotoUrl().toString() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(fotoProfil);
                namaProfil.setText(user.getDisplayName());
            } else {
                Toast.makeText(getActivity(), "userdata == null", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "user == null", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWisata() {

        mUsers.child(getUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getStatus = dataSnapshot.child("Status").getValue(String.class);
                if (getStatus.equalsIgnoreCase("Pemilik Wisata")) {
                    Intent intent = new Intent(getContext(), ProfilWisataOwnerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ProfilWisataNullActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.mpBtnLogOut)
    public void logOut() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UID", null);
        editor.apply();
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.mpBtnEditProfile)
    public void editProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.mpBtnWisata)
    public void wisata() {
        openWisata();
    }

}
