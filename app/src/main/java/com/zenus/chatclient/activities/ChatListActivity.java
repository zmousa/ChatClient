package com.zenus.chatclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zenus.chatclient.R;
import com.zenus.chatclient.controller.ChatController;
import com.zenus.chatclient.model.ChatRoom;

import java.util.ArrayList;
import java.util.List;


public class ChatListActivity extends Activity {

    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_list);

        setTitle("Chat List");
        setTitleFromActivityLabel();

        //TODO test
        ChatController.fillTestData();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat_list);

        fillChatRooms();

        mAdapter = new ChatRoomsAdapter(this, chatRoomArrayList);

        list=(ListView)findViewById(R.id.chat_list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);
                Intent intent = new Intent(ChatListActivity.this, ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", "".equals(chatRoom.getGroupName()) ? chatRoom.getUserName() : chatRoom.getGroupName());
                startActivity(intent);
            }
        });
    }

    private void fillChatRooms(){
        List<ChatRoom> result = ChatController.getChatRooms();
        chatRoomArrayList = new ArrayList<>();
        chatRoomArrayList.addAll(result);
    }

    public void setTitleFromActivityLabel() {
        TextView tv = (TextView) findViewById (R.id.title_text);
        if (tv != null) tv.setText (getTitle ());
    }
}
