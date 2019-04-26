package com.example.wisatain.Activities.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.wisatain.Adapters.MainHomeAdapter;
import com.example.wisatain.Adapters.UlasanAdapter;
import com.example.wisatain.Items.Ulasan;
import com.example.wisatain.Items.Wisata;
import com.example.wisatain.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import butterknife.OnClick;

public class ProfilWisataUserActivity extends AppCompatActivity {

    @BindView(R.id.pwuiGambarWisata)
    ImageView gambarWisata;

//    @BindView(R.id.pwutNamadanKotaWisata)
//    TextView namadanKotaWisata;

    @BindView(R.id.namaWisata)
    TextView namaWisata;

    @BindView(R.id.kotaWisata)
    TextView kotaWisata;

    @BindView(R.id.pwutDeskripsiWisata)
    TextView deskripsiWisata;

    @BindView(R.id.pwutJamOperasional)
    TextView jamOperasional;

    @BindView(R.id.pwutCocokUntukKeterangan)
    TextView cocokUntuk;

    @BindView(R.id.pwutDetailLokasiKeterangan)
    TextView detailLokasi;

    @BindView(R.id.pwutHargaTiketKeterangan)
    TextView hargaTiket;

    @BindView(R.id.pwuBtnFavorite)
    ToggleButton buttonFavorite;

    @BindView(R.id.txtInputUlasan)
    EditText inputUlasan;

    @BindView(R.id.pwuRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.inputStar1) ToggleButton star1;
    @BindView(R.id.inputStar2) ToggleButton star2;
    @BindView(R.id.inputStar3) ToggleButton star3;
    @BindView(R.id.inputStar4) ToggleButton star4;
    @BindView(R.id.inputStar5) ToggleButton star5;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mWisata;
    DatabaseReference mUsers;
    DatabaseReference mUlasan;
    FirebaseRecyclerOptions<Ulasan> options;
    FirebaseRecyclerAdapter<Ulasan, UlasanAdapter> adapter;

    ArrayList<String> favoriteArrayList = new ArrayList<>();

