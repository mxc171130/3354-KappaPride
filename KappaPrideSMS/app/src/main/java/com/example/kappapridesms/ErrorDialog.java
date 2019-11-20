package com.example.errordialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ErrorDialog extends DialogFragment
{
    private String content="Message cannot be sent";
    public static ErrorDialog newInstance()
    {
        ErrorDialog frag=new ErrorDialog();
        Bundle args= new Bundle();
        frag.setArguments(args);
        return frag;

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("ERROR").setMessage(content).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }
}
