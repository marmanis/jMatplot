package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Scatter Star Poly" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_star_poly.html">
 * scatter_star_poly.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class ScatterStarPolyExample {

    /**
     * Build and return the star-polygon scatter figure.
     * Uses the star marker ({@code "*"}) to visualise a uniform random scatter.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.uniformRandom(42, 0, 1, 200);
        double[] y = DataGenerator.uniformRandom(43, 0, 1, 200);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Scatter sc = ax.scatter(x, y);
        sc.setMarker("*");
        sc.setMarkerSize(8f);
        sc.setColor(new Color(0xFF7F0E));
        ax.setTitle("Star Polygon Scatter");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
