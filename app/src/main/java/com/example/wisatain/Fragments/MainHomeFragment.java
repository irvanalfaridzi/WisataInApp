package com.example.wisatain.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.MainHomeAdapter;
import com.example.wisatain.Items.Wisata;
import com.example.wisatain.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHomeFragment extends Fragment {


    @BindView(R.id.mhRecyclerView)
    RecyclerView recyclerView;

    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    FirebaseRecyclerOptions<Wisata> options;
    FirebaseRecyclerAdapter<Wisata, MainHomeAdapter> adapter;

    ArrayList<String> keywisataarray = new ArrayList<>();
    ArrayList<String> namawisataarray = new ArrayList<>();
    ArrayList<String> kotawisataarray = new ArrayList<>();
    ArrayList<String> fotowisataarray = new ArrayList<>();

    String setintent;

    public MainHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        ButterKnife.bind(this, view);

        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");

        recyclerView.setHasFixedSize(true);

        loadData();

        return view;
    }

    public void loadData() {

        mWisata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    String namaWisata = ds.child("NamaWisata").getValue(String.class);
                    String kotawisata = ds.child("WilayahWisata").getValue(String.class);
                    String fotowisata = ds.child("FotoWisataURL").getValue(String.class);
                    keywisataarray.add(key);
                    namawisataarray.add(namaWisata);
                    kotawisataarray.add(kotawisata);
                    fotowisataarray.add(fotowisata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Wisata>()
                .setQuery(mWisata, Wisata.class).build();

        adapter = new FirebaseRecyclerAdapter<Wisata, MainHomeAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainHomeAdapter holder, int position, @NonNull Wisata model) {

                Glide.with(getActivity()).load(fotowisataarray.get(position)).into(holder.foto);
                holder.judul.setText(namawisataarray.get(position));
                holder.kota.setText(kotawisataarray.get(position));
                holder.key = keywisataarray.get(position);

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

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

}
