package com.zenus.chatclient.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatMessageDaoConfig;
    private final DaoConfig chatRoomDaoConfig;

    private final ChatMessageDao chatMessageDao;
    private final ChatRoomDao chatRoomDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatMessageDaoConfig = daoConfigMap.get(ChatMessageDao.class).clone();
        chatMessageDaoConfig.initIdentityScope(type);

        chatRoomDaoConfig = daoConfigMap.get(ChatRoomDao.class).clone();
        chatRoomDaoConfig.initIdentityScope(type);

        chatMessageDao = new ChatMessageDao(chatMessageDaoConfig, this);
        chatRoomDao = new ChatRoomDao(chatRoomDaoConfig, this);

        registerDao(ChatMessage.class, chatMessageDao);
        registerDao(ChatRoom.class, chatRoomDao);
    }
    
    public void clear() {
        chatMessageDaoConfig.getIdentityScope().clear();
        chatRoomDaoConfig.getIdentityScope().clear();
    }

    public ChatMessageDao getChatMessageDao() {
        return chatMessageDao;
    }

    public ChatRoomDao getChatRoomDao() {
        return chatRoomDao;
    }

}
