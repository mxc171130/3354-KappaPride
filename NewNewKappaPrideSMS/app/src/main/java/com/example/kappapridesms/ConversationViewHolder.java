package com.example.kappapridesms;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ConversationViewHolder extends RecyclerView.ViewHolder
{

    private LinearLayout m_conversationBubble;

    public ConversationViewHolder(@NonNull LinearLayout conversationBubble)
    {
        super(conversationBubble);
        m_conversationBubble = conversationBubble;
    }

    public LinearLayout getConversationBubble()
    {
        return m_conversationBubble;
    }

}
