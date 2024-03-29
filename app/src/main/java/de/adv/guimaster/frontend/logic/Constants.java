package de.adv.guimaster.frontend.logic;


import android.content.Context;

import java.io.File;
import java.io.IOException;

public class Constants {
    public static final int WZMWIDTH = 1430;
    public static final int WZMHEIGHT = 1300;
    public static final int DEPTHWIDTH = 300;
    public static final int DEPTHHEIGHT = 1300;
    public static final int WRITE_WAIT_MILLIS = 0;
    public static final int BLOCK_SIZE = 50;
    public static final int APPROX_TIME = 500;
    public static final String USB_PERMISSION = "de.adv.guimaster.USB_PERMISSION";
    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);
        try {
            Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException io){
            io.printStackTrace();
        }
    }
    public static void clearLog(){
        try {
            Runtime.getRuntime().exec("adb logcat -b all -c");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
