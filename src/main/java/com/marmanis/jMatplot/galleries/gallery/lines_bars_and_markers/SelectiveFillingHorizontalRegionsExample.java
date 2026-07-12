package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * jMatplot equivalent of the "Selectively filling horizontal regions"
 * section of the matplotlib "Fill the area between two lines" gallery
 * example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill_between_demo.html">
 * fill_between_demo.html</a>
 *
 * <p>matplotlib's {@code where} parameter restricts {@code fill_between} to
 * the x-ranges where a boolean condition holds; without {@code interpolate},
 * a visible gap is left between neighbouring true/false x-ranges, since the
 * fill stops exactly at the last sample rather than at the true crossing
 * point. With {@code interpolate=True}, the crossing x-position is estimated
 * by linear interpolation and the fill is extended to meet it, closing the
 * gap. {@code Axes#fillBetween} has no {@code where}/{@code interpolate}
 * parameters, so both behaviours are reproduced manually here by splitting
 * the data into contiguous true-condition runs (optionally extended to the
 * interpolated crossing point) and filling each run separately.
 */
public class SelectiveFillingHorizontalRegionsExample {

    /**
     * Build and return the selective-filling figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x  = {0, 1, 2, 3};
        double[] y1 = {0.8, 0.8, 0.2, 0.2};
        double[] y2 = {0, 0, 1, 1};

        boolean[] above = new boolean[x.length]; // y1 > y2
        boolean[] below = new boolean[x.length]; // y1 <= y2
        for (int i = 0; i < x.length; i++) {
            above[i] = y1[i] > y2[i];
            below[i] = !above[i];
        }

        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(2, 1);

        Axes ax1 = grid[0][0];
        ax1.setTitle("interpolation=False");
        plotDashedMarkers(ax1, x, y1);
        plotDashedMarkers(ax1, x, y2);
        fillRegions(ax1, x, y1, y2, above, false, new Color(0x1F77B4), 0.3f);
        fillRegions(ax1, x, y1, y2, below, false, new Color(0xFF7F0E), 0.3f);

        Axes ax2 = grid[1][0];
        ax2.setTitle("interpolation=True");
        plotDashedMarkers(ax2, x, y1);
        plotDashedMarkers(ax2, x, y2);
        fillRegions(ax2, x, y1, y2, above, true, new Color(0x1F77B4), 0.3f);
        fillRegions(ax2, x, y1, y2, below, true, new Color(0xFF7F0E), 0.3f);

        return fig;
    }

    /** Plot a dashed line with circular markers, matching mpl's {@code 'o--'} style. */
    private static void plotDashedMarkers(Axes ax, double[] x, double[] y) {
        Line2D line = ax.plot(x, y);
        line.setMarker("o");
        line.setMarkerSize(6f);
        line.setDashPattern(new float[]{8f, 5f});
    }

    /**
     * Fill every contiguous run of {@code mask == true}, optionally extending
     * each run to the interpolated {@code y1 == y2} crossing point at its
     * boundaries (when {@code interpolate} is set and the run does not touch
     * the edge of the data).
     */
    private static void fillRegions(Axes ax, double[] x, double[] y1, double[] y2,
                                     boolean[] mask, boolean interpolate,
                                     Color color, float alpha) {
        int n = x.length;
        int i = 0;
        while (i < n) {
            if (!mask[i]) { i++; continue; }
            int j = i;
            while (j + 1 < n && mask[j + 1]) j++;

            List<Double> xs = new ArrayList<>(), lo = new ArrayList<>(), hi = new ArrayList<>();
            if (interpolate && i > 0 && !mask[i - 1]) {
                double[] c = crossing(x, y1, y2, i - 1, i);
                xs.add(c[0]); lo.add(c[1]); hi.add(c[1]);
            }
            for (int k = i; k <= j; k++) {
                xs.add(x[k]); lo.add(y1[k]); hi.add(y2[k]);
            }
            if (interpolate && j + 1 < n && !mask[j + 1]) {
                double[] c = crossing(x, y1, y2, j, j + 1);
                xs.add(c[0]); lo.add(c[1]); hi.add(c[1]);
            }

            var fb = ax.fillBetween(toArray(xs), toArray(lo), toArray(hi));
            fb.setColor(color);
            fb.setAlpha(alpha);

            i = j + 1;
        }
    }

    /** Linearly interpolate the x-position where {@code y1 == y2} between indices a and b. */
    private static double[] crossing(double[] x, double[] y1, double[] y2, int a, int b) {
        double da = y1[a] - y2[a];
        double db = y1[b] - y2[b];
        double t = da / (da - db);
        double cx = x[a] + t * (x[b] - x[a]);
        double cy = y1[a] + t * (y1[b] - y1[a]);
        return new double[]{cx, cy};
    }

    private static double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = list.get(i);
        return arr;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
