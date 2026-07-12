package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the "polar plots" group from the matplotlib
 * "Markevery Demo" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/markevery_demo.html">
 * markevery_demo.html</a>
 *
 * <p>jMatplot has no native polar projection, so this group is approximated
 * by converting a {@code (theta, r)} spiral to Cartesian
 * {@code (r·cos θ, r·sin θ)} and plotting it on a regular axes, with the
 * same nine {@code markevery} cases applied.
 */
public class MarkeveryPolarExample {

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
     * Build and return the markevery-with-polar-plots figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        int cols = 3, rows = 3;
        double padX = 0.04, padY = 0.04;
        double cellW = (1.0 - padX * (cols + 1)) / cols;
        double cellH = (1.0 - padY * (rows + 1)) / rows;

        double[] r = DataGenerator.linspace(0, 3.0, 200);
        double[] theta = DataGenerator.apply(r, v -> 2 * Math.PI * v);
        double[] x = new double[r.length];
        double[] y = new double[r.length];
        for (int i = 0; i < r.length; i++) {
            x[i] = r[i] * Math.cos(theta[i]);
            y[i] = r[i] * Math.sin(theta[i]);
        }

        for (int rIdx = 0; rIdx < rows; rIdx++) {
            for (int c = 0; c < cols; c++) {
                int idx = rIdx * cols + c;
                double left   = padX + c * (cellW + padX);
                double bottom = padY + (rows - 1 - rIdx) * (cellH + padY);
                Axes ax = fig.addAxes(left, bottom, cellW, cellH);

                Line2D line = ax.plot(x, y);
                line.setColor(COLORS[idx % COLORS.length]);
                line.setLineWidth(1.5f);
                line.setMarker("o");
                line.setMarkerSize(6f);
                MarkeveryCases.apply(line, idx);

                ax.setTitle("markevery=" + CASE_LABELS[idx] + " (polar)");
                ax.setXTickLabelsVisible(false);
                ax.setYTickLabelsVisible(false);
            }
        }
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
