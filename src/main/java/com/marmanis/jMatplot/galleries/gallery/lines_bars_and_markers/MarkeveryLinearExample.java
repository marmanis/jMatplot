package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "linear" group from the matplotlib "Markevery
 * Demo" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/markevery_demo.html">
 * markevery_demo.html</a>
 *
 * <p>Renders the same curve nine times on plain linear axes, each with a
 * different {@code markevery} case (an integer step, an explicit index list,
 * a start/step tuple, a fractional spacing, etc.), demonstrating the
 * different ways markers can be thinned out along a line.
 */
public class MarkeveryLinearExample {

    /** Pixel width of the exported image. */
    public static final int WIDTH  = 900;
    /** Pixel height of the exported image (one 3x3 grid). */
    public static final int HEIGHT = 600;

    private static final String[] CASE_LABELS = {
        "every point", "8", "(30, 8)", "[16, 24, 32]", "[0, -1]",
        "slice(100, 200, 3)", "0.1", "0.3", "1.5"
    };

    private static final Color[] COLORS = {
        new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
        new Color(0xD62728), new Color(0x9467BD), new Color(0x8C564B),
        new Color(0xE377C2), new Color(0x7F7F7F), new Color(0xBCBD22)
    };

    /**
     * Build and return the markevery-with-linear-scales figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        int cols = 3, rows = 3;
        double padX = 0.04, padY = 0.04;
        double cellW = (1.0 - padX * (cols + 1)) / cols;
        double cellH = (1.0 - padY * (rows + 1)) / rows;

        double delta = 0.11;
        double[] x = DataGenerator.apply(
                DataGenerator.linspace(0, 10 - 2 * delta, 200), v -> v + delta);
        double[] y = DataGenerator.apply(x, v -> Math.sin(v) + 1.0 + delta);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int idx = r * cols + c;
                double left   = padX + c * (cellW + padX);
                double bottom = padY + (rows - 1 - r) * (cellH + padY);
                Axes ax = fig.addAxes(left, bottom, cellW, cellH);

                Line2D line = ax.plot(x, y);
                line.setColor(COLORS[idx % COLORS.length]);
                line.setLineWidth(1.5f);
                line.setMarker("o");
                line.setMarkerSize(6f);
                MarkeveryCases.apply(line, idx);

                ax.setTitle("markevery=" + CASE_LABELS[idx]);
                ax.setXTickLabelsVisible(false);
                ax.setYTickLabelsVisible(false);
            }
        }
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
