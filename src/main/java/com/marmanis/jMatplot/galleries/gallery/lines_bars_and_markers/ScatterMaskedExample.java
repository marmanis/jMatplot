package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;
import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Scatter Masked" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_masked.html">
 * scatter_masked.html</a>
 *
 * <p>Shows how to mask data points so that only a subset meeting a condition
 * is plotted.  Points inside a central disc are coloured differently from
 * those outside it.
 */
public class ScatterMaskedExample {

    /**
     * Build and return the scatter-masked figure.
     *
     * <p>200 points are uniformly sampled in [−1, 1]².  Points inside a
     * circle of radius 0.5 are coloured blue; those outside are coloured orange.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Random rng = new Random(19680801L);
        int n = 200;
        double[] x = new double[n], y = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = rng.nextDouble() * 2 - 1;
            y[i] = rng.nextDouble() * 2 - 1;
        }

        // Split into inside and outside the unit circle r = 0.5
        java.util.List<Double> xIn = new java.util.ArrayList<>(),
                               yIn = new java.util.ArrayList<>(),
                               xOut = new java.util.ArrayList<>(),
                               yOut = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (x[i] * x[i] + y[i] * y[i] < 0.25) {
                xIn.add(x[i]); yIn.add(y[i]);
            } else {
                xOut.add(x[i]); yOut.add(y[i]);
            }
        }

        double[] xi = xIn.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yi = yIn.stream().mapToDouble(Double::doubleValue).toArray();
        double[] xo = xOut.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yo = yOut.stream().mapToDouble(Double::doubleValue).toArray();

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        Scatter sIn = ax.scatter(xi, yi);
        sIn.setColor(new Color(0x1F, 0x77, 0xB4, 180));
        sIn.setMarkerSize(8f);
        sIn.setLabel("inside r=0.5");

        Scatter sOut = ax.scatter(xo, yo);
        sOut.setColor(new Color(0xFF, 0x7F, 0x0E, 120));
        sOut.setMarkerSize(5f);
        sOut.setLabel("outside r=0.5");

        ax.setXLim(-1, 1);
        ax.setYLim(-1, 1);
        ax.setTitle("Scatter Masked");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
