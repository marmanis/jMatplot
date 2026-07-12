package com.marmanis.jMatplot.core;

/**
 * Utility for generating grid data similar to numpy.mgrid.
 */
public class GridUtil {
    public static double[][] mgrid(double start, double stop, int N, boolean isX) {
        double[][] grid = new double[N][N];
        double step = (stop - start) / (N - 1);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (isX) grid[i][j] = start + i * step;
                else grid[i][j] = start + j * step;
            }
        }
        return grid;
    }
}
