package dense;

public class LoopUnrollingMultiplication {
    public static int[][] multiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        int unrollFactor = 4;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j += unrollFactor) {
                int sum0 = 0, sum1 = 0, sum2 = 0, sum3 = 0;
                for (int k = 0; k < n; k++) {
                    sum0 += A[i][k] * B[k][j];
                    if (j + 1 < n) sum1 += A[i][k] * B[k][j + 1];
                    if (j + 2 < n) sum2 += A[i][k] * B[k][j + 2];
                    if (j + 3 < n) sum3 += A[i][k] * B[k][j + 3];
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
