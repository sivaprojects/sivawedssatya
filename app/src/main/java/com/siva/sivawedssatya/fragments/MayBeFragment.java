package com.siva.sivawedssatya.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.adapter.MaybeListAdapter;
import com.siva.sivawedssatya.model.HomeModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by User on 24-03-2016.
 */
public class MayBeFragment extends Fragment {

    ArrayList<HomeModel> goingList = new ArrayList<HomeModel>();

    ArrayList<HomeModel> resultItems = new ArrayList<HomeModel>();
    View rootView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.maybe_fragment, container, false);
        goingList.clear();
        resultItems.clear();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.main_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        MaybeListAdapter adapter = new MaybeListAdapter(resultItems, this);
        recyclerView.setAdapter(adapter);

        try {
            SharedPreferences preferences = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);

            String maybeStr = preferences.getString("MAYBE","");
            if(maybeStr!=null && !maybeStr.equals("")) {
                JSONObject maybe = new JSONObject(maybeStr);

                @SuppressWarnings("unchecked")
                Iterator<String> keySet = maybe.keys();

                while (keySet.hasNext()) {
                    HomeModel model = new HomeModel();
                    String key = keySet.next();
                    JSONObject json = (JSONObject) maybe.getJSONObject(key);

                        String email = key;
                        String name = json.getString("name");
                        String team = json.getString("team");
                        model.setEmail(email);
                        model.setName(name);
                        model.setTeam(team);
                        goingList.add(model);

                }
                Collections.sort(goingList, new Comparator<HomeModel>() {
                    @Override
                    public int compare(HomeModel hm1,
                                       HomeModel hm2) {
                        return hm1.getTeam().compareTo(
                                hm2.getTeam());
                    }
                });

                Set<String> ids = new HashSet<String>();

                for (HomeModel item : goingList) {
                    if (ids.add(item.getEmail())) {
                        resultItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        MayBeAdapter contactsAdapter = new MayBeAdapter(list,this);
//        recyclerView.setAdapter(contactsAdapter);
        return rootView;
    }
}


