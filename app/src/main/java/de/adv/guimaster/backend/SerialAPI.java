package de.adv.guimaster.backend;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import de.adv.guimaster.backend.CncState;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.InputStream;
import java.net.ConnectException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FYI
 * Diese Dokumentation beinhaltet die wichtigsten Informationen zur Erstellung eines Postprozessors, um
 * eine vhf-Fräsmaschine mit 3 Achsen (x, y und z-Achse) anzusteuern. Die hier beschriebenen
 * Befehlsformate sind für die Steuerungen CNC 980 und CNC 580 ausgelegt.
 * Bearbeitungsbefehle werden in einer Ausgabedatei gespeichert. Mit einem Terminal-Programm werden
 * die darin enthaltenen Befehle schließlich über eine USB-Schnittstelle oder seriell über eine RS-232-
 * Schnittstelle an die Steuerung übertragen.
 * <p>
 * Bei einer seriellen Datenübertragung wird die serielle Schnittstelle der Steuerung durch folgende
 * Parameter definiert (parameterisieren durch übetragen der Initialisierungsdatei):
 * Baudrate: 38400
 * Data Bits: 8
 * Stop Bits: 1
 * Parity: keine
 * Flow Control: Xon/Xoff
 * <p>
 * TODO add jSerialComm to externalJARs; download from https://fazecast.github.io/jSerialComm/
 * TODO add apache Commons Lang to externalJARs; download from https://commons.apache.org/proper/commons-lang/download_lang.cgi
 * TODO API erweitern :
 * 1. nach jedem CNC Befehl die Antwort abzuwarten - gewünschter Ablauf: Sende CNC befehl -> CNC führt Befehl aus -> sendet response -> beliebige (Re-) Aktion im Code -> sende nächsten CNC Befehl
 * 2. Abbruchfunktion
 * <p>
 * Interessant:
 * http://www.codeplastic.com/2017/06/05/g-code-with-processing-part-1/
 * https://github.com/winder/Universal-G-Code-Sender/tree/master/ugs-core/src/com/willwinder/universalgcodesender
 * https://github.com/winder/Universal-G-Code-Sender/blob/master/ugs-core/src/com/willwinder/universalgcodesender/connection/JSerialCommConnection.java
 */
public class SerialAPI implements SerialPortDataListener {

    private SerialPort serialPort;

