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
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.OutputStream;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.DimensionDialog;
import de.adv.guimaster.frontend.uitools.DrawingCanvas;

public class ZeichenTool extends AppCompatActivity {

    DataHolder holder = DataHolder.getInstance();
    SeekBar mThickness;
    private DrawingCanvas mDrawLayout;
    Button erase, draw;
    private Paint drawPaint = new Paint();
    int quality;
    OutputStream outstream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zeichentool);
        Toolbar bar = findViewById(R.id.drawingtoolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mThickness = (SeekBar) findViewById(R.id.thickness);
        View root = mThickness.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        mDrawLayout = (DrawingCanvas) findViewById(R.id.viewDraw);
        erase = (Button) findViewById(R.id.erase);
        draw= (Button) findViewById(R.id.draw);

        mDrawLayout.setVisibility(View.VISIBLE);
        //mDrawLayout.setDrawingCacheEnabled(true);
        mDrawLayout.setEnabled(true);
        mThickness.setMax(50);
        mThickness.setProgress(10);
        mDrawLayout.setPaintAlpha(mThickness.getProgress());
        int currLevel = mDrawLayout.getPaintAlpha();
        mThickness.setProgress(currLevel);
        mDrawLayout.invalidate();

        erase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawPaint.setColor(Color.TRANSPARENT);
                mDrawLayout.setErase(true);

            }
        });

        draw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDrawLayout.setErase(false);

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








