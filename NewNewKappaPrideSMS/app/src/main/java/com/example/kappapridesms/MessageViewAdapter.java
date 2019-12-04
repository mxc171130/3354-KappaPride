package com.example.kappapridesms;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewHolder>
{
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LinearLayout messageBubble = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        MessageViewHolder myViewHolder = new MessageViewHolder(messageBubble);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int pos)
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation targetConversation = instance.getTargetConversation();
        ContactManager contactManager = instance.getContactManager();

        long authorPhone = targetConversation.getRecipientPhone();
        Message selectedMessage = targetConversation.getMessage(pos);
        Contact authorContact = contactManager.getContact(authorPhone);


        if(selectedMessage.isSentFromThisPhone())
        {
            ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText("You");
        }
        else
        {
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

        ((TextView) viewHolder.getMessageBubble().getChildAt(1)).setText(selectedMessage.getContent());

        StringBuilder dateTimeBuilder = new StringBuilder();
        dateTimeBuilder.append(selectedMessage.getDate());
        dateTimeBuilder.append(" at ");
        dateTimeBuilder.append(selectedMessage.getTime());

        ((TextView) viewHolder.getMessageBubble().getChildAt(2)).setText(dateTimeBuilder.toString());
    }

    @Override
    public int getItemCount()
    {
        return ConversationRepository.getInstance().getTargetConversation().size();
    }
}
