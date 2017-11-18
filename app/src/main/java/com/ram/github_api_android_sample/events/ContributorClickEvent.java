package com.ram.github_api_android_sample.events;

/**
 * Created by rmreddy on 18/11/17.
 */

public class ContributorClickEvent {
    private int position;

    public ContributorClickEvent(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }
}
