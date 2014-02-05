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
public class postCreateAccountJson {
    public postCreateAccountJson(final String registerPostLink, final String usernameInputString, final String passwordInputString, final String emailInputString, final String fnameInputString, final String lnameInputString) {
        Thread t1 = new Thread() { //Network connections can't run in the main thread
            public void run() {
                Looper.prepare();
                //JSONObject json = new JSONObject(); //json containing header and user info json
                JSONObject userInfo = new JSONObject(); //json containing the user info only
                try {
                    userInfo.put("username",usernameInputString);
                    userInfo.put("pass",passwordInputString); //unsure as to why this is a different than "password" which is required by sign in
                    userInfo.put("mail",emailInputString);
                    if (fnameInputString != null) {
                        userInfo.put("first_name",fnameInputString);
                    }
                    if (lnameInputString != null) {
                        userInfo.put("last_name",lnameInputString);
                    }

                    String preJson = userInfo.toString();
                    //Log.i("preJsonStringWithSlash_tag", preJson);
                    String preJsonWithForm = "{\"form_values\":" + preJson + "}";
                    Log.i("preJsonStringWithOutSlash_tag", preJsonWithForm);

                    JSONObject json = new JSONObject(preJsonWithForm); //json containing header and user info json

                    //json.put("form_values", userInfo.toString());

                    int timeOut = 10000; // 10 seconds
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, timeOut);
                    HttpConnectionParams.setSoTimeout(params, timeOut);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(registerPostLink);
                    StringEntity content = new StringEntity(json.toString());
                    //StringEntity content = new StringEntity(userInfo.toString());
                    //content.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")); //option 1
                    post.setHeader("Content-type","application/json"); //option 2
                    //post.addHeader("Content-Type","application/json"); //option 3
                    post.setEntity(content);
                    Log.i("json_tag", json.toString());
                    //Log.i("json_tag", userInfo.toString());

                    HttpResponse response = client.execute(post);
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
