package de.adv.guimaster.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.adv.guimaster.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View imageView = findViewById(R.id.imageView2);
        View root = imageView.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        Button bcontroller = findViewById(R.id.button);
        bcontroller.setOnClickListener(this);
        Button bcanvas = findViewById(R.id.button2);
        bcanvas.setOnClickListener(this);
        Button bimportfile = findViewById(R.id.button3);
        bimportfile.setOnClickListener(this);
        Button bimportpicture = findViewById(R.id.button4);
        bimportpicture.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case (R.id.button) :
                Intent i1 = new Intent(MainActivity.this,FileActivity.class);
                MainActivity.this.startActivity(i1);
                break;
            case (R.id.button2) :
                Intent i2 = new Intent(MainActivity.this,ManuellerController.class);
                MainActivity.this.startActivity(i2);
                break;
            case (R.id.button3) :
                Intent i3 = new Intent(MainActivity.this,PictureActivity.class);
                MainActivity.this.startActivity(i3);
                break;
            case (R.id.button4) :
                Intent i4 = new Intent(MainActivity.this,ZeichenTool.class);
                MainActivity.this.startActivity(i4);
                break;
            default: break;
        }
    }



    @Override
    public void onBackPressed(){
        System.runFinalization();
        this.finishAffinity();
    }
}