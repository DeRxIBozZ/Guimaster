package de.adv.guimaster.logic;

import android.util.Log;

import java.io.File;

public class Drivers {

    public Drivers(){
        File dev = new File("/dev");
        File [] files = dev.listFiles();
        for(File f : files){
            Log.v("DevFile",f.getName());
        }
    }
}
