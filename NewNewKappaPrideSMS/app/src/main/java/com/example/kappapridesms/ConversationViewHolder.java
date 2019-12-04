package com.example.kappapridesms;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * ViewHolder that deals the UI in the ConversationActivity.
 * This class extends the RecyclerView.ViewHolder.
 * <p>
 *     Has one private attribute:
 *     <p>
 *         - LinearLayout m_conversationBubble
 *     </p>
 *     Has one overloaded constructor:
 *     <p>
 *         - public ConversationViewHolder(@NonNull LinearLayout conversationBubble)
 *     </p>
 *     Has one public method:
 *     <p>
 *         - public LinearLayout getConversationBubble()
 *     </p>
 * </p>
 *
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder
{
    private LinearLayout m_conversationBubble;

    /**
     * Constructs an instance of a ConversationViewHolder. It is
     * passed a conversationBubble of LinearLayout. It then calls
     * its super class constructor and then assigns the conversationBubble
     * to the private member of the class
     *
     * @param conversationBubble conversation bubble in the ConversationActivity
     */
    public ConversationViewHolder(@NonNull LinearLayout conversationBubble)
    {
        super(conversationBubble);
        m_conversationBubble = conversationBubble;
    }

    /**
     * Returns the conversation bubble
     *
     * @return m_conversationBubble
     */
    public LinearLayout getConversationBubble()
    {
        return m_conversationBubble;
    }

}
