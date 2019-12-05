package com.example.kappapridesms;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

/**
 * This fragment is what contains all of the functionality of messaging between another contact.
 * <p>
 *     Contains 13 private attributes and instances:
 *     <p>
 *         - m_fragView
 *     </p>
 *     <p>
 *         - m_messageRecyclerView
 *     </p>
 *     <p>
 *         - s_messageViewAdapter
 *     </p>
 *     <p>
 *         - m_deleteActive
 *     </p>
 *     <p>
 *         - m_deleteView
 *     </p>
 *     <p>
 *         - m_warningDialog
 *     </p>
 *     <p>
 *         - m_forwardActive
 *     </p>
 *     <p>
 *         - m_forwardContent
 *     </p>
 *     <p>
 *         - m_forwardDialog
 *     </p>
 *     <p>
 *         - m_errorDialog
 *     </p>
 *     <p>
 *         - m_blacklistActive
 *     </p>
 *     <p>
 *         - m_blacklistContent
 *     </p>
 *     <p>
 *         - m_blacklistDialog
 *     </p>
 * </p>
 * <p>
 *     Contains 15 public methods:
 *     <p>
 *         - onCreateView()
 *     </p>
 *     <p>
 *         - onActivityCreated()
 *     </p>
 *     <p>
 *         - onResume()
 *     </p>
 *     <p>
 *         - onCLose()
 *     </p>
 *     <p>
 *         - onOptionsItemSelected()
 *     </p>
 *     <p>
 *         - onTouch()
 *     </p>
 *     <p>
 *         - onForwardPositiveClickListener()
 *     </p>
 *     <p>
 *         - onForwardNegativeClockListener()
 *     </p>
 *     <p>
 *         - onWarningPositiveClick()
 *     </p>
 *     <p>
 *         - onWarningNegativeClick()
 *     </p>
 *     <p>
 *         - onBlacklistPositiveClick()
 *     </p>
 *     <p>
 *         - onBlacklistNegativeCLick()
 *     </p>
 *     <p>
 *         - onFailedSend()
 *     </p>
 *     <p>
 *         - sendMessage()
 *     </p>
 *     <p>
 *         - getMessageViewAdapter()
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */
public class MessageFragment extends Fragment implements ForwardDialog.ForwardDialogListener, View.OnTouchListener, SearchView.OnCloseListener, WarningDialog.WarningDialogListener, BlacklistDialog.BlacklistDialogListener
{
    /**
     * Instance of View for the fragment.
     */
    private View m_fragView;

    /**
     * Instance of RecyclerView for messages.
     */
    private RecyclerView m_messageRecyclerView;

    /**
     * Instance of MessageViewAdapter.
     */
    private static MessageViewAdapter s_messageViewAdapter;

    /**
     * Boolean value to determine whether delete was clicked or not.
     */
    private boolean m_deleteActive;

    /**
     * Instance of View for deleting.
     */
    private View m_deleteView;

    /**
     * Instance of WarningDialog.
     */
    private WarningDialog m_warningDialog;

    /**
     * Boolean value to determine whether forward was clicked or not.
     */
    private boolean m_forwardActive;

    /**
     * String value to hold what number the message is being forwarded to.
     */
    private String m_forwardContent;

    /**
     * Instance of ForwardDialog to show the prompt when forward is clicked.
     */
    private ForwardDialog m_forwardDialog;

    /**
     * Instance of ErrorDialog to show the prompt when an error has occurred.
     */
    private ErrorDialog m_errorDialog;

    /**
     * Boolean value to determine whether blacklist was clicked or not.
     */
    private boolean m_blacklistActive;

    /**
     * Long value to hold the number that the user wants to blacklist.
     */
    private long m_blacklistContent;

    /**
     * Instance of BlacklistDialog to show the prompt when the user wants to blacklist a number.
     */
    private BlacklistDialog m_blacklistDialog;

