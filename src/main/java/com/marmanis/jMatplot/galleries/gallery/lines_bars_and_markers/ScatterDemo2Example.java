package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;
import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Scatter Demo 2" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_demo2.html">
 * scatter_demo2.html</a>
 *
 * <p>Demonstrates per-point sizes and colours on a scatter plot.  Points are
 * drawn from two bivariate normal distributions (representing two stocks),
 * sized proportionally to a random "volume" value and coloured by group.
 */
public class ScatterDemo2Example {

    /**
     * Build and return the scatter demo 2 figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Random rng = new Random(19680801L);
        int n = 100;

        // Group A: centre (0.5, 0.5)
        double[] xA = new double[n], yA = new double[n];
        float[] sA = new float[n];
        for (int i = 0; i < n; i++) {
            xA[i] = 0.5 + rng.nextGaussian() * 0.15;
            yA[i] = 0.5 + rng.nextGaussian() * 0.15;
            sA[i] = (float) (20 + rng.nextDouble() * 200);
        }

        // Group B: centre (1.5, 1.0)
        double[] xB = new double[n], yB = new double[n];
        float[] sB = new float[n];
        for (int i = 0; i < n; i++) {
            xB[i] = 1.5 + rng.nextGaussian() * 0.2;
            yB[i] = 1.0 + rng.nextGaussian() * 0.2;
            sB[i] = (float) (20 + rng.nextDouble() * 200);
        }

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        Color[] colorsA = new Color[n];
        java.util.Arrays.fill(colorsA, new Color(0x1F, 0x77, 0xB4, 120));
        Scatter scA = ax.scatter(xA, yA, sA, colorsA, 1f);
        scA.setLabel("Group A");

        Color[] colorsB = new Color[n];
        java.util.Arrays.fill(colorsB, new Color(0xFF, 0x7F, 0x0E, 120));
        Scatter scB = ax.scatter(xB, yB, sB, colorsB, 1f);
        scB.setLabel("Group B");

        ax.setTitle("Scatter Demo 2 — per-point size and colour");
        ax.setXLabel("x");
        ax.setYLabel("y");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
