package sparce;

public class SparseMultiplication {
    public static SparseMatrixCSR multiply(SparseMatrixCSR A, SparseMatrixCSR B) {
        int rows = A.rows;
        int cols = B.cols;
        int[][] resultDense = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int aIdx = A.rowPointers[i]; aIdx < A.rowPointers[i + 1]; aIdx++) {
                int aCol = A.colIndices[aIdx];
                int aVal = A.values[aIdx];

                for (int bIdx = B.rowPointers[aCol]; bIdx < B.rowPointers[aCol + 1]; bIdx++) {
                    int bCol = B.colIndices[bIdx];
                    int bVal = B.values[bIdx];

                    resultDense[i][bCol] += aVal * bVal;
                }
            }
        }

        return new SparseMatrixCSR(resultDense);
    }
}
