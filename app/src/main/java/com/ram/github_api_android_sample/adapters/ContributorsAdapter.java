package com.ram.github_api_android_sample.adapters;

/**
 * Created by rmreddy on 18/11/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ram.github_api_android_sample.GlideCircleTransformation;
import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.events.ContributorClickEvent;
import com.ram.github_api_android_sample.models.Contributors;

import java.util.List;

import de.greenrobot.event.EventBus;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Contributors> aList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView avatar_Img;

        public MyViewHolder(View view) {
            super(view);
            avatar_Img = (ImageView) view.findViewById(R.id.imageView);
            name = (TextView) view.findViewById(R.id.textView);
        }
    }


    public ContributorsAdapter(Context mContext, List<Contributors> albumList) {
        this.mContext = mContext;
        this.aList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributors_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(aList.get(position).getName());
        Glide.with(mContext).load(aList.get(position).getProfile_pic())
                .thumbnail(0.5f)
                .crossFade()
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new GlideCircleTransformation(mContext))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatar_Img);
        holder.avatar_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContributorClickEvent eventBus = new ContributorClickEvent(position);
                EventBus.getDefault().post(eventBus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
}