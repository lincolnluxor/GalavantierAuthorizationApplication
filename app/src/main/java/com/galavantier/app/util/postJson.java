package com.galavantier.app.util;

import android.util.Log;

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
public class postJson {
    String loginPostLink = "http://dev-mlg.gotpantheon.com/api/mglUser/login.json";
    public postJson(String usernameInputString, String passwordInputString) {
        JSONObject json = new JSONObject();
        JSONObject userInfo = new JSONObject();
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
            post.addHeader("Content-Type","application/json");
            StringEntity content = new StringEntity(json.toString());
            post.setEntity(content);
            HttpResponse response = client.execute(post);
            //InputStream is = response.getEntity().getContent();
            //String result = convertStreamToString(is);
            Log.i("content: ", content.toString());
        } catch (Exception e) {
            Log.i("log_tag", "Error: " + e.toString());
        }
    }
}
