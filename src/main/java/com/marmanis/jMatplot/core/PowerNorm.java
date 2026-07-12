package com.marmanis.jMatplot.core;

public class PowerNorm implements Normalize {
    private double gamma, vmin, vmax;
    public PowerNorm(double gamma, double vmin, double vmax) { this.gamma = gamma; this.vmin = vmin; this.vmax = vmax; }
    public double normalize(double value) {
        double v = Math.max(0.0, Math.min(1.0, (value - vmin) / (vmax - vmin)));
        return Math.pow(v, gamma);
    }
}
