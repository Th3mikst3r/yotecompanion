package com.aura.YoteCompanion.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aura.YoteCompanion.NoteActivities.NotesList;
import com.aura.YoteCompanion.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {


    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_stub, container, false);

        Intent intent = new Intent();
        intent.setClass(getActivity(), NotesList.class);
        getActivity().startActivity(intent);

        return v;
    }


}
