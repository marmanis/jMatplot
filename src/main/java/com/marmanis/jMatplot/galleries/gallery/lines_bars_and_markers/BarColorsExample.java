package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Bar Colors" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/bar_colors.html">
 * bar_colors.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class BarColorsExample {

    /**
     * Build and return the bar-colours figure.
     * Demonstrates per-bar colour assignment on a fruit-supply chart.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] fruits = {"apple", "blueberry", "cherry", "orange"};
        double[] counts = {40, 100, 30, 55};
        Color[] colors = {new Color(0xD62728), new Color(0x1F77B4),
                new Color(0xD62728), new Color(0xFF7F0E)};
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Bar bars = ax.bar(fruits, counts);
        bars.setColors(colors);
        ax.setTitle("Fruit supply by kind and color");
        ax.setYLabel("fruit supply");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
