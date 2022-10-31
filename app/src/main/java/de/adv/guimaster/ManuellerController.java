package de.adv.guimaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import de.adv.guimaster.logic.MatrixArray;

public class ManuellerController extends AppCompatActivity {

    int wzmwidth = 716;
    int wzmheight = 632;
    int [][] wzma = new int[wzmwidth][wzmheight];
    Bitmap wzmb;
    CanvasViewController wzm;

    int barwidth = 126;
    int barheight = 632;
    int [][] bara = new int[barwidth][barheight];
    Bitmap barb;
    CanvasViewController bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuellercontroller);
        wzm = findViewById(R.id.canvasViewController);
        for(int i = 0; i < wzmwidth; i++){
            for(int j = 0; j < wzmheight; j++){
                wzma[i][j] = getColor(R.color.purple_200);
            }
        }
        wzmb = Bitmap.createBitmap(MatrixArray.toArray(wzma),wzmwidth,wzmheight,Bitmap.Config.ARGB_8888);
        wzmb = wzmb.copy(Bitmap.Config.ARGB_8888,true);
        wzm.setBitmap(wzmb);
        wzm.invalidate();

        bar = findViewById(R.id.canvasViewController2);
        for(int i = 0; i < barwidth; i++){
            for(int j = 0; j < barheight; j++){
                bara[i][j] = getColor(R.color.black);
            }
        }
        barb = Bitmap.createBitmap(MatrixArray.toArray(bara),barwidth,barheight,Bitmap.Config.ARGB_8888);
        barb = barb.copy(Bitmap.Config.ARGB_8888,true);
        bar.setBitmap(barb);
        bar.invalidate();
    }
}
