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
    public int[] silverpixels;


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
            }
        }
        whitepixels = new int[Constants.WZMWIDTH*Constants.WZMHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                whitepixels[Constants.WZMHEIGHT * i + j] = android.graphics.Color.argb(255, 255, 255, 255);
            }
        }
        opaquepixels = new int[Constants.WZMWIDTH*Constants.WZMHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                opaquepixels[Constants.WZMHEIGHT * i + j] = android.graphics.Color.argb(0,0,0,0);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(MatrixArray.toArray(machinematrix), Constants.WZMWIDTH, Constants.WZMHEIGHT, Bitmap.Config.ARGB_8888);
        wzmbitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);


        depthmatrix = new int[Constants.DEPTHWIDTH][Constants.DEPTHHEIGHT];
        for (int i = 0; i < Constants.DEPTHWIDTH; i++){
            for(int j = 0; j < Constants.DEPTHHEIGHT; j++){
                if(j < 50){
                    depthmatrix[i][j] = android.graphics.Color.argb(255,170,169,173);
                } else{
                    depthmatrix[i][j] = android.graphics.Color.argb(0,0,0,0);
                }
            }
        }
        silverpixels = new int[Constants.DEPTHWIDTH*Constants.DEPTHHEIGHT];
        for (int i = 0; i < Constants.WZMWIDTH; i++) {
            for (int j = 0; j < Constants.WZMHEIGHT; j++) {
                opaquepixels[Constants.WZMHEIGHT * i + j] = android.graphics.Color.argb(0,0,0,0);
            }
        }
        Bitmap bitmap1 = Bitmap.createBitmap(MatrixArray.toArray(depthmatrix), Constants.DEPTHWIDTH, Constants.DEPTHHEIGHT, Bitmap.Config.ARGB_8888);
        depthbitmap = bitmap1.copy(Bitmap.Config.ARGB_8888,true);
    }
}
