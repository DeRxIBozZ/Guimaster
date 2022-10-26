package de.adv.guimaster;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.adv.guimaster.logic.Fragmentdata;


public class Plateformat extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plateformat, container, false);
    }

    public void onBack(){
        Intent i1 = new Intent(getActivity(),MainActivity.class);
        startActivity(i1);
    }

    public void onSubmit(){
        View root = getView();
        EditText length = root.findViewById(R.id.editTextNumber3);
        Fragmentdata.length  =  Integer.parseInt(length.getText().toString());
        EditText width = root.findViewById(R.id.editTextNumber4);
        Fragmentdata.width  =  Integer.parseInt(width.getText().toString());
        Intent i1 = new Intent(getActivity(),ZeichenTool.class);
        startActivity(i1);
    }
}