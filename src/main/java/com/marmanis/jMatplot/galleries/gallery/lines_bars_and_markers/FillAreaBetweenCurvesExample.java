package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "Basic usage" section of the matplotlib
 * "Fill the area between two lines" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_demo.html">
 * fill_between_demo.html</a>
 *
 * <p>{@code fill_between}'s {@code y1}/{@code y2} parameters can be scalars
 * (a horizontal boundary) or curves. This reproduces the three stacked
 * panels from the "Basic usage" section: fill between {@code y1} and the
 * implicit {@code 0} baseline, fill between {@code y1} and the scalar
 * {@code 1}, and fill between two curves {@code y1} and {@code y2}.
 */
public class FillAreaBetweenCurvesExample {

    /**
     * Build and return the "fill area between curves" figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x  = DataGenerator.arange(0.0, 2.0, 0.01);
        double[] y1 = DataGenerator.apply(x, v -> Math.sin(2 * Math.PI * v));
        double[] y2 = DataGenerator.apply(x, v -> 0.8 * Math.sin(4 * Math.PI * v));

        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(3, 1);

        Axes ax1 = grid[0][0];
        ax1.fillBetween(x, y1);
        ax1.setTitle("fill between y1 and 0");

        Axes ax2 = grid[1][0];
        ax2.fillBetween(x, y1, 1.0);
        ax2.setTitle("fill between y1 and 1");

        Axes ax3 = grid[2][0];
        ax3.fillBetween(x, y1, y2);
        ax3.setTitle("fill between y1 and y2");
        ax3.setXLabel("x");

        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
