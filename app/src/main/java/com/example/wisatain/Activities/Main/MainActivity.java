package com.example.wisatain.Activities.Main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.example.wisatain.Fragments.MainCariFragment;
import com.example.wisatain.Fragments.MainHomeFragment;
import com.example.wisatain.Fragments.MainFavoriteFragment;
import com.example.wisatain.Fragments.MainAkunFragment;
import com.example.wisatain.Fragments.MainTiketFragment;
import com.example.wisatain.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @BindView(R.id.mBottomNav)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navlistener);
        getSupportFragmentManager().beginTransaction().replace(R.id.mFragments, new MainHomeFragment()).commit();

    }




    private BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.homeNav :
                    selectedFragment = new MainHomeFragment();
                    break;
                case R.id.favoriteNav :
                    selectedFragment = new MainFavoriteFragment();
                    break;
                case R.id.cariNav :
                    selectedFragment = new MainCariFragment();
                    break;
                case R.id.tiketNav :
                    selectedFragment = new MainTiketFragment(MainActivity.this);
                    break;
                case R.id.akunNav :
                    selectedFragment = new MainAkunFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.mFragments, selectedFragment).commit();

            return true;
        }
    };
}
