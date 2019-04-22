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

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.MainFavoriteAdapter;
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

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Wisata> options;
    FirebaseRecyclerAdapter<Wisata, MainFavoriteAdapter> adapter;

    ValueEventListener myValueEventListener;

    String getUID;

    ArrayList<String> arrayWisata = new ArrayList<>();
    ArrayList<String> arrayWisataKey = new ArrayList<>();

    ArrayList<String> keywisataarray = new ArrayList<>();
    ArrayList<String> namawisataarray = new ArrayList<>();
    ArrayList<String> kotawisataarray = new ArrayList<>();
    ArrayList<String> fotowisataarray = new ArrayList<>();

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();

        getUID = mUser.getUid();

        mUsers = mDatabase.getReference().child("Users").child(getUID).child("Favorite");
        mWisata = mDatabase.getReference().child("Wisata");

        mUsers.keepSynced(true);
        mWisata.keepSynced(true);

        recyclerView.setHasFixedSize(true);

        myValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String wisataFav = ds.getValue(String.class);
                    arrayWisata.add(wisataFav);

                    Log.d("mfwisatafav", "onDataChange: " + wisataFav);

                    mWisata.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String wisataKey = ds.getKey();
                                arrayWisataKey.add(wisataKey);
                                Log.d("mfwisatakey", "onDataChange: " + wisataKey);

                                String namaWisata = ds.child("NamaWisata").getValue(String.class);
                                String kotawisata = ds.child("WilayahWisata").getValue(String.class);
                                String fotowisata = ds.child("FotoWisataURL").getValue(String.class);
                                namawisataarray.add(namaWisata);
                                kotawisataarray.add(kotawisata);
                                fotowisataarray.add(fotowisata);

                                showData();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUsers.addValueEventListener(myValueEventListener);

        return view;
    }

    public void loadData() {
    }

    public void showData() {

        for (int x = 0; x < arrayWisataKey.size(); x++) {
            for (int y = 0; y < arrayWisata.size(); y++) {

                Log.d("mfarraywisata11", "showData: " + arrayWisata.get(y));

                if (arrayWisataKey.get(x).equals(arrayWisata.get(y))) {

                    Log.d("mfarraywisata1", "showData: " + arrayWisata.get(y));

                    options = new FirebaseRecyclerOptions.Builder<Wisata>()
                            .setQuery(mWisata, Wisata.class).build();

                    adapter = new FirebaseRecyclerAdapter<Wisata, MainFavoriteAdapter>(options) {
                        @NonNull
                        @Override
                        public MainFavoriteAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite, viewGroup, false);

                            return new MainFavoriteAdapter(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull MainFavoriteAdapter holder, int position, @NonNull Wisata model) {
                            Glide.with(getActivity()).load(fotowisataarray.get(position)).into(holder.foto);
                            holder.judul.setText(namawisataarray.get(position));
                            holder.kota.setText(kotawisataarray.get(position));
                            holder.key = arrayWisataKey.get(position);

                        }

                    };

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                } else {

                }
                Log.d("mfwisataarray1", "loadData: " + arrayWisataKey.get(x));
                Log.d("mfwisataarray2", "loadData: " + arrayWisata.get(y));
            }
        }

    }
}
