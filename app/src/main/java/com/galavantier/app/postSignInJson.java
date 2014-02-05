package com.galavantier.app;

import android.os.Looper;
import android.util.Log;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Tate on 2/3/14.
 */
public class postSignInJson {
    public postSignInJson(final String loginPostLink, final String usernameInputString, final String passwordInputString) {
        Thread t1 = new Thread() { //Network connections can't run in the main thread
            public void run() {
                Looper.prepare();
                //JSONObject json = new JSONObject(); //json containing header and user info json
                JSONObject userInfo = new JSONObject(); //json containing the user info only
                try {
                    userInfo.put("username",usernameInputString);
                    userInfo.put("password",passwordInputString);
                    //json.put("form_values",userInfo.toString());
                    int timeOut = 10000; // 10 seconds
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, timeOut);
                    HttpConnectionParams.setSoTimeout(params,timeOut);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(loginPostLink);
                    StringEntity content = new StringEntity(userInfo.toString());
                    //StringEntity content = new StringEntity(json.toString());
                    //content.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")); //option 1
                    post.setHeader("Content-type","application/json"); //option 2
                    //post.addHeader("Content-Type","application/json"); //option 3
                    post.setEntity(content);
                    HttpResponse response = client.execute(post);
                    Log.i("json_tag", userInfo.toString());
                    //Log.i("json_tag", json.toString());
                    if(response != null) {
                        SignInActivity.code = response.getStatusLine().getStatusCode();
                        Log.i("code_tag",Integer.toString(SignInActivity.code));
                    } else {
                        Log.i("no_response_tag", "");
                    }
                } catch (Exception e) {
                    Log.i("error_tag", e.toString());
                }
                Looper.loop();
            }
        };
        t1.start();
    }
}
