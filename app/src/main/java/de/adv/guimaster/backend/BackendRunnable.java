package de.adv.guimaster.backend;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.util.SerialInputOutputManager;

import de.adv.guimaster.R;
import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.cnc.CncState;
import de.adv.guimaster.backend.cnc.cnc_instructions.Instructions;
import de.adv.guimaster.backend.cnc.cnc_instructions.Parser;
import de.adv.guimaster.frontend.activity.FileActivity;
import de.adv.guimaster.frontend.logic.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class BackendRunnable implements Runnable {

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

            i.setzeVorschub(25 * 1000);

            i.startTool(20000);

            i.goCoordinate(37000, 17000, 142000);
            i.goCoordinate(37000, 17000, 142800);
            Vector<List<int[]>> gCodeBlocks = splitGCode(gcode);
        }catch (Exception e){
            Log.v("Backend-Exception", e.getMessage());
        }
        sendGCode(gcode);

    }



    /*private void sendGCode(Vector<List<int[]>> gCode){
        for (List<int[]> block: gCode) {
            for (int[] command:block) {
                gCodeToAxisMov(command);
            }
            try {
                Thread.sleep((long) block.size() * Constants.APPROX_TIME);
            } catch (InterruptedException ie){
                System.out.println(ie.getMessage());
            }
        }
        finish();
    }*/

    private void sendGCode(ArrayList<int[]> gCode){
        for ( int[] koordinate : gCode){
            gCodeToAxisMov(koordinate);
        }
        finish();
    }

    private Vector<List<int[]>> splitGCode(ArrayList<int[]> gcode){
        int vectorSize = gcode.size() / Constants.BLOCK_SIZE + 1;
        Vector<List<int[]>> ret = new Vector<>();
        for (int i = 0; i < vectorSize ; i++) {
            if (i + 1 < vectorSize) {
                ret.add(gcode.subList(i * Constants.BLOCK_SIZE, (i + 1) * Constants.BLOCK_SIZE));
            } else {
                ret.add(gcode.subList(i * Constants.BLOCK_SIZE,gcode.size() - 1));
            }
        }
        return  ret;
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
        /*for(int i = 0; i< gcode.size(); i++){
            Log.d("Read from CnC" ,CncState.CNC_CONNECTION.read());
        }*/
        try {
            i.goCoordinate(0, 0, 0);
            i.stopTool();
        } catch (Exception e){
            Log.v("Finishing Error", e.getMessage());
        }
    }
}