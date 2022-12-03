package de.adv.guimaster;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import de.adv.guimaster.logic.Constants;
import de.adv.guimaster.logic.CustomCanvas;
import de.adv.guimaster.logic.DataHolder;
import de.adv.guimaster.logic.MatrixArray;

public class ManuellerController extends AppCompatActivity implements View.OnClickListener {

    int posx = Constants.WZMWIDTH;
    int posy = Constants.WZMHEIGHT;
    int posz = 50;
    CustomCanvas ca;
    CanvasViewController wzm;
    CanvasViewController bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ca = (CustomCanvas) DataHolder.getInstance().retrieve("CustomCanvas");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuellercontroller);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        wzm = findViewById(R.id.canvasViewController);
        wzm.setBitmap(ca.wzmbitmap);
        wzm.invalidate();
        bar = findViewById(R.id.canvasViewController2);
        bar.setBitmap(ca.depthbitmap);
        bar.invalidate();
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
            ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, x, 0, diff, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x2, 0, diff, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x, posy - 30, diff, 30);
        } else {
            x = posxnew - diff;
            x2 = posx - 30;
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x, 0, diff, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, x2, 0, diff, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x2, posy - 30, diff, 30);
        }
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
                ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, 0, y, Constants.WZMWIDTH, diff);
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, 0, y2, Constants.WZMWIDTH, diff);
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx - 30, y, 30, diff);

        } else {
            y = posynew - diff;
            y2 = posy - 30;
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, 0, y, Constants.WZMWIDTH, diff);
                ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, 0, y2, Constants.WZMWIDTH, diff);
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx - 30, y2, 30, diff);

        }
        posy = posynew;
    }

    private void updateZ(int posznew){
        if(posznew < 0) posz = 0;
        if(posznew > (Constants.DEPTHHEIGHT - 50)) posz = Constants.DEPTHHEIGHT - 50;
        int diff = posz - posznew;
        if(diff > 0){
            ca.depthbitmap.setPixels(ca.opaquepixels,0,Constants.DEPTHWIDTH,0,posznew ,Constants.DEPTHWIDTH,diff);
        } else {
            diff *= -1;
            ca.depthbitmap.setPixels(ca.silverpixels,0,Constants.DEPTHWIDTH,0,posz,Constants.DEPTHWIDTH,diff);
        }
        posz = posznew;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button7):
                if (posx <= 30) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posxnew = posx - 10;
                    updatexWzmBitmap(posxnew);
                }
                break;
            case (R.id.button12):
                if (posx >= Constants.WZMWIDTH) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posxnew = posx + 10;
                    updatexWzmBitmap(posxnew);
                }
                break;
            case (R.id.button9):
                if (posy <= 30) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posynew = posy - 10;
                    updateyWzmBitmap(posynew);
                }
                break;
            case (R.id.button10):
                if (posy >= Constants.WZMHEIGHT) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posynew = posy + 10;
                    updateyWzmBitmap(posynew);
                }
                break;
            case (R.id.button8):
                if (posz == 0){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posznew = posz - 50;
                    updateZ(posznew);
                }
                break;
            case (R.id.button11):
                if(posz >= Constants.DEPTHHEIGHT){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posznew = posz + 50;
                    updateZ(posznew);
                }
                break;
        }
    }

}
