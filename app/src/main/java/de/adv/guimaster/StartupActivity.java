package de.adv.guimaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.fazecast.jSerialComm.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import de.adv.guimaster.backend.CncState;
import de.adv.guimaster.backend.Instructions;
import de.adv.guimaster.backend.SerialAPI;
import de.adv.guimaster.logic.CustomCanvas;
import de.adv.guimaster.logic.DataHolder;
import de.adv.guimaster.logic.Drivers;

public class StartupActivity extends AppCompatActivity {

    public TextView tv;
    public ProgressBar pb;
    public Timer t1;
    public CustomCanvas ca;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Process clearprocess = Runtime.getRuntime().exec("logcat -c");
            Thread.sleep(500);
        }catch (Exception e){ Log.v("LogClear","Exception");}
        Drivers d = new Drivers();
        setContentView(R.layout.activity_startup);
        tv = findViewById(R.id.textView);
        pb = findViewById(R.id.progressBar3);
        ca = new CustomCanvas(pb);
        saveLogcatToFile(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        ca.start();
        for(int i = 1; i <= 100; i++){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie){
                ie.printStackTrace();
            }
            pb.setProgress(i);
            if(i % 4 == 0){
                switch (count){
                    case 1:
                        tv.setText(R.string.loading__);
                        count++;
                        break;
                    case 2:
                        tv.setText(R.string.loading___);
                        count++;
                        break;
                    case 3:
                        tv.setText(R.string.loading_);
                        count = 1;
                        break;
                }
            }
        }
        initSerialComm();
        finishStartup();
    }

    public void finishStartup(){
        DataHolder.getInstance().save("CustomCanvas", ca);
        Intent i1 = new Intent(StartupActivity.this, MainActivity.class);
        StartupActivity.this.startActivity(i1);
    }


    public void initSerialComm(){
        /*List<String> ports = Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());
        for (String s: ports) {
            Log.v("Ports",s);
        }*/
       /* SerialPort[] ports = SerialPort.getCommPorts();
        List<String> sports = Arrays.stream(ports).map(SerialPort::getSystemPortName).collect(Collectors.toList());
        for (String s : sports){
            Log.v("Ports",s);
        }*/
        CncState.CNC_CONNECTION = new SerialAPI();
        
        try{
                CncState.CNC_CONNECTION.initPort("/dev/ttyUSB0");
                CncState.CNC_CONNECTION.openPort();
                CncState.CNC_CONNECTION.initCNC();
            Instructions i = new Instructions();
            i.goCoordinate(10000,10000,10000);
            } catch (Exception e) {
                Log.v("CnC",e.getMessage());
                saveLogcatToFile(this);
            }
        }


    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);
        try {
            @SuppressWarnings("unused")

            Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException io){
            io.printStackTrace();
        }
    }
}





