package com.hive.hive.model.forum;

import com.hive.hive.model.marketplace.PointsTransaction;

import java.util.ArrayList;

/**
 * Created by naraujo on 1/28/18.
 */

public class ForumPost extends ForumAction {

    private String title;
    private String content;
    private int supportScore;

    private ArrayList<String> categoriesIds;
    private ArrayList<String> commentsIds;
    private ArrayList<String> supportsIds;

    //--- Constructors

    public ForumPost(
            String id, String authorId, PointsTransaction pointsTransaction, String forumId,
            String title, String content, int supportScore, ArrayList<String> categoriesIds,
            ArrayList<String> commentsIds, ArrayList<String> supportsIds)
    {
        super(id, authorId, pointsTransaction, forumId);
        this.title = title;
        this.content = content;
        this.supportScore = supportScore;
        this.categoriesIds = categoriesIds;
        this.commentsIds = commentsIds;
        this.supportsIds = supportsIds;
    }


    //--- Getters

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getSupportScore() {
        return supportScore;
    }

    public ArrayList<String> getCategories() {
        return categoriesIds;
    }

    public ArrayList<String> getCommentsIds() {
        return commentsIds;
    }

    public ArrayList<String> getSupportsIds() {
        return supportsIds;
    }

    //--- Setters

    public void setTitle(String title) {
        this.title = title;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setSupportScore(int supportScore) {
        this.supportScore = supportScore;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categoriesIds = categories;
    }

    public void setCommentsIds(ArrayList<String> commentsIds) {
        this.commentsIds = commentsIds;
    }

    public void setSupportsIds(ArrayList<String> supportsIds) {
        this.supportsIds = supportsIds;
    }
}
