package com.zenus.chatclient.service;

import android.os.AsyncTask;

import com.zenus.chatclient.AppApplication;
import com.zenus.chatclient.R;
import com.zenus.chatclient.model.ChatMessage;
import com.zenus.chatclient.network.Callback;
import com.zenus.chatclient.util.Logger;

public class SendChatMessageTask extends AsyncTask<Void, String, Void> {
    private SendChatMessageListener sendChatMessageListener;
    private ChatMessage chatMessage;
    private static final String t = SendChatMessageTask.class.getName();
    private String[] msg = null;
    private boolean callbackReceived;

    public SendChatMessageTask(ChatMessage chatMessage){
        this.chatMessage = chatMessage;
    }
    
    @Override
    protected Void doInBackground(Void... params) {

        AppApplication.getInstance().getApiController().sendChatMessage(chatMessage.getMessage(), chatMessage.getChatRoomId(), chatMessage.getCreateDate(), new Callback() {
            @Override
            public void before(Object... params) {
                Logger.log(t, "Before Callback");
            }

            @Override
            public Object after(Object... params) {
                Logger.log(t, "After Callback");
                callbackReceived = true;
                return null;
            }

            @Override
            public void error(Object... params) {
                SendChatMessageTask.this.msg = new String[]{AppApplication.getContext().getString(R.string.server_error)};
                callbackReceived = true;
            }
        });


        while (!callbackReceived) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (msg != null && msg.length > 0 && !"".equals(msg[0])) {
            Logger.log(t, msg[0]);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void arg) {
        synchronized (this) {
            if (sendChatMessageListener != null) {
                if (msg != null && msg.length > 0)
                    sendChatMessageListener.taskFailed(msg[0]);
                else
                    sendChatMessageListener.taskComplete();
            }
        }
    }

    public void setSendChatMessageListener(SendChatMessageListener sendChatMessageListener) {
        synchronized (this) {
            this.sendChatMessageListener = sendChatMessageListener;
        }
    }
}
