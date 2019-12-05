package com.example.kappapridesms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Deals with receiving messages. It extends the BroadcastReceiver.
 * It contains a public interface OnFailedSendListener.
 * <p>
 *     Has one private attribute:
 *     <p>
 *         - OnFailedSendListener m_onFailedSendListener
 *     </p>
 *     Has two public methods:
 *     <p>
 *         - public void onReceive(Context context, Intent intent)
 *         - public SentReceiver(OnFailedSendListener listener)
 *     </p>
 *
 * </p>
 *
 * @author Nathan Beck
 */
public class SentReceiver extends BroadcastReceiver
{
    /**
     * Instance of OnFailedSendListener.
     */
    private OnFailedSendListener m_onFailedSendListener;

    public interface OnFailedSendListener
    {
        void onFailedSend();
    }

    /**
     * Overriden onReceive method. Checks if the
     * message was received. If the message was not
     * received then it displays the dialog.
     *
     * @param context handle for resolving issues
     * @param intent facilitates communication between Components
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals("SMS_SENT"))
        {
            if(getResultCode() != Activity.RESULT_OK)
            {
                // Failed to send message, display dialog

                m_onFailedSendListener.onFailedSend();
            }
        }
    }

    /**
     * Assigns the OnFailedSendLister to the private
     * attribute m_onFailedSendListener.
     *
     * @param listener a failed send listener that listens
     *                 if a msg has failed
     */
    public SentReceiver(OnFailedSendListener listener)
    {
        m_onFailedSendListener = listener;
    }
}
