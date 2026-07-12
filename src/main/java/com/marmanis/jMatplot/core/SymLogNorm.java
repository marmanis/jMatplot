package com.marmanis.jMatplot.core;

public class SymLogNorm implements Normalize {
    private double linthresh, vmin, vmax;
    public SymLogNorm(double linthresh, double vmin, double vmax) {
        this.linthresh = linthresh;
        this.vmin = vmin;
        this.vmax = vmax;
    }
    public double normalize(double value) {
        double absVal = Math.abs(value);
        double norm;
        if (absVal < linthresh) {
            norm = value / (2 * linthresh);
        } else {
            double sign = Math.signum(value);
            double logPart = Math.log10(absVal / linthresh);
            double maxLog = Math.log10(Math.max(Math.abs(vmax), Math.abs(vmin)) / linthresh);
            norm = sign * (0.5 + 0.5 * logPart / maxLog);
        }
        return Math.max(0.0, Math.min(1.0, norm + 0.5));
    }
}
