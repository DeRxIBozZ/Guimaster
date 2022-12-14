package de.adv.guimaster.frontend.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import de.adv.guimaster.R;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.DimensionDialog;

public class ZeichenTool extends AppCompatActivity {

    DataHolder holder = DataHolder.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zeichentool);
        DialogFragment newFragment = DimensionDialog.newInstance(this);
        newFragment.show(getSupportFragmentManager(),"dialog");

    }


    public void afterDialogClose(){
        int length = (Integer) holder.retrieve("length");
        int width = (Integer) holder.retrieve("width");
        int colors = length * width;
        int[] aColors = new int[colors];
        for(int i = 0; i < colors; i++){
            aColors[i] = ContextCompat.getColor(this,R.color.white);
        }
        Bitmap map = Bitmap.createBitmap(aColors,length,width,Bitmap.Config.RGBA_F16);
        Canvas canvas = new Canvas(map);
    }
}








