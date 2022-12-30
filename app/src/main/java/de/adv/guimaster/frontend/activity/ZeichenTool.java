package de.adv.guimaster.frontend.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import de.adv.guimaster.R;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.CanvasViewController;
import de.adv.guimaster.frontend.uitools.DimensionDialog;

public class ZeichenTool extends AppCompatActivity {

    DataHolder holder = DataHolder.getInstance();
    Spinner spinner;
    Button btn;
    CanvasViewController canvasViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zeichentool);
        DialogFragment newFragment = DimensionDialog.newInstance(this);
        newFragment.show(getSupportFragmentManager(),"dialog");

        spinner = (Spinner) findViewById(R.id.planets_spinner);
        btn = (Button) findViewById(R.id.button14);
        canvasViewController = (CanvasViewController) findViewById(R.id.canvasViewController4);
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








