package com.ram.github_api_android_sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ram.github_api_android_sample.R;
import com.ram.github_api_android_sample.adapters.ContributorsAdapter;
import com.ram.github_api_android_sample.events.ContributorClickEvent;
import com.ram.github_api_android_sample.models.Contributors;
import com.ram.github_api_android_sample.utils.ApiService;
import com.ram.github_api_android_sample.utils.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RepoDetailsActivity extends AppCompatActivity {
    static final String TAG = RepoDetailsActivity.class.getSimpleName();
    ImageView avatarimg_iv;
    TextView name_tv, projectLink_tv, description_tv;
    String name, project_Link, description, avatar_img, contributors_url;
    RecyclerView contributors_rv;
    ContributorsListTask contributorsListTask;
    ContributorsAdapter contributorsAdapter;
    List<Contributors> list;
    ConnectionManager connectionManager;
    ProgressBar pb;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        avatarimg_iv = (ImageView) findViewById(R.id.avatar_img);
        name_tv  = (TextView) findViewById(R.id.name);
        projectLink_tv = (TextView) findViewById(R.id.projectlink);
        description_tv = (TextView) findViewById(R.id.description);
        contributors_rv = (RecyclerView) findViewById(R.id.contributorsRV);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        pb.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        apiService = new ApiService();
        list = new ArrayList<>();
        connectionManager = new ConnectionManager(RepoDetailsActivity.this);
        Intent data = getIntent();
        name = data.getStringExtra("NAME");
        project_Link = data.getStringExtra("PROJECT_LINK");
        description = data.getStringExtra("DESCRIPTION");
        avatar_img = data.getStringExtra("AVATAR_IMG");
        contributors_url = data.getStringExtra("CONTRIBUTORS_URL");
        intTextView();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,3 );
        contributors_rv.setLayoutManager(mLayoutManager);
        contributors_rv.setItemAnimator(new DefaultItemAnimator());

        projectLink_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(RepoDetailsActivity.this, R.color.colorPrimary))
                        .build();
                customTabsIntent.launchUrl((Activity) RepoDetailsActivity.this, Uri.parse(project_Link));
            }
        });

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        if(connectionManager.isNetworkAvailable()){
            contributorsListTask = new ContributorsListTask();
            if(contributors_url.length() > 0 && contributors_url != null){
                Log.d(TAG,"contributors_url == "+contributors_url );
                contributorsListTask.execute();
            }
        }else {
            Log.d(TAG,"Check your network connection == "+contributors_url );
            Toast.makeText(RepoDetailsActivity.this,"Check your NetWork Connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void intTextView() {
        Glide.with(RepoDetailsActivity.this).load(avatar_img).into(avatarimg_iv);
        name_tv.setText("Name : "+name);
        String project_txt = "Project Link : <font color='blue'>Click here</font>";
        projectLink_tv.setText(Html.fromHtml(project_txt));
        description_tv.setText("Description\n"+description);
    }

    private class ContributorsListTask extends AsyncTask<String, String, String> {
        String responseStr;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... f_url) {
            try {
                if(contributors_url.length() > 0 && contributors_url != null) {
                    responseStr = apiService.post(contributors_url);
                    list.clear();
                    list = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(responseStr);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObj = jsonArray.getJSONObject(i);
                            String name = itemObj.getString("login");
                            String user_repos = itemObj.getString("repos_url");
                            String avatarImg = itemObj.getString("avatar_url");
                            Contributors contributors = new Contributors();
                            contributors.setName(name);
                            contributors.setRepos_link(user_repos);
                            contributors.setProfile_pic(avatarImg);
                            list.add(contributors);
                        }
                    }
                }
                } catch (JSONException jse) {
                    jse.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if(list.size() > 0){
                contributorsAdapter = new ContributorsAdapter(RepoDetailsActivity.this,list);// ContributorsAdapter(RepoDetailsActivity.this, list);
                contributors_rv.setAdapter(contributorsAdapter);
            }
            pb.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(ContributorClickEvent event) {
        Log.d(TAG,"ContributorClickEvent == ");
        Intent repoDetails = new Intent(RepoDetailsActivity.this,ContributorsDetails.class);
        repoDetails.putExtra("AVATAR_IMG",list.get(event.getPosition()).getProfile_pic());
        repoDetails.putExtra("REPO_LIST",list.get(event.getPosition()).getRepos_link());
        startActivity(repoDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(RepoDetailsActivity.this)){
            EventBus.getDefault().register(RepoDetailsActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(RepoDetailsActivity.this)){
            EventBus.getDefault().unregister(RepoDetailsActivity.this);
        }
    }
}
