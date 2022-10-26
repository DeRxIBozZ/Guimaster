package de.adv.guimaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class DimensionDialog extends DialogFragment implements View.OnClickListener {

    public static String title;

    public static DimensionDialog newInstance() {
        DimensionDialog dd = new DimensionDialog();
        return dd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_plateformat, null);
        view.findViewById(R.id.button5).setOnClickListener(this);
        view.findViewById(R.id.button6).setOnClickListener(this);
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
                Intent i1 = new Intent(getActivity(),ZeichenTool.class);
                startActivity(i1);
                break;
            case (R.id.button6) :
                this.dismiss();
        }
    }
}
