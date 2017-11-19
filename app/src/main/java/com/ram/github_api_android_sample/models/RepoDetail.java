package com.ram.github_api_android_sample.models;

/**
 * Created by rmreddy on 19/11/17.
 */

public class RepoDetail {

    public String name;
    public String full_name;
    public String repo_url;
    public String description ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepo_url() {
        return repo_url;
    }

    public void setRepo_url(String repo_url) {
        this.repo_url = repo_url;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepoDetail)) return false;

        RepoDetail that = (RepoDetail) o;

        if (!name.equals(that.name)) return false;
        if (!full_name.equals(that.full_name)) return false;
        if (!repo_url.equals(that.repo_url)) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + full_name.hashCode();
        result = 31 * result + repo_url.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RepoDetail{" +
                "name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", repo_url='" + repo_url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
