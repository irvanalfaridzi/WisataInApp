package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.MainHomeAdapter;
import com.example.wisatain.Adapters.ResultPencarianAdapter;
import com.example.wisatain.Items.Wisata;
import com.example.wisatain.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultPencarianActivity extends AppCompatActivity {

    @BindView(R.id.rpRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    DatabaseReference mKategori;

    FirebaseRecyclerOptions<Wisata> options;
    FirebaseRecyclerAdapter<Wisata, ResultPencarianAdapter> adapter;

    String intentKategori;
    String getWisataKey, getNamaWisata, getWilayahWisata, getGambarWisataURL;

    ArrayList<String> arrayWisataKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayGambarWisata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pencarian);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent getintent = getIntent();
        intentKategori = getintent.getStringExtra("kategori");
        Log.d("intentkategori", "onCreate: " + intentKategori);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mKategori = FirebaseDatabase.getInstance().getReference().child("Kategori").child(intentKategori);

        loadData();
    }

    public void loadData() {

        mKategori.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getWisataKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getGambarWisataURL = ds.child("FotoWisataURL").getValue(String.class);
                    Log.d("resultwisata", "onDataChange: " + getWisataKey + " " + getNamaWisata);

                    arrayWisataKey.add(getWisataKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayGambarWisata.add(getGambarWisataURL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Wisata>()
                .setQuery(mKategori, Wisata.class).build();

        adapter = new FirebaseRecyclerAdapter<Wisata, ResultPencarianAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ResultPencarianAdapter holder, int position, @NonNull Wisata model) {

                Glide.with(getApplicationContext()).load(arrayGambarWisata.get(position)).into(holder.gambarWisata);
                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                holder.wisataKey = arrayWisataKey.get(position);
            }

            @NonNull
            @Override
            public ResultPencarianAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wisata, viewGroup, false);

                return new ResultPencarianAdapter(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}
