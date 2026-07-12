package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Axline" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/axline.html">
 * axline.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class AxlineExample {

    /**
     * Build and return the axline figure.
     * Shows a diagonal line with horizontal and vertical reference lines
     * via {@link Axes#hlines} and {@link Axes#vlines}.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] t = DataGenerator.linspace(-4, 4, 100);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Line2D l = ax.plot(t, DataGenerator.apply(t, v -> 2 * v + 1));
        l.setColor(Color.BLACK);
        ax.hlines(new double[]{0}, -4, 4);
        ax.vlines(new double[]{0}, -2, 8);
        ax.setXLim(-4, 4);
        ax.setYLim(-2, 8);
        ax.setTitle("Infinite Lines (axline)");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
