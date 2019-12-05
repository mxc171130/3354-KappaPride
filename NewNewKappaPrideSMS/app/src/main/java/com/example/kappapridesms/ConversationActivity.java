package com.example.kappapridesms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Tasked with the creation of the conversations, whether that be the UI, view adapters, or permissions.
 * <p>
 *     Contains two private attributes:
 *     <p>
 *         - s_conversationViewAdapter
 *     </p>
 *     <p>
 *         - m_conversationDialog
 *     </p>
 * </p>
 * <p>
 *     Contains five public methods:
 *     <p>
 *         - oncreateOptionsMenu()
 *     </p>
 *     <p>
 *         - onOptionsItemSelected()
 *     </p>
 *     <p>
 *         - onConversationPositiveClick()
 *     </p>
 *     <p>
 *         - onConversationNegativeClick()
 *     </p>
 *     <p>
 *         - getConversationViewAdapter()
 *     </p>
 * </p>
 * <p>
 *     Contains two protected methods:
 *     <p>
 *         - onCreate()
 *     </p>
 *     <p>
 *         - onResume()
 *     </p>
 * </p>
 * <p>
 *     Contains four private methods:
 *     <p>
 *         - initializeToolBar()
 *     </p>
 *     <p>
 *         - setUpPermissions()
 *     </p>
 *     <p>
 *         - initializeRecyclerView()
 *     </p>
 *     <p>
 *         - initializeViewAdapter
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */

public class ConversationActivity extends AppCompatActivity implements ConversationDialog.ConversationDialogListener
{
    /**
     * Static variable for the ConversationViewAdapter
     */
    private static ConversationViewAdapter s_conversationViewAdapter;

    /**
     * Static final, special value, for the permission request code
     */
    public static final int PERM_REQUEST_CODE = 227;

    /**
     * Variable of class ConversationDialog
     */
    private ConversationDialog m_conversationDialog;

