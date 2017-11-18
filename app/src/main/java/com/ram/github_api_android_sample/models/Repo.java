package com.ram.github_api_android_sample.models;

/**
 * Created by rmreddy on 18/11/17.
 */

public class Repo {

    public String name;
    public String full_name;
    public String avatar_img;
    public int watchers_count;
    public int commits_count;
    public String projectLink ;
    public String contributorsUrl ;
    public String description ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public int getCommits_count() {
        return commits_count;
    }

    public void setCommits_count(int commits_count) {
        this.commits_count = commits_count;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
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
        if (!(o instanceof Repo)) return false;

        Repo repo = (Repo) o;

        if (watchers_count != repo.watchers_count) return false;
        if (commits_count != repo.commits_count) return false;
        if (!name.equals(repo.name)) return false;
        if (!full_name.equals(repo.full_name)) return false;
        if (!avatar_img.equals(repo.avatar_img)) return false;
        if (!projectLink.equals(repo.projectLink)) return false;
        if (!contributorsUrl.equals(repo.contributorsUrl)) return false;
        return description.equals(repo.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + full_name.hashCode();
        result = 31 * result + avatar_img.hashCode();
        result = 31 * result + watchers_count;
        result = 31 * result + commits_count;
        result = 31 * result + projectLink.hashCode();
        result = 31 * result + contributorsUrl.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "name='" + name + '\'' +
                ", full_name='" + full_name + '\'' +
                ", avatar_img='" + avatar_img + '\'' +
                ", watchers_count=" + watchers_count +
                ", commits_count=" + commits_count +
                ", projectLink='" + projectLink + '\'' +
                ", contributorsUrl='" + contributorsUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
