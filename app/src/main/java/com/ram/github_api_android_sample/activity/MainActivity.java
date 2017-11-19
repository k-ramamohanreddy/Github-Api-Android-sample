package com.ram.github_api_android_sample.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.adapters.RecyclerViewAdapter;
import com.ram.github_api_android_sample.events.RepoClickEvent;
import com.ram.github_api_android_sample.models.Repo;
import com.ram.github_api_android_sample.utils.ApiService;
import com.ram.github_api_android_sample.utils.ConnectionManager;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

     static final String TAG = MainActivity.class.getSimpleName();
    SearchView searchView;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RepoListTask repoListTask;
    ConnectionManager connectionManager;
    String query_item;
    List<Repo> list;
    ProgressBar pb;
    ApiService apiService ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.svSearchView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        pb.setVisibility(View.GONE);
        apiService = new ApiService();
        list = new ArrayList<>();
        connectionManager = new ConnectionManager(MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG,"onQueryTextSubmit = "+query);
                query_item = query;
                if(query_item.length() > 0){
                    if(connectionManager.isNetworkAvailable()){
                        repoListTask = new RepoListTask();
                        repoListTask.execute();
                    }else {
                        Toast.makeText(MainActivity.this,"Check your NetWork Connection",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"Enter search can't be blank",Toast.LENGTH_SHORT).show();
                    searchView.requestFocus();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG,"onQueryTextChange = "+newText);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(MainActivity.this)){
            EventBus.getDefault().register(MainActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(MainActivity.this)){
            EventBus.getDefault().unregister(MainActivity.this);
        }
    }

    private class RepoListTask extends AsyncTask<String, String, String> {
        String responseString;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... f_url) {
            try {
                responseString = apiService.post("https://api.github.com/search/repositories?q="+query_item+"+&order=watchers-desc");
                list.clear();
                list = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                if(jsonArray.length() > 0){
                    for (int i = 0; i < 10 && i <jsonArray.length(); i++) {
                        JSONObject itemObj = jsonArray.getJSONObject(i);
                        String name = itemObj.getString("name");
                        String fullName = itemObj.getString("full_name");
                        String projectLink = itemObj.getString("html_url");
                        String contributorsUrl = itemObj.getString("contributors_url");
                        String description = itemObj.getString("description");
                        int watchersCount = itemObj.getInt("watchers_count");
                        JSONObject ownerObj = itemObj.getJSONObject("owner");
                        String avatarImg = ownerObj.getString("avatar_url");
                        Repo repo = new Repo();
                        repo.setName(name);
                        repo.setFull_name(fullName);
                        repo.setProjectLink(projectLink);
                        repo.setContributorsUrl(contributorsUrl);
                        repo.setDescription(description);
                        repo.setWatchers_count(watchersCount);
                        repo.setAvatar_img(avatarImg);
                        list.add(repo);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if(list.size() > 0){
                Collections.sort(list, new Comparator<Repo>() {
                    @Override
                    public int compare(Repo o1, Repo o2) {
                        return o2.getWatchers_count()-o1.getWatchers_count();
                    }
                });
                adapter = new RecyclerViewAdapter(MainActivity.this, list);
                recyclerView.setAdapter(adapter);
            }
            searchView.clearFocus();
            pb.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(RepoClickEvent event) {
        Log.d(TAG,"RepoClickEvent == ");
        Intent repoDetails = new Intent(MainActivity.this, RepoDetailsActivity.class);
        repoDetails.putExtra("AVATAR_IMG",list.get(event.getPosition()).getAvatar_img());
        repoDetails.putExtra("NAME",list.get(event.getPosition()).getName());
        repoDetails.putExtra("PROJECT_LINK",list.get(event.getPosition()).getProjectLink());
        repoDetails.putExtra("DESCRIPTION",list.get(event.getPosition()).getDescription());
        repoDetails.putExtra("CONTRIBUTORS_URL",list.get(event.getPosition()).getContributorsUrl());
        startActivity(repoDetails);
    }

}
