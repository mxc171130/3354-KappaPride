package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

/**
 *  Displays a warning dialog if a user wants to delete their message.
 *  This class extends the DialogFragment class that is available in
 *  in the Android API.
 * <p>
 *     Contains one private attributes:
 *     <p>
 *         - m_content
 *     </p>
 *
 *     Contains three public methods:
 *     <p>
 *         - onCreate(Bundle savedInstancesState)
 *     </p>
 *     <p>
 *         - newInstance(String title)
 *     </p>
 *     <p>
 *         - onCreateDialog(Bundle savedInstanceState)
 *     </p>
 *
 * </p>
 *
 * edited by Mohammad Shalabi
 */
public class WarningDialog extends DialogFragment
{
    private String m_content = "Warning! You are about to delete your message.";
    private WarningDialogListener m_warningListener;

    public interface WarningDialogListener
    {
        void onWarningPositiveClick();
        void onWarningNegativeClick();
    }


    /**
     * Calls the super class onCreate method to create an
     * instance of a dialog.
     * @param savedInstancesState default saved instance state
     *
     */
    @Override
    public void onCreate(Bundle savedInstancesState)
    {
        super.onCreate(savedInstancesState);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            m_warningListener = (WarningDialogListener) context;
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }


    /**
     * Creates a new instance of a WarningDialog fragment
     * @param title title of WarningDialog fragment
     * @return frag WarningDialog fragment
     */
    public static WarningDialog newInstance(String title)
    {
        WarningDialog frag = new WarningDialog();
        Bundle args = new Bundle();
        args.putString("Title",title);
        frag.setArguments(args);
        return frag;
    }

    /**
     * Creates the dialog so that the user gets a warning before
     * they delete a message. Allows the user to cancel deleting
     * the message as well.
     *
     * @param savedInstanceState default saved instance state
     * @return builder.create() an instance of Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("WARNING").setMessage(m_content).setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            // Deletes the message
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                m_warningListener.onWarningPositiveClick();
                dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            // cancels deleting the message
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                m_warningListener.onWarningNegativeClick();
                dismiss();
            }
        });

        return builder.create();
    }
}


