package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    private List<Tweet> tweets;
    private RecyclerView rvTweets;
    private FloatingActionButton fab;
    private EndlessRecyclerViewScrollListener scrollListener;
    private long maxId = 0;

    public final static int COMPOSE_TWEET_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // TODO: this may break
        client = TwitterApp.getRestClient(this);

        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //rvTweets.addOnScrollListener();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }


        };

        rvTweets.addOnScrollListener(scrollListener);


        tweets = new ArrayList<>();

        tweetAdapter = new TweetAdapter(tweets);

        rvTweets.setLayoutManager(layoutManager);

        rvTweets.setAdapter(tweetAdapter);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeMessage();
            }
        });


        //getSupportActionBar().icon

        populateTimeline(maxId);
    }


    public void loadNextDataFromApi(int offset) {
        maxId = tweets.get(tweets.size() - 1).uid;
        // Send an API request to retrieve appropriate paginated data
        populateTimeline(maxId);
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }















    private void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Twitter Client", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Twitter Client", response.toString());
                try {
                    //iterate through JSON array
                    for (int i = 0; i < response.length(); i++) {
                        // for each entry, deserialize JSON object
                        // convert each object to Tweet model
                        // add that tweet model to data source
                        tweets.add(Tweet.fromJSON(response.getJSONObject(i)));
                        // notify adapter that we've added an item
                        tweetAdapter.notifyItemChanged(tweets.size() - 1);
                    }


                } catch (JSONException jsonException) {
                    Log.e("Twitter Error", "Error parsing JSON tweet.");
                    jsonException.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter Client", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Too many Twitter API requests", Toast.LENGTH_LONG).show();
                Log.d("Twitter Client", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("Twitter Client", errorResponse.toString());
                throwable.printStackTrace();
            }
        });

    }

    private void composeMessage() {
        Intent composeTweet = new Intent(this, ComposeActivity.class);
        startActivityForResult(composeTweet, COMPOSE_TWEET_REQUEST_CODE);
        //onActivityResult(COMPOSE_TWEET_REQUEST_CODE, -1 ,composeTweet);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            //extract message
            Tweet response = (Tweet) data.getExtras().get("newTweet");
            tweets.add(0, response);
            tweetAdapter.notifyItemChanged(0);
            Toast.makeText(getApplicationContext(), "Result Ok response", Toast.LENGTH_LONG).show();

            // TODO: finish updating list.
        } else {
            Toast.makeText(getApplicationContext(), "Error sending tweet, OnActivityResult", Toast.LENGTH_LONG).show();
        }
    }
}
