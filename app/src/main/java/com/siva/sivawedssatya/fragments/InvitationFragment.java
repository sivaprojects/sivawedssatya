package com.siva.sivawedssatya.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siva.sivawedssatya.R;

/**
 * Created by Siva on 20/03/16.
 */
public class InvitationFragment extends Fragment {

    public InvitationFragment() {
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
            View rootView = inflater.inflate(R.layout.fragment_invitation, container, false);

            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "black_jack.ttf");
            Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "black_jack.ttf");

            TextView siva = (TextView) rootView.findViewById(R.id.siva);
            TextView satya = (TextView) rootView.findViewById(R.id.satya);
            siva.setTypeface(typeface);
            satya.setTypeface(typeface1);

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
