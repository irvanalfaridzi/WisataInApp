package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.KonfirmasiTiketAdapter;
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

public class KonfirmasiTiketActivity extends AppCompatActivity {

    @BindView(R.id.ktRecyclerView)
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Tiket> options;
    FirebaseRecyclerAdapter<Tiket, KonfirmasiTiketAdapter> adapter;

    String getKey, getUIDUser, getNamaUser, getRefTransaksi, getJumlahTiket, getTotalHarga, getBuktiURL, getTanggalKunjungan;

    ArrayList<String> arrayTiketKey = new ArrayList<>();
    ArrayList<String> arrayNamaUser = new ArrayList<>();
    ArrayList<String> arrayUIDUser = new ArrayList<>();
    ArrayList<String> arrayRefTransaksi = new ArrayList<>();
    ArrayList<String> arrayJumlahTiket = new ArrayList<>();
    ArrayList<String> arrayTotalHarga = new ArrayList<>();
    ArrayList<String> arrayBuktiURL = new ArrayList<>();
    ArrayList<String> arrayTanggalKunjungan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_tiket);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mTiket = mDatabase.getReference().child("Tiket");
        mUsers = mDatabase.getReference().child("Users");
    }

    public void loadData() {

        mTiket.child("TelahKonfirmasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getKey = ds.getKey();
                    getUIDUser = ds.child("UIDUser").getValue(String.class);
                    getRefTransaksi = ds.child("RefTransaksi").getValue(String.class);
                    getJumlahTiket = ds.child("Jumlah").getValue(String.class);
                    getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                    getBuktiURL = ds.child("GambarBuktiURL").getValue(String.class);
                    getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);

                    arrayTiketKey.add(getKey);
                    arrayUIDUser.add(getUIDUser);
                    arrayRefTransaksi.add(getRefTransaksi);
                    arrayJumlahTiket.add(getJumlahTiket);
                    arrayTotalHarga.add(getTotalHarga);
                    arrayBuktiURL.add(getBuktiURL);
                    arrayTanggalKunjungan.add(getTanggalKunjungan);

                    for (int x = 0; x < arrayUIDUser.size(); x++) {

                        mUsers.child(arrayUIDUser.get(x)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                getNamaUser = dataSnapshot.child("Nama").getValue(String.class);
                                arrayNamaUser.add(getNamaUser);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Tiket>()
                .setQuery(mTiket, Tiket.class).build();

        adapter = new FirebaseRecyclerAdapter<Tiket, KonfirmasiTiketAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull KonfirmasiTiketAdapter holder, int position, @NonNull Tiket model) {

                holder.key = arrayTiketKey.get(position);
                Glide.with(getBaseContext()).load(arrayBuktiURL.get(position)).into(holder.gambarBukti);
                holder.jumlahTiket.setText(arrayJumlahTiket.get(position));
                holder.namaUser.setText(arrayNamaUser.get(position));
                holder.refTransaksi.setText(arrayRefTransaksi.get(position));
                holder.tanggalKunjungan.setText(arrayTanggalKunjungan.get(position));
                holder.totalHarga.setText(arrayTotalHarga.get(position));

            }

            @NonNull
            @Override
            public KonfirmasiTiketAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_konfirmasi_tiket, viewGroup, false);

                return new KonfirmasiTiketAdapter(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}
