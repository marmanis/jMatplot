package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Multicolored Line" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/multicolored_line.html">
 * multicolored_line.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class MulticoloredLineExample {

    /**
     * Build and return the multicolored-line figure.
     * Segments of sin(x) are coloured by the value of cos(x) at that point.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.linspace(0, 3 * Math.PI, 300);
        double[] y = DataGenerator.apply(x, Math::sin);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        for (int i = 0; i < x.length - 1; i++) {
            double dydx = Math.cos(0.5 * (x[i] + x[i + 1]));
            Color c = dydx < -0.5 ? Color.RED
                    : dydx < 0.5  ? new Color(0x2CA02C)
                    : new Color(0x1F77B4);
            Line2D seg = ax.plot(
                    new double[]{x[i], x[i + 1]},
                    new double[]{y[i], y[i + 1]});
            seg.setColor(c);
            seg.setLineWidth(2f);
        }
        ax.setTitle("Multicolored Line");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
