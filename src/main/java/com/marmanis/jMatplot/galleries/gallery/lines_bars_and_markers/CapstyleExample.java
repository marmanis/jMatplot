package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Cap Style" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/capstyle.html">
 * capstyle.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class CapstyleExample {

    /**
     * Build and return the cap-style figure.
     * Demonstrates the three Java {@link java.awt.BasicStroke} cap styles.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        double[][] xs = {{0.2, 0.8}, {0.2, 0.8}, {0.2, 0.8}};
        double[][] ys = {{2.5, 2.5}, {1.5, 1.5}, {0.5, 0.5}};
        int[] caps = {java.awt.BasicStroke.CAP_BUTT,
                java.awt.BasicStroke.CAP_ROUND,
                java.awt.BasicStroke.CAP_SQUARE};
        String[] names = {"CAP_BUTT", "CAP_ROUND", "CAP_SQUARE"};
        Color[] cols = {new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C)};
        for (int i = 0; i < 3; i++) {
            Line2D l = ax.plot(xs[i], ys[i]);
            l.setLineWidth(12f);
            l.setCapStyle(caps[i]);
            l.setColor(cols[i]);
            ax.text(-0.02, ys[i][0], names[i]);
        }
        ax.setXLim(-0.1, 1.1);
        ax.setYLim(0.0, 3.2);
        ax.setTitle("Cap Styles");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
