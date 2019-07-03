package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.scribejava.apis.TwitterApi;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    EditText etMessage;
    Button btSend;
    TextView tvCharCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        tvCharCount = findViewById(R.id.tvCharCount);;


        etMessage.addTextChangedListener(new TextWatcher() {
            int lengthOfBox = etMessage.getText().length();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getApplicationContext(),"entered: "+s, Toast.LENGTH_LONG).show();
                if (s.length() > 280){
                    etMessage.setText(etMessage.getText().toString().substring(0,281));
                }
                tvCharCount.setText(s.length()+"/280");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sendTweet(){


        //grab text from textInput box
        //setResult();
        Intent backToTimeLine = new Intent(ComposeActivity.this, TimelineActivity.class);
        backToTimeLine.putExtra("messageString",  etMessage.getText().toString());
        finishActivity(RESULT_OK);
    }
}
