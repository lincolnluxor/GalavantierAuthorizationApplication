package com.galavantier.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.galavantier.app.util.SystemUiHider;

public class CreateAccountActivity extends Activity {
    //EditText usernameInputText;
    //EditText passwordInputText;
    //EditText passwordReenterInputText;
    //EditText loginErrorText;

    public CreateAccountActivity (View view) {
        setContentView(R.layout.create_account_activity);
        Button createAccountButton = (Button) findViewById(R.id.create_account_button);
        /*
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            */
                // getting edittext info
                EditText usernameInputText = (EditText) findViewById(R.id.username_input);
                EditText passwordInputText = (EditText) findViewById(R.id.password_input);
                EditText passwordReenterInputText = (EditText) findViewById(R.id.password_reenter_input);
                EditText loginErrorText = (EditText) findViewById(R.id.login_text);

                // transform to string
                String usernameInputString = usernameInputText.getText().toString();
                String passwordInputString = passwordInputText.getText().toString();
                String passwordReenterInputString = passwordReenterInputText.getText().toString();

                if (passwordInputString != passwordReenterInputString) {
                    loginErrorText.setText("Passwords don't match");
                    loginErrorText.setBackgroundColor(0xffff0000);
                    passwordInputText.setBackgroundColor(0xff7D0B0B);
                    passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                } else if (usernameInputString.length() == 0) {
                    loginErrorText.setText("Username is required");
                    loginErrorText.setBackgroundColor(0xffff0000);
                    usernameInputText.setBackgroundColor(0xff7D0B0B);
                } else if (passwordInputString.length() == 0 || passwordReenterInputString.length() == 0) {
                    loginErrorText.setText("Password is required");
                    loginErrorText.setBackgroundColor(0xffff0000);
                    passwordInputText.setBackgroundColor(0xff7D0B0B);
                    passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                } else {
                    // Proceed
                }
        /*
            }
        });
        */

    }
}
