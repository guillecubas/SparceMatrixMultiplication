package utils;

import dense.NaiveMultiplication;
import dense.StrassenMultiplication;
import org.openjdk.jmh.annotations.*;
import sparse.SparseMatrixCSR;
import sparse.SparseMultiplication;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkRunner {

    @Param({"128", "256", "512" , "1024"})  // Matrix sizes to benchmark
    private int size;

    @Param({"0.0", "0.5", "0.9"})  // Sparsity levels
    private double sparsity;

    private int[][] A;
    private int[][] B;
    private SparseMatrixCSR sparseA;
    private SparseMatrixCSR sparseB;

    @Setup(Level.Invocation)
    public void setup() {
        A = MatrixGenerator.generateDenseMatrix(size, sparsity);
        B = MatrixGenerator.generateDenseMatrix(size, sparsity);
        sparseA = new SparseMatrixCSR(A);
        sparseB = new SparseMatrixCSR(B);
    }

    @Benchmark
    public int[][] naiveMultiply() {
        return NaiveMultiplication.multiply(A, B);
    }

    @Benchmark
    public int[][] strassenMultiply() {
        return StrassenMultiplication.multiply(A, B);
    }

    @Benchmark
    public SparseMatrixCSR sparseMultiply() {
        return SparseMultiplication.multiply(sparseA, sparseB);
    }
}
