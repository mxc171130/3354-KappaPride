package com.example.kappapridesms;

import android.app.Application;
import android.content.Context;

/**
 * This class was created to give other class access to the Activites context
 * <p>
 *     Contains one private instance:
 *     <p>
 *         - context
 *     </p>
 * </p>
 * <p>
 *     Contains two public methods:
 *     <p>
 *         - onCreate()
 *     </p>
 *     <p>
 *         - getAppContext()
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */
public class KappaApplication extends Application
{
    /**
     * Instance of Context.
     */
    private static Context context;

    /**
     * Method that sets the context when the application is started.
     */
    public void onCreate()
    {
        super.onCreate();
        KappaApplication.context = getApplicationContext();
    }

    /**
     * Method that returns the current context or state of the application.
     *
     * @return the context of the application
     */
    public static Context getAppContext()
    {
        return context;
    }
}
