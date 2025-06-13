package dense;

public class StrassenMultiplication {
    private static final int THRESHOLD = 64; // Below this, use naive for efficiency

    public static int[][] multiply(int[][] A, int[][] B) {
        int n = A.length;

        // Pad matrices to next power of two if needed
        int m = nextPowerOfTwo(n);
        int[][] APrep = padMatrix(A, m);
        int[][] BPrep = padMatrix(B, m);

        int[][] CPrep = strassen(APrep, BPrep);

        // Remove padding
        return trimMatrix(CPrep, n);
    }

    private static int[][] strassen(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];

        if (n <= THRESHOLD) {
            return NaiveMultiplication.multiply(A, B);
        }

        int newSize = n / 2;

        // Split matrices into quadrants
        int[][] A11 = new int[newSize][newSize];
        int[][] A12 = new int[newSize][newSize];
        int[][] A21 = new int[newSize][newSize];
        int[][] A22 = new int[newSize][newSize];

        int[][] B11 = new int[newSize][newSize];
        int[][] B12 = new int[newSize][newSize];
        int[][] B21 = new int[newSize][newSize];
        int[][] B22 = new int[newSize][newSize];

        split(A, A11, 0, 0);
        split(A, A12, 0, newSize);
        split(A, A21, newSize, 0);
        split(A, A22, newSize, newSize);

        split(B, B11, 0, 0);
        split(B, B12, 0, newSize);
        split(B, B21, newSize, 0);
        split(B, B22, newSize, newSize);

        // Strassen's 7 multiplications
        int[][] M1 = strassen(add(A11, A22), add(B11, B22));
        int[][] M2 = strassen(add(A21, A22), B11);
        int[][] M3 = strassen(A11, subtract(B12, B22));
        int[][] M4 = strassen(A22, subtract(B21, B11));
        int[][] M5 = strassen(add(A11, A12), B22);
        int[][] M6 = strassen(subtract(A21, A11), add(B11, B12));
        int[][] M7 = strassen(subtract(A12, A22), add(B21, B22));

        // C = [C11 C12; C21 C22]
        int[][] C11 = add(subtract(add(M1, M4), M5), M7);
        int[][] C12 = add(M3, M5);
        int[][] C21 = add(M2, M4);
        int[][] C22 = add(subtract(add(M1, M3), M2), M6);

        // Combine quadrants into result
        join(C11, result, 0, 0);
        join(C12, result, 0, newSize);
        join(C21, result, newSize, 0);
        join(C22, result, newSize, newSize);

        return result;
    }

    // ---------- Helpers ----------
    private static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    private static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    private static void split(int[][] P, int[][] C, int iB, int jB) {
        for (int i = 0; i < C.length; i++)
            System.arraycopy(P[i + iB], jB, C[i], 0, C.length);
    }

    private static void join(int[][] C, int[][] P, int iB, int jB) {
        for (int i = 0; i < C.length; i++)
            System.arraycopy(C[i], 0, P[i + iB], jB, C.length);
    }

    private static int nextPowerOfTwo(int n) {
        int power = 1;
        while (power < n) power *= 2;
        return power;
    }

    private static int[][] padMatrix(int[][] A, int size) {
        int[][] padded = new int[size][size];
        for (int i = 0; i < A.length; i++)
            System.arraycopy(A[i], 0, padded[i], 0, A[i].length);
        return padded;
    }

    private static int[][] trimMatrix(int[][] A, int size) {
        int[][] trimmed = new int[size][size];
        for (int i = 0; i < size; i++)
            System.arraycopy(A[i], 0, trimmed[i], 0, size);
        return trimmed;
    }
}
