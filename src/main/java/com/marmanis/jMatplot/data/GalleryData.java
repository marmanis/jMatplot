package com.marmanis.jMatplot.data;

/**
 * Utility for generating data sets used in gallery examples.
 */
public class GalleryData {

    /**
     * Generates an array of evenly spaced numbers over the interval [start, stop].
     */
    public static double[] linspace(double start, double stop, int num) {
        double[] result = new double[num];
        double step = (stop - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            result[i] = start + i * step;
        }
        return result;
    }
}
