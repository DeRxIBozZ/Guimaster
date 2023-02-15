package de.adv.guimaster.frontend.activity;

import static de.adv.guimaster.frontend.logic.Constants.saveLogcatToFile;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.CncState;
import de.adv.guimaster.frontend.uitools.ProgressBarAnimation;
import de.adv.guimaster.frontend.logic.Constants;
import de.adv.guimaster.frontend.logic.CustomCanvas;
import de.adv.guimaster.frontend.logic.DataHolder;

public class StartupActivity extends AppCompatActivity {

    public DataHolder holder = DataHolder.getInstance();
    public TextView tv;
    public ProgressBar pb;
    public CustomCanvas ca;
    public SerialPort serialPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CncState.CNC_CONNECTION = (SerialPort) holder.retrieve("SerialPort");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_startup);
        tv = findViewById(R.id.textView);
        View root = tv.getRootView();
        root.setBackgroundColor(ContextCompat.getColor(this,R.color.anthrazit));
        pb = findViewById(R.id.progressBar3);
        ca = new CustomCanvas();
    }

    @Override
    protected void onStart(){
        super.onStart();
        progressAnimation();
        ca.start();
        holder.save("CustomCanvas", ca);
        try {
            serialPort = (SerialPort) holder.retrieve("SerialPort");
            serialPort.initSerialComm(this);
        } catch (Exception e) { Log.v("Exception", e.getMessage());}
        try{ Thread.sleep(500); } catch (InterruptedException ie) {ie.printStackTrace();}
        saveLogcatToFile(this);
    }

    public void progressAnimation(){
        ProgressBarAnimation barAnimation = new ProgressBarAnimation(this,pb,tv);
        barAnimation.setDuration(5000);
        pb.setAnimation(barAnimation);
    }

    /*public void initSerialComm(){
        List<String> ports = Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());
        for (String s: ports) {
            Log.v("Ports",s);
        }
        SerialPort[] ports = SerialPort.getCommPorts();
        List<String> sports = Arrays.stream(ports).map(SerialPort::getSystemPortName).collect(Collectors.toList());
        for (String s : sports){
            Log.v("Ports",s);
        }
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
        }*/
}





