package de.adv.guimaster.backend;
import de.adv.guimaster.api.SerialPort;
import java.util.ArrayList;

    public class CncState {

        // Absolute Position
        public static int absolute_X;
        public static int absolute_Y;
        public static int absolute_Z;

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

        // workpart
        public static int workpart_width;
        public static int workpart_length;
        public static int workpart_depth;

        // CNC Response Log
        public static ArrayList<String> cncLOG = new ArrayList<String>();
        public static boolean incompleteEntry = false;
        public static int indexLog = 0;

        // CNC Verbindung
        public static SerialPort CNC_CONNECTION;
    }

