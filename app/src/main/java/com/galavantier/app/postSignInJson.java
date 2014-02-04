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
import org.json.JSONObject;

/**
 * Created by Tate on 2/3/14.
 */
public class postSignInJson {
    public postSignInJson(final String loginPostLink, final String usernameInputString, final String passwordInputString) {
        Thread t1 = new Thread() { //Network connections can't run in the main thread
            public void run() {
                Looper.prepare();
                JSONObject json = new JSONObject(); //json containing header and user info json
                JSONObject userInfo = new JSONObject(); //json containing the user info only
                try {
                    userInfo.put("username",usernameInputString);
                    userInfo.put("password",passwordInputString);
                    json.put("form_values",userInfo.toString());
                    int timeOut = 10000; // 10 seconds
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, timeOut);
                    HttpConnectionParams.setSoTimeout(params,timeOut);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpPost post = new HttpPost(loginPostLink);
                    //post.addHeader("Content-Type","application/json");
                    StringEntity content = new StringEntity(json.toString());
                    content.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(content);
                    HttpResponse response = client.execute(post);
                    //InputStream is = response.getEntity().getContent();
                    //String result = convertStreamToString(is);
                    if(response != null) {
                        Log.i("Response: ", response.toString());
                    } else {
                        Log.i("No response", "");
                    }
                } catch (Exception e) {
                    //Log.i("log_tag", "Error: " + e.toString());
                    Log.i("log_tag", "Error: ", e);
                }
                Looper.loop();
            }
        };
        t1.start();
    }
}
