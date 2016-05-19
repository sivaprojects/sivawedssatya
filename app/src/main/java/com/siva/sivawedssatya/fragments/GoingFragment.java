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
import com.siva.sivawedssatya.adapter.GoingListAdapter;
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
public class GoingFragment  extends Fragment {

    ArrayList<HomeModel> goingList1 = new ArrayList<HomeModel>();
    ArrayList<HomeModel> resultItems = new ArrayList<HomeModel>();
    View rootView = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.maybe_fragment, container, false);
        resultItems.clear();
        goingList1.clear();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.main_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        GoingListAdapter adapter = new GoingListAdapter(resultItems, this);
        recyclerView.setAdapter(adapter);

        try {
            SharedPreferences preferences = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);

            String goStr = preferences.getString("YES","");
            if(goStr!=null && !goStr.equals("")) {
                JSONObject go = new JSONObject(goStr);

                @SuppressWarnings("unchecked")
                Iterator<String> keySet = go.keys();

                while (keySet.hasNext()) {
                    HomeModel model = new HomeModel();
                    String key = keySet.next();
                    JSONObject json = (JSONObject) go.getJSONObject(key);

                    String email = key;
                    String name = json.getString("name");
                    String team = json.getString("team");
                    model.setEmail(email);
                    model.setName(name);
                    model.setTeam(team);
                    goingList1.add(model);

                }

                Collections.sort(goingList1, new Comparator<HomeModel>() {
                    @Override
                    public int compare(HomeModel hm1,
                                       HomeModel hm2) {
                        return hm1.getTeam().compareTo(
                                hm2.getTeam());
                    }
                });

                Set<String> ids = new HashSet<String>();

                for (HomeModel item : goingList1) {
                    if (ids.add(item.getEmail())) {
                        resultItems.add(item);
                    }
                }



                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




//        goAdapter contactsAdapter = new goAdapter(list,this);
//        recyclerView.setAdapter(contactsAdapter);
        return rootView;
    }
}
