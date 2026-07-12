package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Linestyles" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/linestyles.html">
 * linestyles.html</a>
 *
 * <p>The figure is split into two stacked sections:
 * <ol>
 *   <li><strong>Named linestyles</strong> — solid, dotted, dashed, dashdot.</li>
 *   <li><strong>Parametrized linestyles</strong> — thirteen dash patterns derived
 *       from matplotlib's (offset, on_off_seq) tuples, each mapped to a
 *       float[] dash pattern for Java's BasicStroke.</li>
 * </ol>
 *
 * <p><strong>Figure size:</strong> 640 x 920 px.
 */
public class LinestylesExample {

    /** Named linestyles (top panel). */
    private static final String[] NAMED_LABELS = {
        "solid", "dotted", "dashed", "dashdot"
    };

    /** Dash patterns for named linestyles; null = solid. */
    private static final float[][] NAMED_DASHES = {
        null,
        {2f,  4f},
        {10f, 10f},
        {10f, 6f, 2f, 6f}
    };

    /** Parametrized linestyles (bottom panel). */
    private static final String[] PARAM_LABELS = {
        "loosely dotted",
        "dotted",
        "densely dotted",
        "long dash with offset",
        "loosely dashed",
        "dashed",
        "densely dashed",
        "loosely dashdotted",
        "dashdotted",
        "densely dashdotted",
        "dashdotdotted",
        "loosely dashdotdotted",
        "densely dashdotdotted"
    };

    /**
     * Dash patterns for the parametrized linestyles.
     * Values are scaled from matplotlib point units for clarity.
     */
    private static final float[][] PARAM_DASHES = {
        {2f,  20f},
        {2f,  4f},
        {2f,  2f},
        {20f, 6f},
        {10f, 20f},
        {10f, 10f},
        {10f, 2f},
        {6f,  20f, 2f, 20f},
        {6f,  10f, 2f, 10f},
        {6f,  2f,  2f, 2f},
        {6f,  10f, 2f, 10f, 2f, 10f},
        {6f,  20f, 2f, 20f, 2f, 20f},
        {6f,  2f,  2f, 2f,  2f, 2f}
    };

    private static final Color LINE_COLOR = new Color(0x1a, 0x1a, 0x2e);

    /** Pixel width of the exported image. */
    public static final int WIDTH  = 640;

    /** Pixel height of the exported image. */
    public static final int HEIGHT = 920;

    /**
     * Build and return the linestyles figure.
     *
     * <p>Two manually-positioned Axes panels:
     * <ul>
     *   <li>Top (y 57-95%) — four named linestyles.</li>
     *   <li>Bottom (y 3-55%) — thirteen parametrized linestyles.</li>
     * </ul>
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();

        Axes axNamed = fig.addAxes(0.32, 0.57, 0.65, 0.37);
        buildPanel(axNamed, NAMED_LABELS, NAMED_DASHES, "Named linestyles");

        Axes axParam = fig.addAxes(0.32, 0.03, 0.65, 0.51);
        buildPanel(axParam, PARAM_LABELS, PARAM_DASHES, "Parametrized linestyles");

        return fig;
    }

    /**
     * Populate an axes panel with one horizontal line per linestyle.
     *
     * @param ax      axes to populate
     * @param labels  style names (become Y-tick labels)
     * @param dashes  dash patterns (null = solid)
     * @param title   panel title
     */
    private static void buildPanel(Axes ax, String[] labels,
                                   float[][] dashes, String title) {
        int n = labels.length;
        for (int i = 0; i < n; i++) {
            Line2D line = ax.plot(new double[]{0.0, 1.0}, new double[]{i, i});
            line.setColor(LINE_COLOR);
            line.setLineWidth(1.8f);
            line.setCapStyle(java.awt.BasicStroke.CAP_ROUND);
            line.setDashPattern(dashes[i]);
        }
        ax.setYLim(-0.5, n - 0.5);
        ax.setXLim(0.0, 1.0);
        double[] yPos = new double[n];
        for (int i = 0; i < n; i++) yPos[i] = i;
        ax.setYTickLabels(yPos, labels);
        ax.setXTickMarksVisible(false);
        ax.setXTickLabelsVisible(false);
        ax.setYTickMarksVisible(false);
        ax.setTitle(title);
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
