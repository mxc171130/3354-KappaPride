package com.example.kappapridesms;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
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

public class MessageFragment extends Fragment implements ForwardDialog.ForwardDialogListener, View.OnTouchListener, SearchView.OnCloseListener, WarningDialog.WarningDialogListener, AddContactDialog.ContactDialogListener, BlacklistDialog.BlacklistDialogListener
{
    private View m_fragView;
    private RecyclerView m_messageRecyclerView;
    private static MessageViewAdapter s_messageViewAdapter;

    private boolean m_deleteActive;
    private View m_deleteView;
    private WarningDialog m_warningDialog;

    private boolean m_forwardActive;
    private String m_forwardContent;
    private ForwardDialog m_forwardDialog;

    private ErrorDialog m_errorDialog;

    private boolean m_addContactActive;
    private long m_addContactContent;
    private AddContactDialog m_addContactDialog;

    private boolean m_deleteContactActive;
    private boolean m_searchContact;

    private boolean m_blacklistActive;
    private long m_blacklistContent;
    private BlacklistDialog m_blacklistDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_fragView = inflater.inflate(R.layout.fragment_message, container, false);
        return m_fragView;
    }

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
        m_addContactDialog = new AddContactDialog();
        m_blacklistDialog = new BlacklistDialog();

        setHasOptionsMenu(true);

        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        ConversationRepository.getInstance().loadConversations();
        s_messageViewAdapter.notifyDataSetChanged();
    }


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
                ContactManager contactManager = instance.getContactManager();
                Blacklist blacklist = instance.getBlacklist();

                long phoneNumber = contactManager.getNumberFromName(blacklistContactContent);

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


    @Override
    public void onForwardNegativeClickListener()
    {

    }

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


    @Override
    public void onWarningNegativeClick()
    {

    }

    @Override
    public void onContactPositiveClick()
    {
        TextView contactContent = (TextView) m_addContactDialog.getContactContent().findViewById(R.id.conversation_field);
        String contactName = contactContent.getText().toString();
        ContactManager contactManager = ConversationRepository.getInstance().getContactManager();
        contactManager.addContact(m_addContactContent, contactName);
        contactManager.saveContacts();
        s_messageViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactNegativeClick()
    {

    }

    @Override
    public void onBlacklistPositiveClick()
    {
        ConversationRepository instance = ConversationRepository.getInstance();
        ContactManager contactManager = instance.getContactManager();
        Blacklist blacklist = instance.getBlacklist();

        Contact blacklistContact = contactManager.getContact(m_blacklistContent);

        if(blacklistContact.getName().equals("DNE"))
        {
             blacklistContact = new Contact("No Name", m_blacklistContent);
        }

        blacklist.addBlacklistedContact(blacklistContact);
        FileSystem.getInstance().saveBlacklistNumbers(blacklist);
    }

    @Override
    public void onBlacklistNegativeClick()
    {

    }


    public void onFailedSend()
    {
        m_errorDialog.show(getActivity().getSupportFragmentManager(), "error_dialog");
    }



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

    public static MessageViewAdapter getMessageViewAdapter()
    {
        return s_messageViewAdapter;
    }

    public static void deleteContact(ContentResolver contact, String number)
    {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        String[] args = new String[] {String.valueOf(getContactID(contact, number))};
        operations.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try
        {
            contact.applyBatch(ContactsContract.AUTHORITY, operations);
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
        catch(OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }

    private static long getContactID(ContentResolver contact, String number)
    {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] list = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;
        try
        {
            cursor = contact.query(contactUri, list, null, null, null);
            if(cursor.moveToFirst())
            {
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);
            }
            return -1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(cursor != null)
            {
                cursor.close();
                cursor = null;
            }
        }
        return -1;
    }
}