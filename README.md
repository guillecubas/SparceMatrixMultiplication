# Optimized Matrix Multiplication in Java

This project implements and compares different optimized matrix multiplication techniques using Java and JMH (Java Microbenchmark Harness). It includes:

- **Naive multiplication**
- **Strassen’s algorithm**
- **Loop unrolling**
- **Cache-aware optimizations**
- **Sparse matrix multiplication**

Benchmark results are collected and optionally profiled using garbage collection statistics.

## Requirements

- Java 17 or higher
- Maven

## How to Build

```bash
mvn clean install
java -jar target/matrix-optimization-1.0-SNAPSHOT.jar -f 1 -wi 5 -i 5 -prof gc -rff result.csv -rf csv
```
