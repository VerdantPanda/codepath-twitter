package com.codepath.apps.restclienttemplate.models;

import androidx.versionedparcelable.ParcelField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public Entity entity;
    public boolean hasEntities = false; //TODO: stuff


    public Tweet(String body, long uid, String createdAt, User user) {
        this.body = body;
        this.uid = uid;
        this.createdAt = createdAt;
        this.user = user;


    }

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        JSONObject entityObject = jsonObject.getJSONObject("entities");
        if (entityObject.has("media")){
            JSONArray mediaEndpoint = entityObject.getJSONArray("media");
            if (mediaEndpoint != null && mediaEndpoint.length() != 0){
                tweet.entity = Entity.fromJSON(jsonObject.getJSONObject("entities"));
                tweet.hasEntities = true;
            }
        }

        return tweet;
    }

    public Tweet() {
    }


}
