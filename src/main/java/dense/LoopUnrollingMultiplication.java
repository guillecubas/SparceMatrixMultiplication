package dense;

public class LoopUnrollingMultiplication {

    public static int[][] multiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        int[][] B_T = new int[n][n];

        // Transpose B to improve cache efficiency
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B_T[j][i] = B[i][j];
            }
        }

        int unrollFactor = 4;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j += unrollFactor) {
                int sum0 = 0, sum1 = 0, sum2 = 0, sum3 = 0;
                for (int k = 0; k < n; k++) {
                    int a = A[i][k];
                    sum0 += a * B_T[j][k];
                    if (j + 1 < n) sum1 += a * B_T[j + 1][k];
                    if (j + 2 < n) sum2 += a * B_T[j + 2][k];
                    if (j + 3 < n) sum3 += a * B_T[j + 3][k];
                }
                result[i][j] = sum0;
                if (j + 1 < n) result[i][j + 1] = sum1;
                if (j + 2 < n) result[i][j + 2] = sum2;
                if (j + 3 < n) result[i][j + 3] = sum3;
            }
        }

        return result;
    }
}
