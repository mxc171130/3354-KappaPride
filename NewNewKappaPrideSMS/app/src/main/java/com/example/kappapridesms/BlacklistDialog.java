package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * This class specifies the BlacklistDialog that is shown when the user
 * first selects the blacklist option from Message Activity's action bar
 * and then selects a message. Here, the user can confirm through this
 * Dialog that he or she really wants to blacklist the number associated
 * with the selected message.
 *
 * @author Nathan Beck
 */
public class BlacklistDialog extends DialogFragment
{
    /**
     * The content to be displayed upon inflation of the BlacklistDialog.
     */
    private String content = "Blacklist this number?";

    /**
     * The listener subscribed to this BlacklistDialog's onClick events.
     */
    private BlacklistDialogListener m_blacklistListener;

    /**
     * Classes implementing BlacklistDialogListener subscribe for
     * positive button clicks and negative button clicks associated
     * with this BlacklistDialog.
     *
     * @author Nathan Beck
     */
    public interface BlacklistDialogListener
    {
        /**
         * The method called upon clicking the positive button.
         */
        void onBlacklistPositiveClick();

        /**
         * The method called upon clicking the negative button.
         */
        void onBlacklistNegativeClick();
    }

    /**
     * Attaches this BlacklistDialog to a given context, registering this contact
     * as a BlacklistDialogListener for its button events.
     * @param context The context to attach this AddContactDialog.
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            m_blacklistListener = ((MessageActivity) context).getMessageFragment();
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }

    /**
     * Creates the viewable dialog and sets the actions to be taken on button clicks.
     * Here, the listener subscribed to this BlacklistDialog will be called via an
     * OnClick event.
     *
     * @param savedInstanceState The state saved between lifecycle stage changes.
     * @return The constructed Dialog associated with this BlacklistDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Get an AlertDialog.Builder for this BlacklistDialog
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        // Set the dialog message, title, and onClick actions
        builder.setTitle("Blacklist").setMessage(content).setPositiveButton("Blacklist", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                m_blacklistListener.onBlacklistPositiveClick();
                dismiss();
            }
        }).setNegativeButton("Whitelist", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_blacklistListener.onBlacklistNegativeClick();
                dismiss();
            }
        });

        return builder.create();
    }
}