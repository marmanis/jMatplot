package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Gradient Bar" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/gradient_bar.html">
 * gradient_bar.html</a>
 *
 * <p>Simulates a gradient bar chart by stacking many thin bars, each shaded
 * progressively from a dark base colour to a light highlight.
 */
public class GradientBarExample {

    /**
     * Build and return the gradient bar figure.
     *
     * <p>Four bars are rendered, each with a vertical colour gradient
     * produced by stacking {@code N} thin sub-bars with interpolated colours.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] heights = {0.8, 0.5, 0.95, 0.6};
        Color[] baseColors = {
            new Color(0x1F77B4), new Color(0xFF7F0E),
            new Color(0x2CA02C), new Color(0xD62728)
        };
        int N = 50;   // gradient slices per bar

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        for (int b = 0; b < heights.length; b++) {
            double totalH = heights[b];
            double sliceH = totalH / N;
            Color base = baseColors[b];
            for (int k = 0; k < N; k++) {
                double frac = (double) k / (N - 1);
                // Interpolate from base colour to white
                int r = (int) (base.getRed()   + frac * (255 - base.getRed()));
                int g = (int) (base.getGreen() + frac * (255 - base.getGreen()));
                int bv = (int) (base.getBlue() + frac * (255 - base.getBlue()));
                Color sliceColor = new Color(r, g, bv);
                double yBot = k * sliceH;
                Bar slice = ax.bar(new double[]{b}, new double[]{sliceH}, 0.6,
                                   new double[]{yBot});
                slice.setColor(sliceColor);
            }
        }

        ax.setXLim(-0.5, heights.length - 0.5);
        ax.setYLim(0, 1.0);
        ax.setTitle("Gradient Bar");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
