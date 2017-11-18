package com.ram.github_api_android_sample.adapters;

/**
 * Created by rmreddy on 18/11/17.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.models.Repo;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Repo> aList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, full_Name, watcher_Count, commit_Count;
        public ImageView avatar_Img;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            avatar_Img = (ImageView) view.findViewById(R.id.avatar_img);
            name = (TextView) view.findViewById(R.id.name);
            full_Name = (TextView) view.findViewById(R.id.fullName);
            watcher_Count = (TextView) view.findViewById(R.id.watcherCount);
            commit_Count = (TextView) view.findViewById(R.id.commitCount);
        }
    }


    public RecyclerViewAdapter(Context mContext, List<Repo> albumList) {
        this.mContext = mContext;
        this.aList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText("Name : "+aList.get(position).getName());
        holder.full_Name.setText("Full Name : "+aList.get(position).getFull_name());
        holder.watcher_Count.setText("Watcher Count : "+aList.get(position).getWatchers_count());
        holder.commit_Count.setText("Commit Count : 0");

        // loading album cover using Glide library
        Glide.with(mContext).load(aList.get(position).getAvatar_img())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatar_Img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"position=="+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}