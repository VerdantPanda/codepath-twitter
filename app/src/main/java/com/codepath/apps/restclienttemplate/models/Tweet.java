package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    public String body;
    public long uid;
    // public User user;
    public String createdAt;

    public Tweet(String body, long uid, String createdAt) {
        this.body = body;
        this.uid = uid;
        this.createdAt = createdAt;
    }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet(jsonObject.getString("text"),
                jsonObject.getLong("id"),
                jsonObject.getString("created_at"));
        return tweet;
    }

    public Tweet() {

    }


}
