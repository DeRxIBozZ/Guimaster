package de.adv.guimaster.logic;

import android.graphics.Bitmap;

public class CustomCanvas {
    public int[][] matrix;
    public Bitmap bitmap;

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CustomCanvas(int[][] matrix, Bitmap bitmap) {
        this.matrix = matrix;
        this.bitmap = bitmap;
    }
}
