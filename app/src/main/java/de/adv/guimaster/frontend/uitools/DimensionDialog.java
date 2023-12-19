package de.adv.guimaster.frontend.uitools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.BackendRunnable;
import de.adv.guimaster.frontend.logic.DataHolder;

public class DimensionDialog extends DialogFragment {


    String swidth;
    String sheight;
    Context context;
    EditText widthEditText;
    EditText heightEditText;


    public static DimensionDialog newInstance(Context c) {
        return new DimensionDialog(c);
    }

    public DimensionDialog(Context c) {
        context = c;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_dimension, null);
        widthEditText = view.findViewById(R.id.editTextNumber3);
        heightEditText = view.findViewById(R.id.editTextNumber4);
        TextView textview = view.findViewById(R.id.textView8);
        textview.setText(getString(R.string.dimension_fragment));
        view.findViewById(R.id.button5).setOnClickListener(l -> onSubmit());
        view.findViewById(R.id.button6).setOnClickListener(l -> onCancel());
        setCancelable(false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        return dialog;

    }


    public void onCancel(){
        this.dismiss();
    }

    public void onSubmit() {
        swidth = widthEditText.getText().toString();
        sheight = heightEditText.getText().toString();
        if(!swidth.equals("") && !sheight.equals("") && !swidth.equals("0") && !sheight.equals("0")){
            SerialPort.height = Integer.parseInt(sheight);
            SerialPort.width = Integer.parseInt(swidth);
            AlertDialog.Builder mill = new AlertDialog.Builder(context)
                    .setTitle(R.string.mill)
                    .setMessage(getString(R.string.milldescription) + "\nLänge: " + SerialPort.width + "mm\nBreite: " + SerialPort.height + "mm")
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        //openStandbyDialog();
                        Thread backend = new Thread(SerialPort.runnable);
                        backend.start();
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.back, (dialogInterface, i) -> {
                        dialogInterface.dismiss();

                    });
            AlertDialog dialog = mill.create();
            this.dismiss();
            dialog.show();
        } else {
            Toast t1 = Toast.makeText(context,R.string.values,Toast.LENGTH_LONG);
            t1.show();
        }
    }
}
