package com.galavantier.app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends Activity {
    EditText usernameInputText;
    EditText passwordInputText;
    EditText loginErrorText;


    String loginPostLink = "http://dev-mgl.gotpantheon.com/api/user/login.json";
    String registerPostLink = "http://dev-mgl.gotpantheon.com/api/mglUser/register.json";

    boolean goodEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_in_activity);
        loginErrorText = (EditText) findViewById(R.id.login_text);
        loginErrorText.setFocusable(false);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        Button createAccountSwitchButton = (Button) findViewById(R.id.create_account_switch_button);

        createAccountSwitchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    //requestWindowFeature(Window.FEATURE_NO_TITLE);
                    setContentView(R.layout.create_account_activity);
                    loginErrorText = (EditText) findViewById(R.id.login_text);
                    loginErrorText.setFocusable(false);
                    Button createAccountButton = (Button) findViewById(R.id.create_account_button);

                    createAccountButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // getting edittext info
                            usernameInputText = (EditText) findViewById(R.id.username_input);
                            passwordInputText = (EditText) findViewById(R.id.password_input);
                            EditText emailInputText = (EditText) findViewById(R.id.email_input);
                            EditText fnameInputText = (EditText) findViewById(R.id.fname_input);
                            EditText lnameInputText = (EditText) findViewById(R.id.lname_input);
                            EditText passwordReenterInputText = (EditText) findViewById(R.id.password_reenter_input);

                            // transform to string
                            // nulls are ok... checking for that later
                            String usernameInputString = usernameInputText.getText().toString();
                            String passwordInputString = passwordInputText.getText().toString();
                            String passwordReenterInputString = passwordReenterInputText.getText().toString();
                            String emailInputString = emailInputText.getText().toString();
                            String fnameInputString = fnameInputText.getText().toString();
                            String lnameInputString = lnameInputText.getText().toString();

                            //This shows up as an error, but the manifest specifies to use SDK Version 8, which would clear the error
                            //Check the email against a pattern matcher
                            checkEmail(emailInputString);

                            if (usernameInputString.length() == 0) { //is username filled in?
                                loginErrorText.setText("Username is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                usernameInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (!passwordInputString.equals(passwordReenterInputString)) { //do the passwords match?
                                loginErrorText.setText("Passwords don't match");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                passwordInputText.setBackgroundColor(0xff7D0B0B);
                                passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (passwordInputString.length() == 0 || passwordReenterInputString.length() == 0) { //are the passwords filled in?
                                loginErrorText.setText("Password is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                passwordInputText.setBackgroundColor(0xff7D0B0B);
                                passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (emailInputString.length() == 0) { //is email filled in?
                                loginErrorText.setText("Email is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                emailInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (!goodEmail) { //did the email pass the pattern matcher?
                                loginErrorText.setText("Email is not valid");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                emailInputText.setBackgroundColor(0xff7D0B0B);
                            } else { //try to create the account
                                loginErrorText.setText("Create An Account");
                                loginErrorText.setBackgroundColor(0xff3399cc);
                                new postCreateAccountJson(loginErrorText, registerPostLink, usernameInputString, passwordInputString, emailInputString, fnameInputString, lnameInputString);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("error_tag", e.toString());
                }

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    // getting edittext info
                    usernameInputText = (EditText) findViewById(R.id.username_input);
                    passwordInputText = (EditText) findViewById(R.id.password_input);

                    // transform to string
                    String usernameInputString = usernameInputText.getText().toString();
                    String passwordInputString = passwordInputText.getText().toString();

                    if (usernameInputString.length() == 0 && passwordInputString.length() == 0) { //is anything filled in?
                        loginErrorText.setText("Username and Password are required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else if (usernameInputString.length() == 0) { //is username filled in?
                        loginErrorText.setText("Username is required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else if (passwordInputString.length() == 0) { //is password filled in?
                        loginErrorText.setText("Password is required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else { //try to log in
                        loginErrorText.setText("Login"); //reset view
                        loginErrorText.setBackgroundColor(0xff3399cc); //reset view
                        new postSignInJson(loginErrorText, loginPostLink, usernameInputString, passwordInputString);
                    }
                } catch (Exception e) {
                    Log.i("error_tag", e.toString());
                }
            }
        });
    }

    public boolean checkEmail(String emailInputString) {
        if (Build.VERSION.SDK_INT >= 8) {
            // check email against the premade android email match API
            goodEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInputString).matches();
        } else {
            // check email against a bad reg-ex
            Pattern pattern;
            Matcher matcher;
            String EMAIL_PATTERN = "^[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(emailInputString);
            if (matcher.matches()) {
                goodEmail = true;
            }
        }
        return goodEmail;
    }
}
