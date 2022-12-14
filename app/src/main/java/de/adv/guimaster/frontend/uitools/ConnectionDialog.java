package de.adv.guimaster.frontend.uitools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import de.adv.guimaster.R;
import de.adv.guimaster.frontend.activity.ConnectionActivity;


public class ConnectionDialog extends DialogFragment implements View.OnClickListener{

    public ConnectionActivity connectionActivity;


    public static ConnectionDialog newInstance(ConnectionActivity activity) {
        return new ConnectionDialog(activity);
    }

    public ConnectionDialog(ConnectionActivity activity){
        this.connectionActivity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_connection, null);
        view.findViewById(R.id.button13).setOnClickListener(this);
        setCancelable(false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        return dialog;

    }


    @Override
    public void onClick(View v){
        this.dismiss();
        this.connectionActivity.attachDevice();
    }

}

