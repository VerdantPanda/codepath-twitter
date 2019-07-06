package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.github.scribejava.apis.TwitterApi;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    EditText etMessage;
    Button btSend;
    TextView tvCharCount;
    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        tweet = new Tweet();

        client = TwitterApp.getRestClient(getApplicationContext());
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        tvCharCount = findViewById(R.id.tvCharCount);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTweet();
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMessage.getText().toString().length() >= 280){
                    tvCharCount.setText(String.valueOf(0));
                }
                if (etMessage.getText().toString().length() >= 200){
                    tvCharCount.setTextColor(Color.RED);
                }

                if (etMessage.getText().toString().length() == 199){
                    tvCharCount.setTextColor(Color.BLACK);
                }

                tvCharCount.setText(String.valueOf(280 - etMessage.getText().toString().length()));
                //TODO: finish text counter
                //if (cou)
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("Compose activity", "Text typed afterTextChanged()");
            }
        });

    }

    private void sendTweet() {

        client.sendTweet(etMessage.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject res = response;
                    tweet = Tweet.fromJSON(response);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        Intent backToTimeLine = new Intent(ComposeActivity.this, TimelineActivity.class);

        backToTimeLine.putExtra("newTweet", Parcels.wrap(tweet));

        setResult(RESULT_OK, backToTimeLine);

        finishActivity(RESULT_OK  );

        startActivity(backToTimeLine);


    }


}
