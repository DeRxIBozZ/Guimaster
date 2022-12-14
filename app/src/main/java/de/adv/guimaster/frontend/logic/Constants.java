package de.adv.guimaster.frontend.logic;


import android.content.Context;

import java.io.File;
import java.io.IOException;

public class Constants {
    public static final int WZMWIDTH = 1600;
    public static final int WZMHEIGHT = 950;
    public static final int DEPTHWIDTH = 300;
    public static final int DEPTHHEIGHT = 950;
    public static final int WRITE_WAIT_MILLIS = 100;
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
}
