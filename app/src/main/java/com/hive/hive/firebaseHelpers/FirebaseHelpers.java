package com.hive.hive.firebaseHelpers;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelpers {
    static String ASSOCIATION_COLLECTION = "associations";
    static String REQUESTS_COLLECTION = "requests";
    static String COMMENTS_COLLECTION = "comments";
    static String SUPPORTS_COLLECTION = "supports";
    static String BUDGET_CATEGORIES_COLLECTION = "budgetCategories";
    static String REQUEST_CATEGORIES_COLLECTION = "requestCategories";
    static String REPORTS_COLLECTION = "reports";
    static String FILES_COLLECTION = "files";
    static String FORUM_COLLECTION = "forum";
    static String INGRESS_REQUEST_COLLECTION = "ingressRequests";
    static String USER_COLLECTION = "users";
    static String SESSIONS_COLLECTION = "sessions";
    static String AGENDAS_COLLECTION = "agendas";
    static String QUESTIONS_COLLECTION = "questions";
    static String VOTES_COLLECTION = "votes";

    @SuppressLint("StaticFieldLeak")
    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();
}
