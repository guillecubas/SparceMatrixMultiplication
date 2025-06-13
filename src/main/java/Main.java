import dense.NaiveMultiplication;
import dense.StrassenMultiplication;
import sparse.SparseMatrixCSR;
import sparse.SparseMultiplication;
import utils.MatrixGenerator;

public class Main {
    public static void main(String[] args) {
        int size = 512;
        double sparsity = 0.8;

        int[][] denseA = MatrixGenerator.generateDenseMatrix(size, sparsity);
        int[][] denseB = MatrixGenerator.generateDenseMatrix(size, sparsity);

        System.out.println("Running Naive Multiplication...");
        int[][] naiveResult = NaiveMultiplication.multiply(denseA, denseB);

        System.out.println("Running Strassen Multiplication...");
        int[][] strassenResult = StrassenMultiplication.multiply(denseA, denseB);

        System.out.println("Running Sparse Multiplication (CSR)...");
        SparseMatrixCSR sparseA = new SparseMatrixCSR(denseA);
        SparseMatrixCSR sparseB = new SparseMatrixCSR(denseB);
        SparseMatrixCSR sparseResult = SparseMultiplication.multiply(sparseA, sparseB);

        System.out.println("Benchmarks and computations completed.");
    }
}

