package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.FillBetween;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;
import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Fill Between and Alpha" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_alpha.html">
 * fill_between_alpha.html</a>
 *
 * <p>Demonstrates using {@link Axes#fillBetween} with alpha transparency to
 * overlay multiple shaded confidence bands on the same axes without obscuring
 * each other.
 */
public class FillBetweenAlphaExample {

    /**
     * Build and return the fill-between-alpha figure.
     *
     * <p>Three panels stacked vertically, each showing a mean signal with
     * ±1σ and ±2σ confidence bands shaded at different alpha levels.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(3, 1);

        Random rng = new Random(42);
        int n = 80;
        double[] x = DataGenerator.linspace(0, 2 * Math.PI, n);

        Color[] bandColors = {
            new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C)
        };
        String[] titles = {
            "\u03bc ± 1σ  (alpha=0.4)",
            "\u03bc ± 2σ  (alpha=0.3)",
            "Both confidence bands (alpha=0.2)"
        };

        for (int p = 0; p < 3; p++) {
            Axes ax = grid[p][0];
            double[] mean = DataGenerator.apply(x, v -> Math.sin(v) + 0.1 * rng.nextGaussian());
            double[] std  = new double[n];
            for (int i = 0; i < n; i++) std[i] = 0.1 + 0.05 * Math.abs(rng.nextGaussian());

            Line2D l = ax.plot(x, mean);
            l.setColor(bandColors[p]);
            l.setLineWidth(1.5f);

            double[] lo1 = new double[n], hi1 = new double[n];
            double[] lo2 = new double[n], hi2 = new double[n];
            for (int i = 0; i < n; i++) {
                lo1[i] = mean[i] - std[i];       hi1[i] = mean[i] + std[i];
                lo2[i] = mean[i] - 2 * std[i];   hi2[i] = mean[i] + 2 * std[i];
            }

            FillBetween fb1 = ax.fillBetween(x, lo1, hi1);
            fb1.setColor(bandColors[p]);
            fb1.setAlpha(0.4f);

            if (p >= 1) {
                FillBetween fb2 = ax.fillBetween(x, lo2, hi2);
                fb2.setColor(bandColors[p]);
                fb2.setAlpha(0.2f);
            }

            ax.setTitle("Fill Between Alpha — panel " + (p + 1));
        }
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
