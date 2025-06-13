package sparse;

import java.util.ArrayList;
import java.util.List;

public class SparseMatrixCSR {
    public int[] values;
    public int[] colIndices;
    public int[] rowPointers;
    public int rows, cols;

    public SparseMatrixCSR(int[][] denseMatrix) {
        rows = denseMatrix.length;
        cols = denseMatrix[0].length;
        List<Integer> vals = new ArrayList<>();
        List<Integer> colsIdx = new ArrayList<>();
        rowPointers = new int[rows + 1];

        int nnz = 0;
        for (int i = 0; i < rows; i++) {
            rowPointers[i] = nnz;
            for (int j = 0; j < cols; j++) {
                if (denseMatrix[i][j] != 0) {
                    vals.add(denseMatrix[i][j]);
                    colsIdx.add(j);
                    nnz++;
                }
            }
        }
        rowPointers[rows] = nnz;

        values = vals.stream().mapToInt(Integer::intValue).toArray();
        colIndices = colsIdx.stream().mapToInt(Integer::intValue).toArray();
    }
}
