package com.example.wisatain.Activities.Main;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatain.Items.Wisata;
import com.example.wisatain.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WisataMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.wmProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.wmBtnSearch)
    Button wmBtnSearch;

    @BindDrawable(R.drawable.bca)
    Drawable bca;

    private static final int REQUEST_PERMISSION_CODE = 55;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    DatabaseReference mLocationRef;

    GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Marker marker;
    Location lastLocation;
    LocationRequest locationRequest;

    String getUID;

    ArrayList<String> arrayLocation = new ArrayList<String>();
    ArrayList<String> arrayWisata = new ArrayList<String>();
    ArrayList<String> arrayFotoWisata = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_maps);
        ButterKnife.bind(this);

        wmBtnSearch.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mLocationRef = mDatabase.getReference().child("Users");
        getUID = mUser.getUid();

        Log.d("asdzxc", "onDataChange: " + "false");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.wmfmap);
        mapFragment.getMapAsync(this);

        //loadData();

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadData();
    }

    public void loadData() {

        FirebaseDatabase.getInstance().getReference().child("Wisata")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dskey : dataSnapshot.getChildren()) {
                            String key = dskey.getKey();
                            Log.d("wisata1key", "onDataChange: " + key);
                            String dataLokasi = dskey.child("LokasiWisata").getValue(String.class);
                            Log.d("wisata1data", "onDataChange: " + dataLokasi);
                            arrayLocation.add(dataLokasi);
                            String dataNama = dskey.child("NamaWisata").getValue(String.class);
                            Log.d("wisata1data", "onDataChange: " + dataNama);
                            arrayWisata.add(dataNama);
                            String dataFoto = dskey.child("FotoWisataURL").getValue(String.class);
                            Log.d("wisata1data", "onDataChange: " + dataFoto);
                            arrayFotoWisata.add(dataFoto);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        List<Address> addressList = null;

        for (int x = 0; x < arrayLocation.size(); x++) {

            Log.d("arrayLocation", "loadData: " +arrayLocation.get(x));

            MarkerOptions markerOptions = new MarkerOptions();

            if (true) {
                progressBar.setVisibility(View.GONE);
                Geocoder geocoder = new Geocoder(WisataMapsActivity.this);

                try {
                    addressList = geocoder.getFromLocationName(arrayLocation.get(x), 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int y = 0 ; y < addressList.size(); y++) {
                   // Bitmap icon = BitmapFactory.decodeResource(WisataMapsActivity.this.getResources(), R.drawable.bni);
                    Address address = addressList.get(y);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    markerOptions.position(latLng);
                   // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bni));
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    markerOptions.title(arrayWisata.get(y));
                    markerOptions.snippet(arrayLocation.get(y));
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(markerOptions);
                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    Log.d("asdzxc", "onDataChange: " + arrayWisata.get(y));

                    final String tes = arrayFotoWisata.get(y);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                           // Toast.makeText(WisataMapsActivity.this,marker.getTitle(),Toast.LENGTH_SHORT).show();

                            showImageView(marker.getTitle(), tes);
                            return false;
                        }
                    });
                }

            } else {
                progressBar.setVisibility(View.GONE);
                Log.d("asdzxc", "onDataChange: " + "false");

            }
            Log.d("asdzxc", "onDataChange: " + arrayWisata.get(x));
        }

    }

    public void showImageView(String namaWisata, String link){
        final Dialog dialog = new Dialog(WisataMapsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_preview_map);


        ImageView imageView = dialog.findViewById(R.id.imageView);

        Glide.with(WisataMapsActivity.this).load(link).into(imageView);

        dialog.show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            builGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Maaf tidak ada akses", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            builGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void builGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if (marker != null) {
            marker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        // LatLng latLng = new LatLng(-34, 151);

      /*  MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Choose Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo));

        marker = mMap.addMarker(markerOptions);*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        loadData();

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.wmBtnSearch)
    public void mapsSearch(){
        loadData();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }
}