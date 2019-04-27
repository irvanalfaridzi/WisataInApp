package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

        mWisata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    getWisataKey = ds.getKey();
                    getNamaWisata = ds.child("NamaWisata").getValue(String.class);
                    getWilayahWisata = ds.child("WilayahWisata").getValue(String.class);
                    getRataRataRating = ds.child("").getValue(String.class);
                    getGambarWisataURL = ds.child("GambarWisataURL").getValue(String.class);
                    Log.d("rekomendasidata", "onDataChange: " + getWisataKey + " " + getNamaWisata);

                    arrayWisataKey.add(getWisataKey);
                    arrayNamaWisata.add(getNamaWisata);
                    arrayWilayahWisata.add(getWilayahWisata);
                    arrayRataRataRating.add(getRataRataRating);
                    arrayGambarWisataURL.add(getGambarWisataURL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
