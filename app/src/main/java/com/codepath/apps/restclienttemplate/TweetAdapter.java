package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    //for each row, inflate the layout and cache references into ViewHolder

    //invoked when I need to create a new row otherwise will call the onBindViewHolder method
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView=inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder= new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of that element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the data according to position
        Tweet tweet = mTweets.get(i);

        //populate the views according to this data
        viewHolder.tvUsername.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);

        Glide.with(context).load(tweet.user.profileImageUrl).into(viewHolder.ivProfileImage);
    }


    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBody=itemView.findViewById(R.id.tvBody);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

}
