package com.marmanis.jMatplot.core;

public class LogNorm implements Normalize {
    private double vmin, vmax;
    public LogNorm(double vmin, double vmax) { this.vmin = vmin; this.vmax = vmax; }
    public double normalize(double value) {
        if (value <= 0) value = vmin; // Handle non-positive
        double logV = Math.log10(value);
        double logMin = Math.log10(vmin);
        double logMax = Math.log10(vmax);
        return Math.max(0.0, Math.min(1.0, (logV - logMin) / (logMax - logMin)));
    }
}
