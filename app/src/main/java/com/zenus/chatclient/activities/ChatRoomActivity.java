package com.zenus.chatclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zenus.chatclient.AppApplication;
import com.zenus.chatclient.R;
import com.zenus.chatclient.controller.ChatController;
import com.zenus.chatclient.model.ChatMessage;
import com.zenus.chatclient.service.SendChatMessageListener;
import com.zenus.chatclient.service.SendChatMessageTask;
import com.zenus.chatclient.util.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChatRoomActivity extends Activity {

    private long chatRoomId;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<ChatMessage> messageArrayList;
    private EditText inputMessage;
    private Button btnSend;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btn_send);

        Intent intent = getIntent();
        chatRoomId = intent.getLongExtra("chat_room_id", -1);
        String title = intent.getStringExtra("name");
        setTitle(title);
        setTitleFromActivityLabel();

        if (chatRoomId == -1) {
            Toast.makeText(getApplicationContext(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        fillMessages();

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList);

        list=(ListView)findViewById(R.id.chat_list);
        list.setAdapter(mAdapter);
        list.smoothScrollToPosition(mAdapter.getCount() - 1);
        list.setSelection(mAdapter.getCount() - 1);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        ChatController.markNewMessagesAsRead(chatRoomId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void sendMessage() {
        ChatMessage message = new ChatMessage();
        message.setMessage(this.inputMessage.getText().toString().trim());
        message.setCreateDate(Calendar.getInstance().getTime());
        message.setUserName(AppApplication.getInstance().getUserLoginName());
        message.setChatRoomId(chatRoomId);

        ChatController.saveChatMessage(message);

        messageArrayList.add(message);

        mAdapter.notifyDataSetChanged();
        if (mAdapter.getCount() > 1) {
            list.smoothScrollToPosition(mAdapter.getCount() - 1);
            list.setSelection(mAdapter.getCount() - 1);
        }

        SendChatMessageTask sendChatMessageTask = new SendChatMessageTask(message);
        sendChatMessageTask.setSendChatMessageListener(new SendChatMessageListener() {
            @Override
            public void taskComplete() {
                Logger.log("ChatRoom","Message Sent");
            }

            @Override
            public void taskFailed(String message) {
                Logger.log("ChatRoom",message);
            }
        });
        sendChatMessageTask.execute();
        this.inputMessage.setText("");
    }

    private void fillMessages(){
        List<ChatMessage> result = ChatController.getTopChatMessages(chatRoomId);
        messageArrayList = new ArrayList<>();
        messageArrayList.addAll(result);
    }

    public void setTitleFromActivityLabel() {
        TextView tv = (TextView) findViewById (R.id.title_text);
        if (tv != null) tv.setText (getTitle ());
    }

    public void onBackClick (View v)
    {
        final Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
        intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
