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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import static java.lang.System.exit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin,btnCancel,btnSignUp;
    private EditText edtEmail,edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");

        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent A=new Intent(LoginActivity.this, SignUp.class);
                startActivity(A);
                finish();
            }
        });

        edtEmail=findViewById(R.id.edtLoginEmail);
        edtPassword=findViewById(R.id.edtLoginPassword);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnLogin);

                }

                return false;
            }
        });

        btnLogin=findViewById(R.id.btnLogIn);



        btnLogin.setOnClickListener(this);

        btnCancel=findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(0);
            }
        });

    }

    public void rootLayoutIsTappedLogin(View view){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==btnLogin.getId()){

            if(edtEmail.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {

                FancyToast.makeText(LoginActivity.this,"Email And Password are Required", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

            }
            else{

                final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loging In");
                progressDialog.show();

                ParseUser.logInInBackground(edtEmail.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if (user != null && e == null) {

                            FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Signed Up Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            edtEmail.setText("");
                            edtPassword.setText("");
                            progressDialog.dismiss();
                            transtiontoSocialMediaActivity();

                        } else {

                            FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            progressDialog.dismiss();
                        }

                    }
                });

            }

        }

    }
    private void transtiontoSocialMediaActivity(){

        Intent a=new Intent(LoginActivity.this,TwitterUsers.class);
        startActivity(a);
        finish();
    }
}
