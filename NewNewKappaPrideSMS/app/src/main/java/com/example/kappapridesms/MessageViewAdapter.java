package com.example.kappapridesms;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Deals with how the MessageActivity looks and feels in the app.
 * It extends the RecyclerView.Adapter class.
 * <p>
 *     Has three public methods:
 *     <p>
 *         - public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
 *         - public void onBindViewHolder(MessageViewHolder viewHolder, int pos)
 *         - public int getItemCount()
 *     </p>
 * </p>
 */
public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewHolder>
{
    /**
     * Creates a message bubble, which is then passed in the MessageViewHolder
     * constructor to create a ViewHolder. It then returns that ViewHolder.
     *
     * @param parent the parent of the ViewGroup
     * @param viewType an integer representation of the viewType
     * @return myViewHolder ViewHolder that was created
     */
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LinearLayout messageBubble = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        MessageViewHolder myViewHolder = new MessageViewHolder(messageBubble);
        return myViewHolder;
    }

    /**
     * This method is called by RecyclerView which decides how the
     * data is displayed on the screen. This method binds the holder,
     * so that old data is replaced by new data more efficiently.
     *
     * @param viewHolder ViewHolder that deals with how a Message is displayed
     * @param pos position of the view holder
     */
    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int pos)
    {
        // get instances of the conversation, and contact manager
        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation targetConversation = instance.getTargetConversation();

        // gets the author phone, message, and author contact
        long authorPhone = targetConversation.getRecipientPhone();
        Message selectedMessage = targetConversation.getMessage(pos);

        if(selectedMessage.isSentFromThisPhone())
        {
            ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText("You");
        }
        else
        {
            // message is from someone else
            // INSERT CONTACT ALIAS CODE HERE
            String name = ConversationRepository.getInstance().getContactName("" + authorPhone, KappaApplication.getAppContext());
            if(name == null || name.length() == 0)
            {
                ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText(authorPhone + "");
            }
            else
            {
                ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText(name);
            }
        }

        // deals with the UI
        ((TextView) viewHolder.getMessageBubble().getChildAt(1)).setText(selectedMessage.getContent());

        StringBuilder dateTimeBuilder = new StringBuilder();
        dateTimeBuilder.append(selectedMessage.getDate());
        dateTimeBuilder.append(" at ");
        dateTimeBuilder.append(selectedMessage.getTime());

        ((TextView) viewHolder.getMessageBubble().getChildAt(2)).setText(dateTimeBuilder.toString());
    }

    /**
     * Returns the size of the of the target conversation
     *
     * @return size of target conversation
     */
    @Override
    public int getItemCount()
    {
        return ConversationRepository.getInstance().getTargetConversation().size();
    }
}
