package de.adv.guimaster;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.stream.Collectors;
import de.adv.guimaster.backend.CncState;
import de.adv.guimaster.backend.Instructions;
import de.adv.guimaster.backend.SerialAPI;
import de.adv.guimaster.logic.Constants;
import de.adv.guimaster.logic.CustomCanvas;
import de.adv.guimaster.logic.DataHolder;
import de.adv.guimaster.logic.Drivers;

public class StartupActivity extends AppCompatActivity {

    public TextView tv;
    public ProgressBar pb;
    public CustomCanvas ca;
    UsbSerialPort serialPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Process clearprocess = Runtime.getRuntime().exec("logcat -c");
            Thread.sleep(500);
        }catch (Exception e){ Log.v("LogClear","Exception");}
        setContentView(R.layout.activity_startup);
        tv = findViewById(R.id.textView);
        pb = findViewById(R.id.progressBar3);
        ca = new CustomCanvas();
    }

    @Override
    protected void onStart(){
        super.onStart();
        progressAnimation();
        ca.start();
        DataHolder.getInstance().save("CustomCanvas", ca);
        initSerialComm();
        try{ Thread.sleep(500);} catch (InterruptedException ie){ie.printStackTrace();}
        saveLogcatToFile(this);
    }

    public void progressAnimation(){
        ProgressBarAnimation barAnimation = new ProgressBarAnimation(this,pb,tv);
        barAnimation.setDuration(5000);
        pb.setAnimation(barAnimation);
    }

    public void initSerialComm(){
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if(availableDrivers.isEmpty()){
            return;
        }
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection usbconnection = manager.openDevice(driver.getDevice());
        if(usbconnection == null){
            return;
        }
        serialPort = driver.getPorts().get(0);
        try {
            serialPort.open(usbconnection);
            serialPort.setParameters(38400,8,UsbSerialPort.STOPBITS_1,UsbSerialPort.PARITY_NONE);
            initCncCommand();
        } catch (IOException ioException){
            Log.v("IOException",ioException.getMessage());
        }
    }

    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);
        try {
            Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException io){
            io.printStackTrace();
        }
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
        } */

    public void initCncCommand(){
        sendStringToComm("P0010=38400;");
        sendStringToComm("P0011=1;");
        sendStringToComm("P0014=1008;");
        sendStringToComm("P0020=75;");
        sendStringToComm("P0022=75;");
        sendStringToComm("P0023=10000;");
        sendStringToComm("P0024=100;");
        sendStringToComm("P0025=20000;");
        sendStringToComm("P0026=1;");

        // Initiaisierung -> X-Achse Parameter
        sendStringToComm("P1001=X;");
        sendStringToComm("P1002=1;");
        sendStringToComm("P1003=0;");
        sendStringToComm("P1004=50000;");
        sendStringToComm("P1005=0;");
        sendStringToComm("P1007=0;");
        sendStringToComm("P1011=25000;");
        sendStringToComm("P1013=100000;");
        sendStringToComm("P1020=1;");
        sendStringToComm("P1021=0;");
        sendStringToComm("P1022=1000;");
        sendStringToComm("P1023=540000;");
        sendStringToComm("P1024=0;");
        sendStringToComm("P1025=0;");
        sendStringToComm("P1030=75;");
        sendStringToComm("P1031=75;");
        sendStringToComm("P1032=10000;");
        sendStringToComm("P1006=4000;");

        // Initiaisierung -> Y-Achse Parameter
        sendStringToComm("P2001=Y;");
        sendStringToComm("P2002=1;");
        sendStringToComm("P2003=0;");
        sendStringToComm("P2004=50000;");
        sendStringToComm("P2005=0;");
        sendStringToComm("P2007=0;");
        sendStringToComm("P2011=25000;");
        sendStringToComm("P2013=100000;");
        sendStringToComm("P2020=1;");
        sendStringToComm("P2021=0;");
        sendStringToComm("P2022=1000;");
        sendStringToComm("P2023=500000;");
        sendStringToComm("P2024=0;");
        sendStringToComm("P2025=0;");
        sendStringToComm("P2030=75;");
        sendStringToComm("P2031=75;");
        sendStringToComm("P2032=10000;");
        sendStringToComm("P2006=4000;");

        // Initiaisierung -> Z-Achse Parameter
        sendStringToComm("P3001=Z;");
        sendStringToComm("P3002=1;");
        sendStringToComm("P3003=0;");
        sendStringToComm("P3004=50000;");
        sendStringToComm("P3005=0;");
        sendStringToComm("P3007=1;");
        sendStringToComm("P3011=15000;");
        sendStringToComm("P3013=50000;");
        sendStringToComm("P3020=1;");
        sendStringToComm("P3021=0;");
        sendStringToComm("P3022=1000;");
        sendStringToComm("P3023=160000;");
        sendStringToComm("P3024=0;");
        sendStringToComm("P3025=0;");
        sendStringToComm("P3030=75;");
        sendStringToComm("P3031=75;");
        sendStringToComm("P3032=10000;");
        sendStringToComm("P3006=4000;");

        // Initiaisierung -> 4.-ACHSPARAMETER
        sendStringToComm("P4001=0;");

        // Initiaisierung -> 5.-ACHSPARAMETER
        sendStringToComm("P5001=0;");

        // Initiaisierung -> 6.-ACHSPARAMETER
        sendStringToComm("P6001=0;");

        // Initiaisierung -> IOS
        sendStringToComm("P7000=0;");
        sendStringToComm("P7001=1;");
        sendStringToComm("P7100=3;");
        sendStringToComm("P7101=1;");
        sendStringToComm("P7102=10000;");
        sendStringToComm("P7103=11870;");

        // Initiaisierung -> Handsteuerbox
        sendStringToComm("P8001=1;");
        sendStringToComm("P8010=20000;");
        sendStringToComm("P8011=100;");
        sendStringToComm("P8020=100000;");
        sendStringToComm("P8021=1000;");

        // Initiaisierung -> Spindel
        sendStringToComm("P9000=6;");
        sendStringToComm("P9001=1;");
        sendStringToComm("P9101=500;");
        sendStringToComm("P9202=30000;");
        sendStringToComm("P9203=6000;");
        sendStringToComm("P9204=10700;");
        sendStringToComm("P9205=2200;");
        sendStringToComm("!N;RF;");
    }

    public void sendStringToComm(String command){
        byte[] buffer = command.getBytes();
        try {
            serialPort.write(buffer, Constants.WRITE_WAIT_MILLIS);
        } catch (IOException ioException){
            Toast toast = Toast.makeText(this,ioException.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }
    }
}





