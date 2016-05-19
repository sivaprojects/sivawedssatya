package com.siva.sivawedssatya.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siva.sivawedssatya.R;
import com.siva.sivawedssatya.fragments.MayBeFragment;
import com.siva.sivawedssatya.model.HomeModel;

import java.util.List;

public class MaybeListAdapter extends RecyclerView.Adapter<MaybeListAdapter.ViewHolder>{
    private List<HomeModel> mData;
    private MayBeFragment mainActivity;

    public MaybeListAdapter(List<HomeModel> data, MayBeFragment mainActivity) {
        this.mData = data;
        this.mainActivity = mainActivity;
    }

    @Override
    public MaybeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        try {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView, mainActivity);
            return viewHolder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        try {
            String team="";
            if(mData.get(position).getTeam().contains("0")){
                team = "Friends";
            }else if(mData.get(position).getTeam().contains("1")){
                team = "Colleagues";
            }else if(mData.get(position).getTeam().contains("2")){
                team = "Relatives";
            }
            viewHolder.textViewItem.setText(team);
            viewHolder.textViewPersonName.setText(mData.get(position).getName());
            viewHolder.textViewPersonEmail.setText(mData.get(position).getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public long leaseId;

        public MayBeFragment mainActivity;
        public TextView textViewItem;
        public TextView textViewPersonName;
        public TextView textViewPersonEmail;
        public View viewParent;

        @Override
        public void onClick(View v) {

        }

        public ViewHolder(View itemLayoutView, MayBeFragment mainActivity) {
            super(itemLayoutView);
            this.mainActivity = mainActivity;
            textViewItem = (TextView) itemLayoutView.findViewById(R.id.activity_main_item_item);
            textViewPersonName = (TextView) itemLayoutView.findViewById(R.id.activity_main_item_person_name);
            textViewPersonEmail = (TextView) itemLayoutView.findViewById(R.id.activity_main_item_person_email);

            viewParent = (View) itemLayoutView.findViewById(R.id.activity_main_item_parent);
            viewParent.setOnClickListener(this);
        }

        public void setLeaseId(long id) {
            this.leaseId = id;
        }
    }
}