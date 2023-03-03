package de.adv.guimaster.frontend.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.OutputStream;

import de.adv.guimaster.R;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.DrawingCanvas;
import de.adv.guimaster.frontend.uitools.Forms;

public class ZeichenTool extends AppCompatActivity {

    DataHolder holder = DataHolder.getInstance();
    SeekBar mThickness;
    private DrawingCanvas mDrawLayout;
    Button erase;
    private Paint drawPaint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zeichentool);
        Toolbar bar = findViewById(R.id.drawingtoolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mThickness = findViewById(R.id.thickness);
        View root = mThickness.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        mDrawLayout = findViewById(R.id.viewDraw);
        mDrawLayout.setVisibility(View.VISIBLE);
        mDrawLayout.setEnabled(true);
        mThickness.setMax(50);
        mThickness.setProgress(10);
        mDrawLayout.setPaintAlpha(mThickness.getProgress());
        int currLevel = mDrawLayout.getPaintAlpha();
        mThickness.setProgress(currLevel);
        mDrawLayout.invalidate();
        erase = findViewById(R.id.erase);
        findViewById(R.id.draw).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.FREE_FORM);
        });
        findViewById(R.id.buttonRec).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.RECT);
        });
        findViewById(R.id.buttonLine).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.LINE);
        });
        findViewById(R.id.buttonCircle).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.CIRCLE);
        });



        erase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawPaint.setColor(Color.TRANSPARENT);
                mDrawLayout.setErase(true);

            }
        });


        mThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mDrawLayout.setPaintAlpha(mThickness.getProgress());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
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








