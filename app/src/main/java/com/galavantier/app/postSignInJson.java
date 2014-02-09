package com.galavantier.app;

import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

/**
 * Created by Tate on 2/3/14.
 */
public class postSignInJson {
    int code;
    public postSignInJson(final EditText loginErrorText, final String loginPostLink, final String usernameInputString, final String passwordInputString) {
        Thread t1 = new Thread() { //Network connections can't run in the main thread
            public void run() {
                Looper.prepare();
                JSONObject userInfo = new JSONObject(); //json containing the user info only
                try {
                    //construct the userInfo JSON to be inserted into the final json
                    userInfo.put("username",usernameInputString);
                    userInfo.put("password",passwordInputString);
                    int timeOut = 10000; // 10 seconds til time out to not lock up system

                    //construct packet
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, timeOut);
                    HttpConnectionParams.setSoTimeout(params,timeOut);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(loginPostLink);
                    StringEntity content = new StringEntity(userInfo.toString());
                    post.setHeader("Content-type","application/json");
                    post.setEntity(content);

                    //send packet
                    HttpResponse response = client.execute(post);
                    if(response != null) {
                        //find and save the response code
                        code = response.getStatusLine().getStatusCode();
                        Log.i("json_tag",userInfo.toString());
                        Log.i("response_tag",Integer.toString(code));
                    } else {
                        //random int not associated with http response codes to show no server response
                        code = 9999;
                    }
                } catch (Exception e) {
                    Log.i("error_tag", e.toString());
                }
                Looper.loop();
            }
        };
        t1.start();
        //show the user the result
        if (code == 9999) {
            //no server response
            loginErrorText.setText("No server response. Try again.");
            loginErrorText.setBackgroundColor(0xffff0000);
        } else if (code == 200) {
            //logged in
            loginErrorText.setText("Successfully signed in!");
            loginErrorText.setBackgroundColor(0xff00ff00);
        } else {
            //not logged in
            loginErrorText.setText("Please try again");
            loginErrorText.setBackgroundColor(0xffff0000);
        }
    }
}
