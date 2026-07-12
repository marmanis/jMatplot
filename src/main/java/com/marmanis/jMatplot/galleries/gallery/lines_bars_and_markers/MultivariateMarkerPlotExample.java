package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Multivariate Marker Plot" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/multivariate_marker_plot.html">
 * multivariate_marker_plot.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class MultivariateMarkerPlotExample {

    /**
     * Build and return the multivariate marker plot figure.
     * A simple scatter of two normally-distributed variables.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.normalRandom(42, 0, 1, 100);
        double[] y = DataGenerator.normalRandom(43, 0, 1, 100);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Scatter sc = ax.scatter(x, y);
        sc.setColor(new Color(0x2CA02C));
        sc.setAlpha(0.5f);
        ax.setTitle("Multivariate Marker Plot");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
