package com.galavantier.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.galavantier.app.util.SystemUiHider;
import com.galavantier.app.postSignInJson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.galavantier.app.util.SystemUiHider
 */
public class SignInActivity extends Activity {
    EditText usernameInputText;
    EditText passwordInputText;
    //EditText passwordReenterInputText;
    EditText loginErrorText;
    public static int code;

    String loginPostLink = "http://dev-mgl.gotpantheon.com/api/user/login.json";
    String registerPostLink = "http://dev-mgl.gotpantheon.com/api/mglUser/register.json";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link com.galavantier.app.util.SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link com.galavantier.app.util.SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_activity);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        Button createAccountSwitchButton = (Button) findViewById(R.id.create_account_switch_button);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.create_account_switch_button).setOnTouchListener(mDelayHideTouchListener);

        createAccountSwitchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    setContentView(R.layout.create_account_activity);
                    Button createAccountButton = (Button) findViewById(R.id.create_account_button);

                    createAccountButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            /*
                            if (R.id.create_account_activity == view.getId()) {

                            }
                            */
                            // getting edittext info
                            usernameInputText = (EditText) findViewById(R.id.username_input);
                            passwordInputText = (EditText) findViewById(R.id.password_input);
                            EditText emailInputText = (EditText) findViewById(R.id.email_input);
                            EditText fnameInputText = (EditText) findViewById(R.id.fname_input);
                            EditText lnameInputText = (EditText) findViewById(R.id.lname_input);
                            EditText passwordReenterInputText = (EditText) findViewById(R.id.password_reenter_input);
                            loginErrorText = (EditText) findViewById(R.id.login_text);

                            //the password fields are set to regular text to ensure the look of the font is the same.
                            //this will change the fields to still act like a normal password field (mask characters)
                            passwordInputText.setTypeface(Typeface.DEFAULT); //Not working!
                            passwordInputText.setTransformationMethod(new PasswordTransformationMethod());
                            passwordReenterInputText.setTypeface(Typeface.DEFAULT); //Not working!
                            passwordReenterInputText.setTransformationMethod(new PasswordTransformationMethod());

                            // transform to string
                            String usernameInputString = usernameInputText.getText().toString();
                            String passwordInputString = passwordInputText.getText().toString();
                            String passwordReenterInputString = passwordReenterInputText.getText().toString();
                            String emailInputString = emailInputText.getText().toString();
                            String fnameInputString = fnameInputText.getText().toString();
                            String lnameInputString = lnameInputText.getText().toString();
                            boolean goodEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailInputString).matches();
                            if (usernameInputString.length() == 0) {
                                loginErrorText.setText("Username is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                usernameInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (!passwordInputString.equals(passwordReenterInputString)) {
                                loginErrorText.setText("Passwords don't match");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                passwordInputText.setBackgroundColor(0xff7D0B0B);
                                passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (passwordInputString.length() == 0 || passwordReenterInputString.length() == 0) {
                                loginErrorText.setText("Password is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                passwordInputText.setBackgroundColor(0xff7D0B0B);
                                passwordReenterInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (emailInputString.length() == 0) {
                                loginErrorText.setText("Email is required");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                emailInputText.setBackgroundColor(0xff7D0B0B);
                            } else if (!goodEmail) {
                                loginErrorText.setText("Email is not valid");
                                loginErrorText.setBackgroundColor(0xffff0000);
                                emailInputText.setBackgroundColor(0xff7D0B0B);
                            } else {
                                loginErrorText.setText("Create An Account");
                                loginErrorText.setBackgroundColor(0xff3399cc);
                                new postCreateAccountJson(registerPostLink, usernameInputString, passwordInputString, emailInputString, fnameInputString, lnameInputString);
                                // Proceed
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
                    loginErrorText = (EditText) findViewById(R.id.login_text);

                    // transform to string
                    String usernameInputString = usernameInputText.getText().toString();
                    String passwordInputString = passwordInputText.getText().toString();

                    //the password fields are set to regular text to ensure the look of the font is the same.
                    //this will change the fields to still act like a normal password field (mask characters)
                    passwordInputText.setTypeface(Typeface.DEFAULT); //Not working!
                    passwordInputText.setTransformationMethod(new PasswordTransformationMethod());

                    // check to see if either username or password is missing
                    if (usernameInputString.length() == 0 && passwordInputString.length() == 0) {
                        loginErrorText.setText("Username and Password are required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else if (usernameInputString.length() == 0) {
                        loginErrorText.setText("Username is required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else if (passwordInputString.length() == 0) {
                        loginErrorText.setText("Password is required");
                        loginErrorText.setBackgroundColor(0xffff0000);
                    } else {
                        loginErrorText.setText("Login");
                        loginErrorText.setBackgroundColor(0xff3399cc);
                        new postSignInJson(loginPostLink, usernameInputString, passwordInputString);

                        if (code < 300 && code > 0){
                            setContentView(R.layout.result_activity);
                        }

                        //proceed
                    }
                } catch (Exception e) {
                    Log.i("error_tag", e.toString());
                }
            }
        });
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}
