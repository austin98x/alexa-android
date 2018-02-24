package com.austin.android.alexa;

import android.app.Application;

public class AlexaApp extends Application {

    private static AlexaApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AlexaApp getInstance() {
        return mInstance;
    }
}
