package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Joinstyle" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/joinstyle.html">
 * joinstyle.html</a>
 *
 * <p>Demonstrates the three line join styles available in Java's
 * {@link java.awt.BasicStroke}: MITER, ROUND, and BEVEL.  For each join style
 * a row of five subplots shows the style applied to a zigzag path at different
 * line widths (1, 3, 8, 15, 25 px).
 *
 * <p><strong>Figure size:</strong> {@code 700 × 1000 px} — five rows of subplots.
 */
public class JoinstyleExample {

    /** Pixel width of the exported image. */
    public static final int WIDTH  = 700;
    /** Pixel height of the exported image. */
    public static final int HEIGHT = 1000;

    private static final String[] JOIN_NAMES  = {"MITER", "ROUND", "BEVEL"};
    private static final int[]    JOIN_STYLES  = {
        BasicStroke.JOIN_MITER,
        BasicStroke.JOIN_ROUND,
        BasicStroke.JOIN_BEVEL
    };
    private static final float[]  LINE_WIDTHS  = {1f, 3f, 8f, 15f, 25f};
    private static final Color[]  COLORS       = {
        new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
        new Color(0xD62728), new Color(0x9467BD)
    };

    // Zigzag path: up-down-up pattern to clearly show join style at vertices
    private static final double[] ZX = {0.1, 0.3, 0.5, 0.7, 0.9};
    private static final double[] ZY = {0.2, 0.8, 0.2, 0.8, 0.2};

    /**
     * Build and return the join-style figure.
     *
     * <p>Three groups (one per join style) of five subplots each, arranged
     * vertically.  Each subplot shows the zigzag path rendered at a different
     * line width so the join-style effect is clearly visible.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        int nGroups = 3, nCols = 5;
        double groupH = 1.0 / nGroups;
        double padX = 0.03, padY = 0.05;
        double cellW = (1.0 - padX * (nCols + 1)) / nCols;
        double cellH = groupH - 2 * padY;

        for (int g = 0; g < nGroups; g++) {
            double groupBottom = 1.0 - (g + 1) * groupH;
            for (int c = 0; c < nCols; c++) {
                double left   = padX + c * (cellW + padX);
                double bottom = groupBottom + padY;
                Axes ax = fig.addAxes(left, bottom, cellW, cellH);
                Line2D line = ax.plot(ZX, ZY);
                line.setColor(COLORS[c]);
                line.setLineWidth(LINE_WIDTHS[c]);
                line.setJoinStyle(JOIN_STYLES[g]);
                line.setCapStyle(BasicStroke.CAP_BUTT);
                ax.setXLim(0, 1);
                ax.setYLim(0, 1);
                ax.setTitle(JOIN_NAMES[g] + " lw=" + (int) LINE_WIDTHS[c]);
                ax.setXTickLabelsVisible(false);
                ax.setYTickLabelsVisible(false);
            }
        }
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
