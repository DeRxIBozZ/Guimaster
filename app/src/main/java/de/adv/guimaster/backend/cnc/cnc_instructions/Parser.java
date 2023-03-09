package de.adv.guimaster.backend.cnc.cnc_instructions;

import de.adv.guimaster.api.SerialPort;
import de.adv.guimaster.backend.cnc.CncState;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Parser {
    /**
     * GCode in den VHS CNC Code übersetzen
     */

    private ArrayList<int[]> gCode = new ArrayList<int[]>();
    private ArrayList<String[]> svg = new ArrayList<String[]>();

    public void splitSvgCode(String content) throws IOException {
        // get the SVG as String --> batik jar bekommen
        // https://stackoverflow.com/questions/26027313/how-to-load-and-parse-svg-documents

        // TODO in CNC State übertragen und im Frontend eintragen --> Vielleicht als Virtuelen Nullpunkt setzen
        int offsetWerkstueckAufCncX = 40000, offsetWerkstueckAufCncY = 20000, offsetWerkstueckAufCncZ = 0;

        try {
            content = content.replaceAll("[\\t\\n\\r]", " ");

            int start = content.indexOf("width") + 7;
            int end = content.indexOf("\" height");
            CncState.canvas_width = Integer.valueOf(content.substring(start, end));

            start = content.indexOf("height") + 8;
            end = content.indexOf("\" version");
            CncState.canvas_length = Integer.valueOf(content.substring(start, end));
            //System.out.println(content);

            while (content.contains("d=\"")) {

                int d = content.indexOf("d=\"") + 3;
                if (d == -1) {
                    d = 0;
                }
                int z = content.indexOf("Z\" />");
                if (z == -1) {
                    z = content.length();
                }
                this.svg.add(content.substring(d, z).split(" "));
                content = content.substring(content.indexOf("Z\" />") + 4);
            }


            // Resize coordinates
            // TODO Angabe über Frontend - Workpart in mm
            CncState.workpart_width = SerialPort.width;
            CncState.workpart_length = SerialPort.height;
            CncState.workpart_depth = 5;

            // Canvas in px




            // scale
            System.out.println("CncState.workpart_width " + CncState.workpart_width);
            System.out.println("CncState.canvas_width " + CncState.canvas_width);
            System.out.println("CncState.workpart_length " + CncState.workpart_length);
            System.out.println("CncState.canvas_length " + CncState.canvas_length);

            double pxMmScale = (double) CncState.workpart_width / (double) CncState.canvas_width;

            System.out.println("pxMmScale (width) " + pxMmScale);
            if (pxMmScale > ((double) CncState.workpart_length / (double) CncState.canvas_length)) {
                pxMmScale = CncState.workpart_length / CncState.canvas_length;
                System.out.println("pxMmScale (length) " + pxMmScale);
            }
            System.out.println("pxMmScale (final) " + pxMmScale);

            double new_w = pxMmScale * CncState.canvas_width;
            double new_l = pxMmScale * CncState.canvas_length;

            System.out.println("neue länge: " + pxMmScale + " * " + CncState.canvas_length + " = " + new_w);
            System.out.println("neue höhe: " + pxMmScale + " * " + CncState.canvas_width + " = " + new_l);

            // calculate offset
            double offsetX = ((double) CncState.workpart_width - new_w) / 2;
            double offsetY = ((double) CncState.workpart_length - new_l) / 2;

            System.out.println("offsetX: " + CncState.workpart_width + "-" + new_w + "=" + offsetX);
            System.out.println("offsetY: " + offsetY);

            double millimeterPixelRatio = new_w / (double) CncState.canvas_width;

            if (millimeterPixelRatio > new_l / (double) CncState.canvas_length) {
                millimeterPixelRatio = new_l / (double) CncState.canvas_length;
            }

            System.out.println("millimeterPixelRatio: " + millimeterPixelRatio);

            String newSvg = "<svg width=\"" + CncState.workpart_width * 2 + "\" height=\"" + CncState.workpart_length * 2 + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n";

            for (String[] path : this.svg) {
                if (path.length < 20) {
                    continue;
                }
                newSvg += "<path fill=\"white\" stroke=\"red\" stroke-width=\"1\" opacity=\"1.0\"\n d=\"";
                for (String s : path) {
                    if (s.equals("") || s == null) {
                        continue;
                    }
                    if (s.equals("L")) {
                        newSvg += " L ";
                    } else if (s.equals("Q")) {
                        newSvg += " L ";
                    } else if (s.equals("M")) {
                        newSvg += " M ";
                    } else {
                        double temp = Double.valueOf(s) * 2;
                        temp = temp * millimeterPixelRatio;
                        newSvg += " " + temp;
                    }
                }
                newSvg += " Z\"/>";
            }
            newSvg += "</svg>";
            System.out.println(newSvg);

            // TODO kalkuliere die gefahrene Distanz dann alles unter X Millimeter löschen

            // G-Code Koordinaten kalkulieren für goCoordinate Befehle
            for (int path = 0; path < svg.size(); path++) {
                svg.get(path);
                int i = 0;

                //Skip unnecesary coordinates
                //calculatePathLength(svg.get(path), millimeterPixelRatio);
                if (calculatePathLength(svg.get(path), millimeterPixelRatio) < 5.0) {
                    continue;
                }

                while (i < svg.get(path).length) {
                    String s = svg.get(path)[i];

                    // Mache nichs wenn leeres Feld
                    if (s.equals("") || s == null) {
                        continue;
                    }

                    // -1 = Nehme den Wert aus Absolute (CncState)
                    // -2 = Nehme den Wert aus Absolute_Z und rechne ein Offset von 1cm nach oben (CncState)
                    // -3 = Nehme den Wert aus Absolute_Z und rechne ein Offset von 1cm nach unten (CncState)


                    if (s.equals("L") || s.equals("Q")) {
                        // L = Linear im Material fräsen
                        // Q = Rundung im Material fräsen
                        // --> Abstände sind so gering dass auch Linear gefräst werden kann
                        // Wichtig nur die ersten 2 Werte übernehmen

                        int x = (int) Math.floor(Double.valueOf(svg.get(path)[i + 1]) * millimeterPixelRatio * 1000) + ((int) offsetX * 1000) + offsetWerkstueckAufCncX;
                        int y = (int) Math.floor(Double.valueOf(svg.get(path)[i + 2]) * millimeterPixelRatio * 1000) + ((int) offsetY * 1000) + offsetWerkstueckAufCncY;
                        int z = -1;

                        // Koordinate in gCode einfügen
                        gCode.add(new int[]{x, y, z});
                    } else if (s.equals("M")) {
                        // Bewege den Fräskopf an einen neuen Punkt

                        // Fahre an punkt hoch
                        int x = -1;
                        int y = -1;
                        int z = -2;
                        gCode.add(new int[]{x, y, z});

                        // Fahre über neuen Punkt
                        x = (int) Math.floor(Double.valueOf(svg.get(path)[i + 1]) * millimeterPixelRatio * 1000) + ((int) offsetX * 1000) + offsetWerkstueckAufCncX;
                        y = (int) Math.floor(Double.valueOf(svg.get(path)[i + 2]) * millimeterPixelRatio * 1000) + ((int) offsetY * 1000) + offsetWerkstueckAufCncY;
                        z = -1;
                        gCode.add(new int[]{x, y, z});

                        // Fahre an Punkt runter
                        x = -1;
                        y = -1;
                        z = -3;
                        gCode.add(new int[]{x, y, z});
                    } else {
                        // Fehlermeldung + Einfach ignorieren
                        System.out.println("Unbekannter Befehl: " + s);
                    }

                    // Neuen Index herausfinden
                    int j = 2;
                    if ((i + j) >= svg.get(path).length) {
                        break;
                    }
                    while (!isString(svg.get(path)[i + j]) && (i + j < svg.get(path).length)) {
                        j++;
                        if (i + j >= svg.get(path).length) {
                            break;
                        }
                    }
                    i = i + j;      // Index auf den neuen String (nächsten Befehl) setzen
                    if (i >= svg.get(path).length) {
                        break;
                    }
                }
            }

            System.out.println("Anzahl Befehle " + gCode.size());

            for (int[] i : gCode) {
                //System.out.println("x: " + i[0] + "\ty: " + i[1] + "\tz: " + i[2]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isString(String s) {
        try {
            double d = Double.valueOf(s);
            return false;
        } catch (Exception e) {
            // Egal
        }
        return true;
    }

    private double calculatePathLength(String[] path, double millimeterPixelRatio){
        ArrayList<int[]> calcPath = new ArrayList<>();
        int i =0;

        while (i < path.length) {
            String s = path[i];

            // Mache nichs wenn leeres Feld
            if (s.equals("") || s == null) {
                continue;
            }

            if (s.equals("L") || s.equals("Q") || s.equals("M")) {
                int x = (int)(Math.floor(Double.parseDouble(path[i + 1]) * millimeterPixelRatio));
                int y = (int)(Math.floor(Double.parseDouble(path[i + 2]) * millimeterPixelRatio));

                calcPath.add(new int[]{x, y});
            } else {
                // Fehlermeldung + Einfach ignorieren
                System.out.println("EGAL: " + s);
            }

            // Neuen Index herausfinden
            int j = 2;
            if ((i + j) >= path.length) {
                break;
            }
            while (!isString(path[i + j]) && (i + j < path.length)) {
                j++;
                if (i + j >= path.length) {
                    break;
                }
            }
            i = i + j;      // Index auf den neuen String (nächsten Befehl) setzen
            if (i >= path.length) {
                break;
            }
        }

        double length = 0.0;
        // Calculate path length
        for (i = 0; i < calcPath.size()-1; i++) {
            int[] cord1 = calcPath.get(i), cord2 = calcPath.get(i+1);
            //System.out.print("x: " +cord1[0] + " y: " +cord1[1] + "; x: " +cord2[0] + " y: " +cord2[1]);
            double deltaX = cord1[0] - cord2[0], deltaY= cord1[1] - cord2[1];
            double temp = deltaX*deltaX+deltaY*deltaY;
            length = length + Math.sqrt(temp);
        }

        System.out.println("Länge des Pfads: " + length);
        return length;
    }

    public ArrayList<int[]> getgCode() {
        return gCode;
    }
}
