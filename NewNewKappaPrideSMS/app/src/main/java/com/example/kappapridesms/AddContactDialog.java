package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

/**
 * Displays an add contact dialog if a user wants to add a contact to the app.
 * This class extends the DialogFragment class that is available in
 * in the Android API.
 *
 * This dialog is opened when the user first selects the add contact option from
 * the options menu in the toolbar of Message Activity and selects a message with
 * and eligible number.
 *
 * @author Nathan Beck
 */
public class AddContactDialog extends DialogFragment
{
    /**
     * The listener in which to send button click notifications
     */
    private ContactDialogListener m_dialogListener;

    /**
     * The associated View of this Dialog
     */
    private View m_contactContent;

    /**
     * This interface specifies the handler methods for listeners
     * subscribing to be notified of click events on AddContactDialog's
     * buttons.
     *
     * @author Nathan Beck
     */
    public interface ContactDialogListener
    {
        /**
         * The method called when the user selects the positive button
         * for this dialog.
         */
        void onContactPositiveClick();

        /**
         * The method called when the user selects the negative button
         * for this dialog.
         */
        void onContactNegativeClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Attaches this AddContactDialog to a given context, registering this contact
     * as a ContactDialogListener for its button events.
     * @param context The context to attach this AddContactDialog.
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            m_dialogListener = ((MessageActivity) context).getMessageFragment();
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }

    /**
     * Creates the viewable dialog and sets the actions to be taken on button clicks.
     * Here, the listener subscribed to this AddContactDialog will be called via an
     * OnClick event.
     *
     * @param savedInstanceState The state saved between lifecycle stage changes.
     * @return The constructed Dialog associated with this AddContactDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Get an AlertDialog.Builder to construct this dialog.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        // Set the content (View) and the title for this dialog.
        dialogBuilder.setTitle("Add Contact");
        m_contactContent = requireActivity().getLayoutInflater().inflate(R.layout.contact_view, null);
        dialogBuilder.setView(m_contactContent);

        // Specify the onClick functionality for this dialog.
        dialogBuilder.setPositiveButton("Add Contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onContactPositiveClick();
                dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onContactNegativeClick();
                dismiss();
            }
        });

        return dialogBuilder.create();
    }

    /**
     * This method retrieves the view associated with this AddContactDialog.
     *
     * @return The View associated with this AddContactDialog.
     */
    public View getContactContent()
    {
        return m_contactContent;
    }
}