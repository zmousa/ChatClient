package com.zenus.chatclient.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zenus.chatclient.R;
import com.zenus.chatclient.controller.ChatController;
import com.zenus.chatclient.model.ChatRoom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatRoomsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ChatRoom> chatRoomArrayList;

    public ChatRoomsAdapter(Context mContext, ArrayList<ChatRoom> chatRoomArrayList) {
        this.mContext = mContext;
        this.chatRoomArrayList = chatRoomArrayList;
    }

    @Override
    public int getCount() {
        return chatRoomArrayList.size();
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
        View view=convertView;
        if(convertView==null)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_rooms_list_row, parent, false);

        TextView name, message, timestamp, count;

        name = (TextView) view.findViewById(R.id.name);
        message = (TextView) view.findViewById(R.id.message);
        timestamp = (TextView) view.findViewById(R.id.timestamp);
        count = (TextView) view.findViewById(R.id.count);

        ChatRoom chatRoom = chatRoomArrayList.get(position);
        if (chatRoom == null)
            return null;
        name.setText("".equals(chatRoom.getGroupName()) ? chatRoom.getUserName() : chatRoom.getGroupName());
        message.setText(ChatController.getLastMessageForRoom(chatRoom.getId()));
        int unreadCount = ChatController.getNumberOfNewMessageForRoom(chatRoom.getId());
        if (unreadCount > 0) {
            count.setText(String.valueOf(unreadCount));
            count.setVisibility(View.VISIBLE);
        } else {
            count.setVisibility(View.GONE);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(chatRoom.getLastModifiedDate());
        timestamp.setText(formattedDate);

        return view;
    }
}
