package com.example.task8_1c;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private Context context;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.robot_msg, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.isUser()) {
            holder.userMsgTextView.setText(message.getText());
        } else {
            holder.robotMsgTextView.setText(message.getText());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isUser() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMsgTextView;
        TextView robotMsgTextView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userMsgTextView = itemView.findViewById(R.id.userMsgTextView);
            robotMsgTextView = itemView.findViewById(R.id.robotMsgTextView);
        }
    }
}