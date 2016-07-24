package com.zenus.chatclient;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zenus.chatclient.controller.ApiController;
import com.zenus.chatclient.model.DaoMaster;
import com.zenus.chatclient.model.DaoSession;

public class AppApplication extends Application {
    public static final String APP_TAG = "ChatClient";
    private static AppApplication instance;
    private RequestQueue requestQueue;
    public DaoSession daoSession;
    private ApiController apiController;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setApiController(new ApiController());
        setupDatabaseManager();
    }

    public static synchronized AppApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return AppApplication.getInstance().getApplicationContext();
    }

    private void setupDatabaseManager() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ChatClientDB.sqlite", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        } catch (Exception e){
        }
    }

    public synchronized RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public String getUserLoginName(){
        return "zico";
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getDBSession() {
        return getInstance().getDaoSession();
    }

    public ApiController getApiController() {
        return apiController;
    }

    public void setApiController(ApiController apiController) {
        this.apiController = apiController;
    }
}