    String inputRating;
    String intentKey;
    public String getkey, getGambarWisataURL, getNamaWisata, getDeksripsiWisata, getJamOperasionalWisata, getCocokUntukWisata, getDetailLokasiWisata, getHargaTiketWisata, getKotaWisata;
    String getUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_wisata_user);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setRatingUlasan();
        loadRating();

        Intent intent = getIntent();
        intentKey = intent.getStringExtra("intent");
        Log.d("getKey", "onCreate: " + intentKey);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mWisata = mDatabase.getReference().child("Wisata");
        mUsers = mDatabase.getReference().child("Users");
        mUlasan = mDatabase.getReference().child("Ulasan").child(intentKey);

        getUID = mUser.getUid();

        loadData();

        loadDataFav();
    }

    public void loadData() {

        mWisata.child(intentKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getkey = dataSnapshot.getKey();
                getGambarWisataURL = dataSnapshot.child("FotoWisataURL").getValue(String.class);
                getNamaWisata = dataSnapshot.child("NamaWisata").getValue(String.class);
                getDeksripsiWisata = dataSnapshot.child("DeskripsiWisata").getValue(String.class);
                getJamOperasionalWisata = dataSnapshot.child("JamOperasional").getValue(String.class);
                getCocokUntukWisata = dataSnapshot.child("KategoriWisata").getValue(String.class);
                getDetailLokasiWisata = dataSnapshot.child("LokasiWisata").getValue(String.class);
                getHargaTiketWisata = dataSnapshot.child("HargaWisata").getValue(String.class);
                getKotaWisata = dataSnapshot.child("WilayahWisata").getValue(String.class);
                Log.d("wisatapwu", "onDataChange: " + getkey + " " + getNamaWisata + " " + getGambarWisataURL);

                if (getkey != null) {
                    Glide.with(getApplicationContext()).load(getGambarWisataURL).into(gambarWisata);
                    namaWisata.setText(getNamaWisata);
                    kotaWisata.setText(getKotaWisata);
                    deskripsiWisata.setText(getDeksripsiWisata);
                    jamOperasional.setText(getJamOperasionalWisata);
                    cocokUntuk.setText(getCocokUntukWisata);
                    detailLokasi.setText(getDetailLokasiWisata + ", " + getKotaWisata);
                    if (getHargaTiketWisata.toString().equals("0")) {
                        hargaTiket.setText("GRATIS");
                        findViewById(R.id.pwuBtnBeliTiket).setEnabled(false);
                    } else {
                        hargaTiket.setText("Rp" + getHargaTiketWisata);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void loadDataFav() {

        mUsers.child(getUID).child("Favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String check = ds.getValue(String.class);
                    favoriteArrayList.add(check);
                    Log.d("pwucheck", "onDataChange: " + check);

                    for (int x = 0; x < favoriteArrayList.size(); x++) {
                        if (intentKey.equals(favoriteArrayList.get(x))) {
                            buttonFavorite.setChecked(true);
                            buttonFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_red_100dp));
                            break;
                        } else {
                            buttonFavorite.setChecked(false);
                            mUsers.child(getUID).child("Favorite").child(intentKey).removeValue();
                            buttonFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_red_100dp));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setRatingUlasan() {
        star1.setChecked(false);
        star2.setChecked(false);
        star3.setChecked(false);
        star4.setChecked(false);
        star5.setChecked(false);

        star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
        star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
        star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
        star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
        star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));

        inputRating = "";

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                inputRating = "1";
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                inputRating = "2";
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                inputRating = "3";
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                inputRating = "4";
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_isi));
                inputRating = "5";
            }
        });

    }

    public class uploadRatingUlasan {
        public String WisataID, NamaWisata, UIDUser, NamaUser,GambarProfilUser, RatingUser,UlasanUser;

        public uploadRatingUlasan(String wisataID, String namaWisata, String UIDUser, String namaUser, String gambarProfilUser, String ratingUser, String ulasanUser) {
            WisataID = wisataID;
            NamaWisata = namaWisata;
            this.UIDUser = UIDUser;
            NamaUser = namaUser;
            GambarProfilUser = gambarProfilUser;
            RatingUser = ratingUser;
            UlasanUser = ulasanUser;
        }
    }

    public void inputRatingUlasan() {

        mUsers.child(getUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getNamaUser = dataSnapshot.child("Nama").getValue(String.class);
                String getGambarUserURL = dataSnapshot.child("FotoURL").getValue(String.class);

                uploadRatingUlasan RU = new uploadRatingUlasan(
                        intentKey,
                        getNamaWisata,
                        getUID,
                        getNamaUser,
                        getGambarUserURL,
                        inputRating,
                        inputUlasan.getText().toString().trim()
                        );

                mUlasan.setValue(RU).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            star1.setChecked(false);
                            star2.setChecked(false);
                            star3.setChecked(false);
                            star4.setChecked(false);
                            star5.setChecked(false);

                            star1.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                            star2.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                            star3.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                            star4.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));
                            star5.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_tidak));

                            inputUlasan.setText("");
                        } else {

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void loadRating() {

        mUlasan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String totalRating = dataSnapshot.child("TotalRating").getValue(String.class);
                String rataRating = dataSnapshot.child("RataRataRating").getValue(String.class);
                String jumlahRating = dataSnapshot.child("JumlahRating").getValue(String.class);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Ulasan>()
                .setQuery(mUlasan, Ulasan.class).build();

        adapter = new FirebaseRecyclerAdapter<Ulasan, UlasanAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UlasanAdapter holder, int position, @NonNull Ulasan model) {

            }

            @NonNull
            @Override
            public UlasanAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ulasan, viewGroup, false);

                return new UlasanAdapter(view);
            }
        };

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.pwuBtnBukaMaps)
    public void bukaMaps() {
        Intent intent = new Intent(ProfilWisataUserActivity.this, WisataMapsActivity.class);
        intent.putExtra("intentlokasi", getDetailLokasiWisata);
        Toast.makeText(this, getDetailLokasiWisata, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @OnClick(R.id.pwuBtnBeliTiket)
    public void beliTiket() {
        Intent intent = new Intent(ProfilWisataUserActivity.this, PesanTiketActivity.class);
        intent.putExtra("key", getkey);
        startActivity(intent);
    }

    @OnClick(R.id.pwuBtnFavorite)
    public void favorite() {
        if (buttonFavorite.isChecked()) {
           // buttonFavorite.setChecked(true);
            buttonFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_red_100dp));
            mUsers.child(getUID).child("Favorite").child(intentKey).setValue(intentKey);
        } else  {
          //  buttonFavorite.setChecked(false);
            mUsers.child(getUID).child("Favorite").child(intentKey).removeValue();
            buttonFavorite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_red_100dp));
        }
    }

    @OnClick(R.id.btnUlasan)
    public void ratingUlasan() {
        if (inputRating.isEmpty()) {
            Toast.makeText(this, "Masukkan Rating Wisata", Toast.LENGTH_SHORT).show();
            return;
        } if (inputUlasan.getText().toString().trim().isEmpty()) {
            inputUlasan.setError("Masukan Ulasan anda");
            inputUlasan.requestFocus();
            return;
        }
        inputRatingUlasan();
    }
}
