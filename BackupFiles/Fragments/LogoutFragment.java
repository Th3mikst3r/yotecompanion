package com.aura.YoteCompanion.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aura.YoteCompanion.Authentication.LoginActivity;
import com.aura.YoteCompanion.R;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {

    private FirebaseAuth auth;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stub, container, false);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        auth.signOut();

        //After logout login activity is called
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);

        return v;
    }
}
