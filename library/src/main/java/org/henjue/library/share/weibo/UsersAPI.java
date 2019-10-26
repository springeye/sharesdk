package org.henjue.library.share.weibo;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UsersAPI {
    public interface UserRequestListener {
        void onGetUserInfoSuccess(JSONObject info, String token, long expiresTime);

        void onError(Throwable e);
    }

    private Context mContext;
    private String mSinaAppKey;
    private Oauth2AccessToken accessToken;

    public UsersAPI(Context mContext, String mSinaAppKey, Oauth2AccessToken accessToken) {
        this.mContext = mContext;
        this.mSinaAppKey = mSinaAppKey;
        this.accessToken = accessToken;
    }

    public void show(UsersAPI.UserRequestListener listener) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            String url = String.format("https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s&screen_name=%s", accessToken.getToken(), accessToken.getUid(), "");
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {


                inputStream = urlConnection.getInputStream();
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(in);
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                listener.onGetUserInfoSuccess(new JSONObject(sb.toString()), this.accessToken.getToken(), this.accessToken.getExpiresTime());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onError(e);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                listener.onError(e);
            }
        }

    }
}
