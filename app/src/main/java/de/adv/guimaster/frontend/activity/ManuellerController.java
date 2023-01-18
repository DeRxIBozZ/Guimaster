package de.adv.guimaster.frontend.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
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
import de.adv.guimaster.backend.Instructions;
import de.adv.guimaster.frontend.logic.Coordinates;
import de.adv.guimaster.frontend.uitools.CanvasViewController;
import de.adv.guimaster.frontend.logic.Constants;
import de.adv.guimaster.frontend.logic.CustomCanvas;
import de.adv.guimaster.frontend.logic.DataHolder;

public class ManuellerController extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ValueAnimator.AnimatorUpdateListener {

    int posx = Constants.WZMWIDTH;
    int posy = Constants.WZMHEIGHT;
    int posz = 50;
    int stepwidth = 1;
    int maxAnimationValue = 100;
    int maxAnimationValueZ = 300;
    boolean positive;
    CustomCanvas ca;
    CanvasViewController wzm;
    CanvasViewController bar;
    Coordinates xyz;
    ValueAnimator animator;
    ValueAnimator animatorz;
    Spinner spinner;
    boolean tool;
    Instructions instructions = new Instructions();

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
        wzm = findViewById(R.id.canvasViewController);
        wzm.setBitmap(ca.wzmbitmap);
        wzm.invalidate();
        bar = findViewById(R.id.canvasViewController2);
        bar.setBitmap(ca.depthbitmap);
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

        if (this.posx < 40 && this.positive) {
            this.posx = 40;
        }
        if (this.posx > Constants.WZMWIDTH && !this.positive) {
            this.posx = Constants.WZMWIDTH;
        }
        int x;
        int x2;
        if (this.positive) {
            x = posx - value;
            x2 = posx - 30 - value;
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x2, 0, value, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, x, 0, value, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x, posy - 30, value, 30);
        } else {
            x = posx;
            x2 = posx - 30;
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x, 0, value, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.opaquepixels, 0, Constants.WZMWIDTH, x2, 0, value, Constants.WZMHEIGHT);
            ca.wzmbitmap.setPixels(ca.whitepixels, 0, Constants.WZMWIDTH, x2, posy - 30, value, 30);
        }
        if(value == maxAnimationValue){
            if(this.positive){
                posx -= value;
            } else{
                posx += value;
            }
        }
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

    private void updateZ(int value){
        if(posz > (Constants.DEPTHHEIGHT - maxAnimationValueZ) && positive) {
            if((posz - Constants.DEPTHHEIGHT + maxAnimationValueZ) >= value) {
                ca.depthbitmap.setPixels(ca.silverpixels, 0, Constants.DEPTHWIDTH, 0, posz, Constants.DEPTHWIDTH, value);
            }
        } else if (posz < maxAnimationValueZ && !positive) {
            if(posz >= value){
                ca.depthbitmap.setPixels(ca.opaquepixels,0,Constants.DEPTHWIDTH,0,posz-value,Constants.DEPTHWIDTH,value);
            }
        } else if(positive) {
            ca.depthbitmap.setPixels(ca.silverpixels,0,Constants.DEPTHWIDTH,0,posz,Constants.DEPTHWIDTH,value);
        } else{
            int poszmin = posz - value;
            ca.depthbitmap.setPixels(ca.opaquepixels,0,Constants.DEPTHWIDTH,0,poszmin ,Constants.DEPTHWIDTH,value);
        }
        if(value == maxAnimationValueZ){
            if(positive){
                posz = posz + value;
            } else {
                posz = posz - value;
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
                    try {
                        instructions.moveAxis("y", stepwidth * 1000);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
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
                    try {
                        instructions.moveAxis("y", stepwidth * 1000 * -1);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
                }
                break;
            case (R.id.button9):
                if (posy <= 30) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posynew = posy - 10;
                    updateyWzmBitmap(posynew);
                    try {
                        instructions.moveAxis("x", stepwidth * 1000);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
                }
                break;
            case (R.id.button10):
                if (posy >= Constants.WZMHEIGHT) {
                    Toast t1 = Toast.makeText(this, R.string.wzmpos, Toast.LENGTH_LONG);
                    t1.show();
                } else {
                    int posynew = posy + 10;
                    updateyWzmBitmap(posynew);
                    try {
                        instructions.moveAxis("x", stepwidth * 1000 * -1);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
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
                    try {
                        instructions.moveAxis("z", stepwidth * 100 * -1);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
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
                    try {
                        instructions.moveAxis("z", stepwidth * 100);
                    } catch (Exception e) {
                        Log.d("Kessli_Moment", "Felix stinkt");
                    }
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
        maxAnimationValue = (int) (100 * newstepwidth);
        maxAnimationValueZ = maxAnimationValue * 3;
        setAnimator((int) newstepwidth);
        stepwidth = (int) newstepwidth;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    public void setAnimator(int stepwidth){
        animator = ValueAnimator.ofInt(1,maxAnimationValue);
        animator.setDuration(150L * stepwidth);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(this);
        animatorz = ValueAnimator.ofInt(1,maxAnimationValueZ);
        animatorz.setDuration(450L * stepwidth);
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