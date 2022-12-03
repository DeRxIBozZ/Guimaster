package de.adv.guimaster.frontend.uitools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import de.adv.guimaster.R;
import de.adv.guimaster.frontend.activity.MainActivity;
import de.adv.guimaster.frontend.activity.ZeichenTool;
import de.adv.guimaster.frontend.logic.Fragmentdata;

public class DimensionDialog extends DialogFragment implements View.OnClickListener, View.OnKeyListener {

    public ZeichenTool ztool;



    public static DimensionDialog newInstance(ZeichenTool ztool) {
        DimensionDialog dd = new DimensionDialog();
        ztool = ztool;
        return dd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_dimension, null);
        view.findViewById(R.id.button5).setOnClickListener(this);
        view.findViewById(R.id.button6).setOnClickListener(this);
        view.findViewById(R.id.editTextNumber3).setOnKeyListener(this);
        view.findViewById(R.id.editTextNumber4).setOnKeyListener(this);
        TextView textview = view.findViewById(R.id.textView8);
        textview.setText(getString(R.string.dimension_fragment));
        setCancelable(false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        return dialog;

    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case (R.id.button5) :
                if(Fragmentdata.length != "" && Fragmentdata.width != "") {
                    this.dismiss();
                    ztool.afterDialogClose();
                    break;
                } else {
                    Context context = getActivity().getApplicationContext();
                    CharSequence text = getString(R.string.values);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            case (R.id.button6) :
                this.dismiss();
                Intent i1 = new Intent(getActivity(), MainActivity.class);
                startActivity(i1);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText length= v.findViewById(R.id.editTextNumber3);
                Fragmentdata.length = length.getText().toString();
                EditText width = v.findViewById(R.id.editTextNumber4);
                Fragmentdata.width = width.getText().toString();
                return true;
    }
}
