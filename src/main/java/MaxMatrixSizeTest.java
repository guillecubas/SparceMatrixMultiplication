import dense.NaiveMultiplication;
import dense.StrassenMultiplication;
import dense.LoopUnrollingMultiplication;
import sparse.SparseMatrixCSR;
import sparse.SparseMultiplication;
import utils.MatrixGenerator;

public class MaxMatrixSizeTest {

    public static void main(String[] args) {
        testMaxSize("Naive", false);
        testMaxSize("Strassen", false);
        testMaxSize("Sparse", true);
        testMaxSize("LoopUnrolled", false);  // ✅ Añadido
    }

    private static void testMaxSize(String algorithm, boolean sparse) {
        int size = 128;
        int maxSize = 8192;
        double sparsity = sparse ? 0.9 : 0.0;
        int timeoutMs = 10_000; // max 10s per test

        System.out.println("\nTesting " + algorithm + " (sparse=" + sparse + "):");

        while (size <= maxSize) {
            int[][] A = MatrixGenerator.generateDenseMatrix(size, sparsity);
            int[][] B = MatrixGenerator.generateDenseMatrix(size, sparsity);
            long start = System.currentTimeMillis();

            try {
                switch (algorithm) {
                    case "Naive":
                        NaiveMultiplication.multiply(A, B);
                        break;
                    case "Strassen":
                        StrassenMultiplication.multiply(A, B);
                        break;
                    case "Sparse":
                        SparseMatrixCSR sa = new SparseMatrixCSR(A);
                        SparseMatrixCSR sb = new SparseMatrixCSR(B);
                        SparseMultiplication.multiply(sa, sb);
                        break;
                    case "LoopUnrolled":
                        LoopUnrollingMultiplication.multiply(A, B);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
                }
            } catch (OutOfMemoryError e) {
                System.out.println("❌ OOM at size " + size);
                break;
            } catch (Exception e) {
                System.out.println("❌ Failed at size " + size + ": " + e.getMessage());
                break;
            }

            long end = System.currentTimeMillis();
            long duration = end - start;

            if (duration > timeoutMs) {
                System.out.println("⏱ Timeout at size " + size + " (" + duration + " ms)");
                break;
            } else {
                System.out.println("✅ Size " + size + " completed in " + duration + " ms");
                size *= 2;
            }
        }
    }
}
