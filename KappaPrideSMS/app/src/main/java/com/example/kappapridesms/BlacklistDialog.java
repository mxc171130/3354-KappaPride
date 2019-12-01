package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class BlacklistDialog extends DialogFragment
{
    private String content = "Blacklist this number?";
    private BlacklistDialogListener m_blacklistListener;

    public interface BlacklistDialogListener
    {
        void onBlacklistPositiveClick();
        void onBlacklistNegativeClick();
    }


    public static ErrorDialog newInstance()
    {
        ErrorDialog frag=new ErrorDialog();
        Bundle args= new Bundle();
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            // Instantiate the NoticeDialogListener so we can send events to the host
            m_blacklistListener = (BlacklistDialog.BlacklistDialogListener) context;
        }catch(ClassCastException e)
        {
            // Context does not implement ForwardDialogListener
            throw new ClassCastException("Context instance does not implement ForwardDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("Blacklist").setMessage(content).setPositiveButton("Blacklist", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                m_blacklistListener.onBlacklistPositiveClick();
                dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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