package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "Selectively marking horizontal regions across
 * the whole Axes" section of the matplotlib "Fill the area between two
 * lines" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_demo.html">
 * fill_between_demo.html</a>
 *
 * <p>matplotlib marks the regions where {@code y > threshold} across the
 * full vertical extent of the Axes by combining {@code fill_between} with a
 * mixed transform (x in data coordinates, y in axes coordinates), so the
 * shaded bands are independent of the y-limits. {@code Axes} has no such
 * mixed-transform support, so the same visual result is reproduced by
 * explicitly fixing the y-limits and filling each contiguous
 * {@code y > threshold} run (extended to the interpolated crossing point)
 * from {@code yMin} to {@code yMax} with {@link Axes#fillBetween}.
 */
public class SelectiveFillingHorizontalRegionsWholeAxesExample {

    private static final double Y_MIN = -1.2, Y_MAX = 1.2;
    private static final double THRESHOLD = 0.75;

    /**
     * Build and return the whole-axes selective-filling figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.arange(0.0, 4 * Math.PI, 0.01);
        double[] y = DataGenerator.apply(x, Math::sin);

        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.setXLim(x[0], x[x.length - 1]);
        ax.setYLim(Y_MIN, Y_MAX);

        for (double[] run : thresholdRuns(x, y, THRESHOLD)) {
            double[] xs = {run[0], run[1]};
            double[] lo = {Y_MIN, Y_MIN};
            double[] hi = {Y_MAX, Y_MAX};
            var fb = ax.fillBetween(xs, lo, hi);
            fb.setColor(new Color(0x2CA02C));
            fb.setAlpha(0.5f);
        }

        ax.plot(x, y).setColor(Color.BLACK);
        ax.plot(new double[]{x[0], x[x.length - 1]}, new double[]{THRESHOLD, THRESHOLD})
          .setColor(new Color(0x2CA02C));
        ax.setTitle("Selective filling of horizontal regions across whole axes");

        return fig;
    }

    /**
     * Find contiguous x-ranges where {@code y[i] > threshold}, each extended
     * to the linearly-interpolated crossing x-position at its boundaries
     * (unless the run touches the start/end of the data).
     *
     * @return list of {@code {xStart, xEnd}} pairs
     */
    private static java.util.List<double[]> thresholdRuns(double[] x, double[] y, double threshold) {
        java.util.List<double[]> runs = new java.util.ArrayList<>();
        int n = x.length;
        int i = 0;
        while (i < n) {
            if (y[i] <= threshold) { i++; continue; }
            int j = i;
            while (j + 1 < n && y[j + 1] > threshold) j++;

            double xStart = (i > 0) ? crossing(x, y, threshold, i - 1, i) : x[i];
            double xEnd = (j + 1 < n) ? crossing(x, y, threshold, j, j + 1) : x[j];
            runs.add(new double[]{xStart, xEnd});

            i = j + 1;
        }
        return runs;
    }

    /** Linearly interpolate the x-position where {@code y == threshold} between indices a and b. */
    private static double crossing(double[] x, double[] y, double threshold, int a, int b) {
        double da = y[a] - threshold;
        double db = y[b] - threshold;
        double t = da / (da - db);
        return x[a] + t * (x[b] - x[a]);
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
