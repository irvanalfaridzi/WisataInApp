package com.example.wisatain.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wisatain.Adapters.MainTiketAdapter;
import com.example.wisatain.Items.Tiket;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MainTiketFragment extends Fragment {

    Context context;

    @BindView(R.id.mtRecyclerView)
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mTiket;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Tiket> options;
    FirebaseRecyclerAdapter<Tiket, MainTiketAdapter> adapter;

    ArrayList<String> tiketArrayList = new ArrayList<>();

    String getUID;
    String getTiketKey, getNamaWisata, getWilayahWisata, getTanggalKunjungan, getJumlahTiket, getTotalHarga;

    ArrayList<String> arrayTiketKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayTanggalKunjungan = new ArrayList<>();
    ArrayList<String> arrayJumlahTiket = new ArrayList<>();
    ArrayList<String> arrayTotalHarga = new ArrayList<>();


    @SuppressLint("ValidFragment")
    public MainTiketFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_tiket, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");
        mTiket = mDatabase.getReference().child("Tiket");
        mUsers = mDatabase.getReference().child("Users");

        getUID = mUser.getUid();

        loadData();

        return view;
    }

    public void loadData() {

        mUsers.child(getUID).child("Tiket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String tiket = ds.getValue(String.class);
                    tiketArrayList.add(tiket);
                    Log.d("tiketget", "onDataChange: " + tiket);

                    mTiket.orderByChild("UIDUser").equalTo(getUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                getTiketKey = ds.getKey();
                                getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                                getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                                getJumlahTiket = ds.child("JumlahTiket").getValue(String.class);
                                getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);
                                getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                                Log.d("tiketkey", "onDataChange: " + getTiketKey + getNamaWisata);
                                arrayTiketKey.add(getTiketKey);
                                arrayNamaWisata.add(getNamaWisata);
                                arrayWilayahWisata.add(getWilayahWisata);
                                arrayJumlahTiket.add(getJumlahTiket);
                                arrayTotalHarga.add(getTotalHarga);
                                arrayTanggalKunjungan.add(getTanggalKunjungan);

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
        });

    }

    public void showData() {

        options = new FirebaseRecyclerOptions.Builder<Tiket>()
                .setQuery(mTiket, Tiket.class).build();

        adapter = new FirebaseRecyclerAdapter<Tiket, MainTiketAdapter>(options) {
            @NonNull
            @Override
            public MainTiketAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tiket, viewGroup, false);

                return new MainTiketAdapter(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainTiketAdapter holder, int position, @NonNull Tiket model) {
                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                holder.totalHarga.setText(arrayTotalHarga.get(position));
                holder.jumlahTiket.setText(arrayJumlahTiket.get(position));
                holder.tanggalKunjungan.setText(arrayTanggalKunjungan.get(position));
                holder.tiketKey = arrayTiketKey.get(position);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}
