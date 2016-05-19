package com.siva.sivawedssatya.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.siva.sivawedssatya.R;

/**
 * A login screen that offers login via email/password.
 */public class FormFrament extends Fragment {

    public FormFrament() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View rootView = inflater.inflate(R.layout.dialog_layout, container, false);

            // Set up the login form.
            final EditText mEmailView = (EditText) rootView.findViewById(R.id.email);


            final EditText mNameView = (EditText) rootView.findViewById(R.id.name);


            //adding button click event
            Button dismissButton = (Button) rootView.findViewById(R.id.button);
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Store values at the time of the login attempt.
                    String email = mEmailView.getText().toString();
                    String password = mNameView.getText().toString();
                }
            });
            // Inflate the layout for this fragment
            return rootView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}






