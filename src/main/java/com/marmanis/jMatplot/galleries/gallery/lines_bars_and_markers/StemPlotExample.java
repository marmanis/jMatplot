package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Stem Plot" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/stem_plot.html">
 * stem_plot.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class StemPlotExample {

    /**
     * Build and return the stem-plot figure.
     * Plots {@code e^{sin(x)}} with vertical stems and circular markers.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.linspace(0.1, 2 * Math.PI, 30);
        double[] y = DataGenerator.apply(x, v -> Math.exp(Math.sin(v)));
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        for (int i = 0; i < x.length; i++) {
            Line2D stem = ax.plot(
                    new double[]{x[i], x[i]},
                    new double[]{0,    y[i]});
            stem.setColor(new Color(0x4C72B0));
        }
        Scatter tips = ax.scatter(x, y);
        tips.setColor(new Color(0x1F77B4));
        tips.setMarker("o");
        ax.hlines(new double[]{0}, x[0], x[x.length - 1]);
        ax.setTitle("Stem Plot");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
