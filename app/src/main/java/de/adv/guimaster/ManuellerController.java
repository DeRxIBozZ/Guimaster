package de.adv.guimaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.adv.guimaster.logic.Constants;
import de.adv.guimaster.logic.CustomCanvas;
import de.adv.guimaster.logic.MatrixArray;

public class ManuellerController extends AppCompatActivity implements View.OnClickListener{

    int posx;
    int posy;
    Bitmap wzmb ;
    int[][] wzmm;
    int[] whitepixelswzmheight = new int[10*Constants.WZMHEIGHT];
    int[] opaquepixelswzmheight = new int[10*Constants.WZMHEIGHT];
    CanvasViewController wzm;
    public static CustomCanvas initCanvas(){
        int [][] matrix = new int[Constants.WZMWIDTH][Constants.WZMHEIGHT];
        for(int i = 0; i < Constants.WZMWIDTH; i++){
            for (int j = 0; j < Constants.WZMHEIGHT; j++){
                if (j > (Constants.WZMHEIGHT - 30) || i > (Constants.WZMWIDTH - 30)) {
                    matrix[i][j] = android.graphics.Color.argb(255,255,255,255);
                } else {
                    matrix[i][j] = android.graphics.Color.argb(0,0,0,0);
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(MatrixArray.toArray(matrix),Constants.WZMWIDTH,Constants.WZMHEIGHT,Bitmap.Config.ARGB_8888);
        return new CustomCanvas(matrix,bitmap);
    };
    int barwidth = 126;
    int barheight = 632;
    int [][] bara = new int[barwidth][barheight];
    Bitmap barb;
    CanvasViewController bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.wzmb = Constants.WZMCANVAS.getBitmap();
        this.wzmm = Constants.WZMCANVAS.getMatrix();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuellercontroller);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
        wzm = findViewById(R.id.canvasViewController);
        wzmb = wzmb.copy(Bitmap.Config.ARGB_8888,true);
        wzm.setBitmap(wzmb);
        wzm.invalidate();
        for(int i = 0; i < whitepixelswzmheight.length; i++){
            whitepixelswzmheight[i] = android.graphics.Color.argb(255,255,255,255);
        }
        for(int i = 0; i < opaquepixelswzmheight.length; i++){
            opaquepixelswzmheight[i] = android.graphics.Color.argb(0,0,0,0);
        }
        //bar = findViewById(R.id.canvasViewController2);
        /*for(int i = 0; i < barwidth; i++){
            for(int j = 0; j < barheight; j++){
                bara[i][j] = getColor(R.color.black);
            }
        }
        barb = Bitmap.createBitmap(MatrixArray.toArray(bara),barwidth,barheight,Bitmap.Config.ARGB_8888);
        barb = barb.copy(Bitmap.Config.ARGB_8888,true);
        bar.setBitmap(barb);
        bar.invalidate();*/
    }

    private void updatexWzmBitmap(int posxnew) {

        if (posxnew < 30) {
            posxnew = 30;
        }
        if (posxnew > Constants.WZMWIDTH) {
            posxnew = Constants.WZMWIDTH;
        }
        int diff = posxnew - posx;
        int x;
        int x2;
        if (diff < 0) {
            x = posx + diff;
            x2 = posxnew - 30;
            diff *= -1;
            wzmb.setPixels(opaquepixelswzmheight, 0, 0, x, 0, diff, Constants.WZMHEIGHT);
            wzmb.setPixels(whitepixelswzmheight, 0, 0, x2, 0, diff, Constants.WZMHEIGHT);
        } else {
            x = posxnew - diff;
            x2 = posx - 30;
            wzmb.setPixels(whitepixelswzmheight, 0, 0, x, 0, diff, Constants.WZMHEIGHT);
            wzmb.setPixels(opaquepixelswzmheight, 0, 0, x2, 0, diff, Constants.WZMHEIGHT);
        }
        wzm.invalidate();
        posx = posxnew;
    }


    private void updateyWzmBitmap(int posynew) {

        if (posynew < 30) {
            posynew = 30;
        }
        if (posynew > Constants.WZMHEIGHT) {
            posynew = Constants.WZMHEIGHT;
        }
        int diff = posynew - posy;
        int y;
        int y2;
        if (diff < 0) {
            y = posy + diff;
            y2 = posynew - 30;
            diff *= -1;
            wzmb.setPixels(opaquepixelswzmheight, 0, 0, 0, y, Constants.WZMWIDTH, diff);
            wzmb.setPixels(whitepixelswzmheight, 0, 0, 0, y2, Constants.WZMWIDTH, diff);
        } else {
            y = posynew - diff;
            y2 = posy - 30;
            wzmb.setPixels(whitepixelswzmheight, 0, 0, 0, y, diff, Constants.WZMHEIGHT);
            wzmb.setPixels(opaquepixelswzmheight, 0, 0, 0, y2, diff, Constants.WZMHEIGHT);
        }
        wzm.invalidate();
        posy = posynew;
    }

        /*if(posy < 30){
            posy = 30;
        }
        if(posy > wzmheight){
            posy = wzmheight;
        }
        for(int i = 0; i < wzmwidth; i++){
            for(int j = 0; j < wzmheight; j++){
                if(posy-30 < i && i < posy){
                    wzma[i][j] = getColor(R.color.black);
                } else if(posx-30 < j && j < posx){
                    wzma[i][j] = getColor(R.color.black);
                } else{
                    wzma[i][j] = getColor(R.color.white);
                }
            }
        }*/


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case (R.id.button7) :
                if(posx <= 30){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                } else {
                    int posxnew = posx - 10;
                    updatexWzmBitmap(posxnew);
                }
                break;
            case (R.id.button12) :
                if(posx >= Constants.WZMWIDTH){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                } else {
                    int posxnew = posx + 10;
                    updatexWzmBitmap(posxnew);
                }
                break;
            case (R.id.button9) :
                if(posy <= 30){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                } else {
                    int posynew = posy - 10;
                    updateyWzmBitmap(posynew);
                }
                break;
            case (R.id.button10) :
                if(posy >= Constants.WZMHEIGHT){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                } else {
                    int posynew = posy + 10;
                    updateyWzmBitmap(posynew);
                }
                break;
        }
    }
}
