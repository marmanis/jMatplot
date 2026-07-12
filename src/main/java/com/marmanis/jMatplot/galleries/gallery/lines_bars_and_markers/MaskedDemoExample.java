package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * jMatplot equivalent of the matplotlib "Plotting masked and NaN values"
 * gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/masked_demo.html">
 * masked_demo.html</a>
 *
 * <p>Illustrates the three ways of handling missing data points on the same
 * {@code cos(x)^3} curve (each scaled along x so the four curves don't
 * overlap): removing the points entirely (the line bridges the gap),
 * masking them, and setting them to {@code NaN} (the latter two leave a
 * visible break in the line).
 *
 * <p>{@code Line2D} has no built-in masked-array or NaN-aware gap handling,
 * and feeding {@code NaN} into {@link Axes#plot} would corrupt that axes'
 * auto-range tracking (its {@code xMin}/{@code xMax}/{@code yMin}/{@code
 * yMax} use {@code Math.min}/{@code Math.max}, which propagate {@code NaN}
 * once introduced). Both the "masked" and "NaN" curves are therefore
 * reproduced identically and safely by splitting the data into contiguous
 * runs of in-range points and drawing each run as its own {@code plot} call,
 * which leaves the same visual gap without ever passing {@code NaN} to the
 * axes.
 */
public class MaskedDemoExample {

    /**
     * Build and return the masked-and-NaN-values demo figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.linspace(-Math.PI / 2, Math.PI / 2, 31);
        double[] y = DataGenerator.apply(x, v -> Math.pow(Math.cos(v), 3));

        boolean[] keep = new boolean[x.length];
        for (int i = 0; i < x.length; i++) keep[i] = y[i] <= 0.7;

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        // 1) No mask: every point plotted.
        double[] x0 = DataGenerator.apply(x, v -> v * 0.1);
        plotLine(ax, x0, y, new Color(0xD3D3D3), "No mask");

        // 2) Points removed: the filtered points are still one continuous
        //    line, so the curve bridges straight across the gap.
        List<Double> xr = new ArrayList<>(), yr = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            if (keep[i]) { xr.add(x[i] * 0.4); yr.add(y[i]); }
        }
        plotLine(ax, toArray(xr), toArray(yr), new Color(0x1F77B4), "Points removed");

        // 3) Masked values: same data, broken into a line per contiguous run.
        double[] x2 = DataGenerator.apply(x, v -> v * 0.7);
        plotBroken(ax, x2, y, keep, new Color(0xFF7F0E), "Masked values");

        // 4) NaN values: identical break pattern to the masked case.
        double[] x3 = DataGenerator.apply(x, v -> v * 1.0);
        plotBroken(ax, x3, y, keep, new Color(0x2CA02C), "NaN values");

        ax.setTitle("Masked and NaN data");
        ax.legend();
        return fig;
    }

    /** Plot one {@code 'o-'}-style line/marker curve with the given label. */
    private static void plotLine(Axes ax, double[] x, double[] y, Color color, String label) {
        var line = ax.plot(x, y);
        line.setColor(color);
        line.setMarker("o");
        line.setMarkerSize(5f);
        line.setLabel(label);
    }

    /**
     * Plot {@code (x, y)} as a sequence of separate lines, one per
     * contiguous run of {@code keep[i] == true}, leaving a visible gap
     * wherever {@code keep[i]} is false. Only the first run carries
     * {@code label}, so the curve contributes a single legend entry.
     */
    private static void plotBroken(Axes ax, double[] x, double[] y, boolean[] keep,
                                    Color color, String label) {
        int n = x.length;
        int i = 0;
        boolean first = true;
        while (i < n) {
            if (!keep[i]) { i++; continue; }
            int j = i;
            while (j + 1 < n && keep[j + 1]) j++;

            double[] runX = new double[j - i + 1];
            double[] runY = new double[j - i + 1];
            System.arraycopy(x, i, runX, 0, runX.length);
            System.arraycopy(y, i, runY, 0, runY.length);
            plotLine(ax, runX, runY, color, first ? label : null);
            first = false;

            i = j + 1;
        }
    }

    private static double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = list.get(i);
        return arr;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
