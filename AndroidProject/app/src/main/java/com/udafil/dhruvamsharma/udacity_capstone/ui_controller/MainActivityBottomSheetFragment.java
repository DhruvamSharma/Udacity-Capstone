package com.udafil.dhruvamsharma.udacity_capstone.ui_controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.udafil.dhruvamsharma.udacity_capstone.R;


public class MainActivityBottomSheetFragment extends BottomSheetDialogFragment {

    public MainActivityBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main_bottom_sheet_fragment,
                container, false);
    }



}
