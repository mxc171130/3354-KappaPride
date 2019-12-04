package com.example.kappapridesms;

import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
/**
 * This class is responsible for the design of messageBubbles
 *and they are organized within the activities
 *
 */

public class MessageViewHolder extends RecyclerView.ViewHolder
{    /**
        Private variable with data Linear Layout
    */
    private LinearLayout m_messageBubble;

    /**
     *
     * @param messageBubble takes a Linear layout data type
     *On creation the messageBubble is set,and the super is called
     * setting its Layout to also be linear
     */
    public MessageViewHolder(LinearLayout messageBubble)
    {
        super(messageBubble);
        m_messageBubble = messageBubble;
    }

    /**
     *
     * @return the messageBubble layout
     * get() method
     */
    public LinearLayout getMessageBubble()
    {
        return m_messageBubble;
    }
}
