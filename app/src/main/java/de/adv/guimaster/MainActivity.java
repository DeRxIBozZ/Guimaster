package de.adv.guimaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View imageView = findViewById(R.id.imageView2);
        View root = imageView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        Button bcontroller = findViewById(R.id.button);
        bcontroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,ManuellerController.class);
                MainActivity.this.startActivity(i1);
            }
        });
        Button bcanvas = findViewById(R.id.button2);
        bcanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,ZeichenTool.class);
                MainActivity.this.startActivity(i1);
            }
        });
        Button bimportfile = findViewById(R.id.button3);
        bimportfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,FileActivity.class);
                MainActivity.this.startActivity(i1);
            }
        });
        Button bimportpicture = findViewById(R.id.button4);
        bimportpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,PictureActivity.class);
                MainActivity.this.startActivity(i1);
            }
        });

    }

    @Override
    public void onClick(View v){

    }
}