    /**
     * Method called to have the fragment instantiate its user interface view.
     *
     * @param inflater The object that can be used to inflate any views in teh fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. This is used to generate the LayoutParams of the view. The value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given by this parameter.
     * @return The UI view of the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_fragView = inflater.inflate(R.layout.fragment_message, container, false);
        return m_fragView;
    }

    /**
     * Method called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state. This value may be null.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        m_messageRecyclerView = (RecyclerView) m_fragView.findViewById(R.id.message_recycler);
        m_messageRecyclerView.setOnTouchListener(this);
        m_messageRecyclerView.setNestedScrollingEnabled(false);
        m_messageRecyclerView.setHasFixedSize(true);

        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(m_fragView.getContext());
        m_messageRecyclerView.setLayoutManager(myRecyclerLinearLayout);

        s_messageViewAdapter = new MessageViewAdapter();
        s_messageViewAdapter.setHasStableIds(true);
        m_messageRecyclerView.setAdapter(s_messageViewAdapter);

        m_forwardDialog = new ForwardDialog();
        m_errorDialog = new ErrorDialog();
        m_warningDialog = new WarningDialog();
        m_blacklistDialog = new BlacklistDialog();

        setHasOptionsMenu(true);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Method called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        ConversationRepository.getInstance().loadConversations();
        s_messageViewAdapter.notifyDataSetChanged();
    }

    /**
     * Method that runs when the application is closed.
     *
     * @return Unconditionally returns false when called.
     */
    @Override
    public boolean onClose()
    {
        ViewGroup recyclerView = (ViewGroup) m_fragView.findViewById(R.id.message_recycler);
        ViewGroup recyclerElement;

        for(int i = 0; i < recyclerView.getChildCount(); i++)
        {
            recyclerElement = (ViewGroup) recyclerView.getChildAt(i);

            for(int j = 0; j < recyclerElement.getChildCount(); j++)
            {
                recyclerElement.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        return false;
    }

    /**
     * Method called whenever an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return Returns false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch(item.getItemId())
        {
            case R.id.message_delete:
                m_deleteActive = true;
                return true;
            case R.id.message_forward:
                m_forwardActive = true;
                return true;
            case R.id.message_contact:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
                return true;
            case R.id.message_blacklist:
                m_blacklistActive = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when a touch was registered on the touch screen.
     *
     * @param v The view the touch was registered in.
     * @param ev Holds either absolute or relative movements depending on the action and device.
     * @return true if menu item was clicked (info pertaining to whether or not it was clicked was in the previous method), otherwise false.
     */
    @Override
    public boolean onTouch(View v, MotionEvent ev)
    {
        if(m_deleteActive)
        {
            if(v instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) v;
                m_deleteView = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

                if(m_deleteView == null)
                {
                    m_deleteActive = false;
                    return true;
                }

                m_warningDialog.show(getActivity().getSupportFragmentManager(), "warning_dialog");
                m_deleteActive = false;
            }
        }
        else if(m_forwardActive)
        {
            if(v instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) v;
                View childView = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

                if(childView == null)
                {
                    m_forwardActive = false;
                    return true;
                }

                LinearLayout messageLayout = (LinearLayout) childView;
                TextView messageView = (TextView) messageLayout.getChildAt(1);
                m_forwardContent = (String) messageView.getText();

                m_forwardDialog.show(getActivity().getSupportFragmentManager(), "forward_dialog");

                m_forwardActive = false;
            }
        }
        else if(m_blacklistActive)
        {
            if(v instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) v;
                View childView = recyclerView.findChildViewUnder(ev.getX(), ev.getY());

                if(childView == null)
                {
                    m_blacklistActive = false;
                    return true;
                }

                LinearLayout messageLayout = (LinearLayout) childView;
                TextView messageView = (TextView) messageLayout.getChildAt(0);
                String blacklistContactContent = (String) messageView.getText();

                ConversationRepository instance = ConversationRepository.getInstance();
                Blacklist blacklist = instance.getBlacklist();

                if(blacklistContactContent.equals("You"))
                {
                    m_blacklistActive = false;
                    return true;
                }

                long phoneNumber = instance.getPhoneNumber(blacklistContactContent, getContext());

                if(phoneNumber == 0)
                {
                    phoneNumber = Long.parseLong(blacklistContactContent);
                }

                m_blacklistContent = phoneNumber;

                m_blacklistDialog.show(getActivity().getSupportFragmentManager(), "blacklist_dialog");

                m_blacklistActive = false;
            }
        }

