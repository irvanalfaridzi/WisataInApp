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

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.KonfirmasiTiketAdapter;
import com.example.wisatain.Adapters.RekomendasiAdapter;
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
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RekomendasiWisataActivity extends AppCompatActivity {

    @BindView(R.id.rwRecyclerView)
    RecyclerView recyclerView;

    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    FirebaseRecyclerOptions<Wisata> options;
    FirebaseRecyclerAdapter<Wisata, RekomendasiAdapter> adapter;

    String getWisataKey, getNamaWisata, getWilayahWisata, getRataRataRating, getGambarWisataURL;

    ArrayList<String> arrayWisataKey = new ArrayList<>();
    ArrayList<String> arrayNamaWisata = new ArrayList<>();
    ArrayList<String> arrayWilayahWisata = new ArrayList<>();
    ArrayList<String> arrayRataRataRating = new ArrayList<>();
    ArrayList<String> arrayGambarWisataURL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi_wisata);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        loadData();
    }

    public void loadData() {

        mWisata.orderByChild("Ulasan/RataRataRating").endAt("Ulasan/RataRataRating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getWisataKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getRataRataRating = String.valueOf(ds.child("Ulasan").child("RataRataRating").getValue(double.class));
                    getGambarWisataURL = ds.child("FotoWisataURL").getValue(String.class);
                    Log.d("rekomendasidata", "onDataChange: " + getWisataKey + " " + getNamaWisata);

                    arrayWisataKey.add(getWisataKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayRataRataRating.add(getRataRataRating);
                    arrayGambarWisataURL.add(getGambarWisataURL);
                }

                Collections.sort(arrayWisataKey,Collections.<String>reverseOrder());
                Collections.sort(arrayNamaWisata,Collections.<String>reverseOrder());
                Collections.sort(arrayWilayahWisata,Collections.<String>reverseOrder());
                Collections.sort(arrayRataRataRating,Collections.<String>reverseOrder());
                Collections.sort(arrayGambarWisataURL,Collections.<String>reverseOrder());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Wisata>().setQuery(mWisata.orderByChild("Ulasan/RataRataRating"), Wisata.class).build();

        adapter = new FirebaseRecyclerAdapter<Wisata, RekomendasiAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RekomendasiAdapter holder, int position, @NonNull Wisata model) {
                holder.namaWisata.setText(arrayNamaWisata.get(position));
                holder.wilayahWisata.setText(arrayWilayahWisata.get(position));
                Glide.with(getApplicationContext()).load(arrayGambarWisataURL.get(position)).into(holder.gambarWisata);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_isi).into(holder.keteranganRatingImg);
                holder.keteranganRatingText.setText(arrayRataRataRating.get(position));
                holder.key = arrayWisataKey.get(position);
            }

            @NonNull
            @Override
            public RekomendasiAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rekomendasi, viewGroup, false);

                return new RekomendasiAdapter(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
