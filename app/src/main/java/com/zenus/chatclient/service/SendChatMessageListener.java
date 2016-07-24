package com.zenus.chatclient.service;

public interface SendChatMessageListener {
    void taskComplete();
    void taskFailed(String message);
}