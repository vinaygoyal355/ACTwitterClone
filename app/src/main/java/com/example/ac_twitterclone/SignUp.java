package com.example.ac_twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import static java.lang.System.exit;


public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp,btnLogIn;
    private EditText edtUserName,edtPassword,edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Sign Up");

        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        btnLogIn=findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(0);
            }
        });

        edtUserName=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnSignUp);

                }

                return false;
            }
        });

        edtEmail=findViewById(R.id.edtEmail);

        if(ParseUser.getCurrentUser() != null){
            //  ParseUser.getCurrentUser().logOut();
            transtiontoSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==btnSignUp.getId()){

            if(edtUserName.getText().toString().equals("") || edtPassword.getText().toString() .equals("") || edtEmail.getText().toString().equals("")) {

                FancyToast.makeText(SignUp.this,"UserName, Email and Password are Required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            }

            else{

                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(edtUserName.getText().toString());
                parseUser.setPassword(edtPassword.getText().toString());
                parseUser.setEmail(edtEmail.getText().toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing up " + edtUserName.getText().toString());
                progressDialog.show();

                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            FancyToast.makeText(SignUp.this, edtUserName.getText().toString() + " is Signed Up Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            edtUserName.setText("");
                            edtPassword.setText("");
                            edtEmail.setText("");
                            progressDialog.dismiss();
                            transtiontoSocialMediaActivity();

                        } else {

                            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            progressDialog.dismiss();

                        }
                    }
                });

            }

        }

        else if(view.getId()==btnLogIn.getId()){

            Intent A=new Intent(SignUp.this,LoginActivity.class);
            startActivity(A);
            finish();

        }

    }

    public void rootLayoutIsTapped(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transtiontoSocialMediaActivity(){

        Intent a=new Intent(SignUp.this,TwitterUsers.class);
        startActivity(a);
        finish();

    }

}