        return true;
    }

    /**
     * Method that is called when forward is clicked.
     */
    @Override
    public void onForwardPositiveClickListener()
    {
        TextView messageText = (TextView) m_forwardDialog.getForwardContent().findViewById(R.id.forward_phone);
        String stringNumber = "1" + messageText.getText();
        long phoneNumber = Long.parseLong(stringNumber);

        ConversationRepository instance = ConversationRepository.getInstance();

        for(Conversation testConversation : instance.getConversations())
        {
            if(testConversation.getRecipientPhone() == phoneNumber)
            {
                sendMessage(testConversation, m_forwardContent);
                return;
            }
        }

        Conversation newConversation = new Conversation(phoneNumber);
        instance.addConversation(newConversation);
        sendMessage(newConversation, m_forwardContent);
    }

    /**
     * Method that is called when forward is not clicked.
     */
    @Override
    public void onForwardNegativeClickListener()
    {

    }

    /**
     * Method that is called when message is clicked after clicking on delete in the options menu. It will display a warning.
     */
    @Override
    public void onWarningPositiveClick()
    {
        LinearLayout messageLayout = (LinearLayout) m_deleteView;
        TextView messageView = (TextView) messageLayout.getChildAt(1);
        String messageContent = (String) messageView.getText();

        ConversationRepository instance = ConversationRepository.getInstance();
        Conversation deleteFromConversation = instance.getTargetConversation();

        for(int i = 0; i < deleteFromConversation.size(); i++)
        {
            Message testMessage = deleteFromConversation.getMessage(i);

            if(testMessage.getContent().equals(messageContent))
            {
                deleteFromConversation.deleteMessage(testMessage);
                FileSystem.getInstance().saveConversations(instance.getConversations());
                s_messageViewAdapter.notifyDataSetChanged();

                break;
            }
        }
    }

    /**
     * Method that is called when no warning message is suppose to pop up.
     */
    @Override
    public void onWarningNegativeClick()
    {

    }

    /**
     * Method that is called when a number is clicked after blacklist was clicked on in the options menu.
     */
    @Override
    public void onBlacklistPositiveClick()
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        Blacklist blacklist = instance.getBlacklist();

        blacklist.addBlacklistedNumber(m_blacklistContent);
        FileSystem.getInstance().saveBlacklistNumbers(blacklist);
    }

    /**
     * Method that is called when a number is to be whitelisted.
     */
    @Override
    public void onBlacklistNegativeClick()
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        Blacklist blacklist = instance.getBlacklist();

        blacklist.removeBlacklistedNumber(m_blacklistContent);
        FileSystem.getInstance().saveBlacklistNumbers(blacklist);
    }

    /**
     * Method that is called is a message failed to send. A dialog will pop up.
     */
    public void onFailedSend()
    {
        m_errorDialog.show(getActivity().getSupportFragmentManager(), "error_dialog");
    }

    /**
     * Method that is called to send a message. It uses two parameters to determine the conversation to send it in and what it will send.
     *
     * @param targetConversation The conversation to send the message in.
     * @param messageContent String value that holds what the message is.
     */
    public void sendMessage(Conversation targetConversation, String messageContent)
    {
        if(messageContent == null || messageContent.length() == 0)
        {
            return;
        }

        long timestamp = new Date().getTime();

        targetConversation.addMessage(new Message(timestamp, true, messageContent));
        FileSystem.getInstance().saveConversations(ConversationRepository.getInstance().getConversations());

        PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(),0, new Intent("SMS_SENT"),0);


        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId());
        smsManager.sendTextMessage("" + targetConversation.getRecipientPhone(), null, messageContent, sentIntent, null);

        s_messageViewAdapter.notifyDataSetChanged();
    }

    /**
     * Method that will return the MessageViewAdapter.
     *
     * @return the message view adapter.
     */
    public static MessageViewAdapter getMessageViewAdapter()
    {
        return s_messageViewAdapter;
    }
}