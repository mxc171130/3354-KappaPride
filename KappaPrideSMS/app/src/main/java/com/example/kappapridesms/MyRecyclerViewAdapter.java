package com.example.kappapridesms;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>
{
    private static final MyRecyclerViewAdapter INSTANCE = new MyRecyclerViewAdapter();

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout messageBubble;

        public MyViewHolder(LinearLayout messageBubble)
        {
            super(messageBubble);
            this.messageBubble = messageBubble;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LinearLayout messageBubble = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(messageBubble);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int pos)
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation targetConversation = instance.getTargetConversation();


        long authorPhone = targetConversation.getRecipientPhone();
        Message selectedMessage = targetConversation.getMessage(pos);

        // INSERT CONTACT ALIAS CODE HERE

        if(selectedMessage.isSentFromThisPhone())
        {
            ((TextView) viewHolder.messageBubble.getChildAt(0)).setText("You");
        }
        else
        {
            ((TextView) viewHolder.messageBubble.getChildAt(0)).setText(authorPhone + "");
        }

        ((TextView) viewHolder.messageBubble.getChildAt(1)).setText(selectedMessage.getContent());

        StringBuilder dateTimeBuilder = new StringBuilder();
        dateTimeBuilder.append(selectedMessage.getDate());
        dateTimeBuilder.append(" at ");
        dateTimeBuilder.append(selectedMessage.getTime());

        ((TextView) viewHolder.messageBubble.getChildAt(2)).setText(dateTimeBuilder.toString());
    }

    @Override
    public int getItemCount()
    {
        return ConversationRepository.getInstance().getTargetConversation().size();
    }


    public static MyRecyclerViewAdapter getInstance()
    {
        return INSTANCE;
    }


    private MyRecyclerViewAdapter()
    {
    }
}