    /**
     * Open the serial port to the CNC
     *
     * @return boolean if connection was succesful (true) or not (false)
     * @throws Exception for connection errors
     */
    public boolean openPort() throws Exception {
        if (serialPort == null) {
            throw new ConnectException("Connection was not initialized");
        }

        if (serialPort.isOpen()) {
            throw new ConnectException("Can not connect, serial port is already open");
        }

        if (serialPort.openPort() == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initialize the port to the CNC
     *
     * @param portDescriptor name of the port to connect to
     * @throws Exception
     */
    public void initPort(String portDescriptor) throws Exception {
        // close ports before init
        if (serialPort != null) {
            closePort();
        }

        // variables for initialisation
        //portDescriptor = ";                           // TODO portDescriptor richtig bestimmen
        int baudrate = 38400;                           // Wert aus moodle entnommen
        int data_bits = 8;                              // Wert aus moodle entnommen
        int stop_bits = SerialPort.ONE_STOP_BIT;        // Wert aus moodle entnommen
        int parity = SerialPort.NO_PARITY;              // Wert aus moodle entnommen
        int flow_control = SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED; // Wert aus moodle entnommen

        // init port
        serialPort = SerialPort.getCommPort(portDescriptor);
        serialPort.setParity(parity);
        serialPort.setNumStopBits(stop_bits);
        serialPort.setNumDataBits(data_bits);
        serialPort.addDataListener(this);
        serialPort.setBaudRate(baudrate);
        serialPort.setFlowControl(flow_control);
    }

    /**
     * Removes listener an closes serial port
     *
     * @throws Exception
     */
    public void closePort() throws Exception {
        if (serialPort != null) {
            serialPort.removeDataListener();
            serialPort.closePort();
        }
    }

    public void sendByteImmediately(byte b) throws Exception {
        serialPort.writeBytes(new byte[]{b}, 1);
    }

    public void sendStringToComm(String command) throws Exception {
        Log.v("CNCCommunication","Sende an CNC: " + command);
        serialPort.writeBytes(command.getBytes(), command.length());
    }

    public boolean isOpen() {
        return serialPort.isOpen();
    }

    @Override
    public int getListeningEvents() {
        // Keine Ahnung was die Methode macht ich brauche sie halt wegen dem Eventlistener
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        //System.out.println("Es wurde ein Event getriggerd. Eventnr.: " + serialPortEvent.getEventType() + "\n");

        try {
            switch (serialPortEvent.getEventType()) {
                case SerialPort.LISTENING_EVENT_TIMED_OUT:
                    System.out.println("Timeout");
                    // TODO execute method for timeout
                    break;
                case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                    readData();
                    break;
                default:
                    System.out.println("Unknown event: " + serialPortEvent.getEventType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData() throws Exception {
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = serialPort.getInputStream();

        char[] readData = new char[serialPort.bytesAvailable()];
        int i = 0;

        while (serialPort.bytesAvailable() > 0) {
            readData[i] = (char) in.read();
            // System.out.print(". int i \t" + i + "\t ist der char \t\t" + (char) i + "\n");
            i++;
        }

        String data = new String(readData);
        logData(data);

        System.out.println("readData() " + data);

        //TODO - Auf welche Rückgabewerte der CNC müssen wir reagieren und wie werden die Fehler weitergegeben?
        for (i = CncState.indexLog; i < CncState.cncLOG.size(); i++) {
            String entry = CncState.cncLOG.get(i);
            System.out.println("Entry " + i + ":\t" + CncState.cncLOG.get(i));
            if (entry.matches("(E\\d\\d)|(E\\d)")) {
                CncState.indexLog = CncState.cncLOG.size();
                throw new Exception(findExceptionstring(CncState.cncLOG.get(i)));
            }
        }
        // Abgeschlossen
        CncState.indexLog = CncState.cncLOG.size();

        in.close();
    }

    /**
     * Füge die Stringfetzen in einzelne Rückmeldungssrings zusammen
     *
     * @param data
     */
    private void logData(String data) {
        String[] cncResponses = data.split(";");

        for (int i = 0; i < cncResponses.length; i++) {

            // Prüfe ob der erste Datensatz aus cncResponses zu dem letzten Datensatz aus LOG gehört
            if (CncState.incompleteEntry) {
                int lastEntry = CncState.cncLOG.size() - 1;
                String completeEntry = CncState.cncLOG.get(lastEntry) + cncResponses[i];

                // Vervollständigten Datensatz hinzufügen / unvollständigen Datensatz löschen
                CncState.cncLOG.remove(lastEntry);
                CncState.cncLOG.add(completeEntry);

                // Prüfe ob die Nachricht inzwischen vollständig ist --> nachricht enthält ein ;
                if (data.contains(";")) {
                    CncState.incompleteEntry = false;
                }
                continue;
            }

            // Daten in LOG erfassen
            if (!cncResponses[i].equals("")) {
                // Leerzeichen nicht in LOG schreiben
                CncState.cncLOG.add(cncResponses[i]);
            }
        }

        if (data.endsWith(";") || data.endsWith(" ;")) {
            CncState.incompleteEntry = false;
        } else {
            CncState.incompleteEntry = true;
        }
    }

    private String findExceptionstring(String errorcode) {
        String exception = "Kein Bekannter Errorcode der CNC: " + errorcode;
        switch (errorcode) {
            case "E1":
                exception = errorcode + " unsupported command";
                break;
            case "E2":
                exception = errorcode + " invalid command";
                break;
            case "E3":
                exception = errorcode + " command not allowed in this state";
                break;
            case "E4":
                exception = errorcode + " syntax error";
                break;
            case "E5":
                exception = errorcode + " invalid feature";
                break;
            case "E6":
                exception = errorcode + " stopped by user";
                break;
            case "E10":
                exception = errorcode + " invalid parameter";
                break;
            case "E11":
                exception = errorcode + " unknown error";
                break;
            case "E12":
                exception = errorcode + " missing parameter";
                break;
            case "E13":
                exception = errorcode + " parameter is out of range";
                break;
            case "E14":
                exception = errorcode + " no customer profile";
                break;
            case "E20":
                exception = errorcode + " axis not defined";
                break;
            case "E21":
                exception = errorcode + " axis not combinable";
                break;
            case "E22":
                exception = errorcode + " axis not referenced";
                break;
            case "E23":
                exception = errorcode + " no referenzpoint found";
                break;
            case "E24":
                exception = errorcode + " no measurepoint found";
                break;
            case "E25":
                exception = errorcode + " axis range ended";
                break;
            case "E26":
                exception = errorcode + " tool too long";
                break;
            case "E27":
                exception = errorcode + " tool too short";
                break;
            case "E30":
                exception = errorcode + " no spindle defined";
                break;
            case "E31":
                exception = errorcode + " no spindle response";
                break;
            case "E32":
                exception = errorcode + " spindle input active";
                break;
            case "E40":
                exception = errorcode + " can not record macro";
                break;
            case "E41":
                exception = errorcode + " macro too long";
                break;
            case "E42":
                exception = errorcode + " error in last macro";
                break;
            case "E43":
                exception = errorcode + " macro not found";
                break;
            case "E50":
                exception = errorcode + " initial stop";
                break;
            case "E51":
                exception = errorcode + " external stop";
                break;
            case "E52":
                exception = errorcode + " power driver stop";
                break;
            case "E53":
                exception = errorcode + " external spindle stop";
                break;
            case "E54":
                exception = errorcode + " internal spindle stop";
                break;
            case "E55":
                exception = errorcode + " hbox stop";
                break;
            case "E56":
                exception = errorcode + " powerfail stop";
                break;
            case "E57":
                exception = errorcode + " fpga confdone stop";
                break;
            case "E58":
                exception = errorcode + " refswitch stop";
                break;
            case "E59":
                exception = errorcode + " fpga error stop";
                break;
            case "E60":
                exception = errorcode + " overcurrent spindle stop";
                break;
            case "E61":
                exception = errorcode + " overload spindle stop";
                break;
            case "E62":
                exception = errorcode + " wait for input stop";
                break;
            case "E63":
                exception = errorcode + " unexpected input stop";
                break;
            case "E70":
                exception = errorcode + " leveloffset too high";
                break;
            case "E71":
                exception = errorcode + " internal error";
                break;
            case "E72":
                exception = errorcode + " error opening/reading file";
                break;
            case "E73":
                exception = errorcode + " no answer from device";
                break;
            case "E74":
                exception = errorcode + " error while loading fpga";
                break;
            case "E75":
                exception = errorcode + " update not feasible";
                break;
            case "E76":
                exception = errorcode + " update failed";
                break;
            case "E77":
                exception = errorcode + " wait for input failed";
                break;
        }

        return exception;
    }

    public void initCNC() throws Exception {
        // Initiaisierung -> Globale Parameter
        sendStringToComm("P0010=38400;");
        sendStringToComm("P0011=1;");
        sendStringToComm("P0014=1008;");
        sendStringToComm("P0020=75;");
        sendStringToComm("P0022=75;");
        sendStringToComm("P0023=10000;");
        sendStringToComm("P0024=100;");
        sendStringToComm("P0025=20000;");
        sendStringToComm("P0026=1;");

        // Initiaisierung -> X-Achse Parameter
        sendStringToComm("P1001=X;");
        sendStringToComm("P1002=1;");
        sendStringToComm("P1003=0;");
        sendStringToComm("P1004=50000;");
        sendStringToComm("P1005=0;");
        sendStringToComm("P1007=0;");
        sendStringToComm("P1011=25000;");
        sendStringToComm("P1013=100000;");
        sendStringToComm("P1020=1;");
        sendStringToComm("P1021=0;");
        sendStringToComm("P1022=1000;");
        sendStringToComm("P1023=540000;");
        sendStringToComm("P1024=0;");
        sendStringToComm("P1025=0;");
        sendStringToComm("P1030=75;");
        sendStringToComm("P1031=75;");
        sendStringToComm("P1032=10000;");
        sendStringToComm("P1006=4000;");

        // Initiaisierung -> Y-Achse Parameter
        sendStringToComm("P2001=Y;");
        sendStringToComm("P2002=1;");
        sendStringToComm("P2003=0;");
        sendStringToComm("P2004=50000;");
        sendStringToComm("P2005=0;");
        sendStringToComm("P2007=0;");
        sendStringToComm("P2011=25000;");
        sendStringToComm("P2013=100000;");
        sendStringToComm("P2020=1;");
        sendStringToComm("P2021=0;");
        sendStringToComm("P2022=1000;");
        sendStringToComm("P2023=500000;");
        sendStringToComm("P2024=0;");
        sendStringToComm("P2025=0;");
        sendStringToComm("P2030=75;");
        sendStringToComm("P2031=75;");
        sendStringToComm("P2032=10000;");
        sendStringToComm("P2006=4000;");

        // Initiaisierung -> Z-Achse Parameter
        sendStringToComm("P3001=Z;");
        sendStringToComm("P3002=1;");
        sendStringToComm("P3003=0;");
        sendStringToComm("P3004=50000;");
        sendStringToComm("P3005=0;");
        sendStringToComm("P3007=1;");
        sendStringToComm("P3011=15000;");
        sendStringToComm("P3013=50000;");
        sendStringToComm("P3020=1;");
        sendStringToComm("P3021=0;");
        sendStringToComm("P3022=1000;");
        sendStringToComm("P3023=160000;");
        sendStringToComm("P3024=0;");
        sendStringToComm("P3025=0;");
        sendStringToComm("P3030=75;");
        sendStringToComm("P3031=75;");
        sendStringToComm("P3032=10000;");
        sendStringToComm("P3006=4000;");

        // Initiaisierung -> 4.-ACHSPARAMETER
        sendStringToComm("P4001=0;");

        // Initiaisierung -> 5.-ACHSPARAMETER
        sendStringToComm("P5001=0;");

        // Initiaisierung -> 6.-ACHSPARAMETER
        sendStringToComm("P6001=0;");

        // Initiaisierung -> IOS
        sendStringToComm("P7000=0;");
        sendStringToComm("P7001=1;");
        sendStringToComm("P7100=3;");
        sendStringToComm("P7101=1;");
        sendStringToComm("P7102=10000;");
        sendStringToComm("P7103=11870;");

        // Initiaisierung -> Handsteuerbox
        sendStringToComm("P8001=1;");
        sendStringToComm("P8010=20000;");
        sendStringToComm("P8011=100;");
        sendStringToComm("P8020=100000;");
        sendStringToComm("P8021=1000;");

        // Initiaisierung -> Spindel
        sendStringToComm("P9000=6;");
        sendStringToComm("P9001=1;");
        sendStringToComm("P9101=500;");
        sendStringToComm("P9202=30000;");
        sendStringToComm("P9203=6000;");
        sendStringToComm("P9204=10700;");
        sendStringToComm("P9205=2200;");

        sendStringToComm("!N;RF;");

        System.out.println("\n\n\n-----------------------------");
        System.out.println("-----------------------------");
        System.out.println("-------- Ende Init ----------");
        System.out.println("-----------------------------");
        System.out.println("-----------------------------\n\n\n");
    }
}
