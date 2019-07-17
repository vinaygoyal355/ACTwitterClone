package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayList;
    private String followeduser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        listView=findViewById(R.id.my_ListView);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        FancyToast.makeText(getApplicationContext(),"Welcome: "+ ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

        final ProgressDialog progressDialog=new ProgressDialog(TwitterUsers.this);
        progressDialog.setMessage("Loading User...");
        progressDialog.show();

        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){

                    if(objects.size()>0){

                        for(ParseUser user:objects){
                            arrayList.add(user.getUsername());
                        }

                        listView.setAdapter(arrayAdapter);
                        for (String twitterUsers: arrayList){
                            if(ParseUser.getCurrentUser().getList("fanOf")!= null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUsers)) {
                                    followeduser=followeduser +twitterUsers+"\n";
                                    listView.setItemChecked(arrayList.indexOf(twitterUsers), true);
                                    FancyToast.makeText(getApplicationContext(),ParseUser.getCurrentUser().getUsername()+" is following "+followeduser,FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                                }
                            }

                        }
                        progressDialog.dismiss();

                    }
                    else{
                        FancyToast.makeText(getApplicationContext(),"There are no users than current user",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    }
                }
                else{
                    FancyToast.makeText(getApplicationContext(),"Unknown Error: "+e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logOut){

            ParseUser.getCurrentUser().logOut();
            Intent A=new Intent(TwitterUsers.this,SignUp.class);
            startActivity(A);
            finish();
        }

        else if(item.getItemId()==R.id.tweeticon){

            Intent A=new Intent(TwitterUsers.this,SendTweetActivity.class);
            startActivity(A);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        CheckedTextView checkedTextView= (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            FancyToast.makeText(getApplicationContext(),arrayList.get(position)+" is now followed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        }
        else{
            FancyToast.makeText(getApplicationContext(),arrayList.get(position)+" is now unfollowed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserfanOfList=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");

            ParseUser.getCurrentUser().put("fanOf",currentUserfanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(getApplicationContext(),"Saved!!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
                else{
                    FancyToast.makeText(getApplicationContext(),"Unknown Error: "+e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }

            }
        });

    }
}
