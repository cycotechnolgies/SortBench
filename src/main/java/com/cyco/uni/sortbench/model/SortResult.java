package com.cyco.uni.sortbench.model;

/** Immutable result of one sorting algorithm benchmark. */
public class SortResult {
    private final String algorithmName;
    private final double executionTimeMs;
    private final String status;
    private final double[] sortedData;

    public SortResult(String algorithmName, double executionTimeMs, String status, double[] sortedData) {
        this.algorithmName = algorithmName;
        this.executionTimeMs = executionTimeMs;
        this.status = status;
        this.sortedData = sortedData == null ? null : sortedData.clone();
    }
    public String getAlgorithmName() { return algorithmName; }
    public double getExecutionTimeMs() { return executionTimeMs; }
    public String getStatus() { return status; }
    public double[] getSortedData() { return sortedData == null ? null : sortedData.clone(); }
}
