package de.adv.guimaster.backend;

import de.adv.guimaster.backend.CncState;

public class Instructions {

    /**
     * fahre in Achsenrichtung Richtung
     * TODO Prüfe ob außerhalb der CNC Grenzen gefahren wird
     */
    public void moveAxis(String axis, int micrometres) throws Exception {
        int x = CncState.absolute_X, y = CncState.absolute_Y, z = CncState.absolute_Z;

        switch (axis.toUpperCase()) {
            case "X":
                x = x + micrometres;
                break;
            case "Y":
                y = y + micrometres;
                break;
            case "Z":
                z = z + micrometres;
                break;
            default:
                throw new Exception("Achse: " + axis + " nicht bekannt.");
        }

        goCoordinate(x, y, z);
    }


    /**
     * fahre an den Punkt mit den Koordinaten X;Y;Z
     * Koordinaten müssen in mikometer angegeben werden
     */
    public void goCoordinate(int x, int y, int z) throws Exception {
        String command = "";

        if (CncState.toolOn == true) {
            command = "PA" + x + "," + y + ",0;";
        } else {
            command = "GA" + x + "," + y + ",0;";
        }
        CncState.CNC_CONNECTION.sendStringToComm(command);

        if (CncState.toolOn == true) {
            command = "PA,," + z + ";";
        } else {
            command = "GA,," + z + ";";
        }
        CncState.CNC_CONNECTION.sendStringToComm(command);


        CncState.absolute_X = x;
        CncState.absolute_Y = y;
        CncState.absolute_Z = z;

    }

    /**
     * Werkzeug anschalten
     */
    public void startTool(int rotationsPerMinute) throws Exception {
        String command = "RVS" + rotationsPerMinute + ";";
        CncState.CNC_CONNECTION.sendStringToComm(command);
        CncState.toolOn = true;
        CncState.rotationalSpeed = rotationsPerMinute;
    }

    /**
     * Werkzeug vorschub setzen
     */
    public void setzeVorschub(int micrometerPerSecond) throws Exception {
        String command = "VS" + micrometerPerSecond + ";";
        CncState.CNC_CONNECTION.sendStringToComm(command);
        CncState.feed = micrometerPerSecond;
    }

    /**
     * Werkzeug ausschalten
     */
    public void stopTool() throws Exception {
        String command = "RVS0;";
        CncState.CNC_CONNECTION.sendStringToComm(command);
        CncState.toolOn = false;
    }

    /**
     * TODO Über welchen Port wird die Unterdruckplatte angesprochen, sodass die Platte angesaugt wird
     */
    public void startPlateSuction(){

    }

    /**
     * siehe startPlateSuction()
     */
    public void stopPlateSuction(){

    }

    /**
     * Befehl SVN möglicherweise brauch ich den gar nicht
     */
    public void setVirtualNullpoint() throws Exception {
        String command = "?PA;";
        CncState.CNC_CONNECTION.sendStringToComm(command);
    }

    /**
     * Kreisfunktion
     * @param xStart
     * @param yStart
     * @param radius
     * @param xEnd
     * @param yEnd
     */
    public void approximateCircle(int xStart, int yStart, int radius, int xEnd, int yEnd){

    }
}
