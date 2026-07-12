package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Stairs Demo" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/stairs_demo.html">
 * stairs_demo.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class StairsDemoExample {

    /**
     * Build and return the stairs demo figure.
     * Renders a step histogram by drawing each bar as a closed polygon
     * using four-point line segments.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] hist  = {0, 1, 2, 4, 6, 8, 7, 5, 5, 4, 3, 2, 1, 1, 0, 0, 0, 0};
        double[] edges = DataGenerator.linspace(-3, 3, hist.length + 1);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        for (int i = 0; i < hist.length; i++) {
            double x0 = edges[i], x1 = edges[i + 1], h = hist[i];
            Line2D l = ax.plot(
                    new double[]{x0, x0, x1, x1},
                    new double[]{0,  h,  h,  0});
            l.setColor(new Color(0x4C72B0));
        }
        ax.setTitle("Stairs Demo");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
