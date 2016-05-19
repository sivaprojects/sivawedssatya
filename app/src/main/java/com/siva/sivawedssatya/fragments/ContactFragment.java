package com.siva.sivawedssatya.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.activity.MapsActivity;

/**
 * Created by Siva on 20/03/16.
 */
public class ContactFragment extends Fragment {

    public ContactFragment() {
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
            View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "black_jack.ttf");

            ImageView image_map = (ImageView)rootView.findViewById(R.id.image_map);
            TextView label1 = (TextView) rootView.findViewById(R.id.label1);
            TextView label2 = (TextView) rootView.findViewById(R.id.label2);
            TextView label3 = (TextView) rootView.findViewById(R.id.label3);

            label1.setTypeface(typeface);
            label2.setTypeface(typeface);
            label3.setTypeface(typeface);
            image_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mainIntent = new Intent(getActivity(), MapsActivity.class);
                    mainIntent.putExtra("ISCONTACTS",true);
                    startActivityForResult(mainIntent,10);
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
