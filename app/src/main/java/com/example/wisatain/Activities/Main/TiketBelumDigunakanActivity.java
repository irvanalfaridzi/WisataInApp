package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TiketBelumDigunakanActivity extends AppCompatActivity {

    @BindView(R.id.tbdRecyclerView)
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Tiket> options;
    FirebaseRecyclerAdapter<Tiket, MainTiketAdapter> adapter;

    String getUID;
    String getTiketKey, getNamaWisata, getWilayahWisata, getTanggalKunjungan, getJumlahTiket, getTotalHarga;

    ArrayList<String> arrayTiketKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayTanggalKunjungan = new ArrayList<>();
    ArrayList<String> arrayJumlahTiket = new ArrayList<>();
    ArrayList<String> arrayTotalHarga = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket_belum_digunakan);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");
        mUsers = mDatabase.getReference().child("Users");

        getUID = mUser.getUid();

        loadData();
    }

    public void loadData() {

        mTiket.child("BelumDigunakan").orderByChild("UIDUser").equalTo(getUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getTiketKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWIsata").getValue(String.class);
                    getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);
                    getJumlahTiket = ds.child("Jumlah").getValue(String.class);
                    getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                    Log.d("testkey", "onDataChange: " + getTiketKey + " " + getNamaWisata);

                    arrayTiketKey.add(getTiketKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayTanggalKunjungan.add(getTanggalKunjungan);
                    arrayJumlahTiket.add(getJumlahTiket);
                    arrayTotalHarga.add(getTotalHarga);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Tiket>()
                .setQuery(mTiket, Tiket.class).build();

        adapter = new FirebaseRecyclerAdapter<Tiket, MainTiketAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainTiketAdapter holder, int position, @NonNull Tiket model) {

                holder.wisataKey = arrayTiketKey.get(position);
                holder.tiketStatus = "Belum Digunakan";
                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                holder.tanggalKunjungan.setText(arrayTanggalKunjungan.get(position));
                holder.jumlahTiket.setText(arrayJumlahTiket.get(position));
                holder.totalHarga.setText(arrayTotalHarga.get(position));
            }

            @NonNull
            @Override
            public MainTiketAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tiket, viewGroup, false);

                return new MainTiketAdapter(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
