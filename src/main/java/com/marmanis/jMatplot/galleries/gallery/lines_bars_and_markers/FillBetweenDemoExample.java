package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.FillBetween;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Fill Between Demo" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_demo.html">
 * fill_between_demo.html</a>
 *
 * <p>Demonstrates three common use-cases of {@link Axes#fillBetween}:
 * <ol>
 *   <li>Simple fill between two curves.</li>
 *   <li>Fill where one curve is above the other (using alpha).</li>
 *   <li>Fill between a curve and a scalar baseline (y = 0).</li>
 * </ol>
 */
public class FillBetweenDemoExample {

    /**
     * Build and return the fill-between demo figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(3, 1);

        double[] x = DataGenerator.linspace(0, 2 * Math.PI, 200);

        // ── Panel 1: simple fill between sin and cos ─────────────────────────
        {
            Axes ax = grid[0][0];
            double[] y1 = DataGenerator.apply(x, Math::sin);
            double[] y2 = DataGenerator.apply(x, Math::cos);
            ax.plot(x, y1).setColor(new Color(0x1F77B4));
            ax.plot(x, y2).setColor(new Color(0xFF7F0E));
            FillBetween fb = ax.fillBetween(x, y1, y2);
            fb.setColor(new Color(0x9467BD));
            fb.setAlpha(0.3f);
            ax.setTitle("fill_between(x, sin, cos)");
        }

        // ── Panel 2: fill where sin > cos only ───────────────────────────────
        {
            Axes ax = grid[1][0];
            double[] y1 = DataGenerator.apply(x, Math::sin);
            double[] y2 = DataGenerator.apply(x, Math::cos);
            // Build masked arrays: fill only where sin > cos
            double[] lo = new double[x.length], hi = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                if (y1[i] > y2[i]) { lo[i] = y2[i]; hi[i] = y1[i]; }
                else                { lo[i] = y1[i]; hi[i] = y1[i]; } // zero-height
            }
            ax.plot(x, y1).setColor(new Color(0x1F77B4));
            ax.plot(x, y2).setColor(new Color(0xFF7F0E));
            FillBetween fb = ax.fillBetween(x, lo, hi);
            fb.setColor(new Color(0x2CA02C));
            fb.setAlpha(0.4f);
            ax.setTitle("fill_between where sin(x) > cos(x)");
        }

        // ── Panel 3: fill between curve and zero baseline ────────────────────
        {
            Axes ax = grid[2][0];
            double[] y = DataGenerator.apply(x, v -> Math.sin(2 * v) * Math.exp(-v * 0.3));
            double[] zeros = DataGenerator.zeros(x.length);
            ax.plot(x, y).setColor(Color.BLACK);
            double[] pos = DataGenerator.apply(y, v -> Math.max(0, v));
            double[] neg = DataGenerator.apply(y, v -> Math.min(0, v));
            FillBetween fbP = ax.fillBetween(x, zeros, pos);
            fbP.setColor(new Color(0x1F77B4)); fbP.setAlpha(0.5f);
            FillBetween fbN = ax.fillBetween(x, neg, zeros);
            fbN.setColor(new Color(0xD62728)); fbN.setAlpha(0.5f);
            ax.setTitle("fill_between with zero baseline");
        }

        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
