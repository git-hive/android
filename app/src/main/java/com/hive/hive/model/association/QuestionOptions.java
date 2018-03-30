package com.hive.hive.model.association;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class QuestionOptions {

    private String title;
    private String content;
    private int score;

    private ArrayList<DocumentReference> votesRefs;

    //--- Constructor

    public QuestionOptions(
            String title,
            String content,
            int score,
            ArrayList<DocumentReference> votesRefs
    ) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.votesRefs = votesRefs;
    }

    public QuestionOptions() {
    }

//--- Getters

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<DocumentReference> getVotesRefs() {
        return votesRefs;
    }

    //--- Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setVotesRefs(ArrayList<DocumentReference> votesRefs) {
        this.votesRefs = votesRefs;
    }
}
