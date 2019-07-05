package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    //list out the attributes that I want to store
    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public int likes;
    public int retweets;

    //deserialize the data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract all of the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid=jsonObject.getLong("id");
        tweet.createdAt=jsonObject.getString("created_at");
        tweet.user=User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.likes=jsonObject.getInt("favorite_count");
        tweet.retweets=jsonObject.getInt("retweet_count");
        return tweet;
    }

    public Long getPostId() {
        return uid;
    }

}
