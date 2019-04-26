package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TiketMenungguKonfirmasiActivity extends AppCompatActivity {

    @BindView(R.id.tmkRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tmkBelumpunyatiket)
    ImageView keteranganGambar;

    @BindView(R.id.tmkBelummemiliki)
    TextView keteranganTulisan1;

    @BindView(R.id.tmkBeliTiket)
    TextView keteranganTulisan2;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mTiket;
    DatabaseReference mUsers;
    FirebaseRecyclerOptions<Tiket> options;
    FirebaseRecyclerAdapter<Tiket, MainTiketAdapter> adapter;

    public String getUID;
    public String getTiketKey, getNamaWisata, getWilayahWisata, getTanggalKunjungan, getJumlahTiket, getTotalHarga, getTiketStatus;

    ArrayList<String> arrayTiketKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayTanggalKunjungan = new ArrayList<>();
    ArrayList<String> arrayJumlahTiket = new ArrayList<>();
    ArrayList<String> arrayTotalHarga = new ArrayList<>();
    ArrayList<String> arrayTiketStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket_menunggu_konfirmasi);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        getUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference().child("Users").child(getUID);
        mTiket = mUsers.child("Tiket").child("MenungguKonfirmasi");

        loadData();

    }

    public void loadData() {

        mTiket./*orderByChild("TiketStatus").equalTo("Menunggu Konfirmasi").*/addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getTiketKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getTanggalKunjungan = ds.child("TanggalKunjungan").getValue(String.class);
                    getJumlahTiket = ds.child("Jumlah").getValue(String.class);
                    getTotalHarga = ds.child("TotalHarga").getValue(String.class);
                    getTiketStatus = ds.child("TiketStatus").getValue(String.class);
                    Log.d("testkeykey", "onDataChange: " + getTiketKey + " " + getNamaWisata);

                    arrayTiketKey.add(getTiketKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayTanggalKunjungan.add(getTanggalKunjungan);
                    arrayJumlahTiket.add(getJumlahTiket);
                    arrayTotalHarga.add(getTotalHarga);
                    arrayTiketStatus.add(getTiketStatus);

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

       // Log.d("asd", "loadData: " + arrayTiketKey);

        for (int x = 0; x < arrayTiketKey.size(); x++){
            Log.d("array", "loadData: " + arrayTiketKey.get(x));
        }


        options = new FirebaseRecyclerOptions.Builder<Tiket>()
                .setQuery(mTiket, Tiket.class).build();

        adapter = new FirebaseRecyclerAdapter<Tiket, MainTiketAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MainTiketAdapter holder, int position, @NonNull Tiket model) {

                holder.wisataKey = arrayTiketKey.get(position);
                holder.tiketStatus = arrayTiketStatus.get(position);
                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                holder.tanggalKunjungan.setText(arrayTanggalKunjungan.get(position));
                holder.jumlahTiket.setText(arrayJumlahTiket.get(position));
                holder.totalHarga.setText("Rp" + arrayTotalHarga.get(position));
//
//                    holder.wisataKey = "test";
//                    holder.tiketStatus = "test";
//                    holder.namaWisata.setText("test");
//                    holder.wilayahWisata.setText("test");
//                    holder.tanggalKunjungan.setText("test");
//                    holder.jumlahTiket.setText("test");
//                    holder.totalHarga.setText("test");

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

    public void clearArray() {

//        arrayTiketKey.clear();
//        arrayNamaWisata.clear();
//        arrayWilayahWisata.clear();
//        arrayTanggalKunjungan.clear();
//        arrayJumlahTiket.clear();
//        arrayTotalHarga.clear();
//        arrayTiketStatus.clear();
//
//        arrayTiketKey = null;
//        arrayNamaWisata = null;
//        arrayWilayahWisata = null;
//        arrayTanggalKunjungan = null;
//        arrayJumlahTiket = null;
//        arrayTotalHarga = null;
//        arrayTiketStatus = null;


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        arrayTiketKey = null;
//        arrayNamaWisata = null;
//        arrayWilayahWisata = null;
//        arrayTanggalKunjungan = null;
//        arrayJumlahTiket = null;
//        arrayTotalHarga = null;
//        arrayTiketStatus = null;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//        getUID = mUser.getUid();
//        mDatabase = FirebaseDatabase.getInstance();
//        mUsers = mDatabase.getReference().child("Users").child(getUID);
//        mTiket = mUsers.child("Tiket");
//
//        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearArray();
    }
}
