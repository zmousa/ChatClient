package com.zenus.chatclient.controller;

import com.zenus.chatclient.network.Callback;
import com.zenus.chatclient.network.RestCallback;
import com.zenus.chatclient.network.RestClientFactory;
import com.zenus.chatclient.network.RestRequestBuilder;
import com.zenus.chatclient.network.RestRouter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApiController {
    private final String USERNAME = "USERNAME";
    private final String PASSWORD = "PASSWORD";

    private final String POST_CHAT_MESSAGE = "chat_message";
    private final String POST_CHAT_MESSAGEDATE = "chat_msg_date";
    private final String POST_CHAT_ROOM = "chat_room";

    public String[] sendChatMessage(String message, long chatRoomId, Date createDate, final Callback callback){
        final String msg[] = {""};

        if (callback != null)
            callback.before(this);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = format.format(createDate);

        String route = RestRouter.getDefault().to(RestRouter.Route.CHAT_MESSAGE_POST);
        Map<String,String> postParam = new HashMap<>();
        postParam.put(POST_CHAT_MESSAGE, message);
        postParam.put(POST_CHAT_MESSAGEDATE, formattedDate);
        postParam.put(POST_CHAT_ROOM, "" + chatRoomId);

        RestClientFactory.getDefault(RestRequestBuilder.ResponseType.JsonObject).doPost(route, postParam, getAuthenticationHeaders(), new RestCallback<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    callback.after(response);
            }

            @Override
            public void onError(Object error) {
                if (error != null) {
                    msg[0] = error.toString();
                    callback.error(error);
                }
            }
        });

        return msg;
    }

    private Map<String, String> getAuthenticationHeaders(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(USERNAME, "");
        headerMap.put(PASSWORD, "");
        return headerMap;
    }
}
