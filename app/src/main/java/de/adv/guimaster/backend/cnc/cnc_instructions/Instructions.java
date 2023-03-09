package de.adv.guimaster.backend.cnc.cnc_instructions;

import de.adv.guimaster.backend.cnc.CncState;

public class Instructions {

    /**
     * fahre in Achsenrichtung Richtung
     * TODO Prüfe ob außerhalb der CNC Grenzen gefahren wird
     */
    public void moveAxis(String axis, int micrometres) throws Exception {
        int x = CncState.absolute_X, y = CncState.absolute_Y, z = CncState.absolute_Z;

        switch (axis.toUpperCase()) {
            case "X" :
                x = x + micrometres;
                break;
            case "Y" :
                y = y + micrometres;
                break;
            case "Z" :
                z = z + micrometres;
                break;
            default :
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
        if (compareWithBoundary(x, y, z)) {
            if (CncState.toolOn) {
                command = "PA" + x + "," + y + "," + z + ";";
            } else {
                command = "GA" + x + "," + y + "," + z + ";";
            }
            CncState.CNC_CONNECTION.sendStringToComm(command);

            CncState.absolute_X = x;
            CncState.absolute_Y = y;
            CncState.absolute_Z = z;
        } else {
            throw new Exception("WZM kann nicht über die Begrenzungen hinaus fahren");
        }

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
    public void startPlateSuction() {

    }

    /**
     * siehe startPlateSuction()
     */
    public void stopPlateSuction() {

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
     *
     * @param xStart
     * @param yStart
     * @param radius
     * @param xEnd
     * @param yEnd
     */
    public void approximateCircle(int xStart, int yStart, int radius, int xEnd, int yEnd) {

    }

    private boolean compareWithBoundary(int x, int y, int z) {
        return (x < CncState.maximum_X || y < CncState.maximum_Y || z < CncState.maximum_Z);
    }
}