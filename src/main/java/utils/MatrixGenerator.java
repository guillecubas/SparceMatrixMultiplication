package utils;

import java.util.Random;

public class MatrixGenerator {
    public static int[][] generateDenseMatrix(int size, double sparsity) {
        int[][] matrix = new int[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextDouble() > sparsity ? random.nextInt(10) + 1 : 0;
            }
        }
        return matrix;
    }
}
