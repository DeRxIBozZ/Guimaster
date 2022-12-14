package de.adv.guimaster.frontend.uitools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.frontend.activity.ConnectionActivity;
import de.adv.guimaster.frontend.logic.DataHolder;


public class PermissionDialog extends DialogFragment implements View.OnClickListener{

    public static PermissionDialog newInstance() {
        return new PermissionDialog();
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_connection, null);
        TextView textview = view.findViewById(R.id.textView2);
        textview.setText(R.string.permission);
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
        DataHolder holder = DataHolder.getInstance();
        SerialPort serialPort = (SerialPort) holder.retrieve("SerialPort");
        ConnectionActivity activity = (ConnectionActivity) holder.retrieve("ConnectionActivity");
        serialPort.requestPermission(activity);
    }

}
