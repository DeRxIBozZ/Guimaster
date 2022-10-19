package de.adv.guimaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View imageView = findViewById(R.id.imageView2);
        View root = imageView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.black));


    }
}