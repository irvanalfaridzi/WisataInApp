package com.example.wisatain.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatain.Activities.Main.MainActivity;
import com.example.wisatain.Adapters.MainFavoriteAdapter;
import com.example.wisatain.Adapters.MainHomeAdapter;
import com.example.wisatain.Items.Wisata;
import com.example.wisatain.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFavoriteFragment extends Fragment {

    @BindView(R.id.mfRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.mfGambarBelum)
    ImageView keteranganGambar;

    @BindView(R.id.mfBelumada)
    TextView keteranganTulisan1;

    @BindView(R.id.mfSilahkan)
    TextView keteranganTulisan2;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mFavorit;
    FirebaseRecyclerOptions<Wisata> options;
    FirebaseRecyclerAdapter<Wisata, MainHomeAdapter> adapter;

    String getUID;
    public String getWisataKey, getFotoWisataURL, getNamaWisata, getWilayahWisata;

    ArrayList<String> arrayWisataKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayFotoWisataURL = new ArrayList<>();

    public MainFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_favorite, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Favorit");

        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mFavorit = mDatabase.getReference().child("Favorit").child(getUID);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        loadData();

        return view;
    }

    public void loadData() {

        mFavorit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getWisataKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getFotoWisataURL = ds.child("FotoWisataURL").getValue(String.class);
                    Log.d("wisatakey", "onDataChange: " + getWisataKey + " " + getNamaWisata);

                    arrayWisataKey.add(getWisataKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayFotoWisataURL.add(getFotoWisataURL);

                    if (getWisataKey != null) {
                        keteranganGambar.setVisibility(View.GONE);
                        keteranganTulisan1.setVisibility(View.GONE);
                        keteranganTulisan2.setVisibility(View.GONE);
                    } else {
                        keteranganGambar.setVisibility(View.VISIBLE);
                        keteranganTulisan1.setVisibility(View.VISIBLE);
                        keteranganTulisan2.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showData();
    }

    public void showData() {

        options = new FirebaseRecyclerOptions.Builder<Wisata>().setQuery(mFavorit, Wisata.class).build();

        Log.d("opt123", "showData: " + options);

        adapter = new FirebaseRecyclerAdapter<Wisata, MainHomeAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainHomeAdapter holder, int position, @NonNull Wisata model) {

                holder.key = arrayWisataKey.get(position);
                holder.judul.setText(arrayNamaWisata.get(position));
                holder.kota.setText(arrayWilayahWisata.get(position));
                Glide.with(getActivity()).load(arrayFotoWisataURL.get(position)).into(holder.foto);
            }

            @NonNull
            @Override
            public MainHomeAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wisata, viewGroup, false);

                return new MainHomeAdapter(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
