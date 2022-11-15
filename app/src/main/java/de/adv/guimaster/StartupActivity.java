package de.adv.guimaster;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

import de.adv.guimaster.logic.CustomCanvas;
import de.adv.guimaster.logic.DataHolder;
import de.adv.guimaster.logic.ThreadJoin;

public class StartupActivity extends AppCompatActivity {

    public TextView tv;
    public ProgressBar pb;
    public Timer t1;
    public CustomCanvas ca;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        VideoView vid = findViewById(R.id.videoView);
        tv = findViewById(R.id.textView);
        pb = findViewById(R.id.progressBar3);
        ca = new CustomCanvas(pb);
        ThreadJoin tj = new ThreadJoin(ca,this);
        ca.start();
        tj.start();
    }

    public void finishStartup(){
        DataHolder.getInstance().save("CustomCanvas", ca);
        Intent i1 = new Intent(StartupActivity.this, MainActivity.class);
        StartupActivity.this.startActivity(i1);
    }

}





