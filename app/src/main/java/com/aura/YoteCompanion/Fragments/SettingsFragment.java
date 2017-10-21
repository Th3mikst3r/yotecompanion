package com.aura.YoteCompanion.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aura.YoteCompanion.Authentication.SettingsActivity;
import com.aura.YoteCompanion.R;


public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_note, container, false);

        Intent intent = new Intent();
        intent.setClass(getActivity(), SettingsActivity.class);
        getActivity().startActivity(intent);

        return v;
    }

}
