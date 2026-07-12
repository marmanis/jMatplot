package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
import java.util.Arrays;
/**
 * jMatplot equivalent of the matplotlib "Eventcollection Demo" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/eventcollection_demo.html">
 * eventcollection_demo.html</a>
 *
 * <p>Plots two curves — {@code y = x^2} and {@code y = 1 - x^3} over ten
 * sorted random x values each — then marks the x and y data points of each
 * curve with short tick marks ("rug" marks) along the bottom and left axes,
 * colored to match their curve. matplotlib builds these with
 * {@code matplotlib.collections.EventCollection}; {@code Axes} has no such
 * primitive, so each tick is drawn directly as a short {@link Axes#plot}
 * segment anchored at the axis edge (the data-bearing half of what
 * {@code EventCollection}'s default centred tick would show, since the
 * other half falls outside the {@code [0, 1]} axis limits and is clipped
 * away in the original anyway).
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class EventcollectionDemoExample {

    /**
     * Build and return the event-collection demo figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] xdata1 = DataGenerator.uniformRandom(19680801L, 0, 1, 10);
        double[] xdata2 = DataGenerator.uniformRandom(19680802L, 0, 1, 10);
        Arrays.sort(xdata1);
        Arrays.sort(xdata2);
        double[] ydata1 = DataGenerator.apply(xdata1, v -> v * v);
        double[] ydata2 = DataGenerator.apply(xdata2, v -> 1 - v * v * v);

        Color blue = new Color(0x1F77B4);
        Color orange = new Color(0xFF7F0E);
        double tick = 0.03;

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        ax.plot(xdata1, ydata1).setColor(blue);
        ax.plot(xdata2, ydata2).setColor(orange);

        // x-data event ticks along the bottom edge.
        for (double xi : xdata1) ax.plot(new double[]{xi, xi}, new double[]{0, tick}).setColor(blue);
        for (double xi : xdata2) ax.plot(new double[]{xi, xi}, new double[]{0, tick}).setColor(orange);

        // y-data event ticks along the left edge.
        for (double yi : ydata1) ax.plot(new double[]{0, tick}, new double[]{yi, yi}).setColor(blue);
        for (double yi : ydata2) ax.plot(new double[]{0, tick}, new double[]{yi, yi}).setColor(orange);

        ax.setXLim(0, 1);
        ax.setYLim(0, 1);
        ax.setTitle("line plot with data points");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
