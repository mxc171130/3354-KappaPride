package com.example.kappapridesms;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class describes an item view and metadata about its place within the RecyclerView.
 * <p>
 *     Contains one private attribute:
 *     <p>
 *         - m_conversationBubble
 *     </p>
 * </p>
 * <p>
 *     Contains a public Constructor and a public method:
 *     <p>
 *         - ConversationViewHolder()
 *     </p>
 *     <p>
 *         - getConversationBubble
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder
{

    /**
     * Variable that holds the value of the LinearLayout used.
     */
    private LinearLayout m_conversationBubble;

    /**
     * Constructor that sets the LinearLayout.
     *
     * @param conversationBubble The linear layout that will be used in the view holder.
     */
    public ConversationViewHolder(@NonNull LinearLayout conversationBubble)
    {
        super(conversationBubble);
        m_conversationBubble = conversationBubble;
    }

    /**
     * Method that returns the linear layout instantiated in the constructor.
     *
     * @return Value of the linear layout.
     */
    public LinearLayout getConversationBubble()
    {
        return m_conversationBubble;
    }

}
