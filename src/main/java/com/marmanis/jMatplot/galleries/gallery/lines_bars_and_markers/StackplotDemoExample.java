package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Stackplots and Streamgraphs" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/stackplot_demo.html">
 * stackplot_demo.html</a>
 *
 * <p>The figure contains two subplots stacked vertically:
 * <ol>
 *   <li><strong>World population stackplot</strong> — UN population data for five
 *       regions (Africa, the Americas, Asia, Europe, Oceania) from 1950 to 2018,
 *       stacked on a zero baseline with alpha&nbsp;= 0.8 and a legend.</li>
 *   <li><strong>Streamgraph</strong> — three Gaussian-mixture series stacked with
 *       {@code baseline="wiggle"} to produce a centred streamgraph effect.</li>
 * </ol>
 *
 * <h2>Key Python → Java translations</h2>
 * <ul>
 *   <li>{@code ax.stackplot(x, ys, labels=…, alpha=0.8)} →
 *       {@link Axes#stackplot(double[], double[][], String[], float, String)}.</li>
 *   <li>{@code baseline='wiggle'} — each band is shifted by {@code −0.5 × total(x)}
 *       so the stack is vertically centred around y = 0.</li>
 * </ul>
 */
public class StackplotDemoExample {

    // ── UN world population data (billions), 1950–2018 ────────────────────────
    // Source: United Nations, Department of Economic and Social Affairs

    private static final double[] YEARS = {
        1950, 1955, 1960, 1965, 1970, 1975, 1980, 1985,
        1990, 1995, 2000, 2005, 2010, 2015, 2018
    };

    private static final double[] AFRICA    = {
        0.228, 0.260, 0.296, 0.340, 0.391, 0.449, 0.517, 0.595,
        0.690, 0.800, 0.927, 1.076, 1.244, 1.435, 1.546
    };
    private static final double[] AMERICAS  = {
        0.340, 0.374, 0.412, 0.452, 0.493, 0.534, 0.578, 0.623,
        0.669, 0.716, 0.763, 0.810, 0.857, 0.903, 0.935
    };
    private static final double[] ASIA      = {
        1.394, 1.547, 1.717, 1.914, 2.148, 2.397, 2.633, 2.879,
        3.167, 3.464, 3.741, 3.999, 4.259, 4.517, 4.673
    };
    private static final double[] EUROPE    = {
        0.549, 0.572, 0.593, 0.613, 0.634, 0.657, 0.676, 0.685,
        0.723, 0.728, 0.729, 0.731, 0.736, 0.742, 0.746
    };
    private static final double[] OCEANIA   = {
        0.013, 0.014, 0.016, 0.017, 0.019, 0.021, 0.023, 0.025,
        0.027, 0.029, 0.031, 0.033, 0.036, 0.039, 0.041
    };

    /**
     * Build and return the stackplot figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(2, 1);

        // ── Subplot 1: world population stackplot ─────────────────────────────
        Axes ax1 = grid[0][0];
        double[][] ys = {AFRICA, AMERICAS, ASIA, EUROPE, OCEANIA};
        String[] labels = {"Africa", "Americas", "Asia", "Europe", "Oceania"};
        ax1.stackplot(YEARS, ys, labels, 0.8f, "zero");
        ax1.setTitle("World population 1950–2018");
        ax1.setXLabel("Year");
        ax1.setYLabel("Population (billions)");
        ax1.legend();

        // ── Subplot 2: Gaussian-mixture streamgraph ───────────────────────────
        Axes ax2 = grid[1][0];
        Random rng = new Random(19680801L);
        double[] x = DataGenerator.linspace(0, 10, 200);
        double[][] streams = {
            gaussianMixture(x, rng, 3),
            gaussianMixture(x, rng, 3),
            gaussianMixture(x, rng, 3)
        };
        ax2.stackplot(x, streams, null, 0.8f, "wiggle");
        ax2.setTitle("Streamgraph (wiggle baseline)");

        return fig;
    }

    /**
     * Generate a random Gaussian-mixture signal over {@code x}.
     *
     * <p>The mixture consists of {@code n} Gaussians with random amplitude,
     * centre, and width, producing a smooth random bump function.
     *
     * @param x   domain points
     * @param rng seeded random number generator
     * @param n   number of Gaussian bumps to sum
     * @return    non-negative signal array of the same length as {@code x}
     */
    private static double[] gaussianMixture(double[] x, Random rng, int n) {
        double[] a = new double[x.length];
        double dx = x[x.length - 1] - x[0];
        for (int j = 0; j < n; j++) {
            double amplitude = 1.0 / (0.1 + rng.nextDouble());
            double x0 = (2.0 * rng.nextDouble() - 0.5) * dx;
            double z   = 10.0 / (0.1 + rng.nextDouble()) / dx;
            for (int i = 0; i < x.length; i++) {
                double d = z * (x[i] - x0);
                a[i] += amplitude * Math.exp(-d * d);
            }
        }
        return a;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
