package com.example.kappapridesms;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder
{
    private LinearLayout m_messageBubble;


    public MessageViewHolder(LinearLayout messageBubble)
    {
        super(messageBubble);
        m_messageBubble = messageBubble;
    }


    public LinearLayout getMessageBubble()
    {
        return m_messageBubble;
    }
}
