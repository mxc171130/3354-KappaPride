package com.example.kappapridesms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SentReceiver extends BroadcastReceiver
{
    private OnFailedSendListener m_onFailedSendListener;

    public interface OnFailedSendListener
    {
        void onFailedSend();
    }

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

    public SentReceiver(OnFailedSendListener listener)
    {
        m_onFailedSendListener = listener;
    }
}
