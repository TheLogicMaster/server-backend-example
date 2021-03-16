package com.thelogicmaster.backend_server_example;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class MessagesFragment extends Fragment {

    private RequestQueue queue;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private MessageRecyclerViewAdapter recyclerAdapter;
    private EditText messageEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        queue = Volley.newRequestQueue(requireContext());
        swipeRefresh = view.findViewById(R.id.refresh);
        swipeRefresh.setOnRefreshListener(this::refresh);
        messageEdit = view.findViewById(R.id.message);
        Button sendButton = view.findViewById(R.id.send);

        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(s.length() != 0);
            }
        });

        sendButton.setOnClickListener(v -> sendMessage());

        messageEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && messageEdit.length() > 0) {
                sendMessage();
                return true;
            }
            return false;
        });

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerAdapter = new MessageRecyclerViewAdapter(Collections.emptyList());
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        queue.stop();
        swipeRefresh.setRefreshing(false);
    }

    private void sendMessage() {
        String message = messageEdit.getText().toString();
        queue.add(new AuthStringRequest(Request.Method.POST, Helpers.BASE_URL + "post",
                response -> {
                    messageEdit.setText("");
                    refresh();
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to send message", Toast.LENGTH_LONG).show();
                    Log.e("postRequest", "Failed to post message", error)
                    ;}, Helpers.getAuth(requireActivity())) {
            @Override
            public byte[] getBody() {
                try {
                    return message.getBytes("utf-8");
                } catch (UnsupportedEncodingException ignored) {}
                return null;
            }
        });
    }

    private void refresh() {
        queue.start();
        queue.add(new AuthJsonRequest(Request.Method.GET, Helpers.BASE_URL + "messages", null,
                response -> {
                    try {
                        JSONArray messageArray = response.getJSONArray("messages");
                        ArrayList<Message> messages = new ArrayList<>();
                        for (int i = 0; i < messageArray.length(); i++) {
                            JSONObject messageObj = messageArray.getJSONObject(i);
                            messages.add(new Message(messageObj.getString("user"), messageObj.getString("message")));
                        }
                        recyclerAdapter.setMessages(messages);
                        recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
                    } catch (JSONException e) {
                        Log.e("Messages", "Failed to parse messages", e);
                    }
                    swipeRefresh.setRefreshing(false);
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to retrieve messages", Toast.LENGTH_LONG).show();
                    Log.e("messagesRequest", "Failed to get messages", error);
                    swipeRefresh.setRefreshing(false);
                }, Helpers.getAuth(requireActivity())));
    }
}