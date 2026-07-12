package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "Example: Confidence bands" section of the
 * matplotlib "Fill the area between two lines" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_demo.html">
 * fill_between_demo.html</a>
 *
 * <p>Fits a linear curve to a small noisy data set, estimates the standard
 * error at each x position, and shades a semi-transparent confidence band
 * of {@code y_est ± y_err} around the fitted line using
 * {@link Axes#fillBetween}, with the raw observations overlaid as points.
 */
public class ConfidenceBandsExample {

    /**
     * Build and return the confidence-bands figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.linspace(0, 10, 11);
        double[] y = {3.9, 4.4, 10.8, 10.3, 11.2, 13.1, 14.1, 9.9, 13.9, 15.1, 12.5};

        double xMean = mean(x);
        double xStd = std(x);

        // Linear least-squares fit: y = a*x + b.
        double sumXY = 0, sumXX = 0;
        for (int i = 0; i < x.length; i++) {
            sumXY += (x[i] - xMean) * (y[i] - mean(y));
            sumXX += (x[i] - xMean) * (x[i] - xMean);
        }
        double a = sumXY / sumXX;
        double b = mean(y) - a * xMean;

        double[] yEst = DataGenerator.apply(x, v -> a * v + b);

        double sumSq = 0;
        for (double v : x) sumSq += (v - xMean) * (v - xMean);
        double[] yErr = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            yErr[i] = xStd * Math.sqrt(1.0 / x.length + (x[i] - xMean) * (x[i] - xMean) / sumSq);
        }

        double[] lo = new double[x.length], hi = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            lo[i] = yEst[i] - yErr[i];
            hi[i] = yEst[i] + yErr[i];
        }

        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(x, yEst).setColor(new Color(0x1F77B4));
        ax.fillBetween(x, lo, hi).setAlpha(0.2f);

        Scatter sc = ax.scatter(x, y);
        sc.setMarker("o");
        sc.setColor(new Color(0x8C564B)); // tab:brown
        sc.setMarkerSize(7f);

        ax.setTitle("Confidence Bands");
        return fig;
    }

    private static double mean(double[] v) {
        double s = 0;
        for (double d : v) s += d;
        return s / v.length;
    }

    private static double std(double[] v) {
        double m = mean(v);
        double s = 0;
        for (double d : v) s += (d - m) * (d - m);
        return Math.sqrt(s / v.length);
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
