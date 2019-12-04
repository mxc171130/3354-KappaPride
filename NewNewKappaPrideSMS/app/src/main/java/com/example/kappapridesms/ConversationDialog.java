package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class ConversationDialog extends DialogFragment
{
    /**
     * The listener in which to send button click notifications
     */
    private ConversationDialogListener m_dialogListener;

    /**
     * The associated View of this Dialog
     */
    private View m_conversationContent;


    public interface ConversationDialogListener
    {
        /**
         * The method called when the user selects the positive button
         * for this dialog.
         */
        void onConversationPositiveClick();

        /**
         * The method called when the user selects the negative button
         * for this dialog.
         */
        void onConversationNegativeClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            m_dialogListener = ((ConversationActivity) context);
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Get an AlertDialog.Builder to construct this dialog.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        // Set the content (View) and the title for this dialog.
        dialogBuilder.setTitle("Start Conversation");
        m_conversationContent = requireActivity().getLayoutInflater().inflate(R.layout.conversation_view, null);
        dialogBuilder.setView(m_conversationContent);

        // Specify the onClick functionality for this dialog.
        dialogBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onConversationPositiveClick();
                dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onConversationNegativeClick();
                dismiss();
            }
        });

        return dialogBuilder.create();
    }


    public View getConversationContent()
    {
        return m_conversationContent;
    }
}
