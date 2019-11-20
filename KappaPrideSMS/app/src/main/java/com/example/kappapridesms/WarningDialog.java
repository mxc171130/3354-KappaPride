package com.example.warningdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class WarningDialog extends DialogFragment {
    private String content = "Warning, you are about to delete your message";

    /**
     *
     * @param savedInstancesState
     *
     */
    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
    }

    /**
     *
     * @param title
     * @return
     */
    public static WarningDialog newInstance(String title)
    {
        WarningDialog frag=new WarningDialog();
        Bundle args= new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        return frag;

    }

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("WARNING").setMessage(content).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        return builder.create();
    }
}