    /**
     * Method that saves the state of the application and sets many variables when said application is created. i.e. when the screen is rotated.
     *
     * @param savedInstanceState This contains the data of the state of the application in a bundle. This is in case the activity needs to be recreated, they know this is the state.
     */
    //TODO
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);

        m_conversationDialog = new ConversationDialog();

        RecyclerView conversationRecyclerView = initializeRecyclerView();
        LinearLayoutManager myRecyclerLinearLayout = new LinearLayoutManager(this);
        conversationRecyclerView.setLayoutManager(myRecyclerLinearLayout);
        initializeViewAdapter(conversationRecyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, myRecyclerLinearLayout.getOrientation());
        conversationRecyclerView.addItemDecoration(dividerItemDecoration);

        Toolbar mainTool = initializeToolBar();
        getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setUpPermissions();
    }


    /**
     * Method that initializes the toolbar so that it appears when the activity is created.
     *
     * @return A new toolbar instance to be displayed by the Activity.
     */
    private Toolbar initializeToolBar()
    {
        Toolbar mainTool = findViewById(R.id.conversation_toolbar);
        setSupportActionBar(mainTool);
        return mainTool;
    }


    /**
     * Method that sets up the permissions so that various functions are able to work with the Android OS. Users must respond to a prompt in order to agree to these permissions.
     */
    private void setUpPermissions()
    {
        boolean[] perms = new boolean[6];
        perms[0] = checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[1] = checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[2] = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        perms[3] = checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        perms[4] = checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        perms[5] = checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        int numbPerms = 0;

        for(boolean perm : perms)
        {
            if(!perm)
            {
                numbPerms++;
            }
        }

        String[] requestPermissions = new String[numbPerms];
        int requestPointer = 0;

        if(!perms[0])
        {
            requestPermissions[requestPointer] = Manifest.permission.RECEIVE_SMS;
            requestPointer++;
        }

        if(!perms[1])
        {
            requestPermissions[requestPointer] = Manifest.permission.SEND_SMS;
            requestPointer++;
        }

        if(!perms[2])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_PHONE_STATE;
            requestPointer++;
        }

        if(!perms[3])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_SMS;
            requestPointer++;
        }

        if(!perms[4])
        {
            requestPermissions[requestPointer] = Manifest.permission.READ_CONTACTS;
            requestPointer++;
        }

        if(!perms[5])
        {
            requestPermissions[requestPointer] = Manifest.permission.WRITE_CONTACTS;
            requestPointer++;
        }

        if(numbPerms != 0)
        {
            requestPermissions(requestPermissions, PERM_REQUEST_CODE);
        }
    }


    /**
     * Method that initializes the RecyclerView so that conversations can be displayed in the UI. Sets some values of the recycler view.
     *
     * @return A new RecyclerView instance to be displayed by the Activity.
     */
    private RecyclerView initializeRecyclerView()
    {
        RecyclerView conversationRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler);
        conversationRecyclerView.setNestedScrollingEnabled(true);
        conversationRecyclerView.setHasFixedSize(true);
        return conversationRecyclerView;
    }


    /**
     * Method that initializes the ViewAdapter that acts as the bridge between the UI components and data sources.
     *
     * @param conversationRecyclerView the RecyclerView that we initialized earlier before calling this function.
     */
    private void initializeViewAdapter(RecyclerView conversationRecyclerView)
    {
        s_conversationViewAdapter = new ConversationViewAdapter();
        s_conversationViewAdapter.setHasStableIds(true);
        conversationRecyclerView.setAdapter(s_conversationViewAdapter);
    }

    /**
     * Method that will retrieve the conversations from the ConversationRepository and notify the view adapter that the data set has changed. This method is called when the activity is restarted.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        ConversationRepository.getInstance().loadConversations();
        s_conversationViewAdapter.notifyDataSetChanged();
    }


    /**
     * Method that creates the three dot options menu on the right side of the toolbar.
     *
     * @param menu This indicates which menu was passed to it.
     * @return Unconditional true to indicate that the menu was created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversation_menu, menu);

        return true;
    }


    /**
     * Method that finds out which item was selected from the options menu.
     *
     * @param item Indicates which item it is.
     * @return Unconditional true to indicate that an options item was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.conversation_add)
        {
            m_conversationDialog.show(getSupportFragmentManager(), "conversation_dialog");
        }
        else
        {
            super.onOptionsItemSelected(item);
        }

        return true;
    }


    /**
     * Method that will create a new message activity when create a new conversation is clicked.
     */
    @Override
    public void onConversationPositiveClick()
    {
        TextView view = (TextView) m_conversationDialog.getConversationContent().findViewById(R.id.conversation_field);
        Long phoneNumber = Long.parseLong("1" + view.getText().toString());

        ConversationRepository instance = ConversationRepository.getInstance();
        ArrayList<Conversation> conversations = instance.getConversations();

        for(int i = 0; i < conversations.size(); i++)
        {
            Conversation queryConversation = conversations.get(i);

            if(phoneNumber == queryConversation.getRecipientPhone())
            {
                instance.setTargetConversation(i);

                Intent startMessageActivityIntent = new Intent(this, MessageActivity.class);
                startMessageActivityIntent.setAction("switch");
                startActivity(startMessageActivityIntent);
                return;
            }
        }

        Conversation newConversation = new Conversation(phoneNumber);
        instance.addConversation(newConversation);
        instance.setTargetConversation(conversations.size() - 1);
        FileSystem.getInstance().saveConversations(conversations);

        Intent startMessageActvityIntent = new Intent(this, MessageActivity.class);
        startMessageActvityIntent.setAction("switch");
        startActivity(startMessageActvityIntent);
    }

    /**
     * Method that does nothing.
     */
    @Override
    public void onConversationNegativeClick()
    {

    }

    /**
     * Method that will return the view adapter for the conversation.
     *
     * @return the view adapter for the conversation
     */
    public static ConversationViewAdapter getConversationViewAdapter()
    {
        return s_conversationViewAdapter;
    }
}