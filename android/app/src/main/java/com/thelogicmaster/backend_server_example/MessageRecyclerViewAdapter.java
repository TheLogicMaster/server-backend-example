package com.thelogicmaster.backend_server_example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message}.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private List<Message> messages;

    public MessageRecyclerViewAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.message = messages.get(position);
        holder.usernameView.setText(messages.get(position).user);
        holder.messageView.setText(messages.get(position).message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView usernameView;
        public final TextView messageView;
        public Message message;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.usernameView = view.findViewById(R.id.username);
            this.messageView = view.findViewById(R.id.message);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + messageView.getText() + "'";
        }
    }
}