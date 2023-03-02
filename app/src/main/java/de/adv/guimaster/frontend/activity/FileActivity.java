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
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;
import java.io.OutputStream;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;

public class FileActivity extends AppCompatActivity {

    ImageView imgView;
    Button btn;
    Button previewBtn;
    Intent intent1;
    Uri bilduri;
    InputStream is;
    SVG svg;
    Context c;
    OutputStream outstream;
    int quality;
    Button btnMillONPlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_file);
        imgView = findViewById(R.id.svgView);
        btn = findViewById(R.id.button16);
        View root = btn.getRootView();
        previewBtn = findViewById(R.id.button14);
        btnMillONPlate = findViewById(R.id.button17);
        c = this;
        previewBtn.setOnClickListener(v -> {
            Bitmap bitmap = ConvertBitmaptoPNG.createBitmapFromView(imgView,0,0);
            Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,src);
            Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.threshold(gray,src,127,255,Imgproc.THRESH_BINARY);
            Utils.matToBitmap(src,bitmap);
            imgView.setImageBitmap(bitmap);
            ConvertBitmaptoPNG.compressBitmap(c, bitmap, quality, outstream);
        });
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        btnMillONPlate.setOnClickListener(v ->{
            Bitmap bitmap = ConvertBitmaptoPNG.createBitmapFromView(imgView,0,0);
            Mat src = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap,src);
            Mat gray = new Mat(bitmap.getWidth(),bitmap.getHeight(),CvType.CV_8UC1);
            Imgproc.cvtColor(src,gray,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.threshold(gray,src,127,255,Imgproc.THRESH_BINARY);
            Utils.matToBitmap(src,bitmap);
            imgView.setImageBitmap(bitmap);
            ConvertBitmaptoPNG.compressBitmap(c, bitmap, quality, outstream);
        });
        btn.setOnClickListener(v -> {
            intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/svg+xml");
            someActivityResultLauncher.launch(intent1);
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
                            svg = SVG.getFromInputStream(is);
                            svg.setDocumentHeight(imgView.getHeight());
                            svg.setDocumentWidth(imgView.getWidth());
                            Bitmap bitmap = Bitmap.createBitmap(imgView.getWidth(),imgView.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            svg.renderToCanvas(canvas);
                            imgView.setImageBitmap(bitmap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

}