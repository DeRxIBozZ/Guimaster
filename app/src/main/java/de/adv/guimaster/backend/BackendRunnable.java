package de.adv.guimaster.backend;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.util.SerialInputOutputManager;

import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.cnc.CncState;
import de.adv.guimaster.backend.cnc.cnc_instructions.Instructions;
import de.adv.guimaster.backend.cnc.cnc_instructions.Parser;
import de.adv.guimaster.frontend.activity.FileActivity;
import de.adv.guimaster.frontend.logic.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class BackendRunnable implements Runnable, SerialInputOutputManager.Listener {

    private int count = 0;
    private ArrayList<int[]> gcode;
    private Instructions i;
    Context c;

    public void setContext(Context context){
        c = context;
    }

    @Override
    public void run() {
        try {
            // https://docs.google.com/presentation/d/1tZuU7LJWOnLlUOs3lPrZc1c8jhYz_w3FXTQQsNvyV_o/edit?usp=sharing
            //System.out.println("Hello CNC Backend");
            String s = ConvertSvg.convert();
            Log.v("SvgString", s);
            Parser p = new Parser();
            i = SerialPort.instructions;
            p.splitSvgCode(s);

            gcode = p.getgCode();
            // verbinde dich mit COMPort

            i.setzeVorschub(38 * 1000);

            i.startTool(20000);

            i.goCoordinate(40000, 20000, 130000);
            i.goCoordinate(40000, 20000, 142300);
            sendGCode();

        }catch (Exception e){
            Log.v("Backend-Exception", e.getMessage());
        }
    }

    @Override
    public void onNewData(byte[] data) {
       /* CharSequence txt = Arrays.toString(data);
        Toast t1 = Toast.makeText(c,txt,Toast.LENGTH_LONG);
        for (byte b:
             data) {
            if(b == ';'){
                count += 1;
                CharSequence txt = b + "" + count;
                Toast t1 = Toast.makeText(c,txt,Toast.LENGTH_SHORT);
                t1.show();
            }
        }
        if (count >= Constants.BLOCKSIZE){
            count = 0;
            sendGCode();
        }*/
    }

    @Override
    public void onRunError(Exception e) {
        Log.v("Data Transfer error", e.getMessage());
    }

    private void sendGCode(){
        System.out.println("KOORDINATENARRAYGROESSE: " + gcode.size());
        for (int[] coordinate:gcode) {
            gCodeToAxisMov(coordinate);
            count += 1;
            if(count == 10){
                count = 0;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    Log.v("Sleep interrupt",e.getMessage());
                }
            }
        }
        finish();
    }

    private void gCodeToAxisMov(int[] koordinate) {
        if (koordinate[0] == -1) {
            koordinate[0] = CncState.absolute_X;
        }
        if (koordinate[1] == -1) {
            koordinate[1] = CncState.absolute_Y;
        }
        if (koordinate[2] == -1) {
            koordinate[2] = CncState.absolute_Z;
        }
        if (koordinate[2] == -2) {
            koordinate[2] = CncState.absolute_Z - 10 * 1000;
        }
        if (koordinate[2] == -3) {
            koordinate[2] = CncState.absolute_Z + 10 * 1000;
        }
        int x = koordinate[0];
        int y = koordinate[1];
        int z = koordinate[2];
        try {
            i.goCoordinate(x, y, z);
        } catch (Exception e){
            Log.v("Axis Movement Error", e.getMessage());
        }
    }

    private void finish(){
        try {
            i.goCoordinate(0, 0, 0);
            i.stopTool();
        } catch (Exception e){
            Log.v("Finishing Error", e.getMessage());
        }
    }
}