package com.example.kappapridesms;

import android.app.Application;
import android.content.Context;

public class KappaApplication extends Application
{
    private static Context context;

    public void onCreate()
    {
        super.onCreate();
        KappaApplication.context = getApplicationContext();
    }

    public static Context getAppContext()
    {
        return context;
    }
}
