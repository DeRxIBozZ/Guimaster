package de.adv.guimaster.backend;

import android.util.Log;

import com.hoho.android.usbserial.util.SerialInputOutputManager;

import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.cnc.CncState;
import de.adv.guimaster.backend.cnc.cnc_instructions.Instructions;
import de.adv.guimaster.backend.cnc.cnc_instructions.Parser;
import de.adv.guimaster.frontend.logic.Constants;

import java.util.ArrayList;

public class BackendRunnable implements Runnable, SerialInputOutputManager.Listener {

    private int count = 0;
    private ArrayList<int[]> gcode;
    private Instructions i;

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
            sendSomeGCode();

        }catch (Exception e){
            Log.v("Backend-Exception", e.getMessage());
        }
    }

    @Override
    public void onNewData(byte[] data) {
        for (byte b:
             data) {
            if(b == ';'){
                count += 1;
            }
        }
        if (count >= 1000){
            count = 0;
            sendSomeGCode();
        }
    }

    @Override
    public void onRunError(Exception e) {
        Log.v("Data Transfer error", e.getMessage());
    }

    private void sendSomeGCode(){
        for (int i = 0; i < Constants.BLOCKSIZE; i++){
            if(gcode.isEmpty()){
                finish();
                break;
            } else {
                int[] tmp = gcode.get(0);
                gcode.remove(0);
                gCodeToAxisMov(tmp);
            }
        }
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