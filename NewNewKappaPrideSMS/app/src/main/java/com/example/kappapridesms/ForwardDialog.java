package com.example.kappapridesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

public class ForwardDialog extends DialogFragment
{
    private ForwardDialogListener m_dialogListener;
    private View m_forwardContent;

    public interface ForwardDialogListener
    {
        void onForwardPositiveClickListener();
        void onForwardNegativeClickListener();
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
            m_dialogListener = (ForwardDialogListener) context;
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

        dialogBuilder.setTitle("Forward");
        m_forwardContent = requireActivity().getLayoutInflater().inflate(R.layout.forward_view, null);
        dialogBuilder.setView(m_forwardContent);

        dialogBuilder.setPositiveButton("Forward", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onForwardPositiveClickListener();
                dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                m_dialogListener.onForwardNegativeClickListener();
                dismiss();
            }
        });

        return dialogBuilder.create();
    }

    public View getForwardContent()
    {
        return m_forwardContent;
    }
}
