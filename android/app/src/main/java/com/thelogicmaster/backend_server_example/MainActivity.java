package com.thelogicmaster.backend_server_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String baseURL = "https://example.thelogicmaster.com/";
        final String userAuth = "basic " + Base64.encodeToString("user:1234".getBytes(), Base64.NO_WRAP);
        final String user1Auth = "basic " + Base64.encodeToString("user1:12345678".getBytes(), Base64.NO_WRAP);

        RequestQueue queue = Volley.newRequestQueue(this);

        AuthStringRequest loginRequest1 = new AuthStringRequest(Request.Method.GET, baseURL + "login",
                response -> Log.i("loginRequest1", response),
                error -> Log.e("loginRequest1", "Failed to login", error), user1Auth);
        queue.add(loginRequest1);

        StringRequest signupRequest1 = new StringRequest(Request.Method.GET, baseURL + "signup?username=user&password=1234",
                response -> Log.i("signupRequest1", response),
                error -> Log.e("signupRequest1", "Failed to signup", error));
        queue.add(signupRequest1);

        StringRequest signupRequest2 = new StringRequest(Request.Method.GET, baseURL + "signup?username=user1&password=1234",
                response -> Log.i("signupRequest2", response),
                error -> Log.e("signupRequest2", "Failed to signup", error));
        queue.add(signupRequest2);

        StringRequest signupRequest3 = new StringRequest(Request.Method.GET, baseURL + "signup?username=user1&password=12345678",
                response -> Log.i("signupRequest3", response),
                error -> Log.e("signupRequest3", "Failed to signup", error));
        queue.add(signupRequest3);

        AuthStringRequest loginRequest2 = new AuthStringRequest(Request.Method.GET, baseURL + "login",
                response -> Log.i("loginRequest2", response),
                error -> Log.e("loginRequest2", "Failed to login", error), user1Auth);
        queue.add(loginRequest2);

        AuthStringRequest loginRequest3 = new AuthStringRequest(Request.Method.GET, baseURL + "login",
                response -> Log.i("loginRequest3", response),
                error -> Log.e("loginRequest3", "Failed to login", error), userAuth);
        queue.add(loginRequest3);

        AuthStringRequest postRequest = new AuthStringRequest(Request.Method.POST, baseURL + "post",
                response -> Log.i("postRequest", response),
                error -> Log.e("postRequest", "Failed to post message", error), userAuth) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return "Hi".getBytes("utf-8");
                } catch (UnsupportedEncodingException ignored) {}
                return null;
            }
        };
        queue.add(postRequest);

        AuthJsonRequest messagesRequest = new AuthJsonRequest(Request.Method.GET, baseURL + "messages", null,
                response -> Log.i("messagesRequest", response.toString()),
                error -> Log.e("messagesRequest", "Failed to get messages", error), userAuth);
        queue.add(messagesRequest);
    }
}