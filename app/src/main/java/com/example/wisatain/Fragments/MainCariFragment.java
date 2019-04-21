package com.example.wisatain.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wisatain.Activities.Main.WisataMapsActivity;
import com.example.wisatain.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCariFragment extends Fragment {


    public MainCariFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_cari, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.mfBtnMaps)
    public void maps() {
        Intent intent = new Intent(getActivity(), WisataMapsActivity.class);
        startActivity(intent);
    }

}
