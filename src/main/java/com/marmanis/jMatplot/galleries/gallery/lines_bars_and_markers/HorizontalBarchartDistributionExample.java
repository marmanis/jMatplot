package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.BarH;
import com.marmanis.jMatplot.core.Figure;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Horizontal Barchart Distribution" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/horizontal_barchart_distribution.html">
 * horizontal_barchart_distribution.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class HorizontalBarchartDistributionExample {

    /**
     * Build and return the horizontal barchart distribution figure.
     * Stacked horizontal bars show the distribution of responses for three
     * survey questions across five categories.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[][] data = {
                {10, 15, 17, 32, 26},
                {26, 22, 29, 10, 13},
                {35, 37,  7,  2, 19}
        };
        Color[] palette = {
                new Color(0xD62728), new Color(0xFF7F0E), new Color(0xBCBD22),
                new Color(0x2CA02C), new Color(0x1F77B4)
        };
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        for (int q = 0; q < 3; q++) {
            double left = 0;
            for (int cat = 0; cat < 5; cat++) {
                double val = data[q][cat];
                BarH seg = ax.barh(new double[]{q}, new double[]{val}, 0.5);
                seg.setColor(palette[cat]);
                seg.setLeft(new double[]{left});
                left += val;
            }
        }
        ax.setTitle("Discrete Distribution");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
