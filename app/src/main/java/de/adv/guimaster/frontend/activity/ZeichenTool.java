package de.adv.guimaster.frontend.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.OutputStream;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;
import de.adv.guimaster.frontend.logic.DataHolder;
import de.adv.guimaster.frontend.uitools.DrawingCanvas;
import de.adv.guimaster.frontend.uitools.Forms;

public class ZeichenTool extends AppCompatActivity {

    ImageView imgView;
    DataHolder holder = DataHolder.getInstance();
    SeekBar mThickness;
    private DrawingCanvas mDrawLayout;
    Button erase;
    private Paint drawPaint = new Paint();
    OutputStream outstream;
    int quality;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zeichentool);
        Toolbar bar = findViewById(R.id.drawingtoolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mThickness = findViewById(R.id.thickness);
        View root = mThickness.getRootView();
        Button btnMillONPlate = findViewById(R.id.millOnplate);
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        mDrawLayout = findViewById(R.id.viewDraw);
        erase = findViewById(R.id.erase);
        findViewById(R.id.draw).setOnClickListener(v -> {
            mDrawLayout.setErase(false);
            mDrawLayout.setForms(Forms.FREE_FORM);
        });
        findViewById(R.id.buttonRec).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.RECT);
        });
        findViewById(R.id.buttonLine).setOnClickListener(v -> {
            mDrawLayout.setErase(false);
            mDrawLayout.setForms(Forms.LINE);
        });
        findViewById(R.id.buttonCircle).setOnClickListener(v -> {
            mDrawLayout.setForms(Forms.CIRCLE);
        });
        findViewById(R.id.ClearAll).setOnClickListener(v -> {
            mDrawLayout.eraseAll();
            mDrawLayout.invalidate();
        });

        mDrawLayout.setVisibility(View.VISIBLE);
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
                mDrawLayout.setForms(Forms.FREE_FORM);
                drawPaint.setColor(Color.TRANSPARENT);
                mDrawLayout.setErase(true);
            }
        });

//        draw.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                mDrawLayout.setErase(false);

//            }
//        });

        btnMillONPlate.setOnClickListener(v ->{
            imgView.setImageBitmap(mDrawLayout.getDrawingCache());
            Bitmap bitmap = ConvertBitmaptoPNG.createBitmapFromView(imgView,0,0);
            Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,src);
            Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.threshold(gray,src,127,255,Imgproc.THRESH_BINARY);
            Utils.matToBitmap(src,bitmap);
            imgView.setImageBitmap(bitmap);
            ConvertBitmaptoPNG.compressBitmap(c, bitmap, quality, outstream);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


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








