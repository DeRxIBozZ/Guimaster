package de.adv.guimaster.logic;

import android.graphics.Bitmap;
import android.widget.ProgressBar;

public class CustomCanvas extends Thread {
    public Bitmap wzmbitmap;
    public Bitmap depthbitmap;
    public int[] whitepixels;
    public int[] opaquepixels;
    public int[][] machinematrix;
    public int[][] depthmatrix;
    public ProgressBar pb;
    public int percent;
    public long counter;
    public long operations;

    public CustomCanvas(ProgressBar pb) {
        this.pb = pb;
        percent = 0;
        counter = 0;
        operations = 3 * (Constants.WZMWIDTH * Constants.WZMHEIGHT);
    }

    @Override
    public void run() {
        machinematrix = new int[Constants.WZMWIDTH][Constants.WZMHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                if (j >= (Constants.WZMHEIGHT - 30) || i >= (Constants.WZMWIDTH - 30)) {
                    machinematrix[i][j] = android.graphics.Color.argb(255, 255, 255, 255);
                } else {
                    machinematrix[i][j] = android.graphics.Color.argb(0, 0, 0, 0);
                }
                counter++;
                if (operations % counter == 0) {
                    percent++;
                    pb.setProgress(percent);
                }
            }
        }
        whitepixels = new int[Constants.WZMWIDTH*Constants.WZMHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                whitepixels[Constants.WZMHEIGHT * i + j] = android.graphics.Color.argb(255,255,255,255);
                counter++;
                if (operations % counter == 0) {
                    percent++;
                    //pb.setProgress(percent);
                }
            }
        }
        opaquepixels = new int[Constants.WZMWIDTH*Constants.WZMHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                opaquepixels[Constants.WZMHEIGHT * i + j] = android.graphics.Color.argb(0,0,0,0);
                counter++;
                if (operations % counter == 0) {
                    percent++;
                    pb.setProgress(percent);
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(MatrixArray.toArray(machinematrix), Constants.WZMWIDTH, Constants.WZMHEIGHT, Bitmap.Config.ARGB_8888);
        wzmbitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);

    }
}
