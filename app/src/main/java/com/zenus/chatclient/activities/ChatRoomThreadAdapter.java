package com.zenus.chatclient.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zenus.chatclient.AppApplication;
import com.zenus.chatclient.R;
import com.zenus.chatclient.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatRoomThreadAdapter extends BaseAdapter {
    private String userId;
    private int SELF = 1000;

    private ArrayList<ChatMessage> messageArrayList;

    public ChatRoomThreadAdapter(Context mContext, ArrayList<ChatMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
        this.userId = AppApplication.getInstance().getUserLoginName();
    }

    @Override
    public int getCount() {
        return messageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (getItemViewType(position) == SELF) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
        }

        TextView message, timestamp;
        message = (TextView) view.findViewById(R.id.message);
        timestamp = (TextView) view.findViewById(R.id.timestamp);

        ChatMessage chatMessage = messageArrayList.get(position);

        message.setText(chatMessage.getMessage());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(chatMessage.getCreateDate());
        String timestampText = chatMessage.getUserName() + ", " + formattedDate;
        timestamp.setText(timestampText);

        return view;
    }

    public int getItemViewType(int position) {
        ChatMessage message = messageArrayList.get(position);
        if (message.getUserName().equals(userId)) {
            return SELF;
        }

        return position;
    }
}

