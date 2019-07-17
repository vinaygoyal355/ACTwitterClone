package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtSendTweet;
    Button btnSendTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        setTitle("Send A Tweet");

        edtSendTweet=findViewById(R.id.edtSendTweet);
        btnSendTweet=findViewById(R.id.btnSendTweet);

        btnSendTweet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==btnSendTweet.getId()){

            ParseObject parseObject=new ParseObject("MyTweet");
            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
            parseObject.put("tweet",edtSendTweet.getText().toString());

            final ProgressDialog progressDialog=new ProgressDialog(SendTweetActivity.this);
            progressDialog.setMessage("Sending Tweet...");
            progressDialog.show();

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if(e == null){
                        FancyToast.makeText(getApplicationContext(),
                                ParseUser.getCurrentUser().getUsername()+" tweet( "+edtSendTweet.getText().toString()+" ) is saved"
                                ,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    }
                    else {
                        FancyToast.makeText(getApplicationContext(),"Unknown Error: "+e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                    progressDialog.dismiss();
                }
            });

        }

    }
}
