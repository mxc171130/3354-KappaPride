package com.example.kappapridesms;

import android.app.Application;
import android.content.Context;

/**
 * This class was created to give other class access to the Activites context
 *
 */
public class KappaApplication extends Application
{
    private static Context context;
    /**
     *At onCreate the context is set
     */
    public void onCreate()
    {
        super.onCreate();
        KappaApplication.context = getApplicationContext();
    }

    /**
     *
     * @return the context of the application
     */
    public static Context getAppContext()
    {
        return context;
    }
}
