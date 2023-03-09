package de.adv.guimaster.backend.cnc;

import de.adv.guimaster.api.SerialPort;

import java.util.ArrayList;

public class CncState {

    // Absolute Position
    public static int absolute_X;
    public static int absolute_Y;
    public static int absolute_Z;

    //maximum Position
    public static int maximum_X = 55*10*1000; // 55cm in microMeter
    public static int maximum_Y = 50*10*1000; // 50cm in microMeter
    public static int maximum_Z = 15*10*1000; // 15cm in microMeter

    // Relative Position
    public static int relative_X;
    public static int relative_Y;
    public static int relative_Z;

    // Tool
    public static String toolEquipped;      // Wird wahrscheinlich nicht benötigt
    public static boolean toolOn;
    public static int toolWidth;            // im SVG vergleichbar mit stroke-width
    public static int feed;                 // Vorschub in 1/1000 mm/s
    public static int rotationalSpeed;      // Drehzahl Umdrehungen/Minuten

    // workpart in mm
    public static int workpart_width = 150;
    public static int workpart_length = 100;
    public static int workpart_depth = 5;

    // Canvas in px
    public static int canvas_width = 150;
    public static int canvas_length = 100;

    // CNC Response Log
    public static ArrayList<String> cncLOG = new ArrayList<String>();
    public static boolean incompleteEntry = false;
    public static int indexLog = 0;

    // CNC Verbindung
    public static SerialPort CNC_CONNECTION;
}
