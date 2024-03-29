package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

/**
 * Responsible for generating a dialog testbox if there is an error.
 * <p>
 *     Contains one private attribute:
 *     <p>
 *         - m_content
 *     </p>
 * </p>
 * <p>
 *     Contains two public methods:
 *     <p>
 *         - newInstance()
 *         - onCreateDialog(Bundle savedInstanceState)
 *     </p>
 * </p>
 *
 * @author Nathan Beck
 */
public class ErrorDialog extends DialogFragment
{
    /**
     * String that contains the dialog for an error message.
     */
    private String m_content = "Message cannot be sent";

    /**
     * Creates the dialog so that the user is notified when
     * there is an error. Allows the user to dismiss the
     * error as well.
     *
     * @param savedInstanceState default saved instance state
     * @return builder.create() an instance of Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ERROR").setMessage(m_content).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }
}
