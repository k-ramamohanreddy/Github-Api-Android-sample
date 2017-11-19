package com.ram.github_api_android_sample.adapters;

/**
 * Created by rmreddy on 19/11/17.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.events.RepoListClickEvent;
import com.ram.github_api_android_sample.models.RepoDetail;

import java.util.List;

import de.greenrobot.event.EventBus;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.MyViewHolder> {

    private Context mContext;
    private List<RepoDetail> aList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            name = (TextView) view.findViewById(R.id.repoName);
        }
    }


    public RepoListAdapter(Context mContext, List<RepoDetail> albumList) {
        this.mContext = mContext;
        this.aList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributor_repo_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(aList.get(position).getRepo_url());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepoListClickEvent eventBus = new RepoListClickEvent(position);
                EventBus.getDefault().post(eventBus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}