package com.marmanis.jMatplot.core;

public class BoundaryNorm implements Normalize {
    private double[] boundaries;
    public BoundaryNorm(double[] boundaries) { this.boundaries = boundaries; }
    public double normalize(double value) {
        for (int i = 0; i < boundaries.length - 1; i++) {
            if (value >= boundaries[i] && value < boundaries[i+1]) {
                return (double) i / (boundaries.length - 2);
            }
        }
        return value < boundaries[0] ? 0.0 : 1.0;
    }
}
