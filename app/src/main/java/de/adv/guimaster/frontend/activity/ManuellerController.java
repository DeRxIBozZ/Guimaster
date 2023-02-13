package de.adv.guimaster.frontend.activity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.frontend.logic.Coordinates;
import de.adv.guimaster.frontend.uitools.CanvasViewController;
import de.adv.guimaster.frontend.logic.Constants;
import de.adv.guimaster.frontend.logic.CustomCanvas;
import de.adv.guimaster.frontend.logic.DataHolder;

public class ManuellerController extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ValueAnimator.AnimatorUpdateListener {

    int thicknessxy = 30;
    int posx = Constants.WZMWIDTH - thicknessxy;
    int posy = 0;
    int posz = 50;

    boolean positive;
    Coordinates xyz;
    private int stepwidth;
    int maxAnimationValue;
    int maxAnimationValueZ;
    ValueAnimator animator;
    ValueAnimator animatorz;

    CustomCanvas ca;
    CanvasViewController wzm;
    CanvasViewController bar;
    Bitmap bitmapxy;
    Bitmap bitmapz;

    SerialPort serialPort = (SerialPort) DataHolder.getInstance().retrieve("SerialPort");

    Spinner spinner;
    boolean tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ca = (CustomCanvas) DataHolder.getInstance().retrieve("CustomCanvas");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.manuellercontroller);
        wzm = findViewById(R.id.canvasViewController);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        SwitchCompat switchCompat = findViewById(R.id.switch1);
        View root = switchCompat.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> tool = b);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.comboitems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(3);
        wzm.setBitmap(ca.wzmbitmap);
        bitmapxy = ca.wzmbitmap;
        wzm.invalidate();
        bar = findViewById(R.id.canvasViewController2);
        bar.setBitmap(ca.depthbitmap);
        bitmapz = ca.depthbitmap;
        bar.invalidate();
        setAnimator(1);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int value = (int) valueAnimator.getAnimatedValue();
        updateBitmap(xyz,value);
    }

    private void updateBitmap(Coordinates xyz, int pos){
        switch (xyz){
            case X:
                updatexWzmBitmap(pos);
                break;
            case Y:
                updateyWzmBitmap(pos);
                break;
            case Z:
                updateZ(pos);
                break;
        }
    }

    private void updatexWzmBitmap(int value) {
        if (this.positive) {
            if (posx + value + thicknessxy < Constants.WZMWIDTH) {
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx + thicknessxy, 0, value, Constants.WZMHEIGHT);
                ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, posx, 0, value, Constants.WZMHEIGHT);
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx, posy, value, thicknessxy);
                if (value == maxAnimationValue || posx + value + thicknessxy + 1 == Constants.WZMWIDTH) {
                    posx += value;
                }
            }
        } else {
            if (posx >= 0) {
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx - value, 0, value, Constants.WZMHEIGHT);
                ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, posx - value + thicknessxy, 0, value, Constants.WZMHEIGHT);
                ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, posx - value + thicknessxy, posy, value, thicknessxy);
                if (value == maxAnimationValue || posx - value == 0) {
                    posx -= value;
                }
            }
        }
    }


    private void updateyWzmBitmap(int value) {
        if (this.positive) {
            if(posy + value + thicknessxy < Constants.WZMHEIGHT) {
                bitmapxy.setPixels(ca.whitepixels,0, Constants.WZMWIDTH,0,posy + thicknessxy , Constants.WZMWIDTH,value);
                bitmapxy.setPixels(ca.opaquepixels,0,Constants.WZMWIDTH,0,posy,Constants.WZMWIDTH,value);
                bitmapxy.setPixels(ca.whitepixels,0,Constants.WZMWIDTH,posx,posy,thicknessxy,value);
                if(value == maxAnimationValue || posy +  value + thicknessxy + 1 == Constants.WZMHEIGHT){
                    posy += value;
                }
            }
        } else {
            if(posy - value >= 0){
                bitmapxy.setPixels(ca.whitepixels,0, Constants.WZMWIDTH,0,posy - value, Constants.WZMWIDTH,value);
                bitmapxy.setPixels(ca.opaquepixels,0,Constants.WZMWIDTH,0,posy + thicknessxy - value,Constants.WZMWIDTH,value);
                bitmapxy.setPixels(ca.whitepixels,0,Constants.WZMWIDTH,posx,posy + thicknessxy - value,thicknessxy,value);
                if(value == maxAnimationValue || posy - value == 0) {
                    posy -= value;
                }
            }
        }
    }

    private void updateZ(int value){
        if(this.positive){
            if(posz + value < Constants.DEPTHHEIGHT){
                bitmapz.setPixels(ca.silverpixels,0,Constants.DEPTHWIDTH,0,posz,Constants.DEPTHWIDTH,value);
                if(value == maxAnimationValueZ || posz + value + 1 == Constants.DEPTHHEIGHT){
                    posz += value;
                }
            }
        } else{
            if(posz - value >= 0){
                bitmapz.setPixels(ca.opaquepixels,0,Constants.DEPTHWIDTH,0,posz - value,Constants.DEPTHWIDTH,value);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button7):
                if (posx <= 30) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else if(!this.animator.isRunning()){
                    xyz = Coordinates.X;
                    positive = true;
                    animator.start();
                    serialPort.moveAxis("y",stepwidth * 1000);
                }
                break;
            case (R.id.button12):
                if (posx >= Constants.WZMWIDTH) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else if(!this.animator.isRunning()){
                    xyz = Coordinates.X;
                    positive = false;
                    animator.start();
                    serialPort.moveAxis("y", stepwidth * -1000);
                }
                break;
            case (R.id.button9):
                if (posy <= 30) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    xyz = Coordinates.Y;
                    positive = true;
                    animator.start();
                    serialPort.moveAxis("x",stepwidth * 1000);
                }
                break;
            case (R.id.button10):
                if (posy >= Constants.WZMHEIGHT) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    xyz = Coordinates.Y;
                    positive = false;
                    animator.start();
                    serialPort.moveAxis("x",stepwidth * -1000);
                }
                break;
            case (R.id.button8):
                if (posz <= 0){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                    t1.show();
                } else if (!this.animatorz.isRunning()) {
                    xyz = Coordinates.Z;
                    positive = false;
                    animatorz.start();
                    serialPort.moveAxis("z",stepwidth * 1000);
                }
                break;
            case (R.id.button11):
                if(posz >= Constants.DEPTHHEIGHT){
                    Toast t1 = Toast.makeText(this,R.string.wzmpos,Toast.LENGTH_LONG);
                    t1.show();
                } else if (!this.animatorz.isRunning()){
                    xyz = Coordinates.Z;
                    positive = true;
                    animatorz.start();
                    serialPort.moveAxis("z", stepwidth * -1000);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
        float newstepwidth;
        if(tool && pos > 3){
            spinner.setSelection(3);
            Toast t = Toast.makeText(this,R.string.steptoolon,Toast.LENGTH_LONG);
            t.show();
            newstepwidth = 1;
        } else {
            String selectedItem = (String) parent.getItemAtPosition(pos);
            selectedItem = selectedItem.replace(",", ".");
            newstepwidth = Float.parseFloat(selectedItem);
        }
        setAnimator((int) newstepwidth);
        stepwidth = (int) newstepwidth;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    public void setAnimator(int stepwidth){
        maxAnimationValue = stepwidth;
        maxAnimationValueZ = stepwidth * 3;
        animator = ValueAnimator.ofInt(0,maxAnimationValue);
        animator.setDuration(50L * stepwidth);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(this);
        animatorz = ValueAnimator.ofInt(0,maxAnimationValueZ);
        animatorz.setDuration(30L * stepwidth);
        animatorz.setRepeatCount(0);
        animatorz.setInterpolator(new LinearInterpolator());
        animatorz.addUpdateListener(this);
    }

}


/*
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
 */