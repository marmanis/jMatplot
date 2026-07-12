package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Barchart" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/barchart.html">
 * barchart.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class BarchartExample {

    /**
     * Build and return the grouped bar chart figure.
     * Shows grouped bars for men and women scores across five categories.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.arange(5);
        double[] men   = {20, 34, 30, 35, 27};
        double[] women = {25, 32, 34, 20, 25};
        double w = 0.35;
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Bar m = ax.bar(DataGenerator.add(x, -w / 2), men, w);
        m.setColor(new Color(0x4C72B0));
        Bar f = ax.bar(DataGenerator.add(x, w / 2), women, w);
        f.setColor(new Color(0xDD8452));
        ax.setTitle("Scores by group and gender");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
