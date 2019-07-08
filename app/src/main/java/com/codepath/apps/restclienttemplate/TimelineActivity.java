package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swipeContainer;


    ArrayList<Tweet> tweets;
    // Normally this data should be encapsulated in ViewModels, but shown here for simplicity
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView rvTweets;
    Long max_id=0L; //the lowest id seen

    private final int REQUEST_CODE = 20;



    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetAdapter = new TweetAdapter();
        // Setup rest of TweetAdapter here (i.e. LayoutManager)



        // Initial page size to fetch can also be configured here too
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(20).build();

        // Pass in dependency
        TweetDataSourceFactory factory = new TweetDataSourceFactory(TwitterApplication.getRestClient(this));


        tweets = new LivePagedListBuilder(factory, config).build();

        tweets.observe(this, new Observer<PagedList<Tweet>>() {
            @Override
            public void onChanged(@Nullable PagedList<Tweet> tweets) {
                tweetAdapter.submitList(tweets);
            }
        });
    }*/

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient(this);

        //find the RecyclerView
        rvTweets = findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

       scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
           @Override
           public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               // Triggered only when new data needs to be appended to the list
               // Add whatever code is needed to append new items to the bottom of the list
               //loadNextDataFromApi(page);
               populateTimeline();
           }
       };
       // Adds the scroll listener to RecyclerView
       rvTweets.addOnScrollListener(scrollListener);

        populateTimeline();

       // Lookup the swipe container view
       swipeContainer = findViewById(R.id.swipeContainer);
       // Setup refresh listener which triggers new data loading
       swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               // Your code to refresh the list here.
               // Make sure you call swipeContainer.setRefreshing(false)
               // once the network request has completed successfully.
               fetchTimelineAsync();
           }
       });
       // Configure the refreshing colors
       swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
               android.R.color.holo_green_light,
               android.R.color.holo_orange_light,
               android.R.color.holo_red_light);
   }

    public void fetchTimelineAsync() {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        tweetAdapter.clear();
        populateTimeline();
        swipeContainer.setRefreshing(false);
    }



    //populates the toolbar
    //references the menu file itself
    //onPrepareOptionsMenu actually called before this then created then we deal with what happens when an icon is clicked (see below)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    //handles what happens when the compose icon is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        //can add switch statement for cases here if I want later
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
        return true;
    }

    //this is the method that is called after ComposeActivity pushes us back here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // check request code and result code first
        if (requestCode==REQUEST_CODE) {


            //// get the tweet and add it to the timeline
            Tweet tweet = data.getParcelableExtra("currTweet");

            //tweetAdapter.addMoreContacts((List<Tweet>) tweets);
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            // update based on adapter
            rvTweets.scrollToPosition(tweetAdapter.getItemCount() - 1);
        }
    }

    //TODO
    private void populateTimeline() {
        client.getHomeTimeline(max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                //iterator through the JSON array
                //for each entry, deserialize the JSON object
                for(int i = 0; i <response.length(); i++){

                    //convert each object to a Tweet model
                    //add that Tweet model to our data source
                    //notify the adapter that we've added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        if (max_id==0L || tweet.getPostId() < max_id) {
                            tweets.add(tweet);
                            tweetAdapter.notifyItemInserted(tweets.size() - 1);
                            max_id = tweet.getPostId();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
