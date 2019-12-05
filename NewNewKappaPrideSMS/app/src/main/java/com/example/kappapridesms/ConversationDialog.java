package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

/**
 * Tasked with the creation of the conversations, whether that be the UI, view adapters, or permissions.
 * <p>
 *     Contains two private attributes:
 *     <p>
 *         - m_dialogListener
 *     </p>
 *     <p>
 *         - m_conversationContent
 *     </p>
 * </p>
 * <p>
 *     Contains six public methods:
 *     <p>
 *         - onCreate()
 *     </p>
 *     <p>
 *         - onAttach()
 *     </p>
 *     <p>
 *         - onCreateDialog()
 *         <p>
 *             - onCLick()
 *         </p>
 *         <p>
 *             - onClick()
 *         </p>
 *     </p>
 *     <p>
 *         - getConversationContent()
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */

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

    /**
     * Method that saves the state of the application and sets many variables when said application is created. i.e. when the screen is rotated.
     *
     * @param savedInstanceState This contains the data of the state of the application in a bundle. This is in case the fragment needs to be recreated, they know this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method that is called once the fragment is associated with its activity.
     *
     * @param context This contains the data that lets it know which activity it needs to "attach" to.
     */
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

    /**
     * Method that creates and entirely custom dialog with its own content.
     *
     * @param savedInstanceState This contains the data of the state of the application in a bundle. This is in case the fragment needs to be recreated, they know this is the state.
     * @return A new dialog instance to be displayed by the fragment.
     */
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
            /**
             * Method that is called when the button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click
             * @param which Denotes which button was clicked
             */
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onConversationPositiveClick();
                dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            /**
             * Method that is called when the button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click
             * @param which Denotes which button was clicked
             */
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onConversationNegativeClick();
                dismiss();
            }
        });

        return dialogBuilder.create();
    }

    /**
     * Method that will return the content of the conversation.
     *
     * @return The associated view for this dialog.
     */
    public View getConversationContent()
    {
        return m_conversationContent;
    }
}
