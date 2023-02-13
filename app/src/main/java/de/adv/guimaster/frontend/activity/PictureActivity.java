package de.adv.guimaster.frontend.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
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

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.adv.guimaster.R;

public class PictureActivity extends AppCompatActivity {

    ImageView iv;
    Button btn;
    Button btnBack;
    Intent intent1;
    Uri bilduri;
    Bitmap bm;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        iv = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.button15);
        View root = btn.getRootView();
        btnBack = (Button) findViewById(R.id.button18);
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