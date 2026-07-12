package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Simple Plot" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/simple_plot.html">
 * simple_plot.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class SimplePlotExample {

    /**
     * Build and return the simple plot figure.
     * Plots {@code s(t) = 1 + sin(2πt)} over [0, 2].
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] t = DataGenerator.arange(0.0, 2.0, 0.01);
        double[] s = DataGenerator.apply(t, v -> 1 + Math.sin(2 * Math.PI * v));
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Line2D line = ax.plot(t, s);
        line.setColor(new Color(0x1F77B4));
        ax.setTitle("About as simple as it gets, folks");
        ax.setXLabel("time (s)");
        ax.setYLabel("voltage (mV)");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
