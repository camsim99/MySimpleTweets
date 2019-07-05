package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private EditText currTweet;
    private TextView charCount;
    //private TextWatcher characterCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose); //attach things to your layout

        currTweet = findViewById(R.id.currTweet);
        charCount = findViewById(R.id.charCount);
        currTweet.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //None
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //charCount = findViewById(R.id.charCount);
                int charsLeft = 280-s.length();
                if (charsLeft >= 0) {
                    charCount.setText(String.valueOf(charsLeft));
                }
                else {
                    charCount.setText("Your tweete will be cut off!");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //None
            }
        });

        //currTweet.addTextChangedListener(characterCounter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    //configure the button such that when clicked, a network request should be sent to the statuses/update endpoint
    public void onClick(View view) {
        //This gets the Twitter client we need to send the network request
        TwitterClient client = TwitterApplication.getRestClient(this);

        String tweetMessage= currTweet.getText().toString();

        //let's try tweeting! (network request)
        client.sendTweet(tweetMessage, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //create intent to pass the tweet back
                Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);

                //construct a new tweet
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    i.putExtra("currTweet", Parcels.wrap(tweet));
                    //start the activity
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }
}
