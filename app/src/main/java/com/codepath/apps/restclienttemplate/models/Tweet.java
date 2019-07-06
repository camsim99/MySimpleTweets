package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
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
    public String media;

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

        JSONObject entities =jsonObject.getJSONObject("entities");
        try {
            JSONArray media1 = entities.getJSONArray("media");
            JSONObject media2= media1.getJSONObject(0);
            tweet.media = media2.getString("media_url_https");

        }
        catch(Throwable e) {
            tweet.media="";
        }
        return tweet;
    }

    public Long getPostId() {
        return uid;
    }

}
