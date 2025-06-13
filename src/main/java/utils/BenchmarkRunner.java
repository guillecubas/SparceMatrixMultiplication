package utils;

import dence.NaiveMultiplication;
import dence.StrassenMultiplication;
import org.openjdk.jmh.annotations.*;
import sparce.SparseMatrixCSR;
import sparce.SparseMultiplication;

@State(Scope.Thread)
public class BenchmarkRunner {
    private int[][] A;
    private int[][] B;

    @Param({"128", "256", "512", "1024", "2048"})
    private int size;

    @Setup
    public void setup() {
        A = MatrixGenerator.generateDenseMatrix(size, 0.5);
        B = MatrixGenerator.generateDenseMatrix(size, 0.5);
    }

    @Benchmark
    public int[][] benchmarkNaive() {
        return NaiveMultiplication.multiply(A, B);
    }

    @Benchmark
    public int[][] benchmarkStrassen() {
        return StrassenMultiplication.multiply(A, B);
    }

    @Benchmark
    public SparseMatrixCSR benchmarkSparseCSR() {
        SparseMatrixCSR sparseA = new SparseMatrixCSR(A);
        SparseMatrixCSR sparseB = new SparseMatrixCSR(B);
        return SparseMultiplication.multiply(sparseA, sparseB);
    }
}
