package de.adv.guimaster.frontend.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.adv.guimaster.R;

public class FileActivity extends AppCompatActivity {

    ImageView iv;
    Button btn;
    Intent intent1;
    Uri bilduri;
    Bitmap bm;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        iv = (ImageView) findViewById(R.id.imageView2);
        btn = (Button) findViewById(R.id.button16);
        btn.setOnClickListener(v -> {
            intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("document/svg");
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