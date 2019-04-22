//package com.example.wisatain.Fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.CardView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.wisatain.Activities.Main.TiketBelumDigunakanActivity;
//import com.example.wisatain.Activities.Main.TiketMenungguKonfirmasiActivity;
//import com.example.wisatain.Activities.Main.TiketTelahDigunakanActivity;
//import com.example.wisatain.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class MainTiketFragment extends Fragment {
//
//    @BindView(R.id.mtcvTiketMenungguKonfirmasi)
//    CardView cvMenungguKonfirmasi;
//
//    @BindView(R.id.mtcvTiketBelumDigunakan)
//    CardView cvBelumDigunakan;
//
//    @BindView(R.id.mtcvTiketSudahDigunakan)
//    CardView cvSudahDigunakan;
//
//    public MainTiketFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_main_tiket, container, false);
//        ButterKnife.bind(this, view);
//
//        cvIntent();
//
//        return view;
//    }
//
//    public void cvIntent() {
//
//        cvMenungguKonfirmasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TiketMenungguKonfirmasiActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        cvBelumDigunakan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TiketBelumDigunakanActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        cvSudahDigunakan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TiketTelahDigunakanActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//}


package com.example.wisatain.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wisatain.Activities.Main.MainActivity;
import com.example.wisatain.Activities.Main.TiketBelumDigunakanActivity;
import com.example.wisatain.Activities.Main.TiketMenungguKonfirmasiActivity;
import com.example.wisatain.Activities.Main.TiketTelahDigunakanActivity;
import com.example.wisatain.R;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MainTiketFragment extends Fragment {

    Context context;

    @BindView(R.id.mtcvTiketMenungguKonfirmasi)
    CardView cvMenungguKonfirmasi;

    @BindView(R.id.mtcvTiketBelumDigunakan)
    CardView cvBelumDigunakan;

    @BindView(R.id.mtcvTiketSudahDigunakan)
    CardView cvSudahDigunakan;

    @SuppressLint("ValidFragment")
    public MainTiketFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_tiket, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Tiket");

        ButterKnife.bind(this, view);

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        cvIntent();

        return view;
    }

    public void cvIntent() {

        cvMenungguKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TiketMenungguKonfirmasiActivity.class);
                startActivity(intent);
            }
        });

        cvBelumDigunakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TiketBelumDigunakanActivity.class);
                startActivity(intent);
            }
        });

        cvSudahDigunakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TiketTelahDigunakanActivity.class);
                startActivity(intent);
            }
        });

    }

}
