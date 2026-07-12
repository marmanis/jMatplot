package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "Comparison of {@code pyplot.step} and
 * {@code pyplot.stairs}" figure from the matplotlib "Stairs Demo" gallery
 * page.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/stairs_demo.html">
 * stairs_demo.html</a>
 *
 * <p>{@code pyplot.step} positions steps with a left-edge array the same
 * length as the values, so the staircase stops at the last given x with no
 * further extension. {@code pyplot.stairs} instead takes a bin-{@code edges}
 * array one element longer than the values, so it draws one more horizontal
 * segment out to the final edge. This class reproduces that exact
 * discrepancy alongside grey dashed reference lines (showing the underlying
 * data points the staircases are built from) and red corner markers
 * (showing every vertex of the {@code stairs()} outline) — exactly as the
 * matplotlib example does.
 *
 * <p>{@code Axes} has no native {@code step()}/{@code stairs()} methods, so
 * both staircases are built manually as expanded polylines via
 * {@link Axes#plot}.
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class StepVsStairsExample {

    /**
     * Build and return the step-vs-stairs comparison figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] bins = DataGenerator.arange(14); // 0..13
        double[] centers = new double[bins.length - 1];
        for (int i = 0; i < centers.length; i++) centers[i] = bins[i] + 0.5;
        double[] y = DataGenerator.apply(centers, v -> Math.sin(v / 2));

        double[] xLeft = new double[bins.length - 1]; // bins[:-1]
        System.arraycopy(bins, 0, xLeft, 0, xLeft.length);

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        Color grey = new Color(0x80, 0x80, 0x80, 77);  // alpha 0.3
        Color red  = new Color(0xFF, 0x00, 0x00, 51);  // alpha 0.2

        // step(bins[:-1], y, where='post')
        double[][] stepPath = stepPost(xLeft, y);
        var stepLine = ax.plot(stepPath[0], stepPath[1]);
        stepLine.setColor(new Color(0x1F77B4));
        stepLine.setLabel("step(where=\"post\")");

        // Grey dashed reference line at the raw (xLeft, y) points.
        var grey1 = ax.plot(xLeft, y);
        grey1.setColor(grey);
        grey1.setDashPattern(new float[]{8f, 6f});
        grey1.setMarker("o");
        grey1.setMarkerSize(5f);

        // stairs(y - 1, bins, baseline=None)
        double[] yShifted = DataGenerator.add(y, -1);
        double[][] stairsPath = stairsNoBaseline(bins, yShifted);
        var stairsLine = ax.plot(stairsPath[0], stairsPath[1]);
        stairsLine.setColor(new Color(0xFF7F0E));
        stairsLine.setLabel("stairs()");

        // Grey dashed reference line at the raw (centers, y - 1) points.
        var grey2 = ax.plot(centers, yShifted);
        grey2.setColor(grey);
        grey2.setDashPattern(new float[]{8f, 6f});
        grey2.setMarker("o");
        grey2.setMarkerSize(5f);

        // Red corner markers at every vertex of the stairs() outline.
        double[] cornerX = new double[bins.length * 2];
        double[] cornerY = new double[bins.length * 2];
        for (int i = 0; i < bins.length; i++) {
            cornerX[2 * i] = bins[i];
            cornerX[2 * i + 1] = bins[i];
        }
        cornerY[0] = y[0] - 1;
        for (int i = 0; i < y.length; i++) {
            cornerY[2 * i + 1] = y[i] - 1;
            cornerY[2 * i + 2] = y[i] - 1;
        }
        cornerY[cornerY.length - 1] = y[y.length - 1] - 1;
        var corners = ax.scatter(cornerX, cornerY);
        corners.setMarker("o");
        corners.setMarkerSize(5f);
        corners.setColor(red);

        ax.setTitle("step() vs. stairs()");
        ax.legend();
        return fig;
    }

    /**
     * Build a post-step staircase path from left-edge x-values and their
     * step heights: {@code (x[0],y[0]), (x[1],y[0]), (x[1],y[1]), ...},
     * stopping at {@code (x[n-1], y[n-1])} with no further extension —
     * matching {@code pyplot.step(..., where='post')}.
     *
     * @return {@code {xs, ys}} expanded path arrays
     */
    private static double[][] stepPost(double[] x, double[] y) {
        int n = x.length;
        double[] xs = new double[2 * n - 1];
        double[] ys = new double[2 * n - 1];
        xs[0] = x[0];
        ys[0] = y[0];
        for (int i = 1; i < n; i++) {
            xs[2 * i - 1] = x[i];
            ys[2 * i - 1] = y[i - 1];
            xs[2 * i] = x[i];
            ys[2 * i] = y[i];
        }
        return new double[][]{xs, ys};
    }

    /**
     * Build a full staircase outline from {@code edges} (one longer than
     * {@code values}): a horizontal segment for every value out to its right
     * edge, with vertical connectors between them — matching
     * {@code pyplot.stairs(values, edges, baseline=None)}.
     *
     * @return {@code {xs, ys}} expanded path arrays
     */
    private static double[][] stairsNoBaseline(double[] edges, double[] values) {
        int n = values.length;
        double[] xs = new double[2 * n];
        double[] ys = new double[2 * n];
        for (int i = 0; i < n; i++) {
            xs[2 * i] = edges[i];
            ys[2 * i] = values[i];
            xs[2 * i + 1] = edges[i + 1];
            ys[2 * i + 1] = values[i];
        }
        return new double[][]{xs, ys};
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
