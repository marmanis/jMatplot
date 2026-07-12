package com.marmanis.jMatplot.galleries.plot_types.basic;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import com.marmanis.jMatplot.galleries.GalleryExample;
import com.marmanis.jMatplot.style.StyleSheet;

/**
 * Simple line plot — a sine wave over time.
 * <p>Java equivalent of {@code galleries/gallery/lines_bars_and_markers/simple_plot.py}</p>
 *
 * @see com.marmanis.jMatplot.core.Axes#plot(double[], double[])
 * @see <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/simple_plot.html">
 *      matplotlib: Line plot</a>
 */
public class SimpleLinePlot implements GalleryExample {

    @Override
    public Figure create() {
        StyleSheet.use(StyleSheet.MPL_GALLERY);
        double[] x = DataGenerator.linspace(0, 10, 100);
        double[] y = DataGenerator.apply(x, v -> Math.sin(v));
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(x, y);
        ax.setTitle("A simple plot");
        ax.setXLabel("Time (s)");
        ax.setYLabel("Amplitude");
        return fig;
    }

    public static void main(String[] args) {
        Figure fig = new SimpleLinePlot().create();
        System.out.println("Figure created with " + fig.getAxes().size() + " axes.");
    }
}
