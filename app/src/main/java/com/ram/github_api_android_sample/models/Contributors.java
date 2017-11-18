package com.ram.github_api_android_sample.models;

/**
 * Created by rmreddy on 18/11/17.
 */

public class Contributors {
    String name;
    String profile_pic;
    String repos_link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getRepos_link() {
        return repos_link;
    }

    public void setRepos_link(String repos_link) {
        this.repos_link = repos_link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contributors)) return false;

        Contributors that = (Contributors) o;

        if (!name.equals(that.name)) return false;
        if (!profile_pic.equals(that.profile_pic)) return false;
        return repos_link.equals(that.repos_link);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + profile_pic.hashCode();
        result = 31 * result + repos_link.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Contributors{" +
                "name='" + name + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", repos_link='" + repos_link + '\'' +
                '}';
    }
}
