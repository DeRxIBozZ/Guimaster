package de.adv.guimaster.logic;

public class MatrixArray {

    public static int[] toArray(int[][] matrix){
        int i = matrix.length;
        int j = matrix[0].length;
        int[] array = new int[i*j];
        int acount = 0;
        for(int j1 = 0; j1 < j; j1++){
            for(int i1 = 0; i1 < i; i1++) {
                array[acount] = matrix[i1][j1];
                acount ++;
            }
        }
        return array;
    }


    public static int[][] toMatrix(int[] array, int i, int j){
        int[][] matrix = new int[i][j];
        int acount = 0;
        for(int j1 = 0; j1 < j; j++){
            for(int i1 = 0; i1 < i; i++) {
                matrix[i][j] = array[acount];
                acount++;
            }
        }
        return matrix;
    }
}
