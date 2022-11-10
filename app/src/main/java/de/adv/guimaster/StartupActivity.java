package de.adv.guimaster;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import de.adv.guimaster.logic.Constants;

public class StartupActivity extends AppCompatActivity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        VideoView vid = findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gottlos));
        vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int duration = mediaPlayer.getDuration();
                duration += 500;
                mediaPlayer.start();
                StartupActivity sa = new StartupActivity();
                Thread initThread = new Thread(sa);
                initThread.start();
                try{
                    Thread.sleep(duration);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                mediaPlayer.stop();
                Intent i1 = new Intent(StartupActivity.this,MainActivity.class);
                StartupActivity.this.startActivity(i1);
            }
        });


    }

    @Override
    public void run() {
        Constants.WZMCANVAS = ManuellerController.initCanvas();
    }
}
