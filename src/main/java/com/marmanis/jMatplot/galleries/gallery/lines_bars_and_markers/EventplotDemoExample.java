package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;

import java.awt.Color;
import java.util.Random;

/**
 * jMatplot equivalent of the matplotlib "Eventplot demo" gallery example,
 * built on the native {@link Axes#eventplot} method.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/eventplot_demo.html">
 * eventplot_demo.html</a>
 *
 * <p>Reproduces both 2×2 panels from the real example:
 * <ul>
 *   <li>left column: six rows of 50 uniform-random events, with distinct
 *       per-row colors, offsets, and lengths (some overlapping)</li>
 *   <li>right column: sixty rows of 50 Gamma(4)-distributed events
 *       (chosen only for visual texture, as in the original), all black,
 *       with a uniform offset increment of 1 and length 1</li>
 *   <li>top row: {@code orientation = "horizontal"}; bottom row:
 *       {@code orientation = "vertical"}</li>
 * </ul>
 */
public class EventplotDemoExample {

    private static final Color[] COLORS_6 = {
        new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
        new Color(0xD62728), new Color(0x9467BD), new Color(0x8C564B)
    };
    private static final double[] OFFSETS_1 = {-15, -3, 1, 1.5, 6, 10};
    private static final double[] LENGTHS_1 = {5, 2, 1, 1, 3, 1.5};

    /**
     * Build and return the eventplot demo figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[][] data1 = randomData(19680801L, 6, 50);
        double[][] data2 = gammaData(19680802L, 60, 50, 4);

        Color[] colors2 = new Color[data2.length];
        double[] offsets2 = new double[data2.length];
        double[] lengths2 = new double[data2.length];
        for (int i = 0; i < data2.length; i++) {
            colors2[i] = Color.BLACK;
            offsets2[i] = i + 1; // lineoffsets2=1 increments the offset for each row
            lengths2[i] = 1;
        }

        Figure fig = new Figure();
        Axes[][] grid = fig.subplots(2, 2);

        grid[0][0].eventplot(data1, COLORS_6, OFFSETS_1, LENGTHS_1, "horizontal");
        grid[1][0].eventplot(data1, COLORS_6, OFFSETS_1, LENGTHS_1, "vertical");
        grid[0][1].eventplot(data2, colors2, offsets2, lengths2, "horizontal");
        grid[1][1].eventplot(data2, colors2, offsets2, lengths2, "vertical");

        return fig;
    }

    /** {@code rows} independent rows of {@code cols} uniform-random values in [0, 1). */
    private static double[][] randomData(long seed, int rows, int cols) {
        Random rnd = new Random(seed);
        double[][] data = new double[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                data[r][c] = rnd.nextDouble();
        return data;
    }

    /**
     * {@code rows} independent rows of {@code cols} Gamma({@code shape}, scale=1)
     * distributed values, built as the sum of {@code shape} unit-exponential
     * draws (the Erlang special case of the Gamma distribution). Used purely
     * for visual texture, matching the original example's intent.
     */
    private static double[][] gammaData(long seed, int rows, int cols, int shape) {
        Random rnd = new Random(seed);
        double[][] data = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double sum = 0;
                for (int k = 0; k < shape; k++) sum += -Math.log(rnd.nextDouble());
                data[r][c] = sum;
            }
        }
        return data;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
