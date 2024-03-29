package com.example.kappapridesms;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

/**
 * Class that provides a binding from an app-specific data set to views that are displayed within a RecyclerView.
 * <p>
 *     Contains four public methods:
 *     <p>
 *         - onCreateViewHolder()
 *     </p>
 *     <p>
 *         - onBindViewHolder()
 *         <p>
 *             - onClick()
 *         </p>
 *     </p>
 *     <p>
 *         - getItemCount()
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */

public class ConversationViewAdapter extends RecyclerView.Adapter<ConversationViewHolder>
{
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ConversationViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ConversationViewHolder, int)
     */
    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LinearLayout conversationBox = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_bubble, parent, false);
        ConversationViewHolder viewHolder = new ConversationViewHolder(conversationBox);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ConversationViewHolder, int)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position)
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        ArrayList<Conversation> conversations = instance.getConversations();
        Conversation conversation = conversations.get(position);

        if(conversation.size() > 0)
        {
            Message previewMessage = conversation.getMessage(conversation.size() - 1);
            ((TextView) holder.getConversationBubble().getChildAt(1)).setText(previewMessage.getContent());
        }

        Long recipientPhone = conversation.getRecipientPhone();

        String name = ConversationRepository.getInstance().getContactName("" + recipientPhone, KappaApplication.getAppContext());

        if(name == null || name.length() == 0)
        {
            ((TextView) holder.getConversationBubble().getChildAt(0)).setText(recipientPhone.toString());
        }
        else
        {
            ((TextView) holder.getConversationBubble().getChildAt(0)).setText(name);
        }

        holder.getConversationBubble().setOnClickListener(new View.OnClickListener() {
            /**
             * Method that is called when the button in the ViewAdapter is clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v)
            {
                LinearLayout messageLayout = (LinearLayout) v;
                TextView messageView = (TextView) messageLayout.getChildAt(0);
                String content = messageView.getText().toString();
                ConversationRepository instance = ConversationRepository.getInstance();

                long phoneNumber = instance.getPhoneNumber(content, KappaApplication.getAppContext());

                if(phoneNumber == 0)
                {
                    phoneNumber = Long.parseLong(content);
                }

                ArrayList<Conversation> allConversations = instance.getConversations();

                for(int i = 0; i < allConversations.size(); i++)
                {
                    Conversation testConversation = allConversations.get(i);

                    // Switch to this conversation
                    if(testConversation.getRecipientPhone() == phoneNumber)
                    {
                        instance.setTargetConversation(i);

                        Intent startMessageActvityIntent = new Intent(v.getContext(), MessageActivity.class);
                        startMessageActvityIntent.setAction("switch");
                        v.getContext().startActivity(startMessageActvityIntent);
                    }
                }
            }
        });
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount()
    {
        return ConversationRepository.getInstance().getConversations().size();
    }
}
