package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Vline Hline Demo" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/vline_hline_demo.html">
 * vline_hline_demo.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class VlineHlineDemoExample {

    /**
     * Build and return the hlines-and-vlines demo figure.
     * A damped cosine with horizontal and vertical reference lines.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] t = DataGenerator.arange(0.0, 5.0, 0.1);
        double[] s = DataGenerator.apply(t,
                v -> Math.exp(-v) * Math.cos(2 * Math.PI * v));
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(t, s).setColor(new Color(0x1F77B4));
        ax.vlines(new double[]{1, 2, 3, 4}, -0.7, 0.7);
        ax.hlines(new double[]{-0.5, 0, 0.5}, 0, 5);
        ax.setTitle("hlines and vlines");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
