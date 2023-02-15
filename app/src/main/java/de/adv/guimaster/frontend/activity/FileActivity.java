package de.adv.guimaster.frontend.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import de.adv.guimaster.R;
import de.adv.guimaster.api.ConvertBitmaptoPNG;
import de.adv.guimaster.frontend.uitools.SvgView;

public class FileActivity extends AppCompatActivity {

    SVGImageView svgView;
    Button btn;
    Button btnBack;
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
        svgView = findViewById(R.id.svgView);
        btn = findViewById(R.id.button16);
        View root = btn.getRootView();
        btnBack = (Button) findViewById(R.id.button14);
        btnMillONPlate = findViewById(R.id.button17);
        btnBack.setOnClickListener(v -> {
            Intent i1 = new Intent(FileActivity.this,MainActivity.class);
            FileActivity.this.startActivity(i1);
        });
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        c = this;
        btn.setOnClickListener(v -> {
            intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/svg+xml");
            someActivityResultLauncher.launch(intent1);
        });
        Context context = this;
        btnMillONPlate.setOnClickListener(v -> {
            Bitmap bitmap = ConvertBitmaptoPNG.createBitmapFromView(svgView,0,0);
            ConvertBitmaptoPNG.compressBitmap(context, bitmap, quality, outstream);
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
                            svgView.setSVG(svg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

}