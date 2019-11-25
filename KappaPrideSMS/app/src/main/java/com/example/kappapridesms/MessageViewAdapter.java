package com.example.kappapridesms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewHolder> implements View.OnClickListener
{
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LinearLayout messageBubble = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        MessageViewHolder myViewHolder = new MessageViewHolder(messageBubble);
        messageBubble.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int pos)
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation targetConversation = instance.getTargetConversation();


        long authorPhone = targetConversation.getRecipientPhone();
        Message selectedMessage = targetConversation.getMessage(pos);

        // INSERT CONTACT ALIAS CODE HERE

        if(selectedMessage.isSentFromThisPhone())
        {
            ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText("You");
        }
        else
        {
            ((TextView) viewHolder.getMessageBubble().getChildAt(0)).setText(authorPhone + "");
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


    @Override
    public void onClick(View v)
    {
        // Response for deleting/forwarding
    }
}
