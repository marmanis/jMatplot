package com.marmanis.jMatplot.core;

public class LinearNorm implements Normalize {
    private double vmin, vmax;
    public LinearNorm(double vmin, double vmax) { this.vmin = vmin; this.vmax = vmax; }
    public double normalize(double value) {
        if (vmax == vmin) return 0.0;
        return Math.max(0.0, Math.min(1.0, (value - vmin) / (vmax - vmin)));
    }
}
