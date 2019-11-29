package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class AddContactDialog extends DialogFragment
{
    private ContactDialogListener m_dialogListener;
    private View m_contactContent;

    public interface ContactDialogListener
    {
        void onContactPositiveClick();
        void onContactNegativeClick();
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
            m_dialogListener = (ContactDialogListener) context;
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("Add Contact");
        m_contactContent = requireActivity().getLayoutInflater().inflate(R.layout.contact_view, null);
        dialogBuilder.setView(m_contactContent);

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

    public View getContactContent()
    {
        return m_contactContent;
    }
}