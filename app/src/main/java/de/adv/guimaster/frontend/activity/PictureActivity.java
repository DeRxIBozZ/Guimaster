package de.adv.guimaster.frontend.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Iterator;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;

public class PictureActivity extends AppCompatActivity {

    ImageView iv;
    Button btn;
    Button previewBtn;
    Button takePicBtn;
    Button btnMillONPlate;
    TextView textView;
    Intent intent1;
    Uri bilduri;
    Bitmap bm;
    InputStream is;
    int quality;
    OutputStream outstream;
    SeekBar thresholdseek;
    Uri picUri;
    File takenPicture;
    Context context = this;
    int cannythreshold = 90;
    int cannyratio = 1;

    /*
    Canny does use two thresholds (upper and lower): cannythreshold = lower, cannythreshold*cannyratio = upper
    If a pixel gradient is higher than the upper threshold, the pixel is accepted as an edge
    If a pixel gradient value is below the lower threshold, then it is rejected.
    If the pixel gradient is between the two thresholds, then it will be accepted only if it is connected to a pixel that is above the upper threshold.
    Canny recommended a upper:lower ratio between 2:1 and 3:1.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takenPicture = new File(getExternalCacheDir(),"temptakenpic.png");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_picture);
        thresholdseek = findViewById(R.id.seekBar);
        thresholdseek.setMin(70);
        thresholdseek.setMax(120);
        thresholdseek.setProgress(cannythreshold);
        iv = findViewById(R.id.imageView);
        View background = iv.getRootView();
        background.setBackgroundColor(ContextCompat.getColor(this,R.color.royalblue));
        btn = findViewById(R.id.button15);
        textView = findViewById(R.id.textView4);
        View root = btn.getRootView();
        previewBtn = findViewById(R.id.button18);
        btnMillONPlate = findViewById(R.id.button19);
        takePicBtn = findViewById(R.id.button23);
        picUri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() + ".provider",takenPicture);
        takePicBtn.setOnClickListener(v -> {
                    takePicActivityResultLauncher.launch(picUri);
        });
        btnMillONPlate.setOnClickListener(v -> {
            Bitmap bitmap = ConvertBitmaptoPNG.createBitmapFromView(iv,0,0);
            Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,src);
            Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.threshold(gray,src,127,255,Imgproc.THRESH_BINARY);
            Utils.matToBitmap(src,bitmap);
            iv.setImageBitmap(bitmap);
            ConvertBitmaptoPNG.compressBitmap(context, bitmap, quality, outstream);
        });
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        btn.setOnClickListener(v -> {
            intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/*");
            someActivityResultLauncher.launch(intent1);
        });
        previewBtn.setOnClickListener(v -> {
            processImage();
            textView.setVisibility(View.VISIBLE);
            thresholdseek.setVisibility(View.VISIBLE);
        });
        setSeekBarListener();
    }

    protected ActivityResultLauncher<Uri> takePicActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result){
                        String filepath = takenPicture.getPath();
                        bm = BitmapFactory.decodeFile(filepath);
                        iv.setImageBitmap(bm);
                        iv.invalidate();
                    }
                }
            }
    );

    protected ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        bilduri = data.getData();
                        try {
                            is = getContentResolver().openInputStream(bilduri);
                            bm = BitmapFactory.decodeStream(is);
                            ConvertBitmaptoPNG.compressBitmap(context,bm,90,new FileOutputStream(takenPicture));
                            iv.setImageBitmap(bm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void setSeekBarListener(){
        thresholdseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cannythreshold = thresholdseek.getProgress();
                processImage();
                iv.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void processImage(){
        Bitmap bitmap = bm;
        Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap,src);
        Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
        Mat edges = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
        Mat dest = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
        Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
        Imgproc.blur(gray,edges,new Size(3,3));
        Imgproc.Canny(edges,edges,cannythreshold,cannythreshold*cannyratio);
        gray.copyTo(dest,edges);
        Bitmap viewBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Utils.matToBitmap(dest,viewBitmap);
        iv.setImageBitmap(viewBitmap);
        iv.invalidate();
        ConvertBitmaptoPNG.compressBitmap(this,bitmap,quality,outstream);
    }

}