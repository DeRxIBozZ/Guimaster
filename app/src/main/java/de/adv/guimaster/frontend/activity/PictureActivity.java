package de.adv.guimaster.frontend.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;

public class PictureActivity extends AppCompatActivity {

    ImageView iv;
    Button btn;
    Button btnBack;
    Button btnMillONPlate;
    Intent intent1;
    Uri bilduri;
    Bitmap bm;
    InputStream is;
    int quality;
    OutputStream outstream;
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
        getSupportActionBar().hide();
        setContentView(R.layout.activity_picture);

        iv = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.button15);
        View root = btn.getRootView();
        btnBack = (Button) findViewById(R.id.button18);
        btnMillONPlate = findViewById(R.id.button19);
        btnBack.setOnClickListener(v -> {
            Intent i1 = new Intent(PictureActivity.this,MainActivity.class);
            PictureActivity.this.startActivity(i1);
        });
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        btn.setOnClickListener(v -> {
            intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/*");
            someActivityResultLauncher.launch(intent1);
        });
        Context context = this;
        btnMillONPlate.setOnClickListener(v -> {
            Bitmap bitmap =  ConvertBitmaptoPNG.createBitmapFromView(iv,0,0);
            Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,src);
            Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Mat temp = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Mat edges = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Mat dest = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.blur(gray,edges,new Size(3,3));
            Imgproc.Canny(edges,edges,cannythreshold,cannythreshold*cannyratio);
            gray.copyTo(dest,edges);
            /*Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Mat dest = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.Sobel(gray,dest,-1,1,1);
            Imgproc.threshold(dest,dest,5,255,Imgproc.THRESH_BINARY);*/
            Utils.matToBitmap(dest,bitmap);
            iv.setImageBitmap(bitmap);
            ConvertBitmaptoPNG.compressBitmap(context,bitmap,quality,outstream);
        });
    }

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
                            iv.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}