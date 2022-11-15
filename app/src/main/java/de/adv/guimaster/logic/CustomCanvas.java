package de.adv.guimaster.logic;

import android.graphics.Bitmap;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomCanvas implements Runnable{
    public Bitmap bitmap;
    public int[] whitepixels;
    public int[] opaquepixels;
    public int[][] machinematrix;
    public int[][] depthmatrix;
    public ProgressBar pb;

    public CustomCanvas(ProgressBar pb){
        this.pb = pb;
    }
}
