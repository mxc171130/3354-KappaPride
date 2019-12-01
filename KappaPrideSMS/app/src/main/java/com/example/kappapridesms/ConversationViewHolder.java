package com.example.kappapridesms;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// This class is very similar to MessageViewHolder class

public class ConversationViewHolder extends RecyclerView.ViewHolder
{

    private LinearLayout m_conversationBox;

    public ConversationViewHolder(@NonNull LinearLayout conversationBox)
    {
        super(conversationBox);
        m_conversationBox = conversationBox;
    }

    public LinearLayout getConversationBox()
    {
        return m_conversationBox;
    }

}
