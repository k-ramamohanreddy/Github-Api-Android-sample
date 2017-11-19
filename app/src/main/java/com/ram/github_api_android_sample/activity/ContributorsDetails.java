package com.ram.github_api_android_sample.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.adapters.RepoListAdapter;
import com.ram.github_api_android_sample.events.RepoListClickEvent;
import com.ram.github_api_android_sample.models.RepoDetail;
import com.ram.github_api_android_sample.utils.ApiService;
import com.ram.github_api_android_sample.utils.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ContributorsDetails extends AppCompatActivity {
    static final String TAG = ContributorsDetails.class.getSimpleName();
    ImageView avatarImg;
    TextView repoList;
    ConnectionManager connectionManager;
    RecyclerView repoList_LV;
    RepoListTask repoListTask;
    RepoListAdapter repoListAdapter;
    String repo_List, profile_pic;
    ProgressBar pb;
    List<RepoDetail> list;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contributor");
        //toolbar.setSubtitle("Subtitle");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        avatarImg = (ImageView) findViewById(R.id.imageView2);
        repoList = (TextView) findViewById(R.id.textView2);
        repoList_LV = (RecyclerView) findViewById(R.id.recycler_view);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        pb.setVisibility(View.GONE);

        apiService = new ApiService();
        list = new ArrayList<>();
        connectionManager = new ConnectionManager(ContributorsDetails.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1 );
        repoList_LV.setLayoutManager(mLayoutManager);
        repoList_LV.setItemAnimator(new DefaultItemAnimator());

        Intent dataIntent = getIntent();
        profile_pic = dataIntent.getStringExtra("AVATAR_IMG");
        repo_List = dataIntent.getStringExtra("REPO_LIST");

        Glide.with(ContributorsDetails.this).load(profile_pic).into(avatarImg);

        if(connectionManager.isNetworkAvailable()){
            Log.d(TAG,"Repo List URL == "+repo_List);
            repoListTask = new RepoListTask();
            repoListTask.execute();
        } else {
            Log.d(TAG,"Check your NetWork Connection");
            Toast.makeText(ContributorsDetails.this, "Check your NetWork Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class RepoListTask extends AsyncTask<String, String, String> {
        String responseStr;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... f_url) {
            try {
                if(repo_List.length() > 0 && repo_List != null){
                    responseStr = apiService.post(repo_List);
                    list.clear();
                    list = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(responseStr);
                    if(jsonArray.length() > 0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObj = jsonArray.getJSONObject(i);
                            String repo_url = itemObj.getString("html_url");
                            String name = itemObj.getString("name");
                            String full_name = itemObj.getString("full_name");
                            String description = itemObj.getString("description");
                            RepoDetail repo = new RepoDetail();
                            repo.setName(name);
                            repo.setFull_name(full_name);
                            repo.setRepo_url(repo_url);
                            repo.setDescription(description);
                            list.add(repo);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if(list.size() > 0){
                repoListAdapter = new RepoListAdapter(ContributorsDetails.this,list);
                repoList_LV.setAdapter(repoListAdapter);
                repoListAdapter.notifyDataSetChanged();
            }
            pb.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(RepoListClickEvent event) {
        Log.d(TAG,"RepoListClickEvent ==");
        Toast.makeText(ContributorsDetails.this, "Repo position === "+event.getPosition(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(ContributorsDetails.this)){
            EventBus.getDefault().register(ContributorsDetails.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(ContributorsDetails.this)){
            EventBus.getDefault().unregister(ContributorsDetails.this);
        }
    }

}
