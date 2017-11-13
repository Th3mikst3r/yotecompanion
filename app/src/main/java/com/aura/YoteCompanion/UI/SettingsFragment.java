package com.aura.YoteCompanion.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aura.YoteCompanion.R;


public class SettingsFragment extends Fragment {

    private TextView userloggedin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_home, container, false);

        /*Displays the logged in user by grabbing the Firebase auth from SignInActivity*/
        userloggedin = (TextView) v.findViewById(R.id.currloggedinuser);
        Bundle extras = getActivity().getIntent().getExtras();
        userloggedin.setText("Welcome");
        if(extras!=null){
            userloggedin.setText("Welcome "+extras.getString("UserName"));
        }


        return  v;
    }


}
