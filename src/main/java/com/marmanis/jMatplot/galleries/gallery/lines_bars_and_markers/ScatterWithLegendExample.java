package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;
import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Scatter with Legend" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_with_legend.html">
 * scatter_with_legend.html</a>
 *
 * <p>Demonstrates combining a scatter plot with a legend.  Each of five species
 * (groups) is given a distinct colour; the legend identifies each group.
 */
public class ScatterWithLegendExample {

    /**
     * Build and return the scatter-with-legend figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] species = {"setosa", "versicolor", "virginica", "formosa", "pallida"};
        Color[] colors = {
            new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
            new Color(0xD62728), new Color(0x9467BD)
        };
        double[] cx = {0.2, 0.5, 0.8, 0.35, 0.65};
        double[] cy = {0.3, 0.6, 0.4, 0.7, 0.2};

        Random rng = new Random(42);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        for (int s = 0; s < species.length; s++) {
            int n = 40;
            double[] x = new double[n], y = new double[n];
            float[] sz = new float[n];
            for (int i = 0; i < n; i++) {
                x[i] = cx[s] + rng.nextGaussian() * 0.07;
                y[i] = cy[s] + rng.nextGaussian() * 0.07;
                sz[i] = (float) (30 + rng.nextDouble() * 70);
            }
            Color c = colors[s];
            Color withAlpha = new Color(c.getRed(), c.getGreen(), c.getBlue(), 160);
            Color[] perPointColors = new Color[n];
            java.util.Arrays.fill(perPointColors, withAlpha);
            Scatter sc = ax.scatter(x, y, sz, perPointColors, 1f);
            sc.setLabel(species[s]);
        }

        ax.setXLim(0, 1);
        ax.setYLim(0, 1);
        ax.setTitle("Scatter with Legend");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
