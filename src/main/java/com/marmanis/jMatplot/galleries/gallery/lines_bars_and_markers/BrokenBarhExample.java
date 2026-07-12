package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Broken Barh" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/broken_barh.html">
 * broken_barh.html</a>
 *
 * <p>Reproduces matplotlib's "Resource usage" example: six rows of
 * {@code broken_barh}-style segments — four CPU activity rows plus disk and
 * network rows — ordered top-to-bottom as CPU 1, CPU 2, CPU 3, CPU 4, disk,
 * network (matching the source's {@code ax.invert_yaxis()} call). Each
 * segment is rendered as a {@link Bar} rectangle: {@code (start, width)}
 * along x at a fixed {@code (y, height)} row, exactly matching
 * matplotlib's {@code ax.broken_barh(xranges, (ypos, height))} semantics.
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class BrokenBarhExample {

    /**
     * Build and return the broken-horizontal-bars figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        // data is a sequence of (start, duration) pairs, one row per resource.
        double[][] cpu1 = {{0, 3}, {3.5, 1}, {5, 5}};
        double[][] cpu2 = columnStack(DataGenerator.linspace(0, 9, 10), 0.5);
        double[][] cpu3 = columnStack(DataGenerator.apply(
                DataGenerator.uniformRandom(1, 0, 1, 61), v -> 10 * v), 0.05);
        double[][] cpu4 = {{2, 1.7}, {7, 1.2}};
        double[][] disk = {{1, 1.5}};
        double[][] network = columnStack(DataGenerator.apply(
                DataGenerator.uniformRandom(2, 0, 1, 10), v -> 10 * v), 0.05);

        String[] rowLabels = {"CPU 1", "CPU 2", "CPU 3", "CPU 4", "disk", "network"};
        double[][][] rows = {cpu1, cpu2, cpu3, cpu4, disk, network};
        Color[] rowColors = {
            new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
            new Color(0xD62728), new Color(0xFF7F0E), new Color(0x2CA02C)
        };

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double height = 0.4;
        int n = rowLabels.length;
        double[] yTickPos = new double[n];
        for (int row = 0; row < n; row++) {
            // invert_yaxis(): row 0 (CPU 1) plotted at the top.
            double yCenter = n - 1 - row;
            yTickPos[row] = yCenter;
            for (double[] seg : rows[row]) {
                double start = seg[0], width = seg[1];
                Bar b = ax.bar(new double[]{start + width / 2}, new double[]{height},
                        width, new double[]{yCenter - height / 2});
                b.setColor(rowColors[row]);
            }
        }

        ax.setXLim(0, 10);
        ax.setYTickLabels(yTickPos, rowLabels);
        ax.setTitle("Resource usage");
        return fig;
    }

    /** Pair each x value with a constant width, mirroring numpy's {@code column_stack}. */
    private static double[][] columnStack(double[] starts, double width) {
        double[][] out = new double[starts.length][2];
        for (int i = 0; i < starts.length; i++) {
            out[i][0] = starts[i];
            out[i][1] = width;
        }
        return out;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
