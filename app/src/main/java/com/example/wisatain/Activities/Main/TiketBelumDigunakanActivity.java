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
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindView(R.id.tbdBelumpunyatiket)
    ImageView keteranganGambar;

    @BindView(R.id.tbdBelummemiliki)
    TextView keteranganTulisan1;

    @BindView(R.id.tbdBeliTiket)
    TextView keteranganTulisan2;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Tiket> options;
    FirebaseRecyclerAdapter<Tiket, MainTiketAdapter> adapter;

    String getUID;
    String getTiketKey, getNamaWisataTiket, getWilayahWisata, getTanggalKunjungan, getJumlahTiket, getTotalHarga;

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
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users");
        mTiket = mUsers.child(getUID).child("Tiket").child("BelumDigunakan");

        loadData();
    }

    public void loadData() {

        mTiket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getTiketKey = ds.getKey();
                    getNamaWisataTiket = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);
                    getJumlahTiket = ds.child("Jumlah").getValue(String.class);
                    getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                    Log.d("testkey", "onDataChange: " + getTiketKey + " " + getNamaWisataTiket + " " + getWilayahWisata);

                    arrayTiketKey.add(getTiketKey);
                    arrayNamaWisata.add(getNamaWisataTiket);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayTanggalKunjungan.add(getTanggalKunjungan);
                    arrayJumlahTiket.add(getJumlahTiket);
                    arrayTotalHarga.add(getTotalHarga);

                    if (getTiketKey != null) {
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

        options = new FirebaseRecyclerOptions.Builder<Tiket>()
                .setQuery(mTiket, Tiket.class).build();

        adapter = new FirebaseRecyclerAdapter<Tiket, MainTiketAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainTiketAdapter holder, int position, @NonNull Tiket model) {

                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                holder.tanggalKunjungan.setText(arrayTanggalKunjungan.get(position));
                holder.jumlahTiket.setText(arrayJumlahTiket.get(position));
                holder.totalHarga.setText(arrayTotalHarga.get(position));
                holder.wisataKey = arrayTiketKey.get(position);
                holder.tiketStatus = "Belum Digunakan";

//                holder.namaWisata.setText("Nama wisata");
//                holder.wilayahWisata.setText("Wilayah anjing");
//                holder.tanggalKunjungan.setText("tanggal kunjungan");
//                holder.jumlahTiket.setText("jumlah tiket");
//                holder.totalHarga.setText("total harga");
//                holder.wisataKey = arrayTiketKey.get(position);
//                holder.tiketStatus = "Belum Digunakan";
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
