package com.exemple.eatmore;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

public class AuthenticationActivity extends AppCompatActivity {

    protected static User user;
    private ConstraintLayout forum;
    private ConstraintLayout signUpView, loginView;
    private TextView loginViewButton, signUpViewButton;
    private MaterialButton loginButton, signUpButton;
    private boolean logsign = true;
    private EditText signUpEmail, signUpPassword, logInEmail, logInPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseUtils.isCurrentUserConnected()) {
            openMainPage();
        }

        setContentView(R.layout.activity_authentication);

        forum = findViewById(R.id.infos_view);
        signUpView = findViewById(R.id.signup_view);
        loginView = findViewById(R.id.login_view);
        loginViewButton = findViewById(R.id.login_page_button);
        signUpViewButton = findViewById(R.id.signup_page_button);
        loginButton = findViewById(R.id.login_button);
        logInEmail = findViewById(R.id.login_email_edit_text);
        logInPassword = findViewById(R.id.login_password_edit_text);

        loginViewButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!logsign) {
                    switchUI(forum, R.layout.login_view, signUpViewButton, loginViewButton);
                }
            }
        });

        signUpViewButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (logsign) {
                    switchUI(forum, R.layout.signup_view, loginViewButton, signUpViewButton);
                    signUpButton = findViewById(R.id.signup_button);
                    signUpEmail = findViewById(R.id.signup_email_edit_text);
                    signUpPassword = findViewById(R.id.signup_password_edit_text);
                    signUpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUtils.signUp(AuthenticationActivity.this, signUpEmail.getText().toString().trim(), signUpPassword.getText().toString());
                            openMainPage();
                        }
                    });

                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.logIn(logInEmail.getText().toString().trim(), logInPassword.getText().toString());
                openMainPage();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void switchUI(ConstraintLayout c, int id, TextView t1, TextView t2) {
        logsign = !logsign;

        t1.setTextColor(getResources().getColor(R.color.pink));
        t1.setTypeface(getResources().getFont(R.font.poppins), Typeface.NORMAL);
        t1.setBackgroundColor(Color.TRANSPARENT);

        t2.setTextColor(getResources().getColor(R.color.white));
        t2.setTypeface(getResources().getFont(R.font.poppins_semibold), Typeface.BOLD);
        t2.setBackground(getDrawable(R.drawable.pink_rectangle));

        c.removeAllViews();
        LayoutInflater.from(AuthenticationActivity.this).inflate(id, c, true);

    }

    private void openMainPage() {
        finish();
        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
    }

}
