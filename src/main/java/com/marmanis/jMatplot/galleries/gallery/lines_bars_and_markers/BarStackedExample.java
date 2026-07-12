package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Stacked Bar Chart" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/bar_stacked.html">
 * bar_stacked.html</a>
 *
 * <p>Each bar is composed of stacked segments — one segment per group.
 * The {@code bottom} parameter shifts each successive segment upward to sit
 * on top of the previous one.
 */
public class BarStackedExample {

    /**
     * Build and return the stacked bar chart figure.
     *
     * <p>Matches matplotlib's penguin body-mass example: three species
     * categories, each bar split into two stacked segments ("Below" and
     * "Above" average body mass).
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] categories = {
            "Adelie (μ=3700.66g)",
            "Chinstrap (μ=3733.09g)",
            "Gentoo (μ=5076.02g)"
        };
        double[][] segments = {
            {70, 31, 58},   // Below average
            {82, 37, 66}    // Above average
        };
        Color[] colors = {
            new Color(0x4C72B0), new Color(0xDD8452)
        };
        String[] labels = {"Below", "Above"};

        double[] x = DataGenerator.arange(categories.length);
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] bottom = new double[categories.length];
        for (int g = 0; g < segments.length; g++) {
            // Pass a snapshot: Bar.setBottom() keeps this array by reference,
            // so mutating `bottom` in place below would retroactively corrupt
            // the base of every previously created Bar sharing the same array.
            Bar bar = ax.bar(x, segments[g], 0.5, bottom.clone());
            bar.setColor(colors[g]);
            bar.setLabel(labels[g]);
            // Accumulate bottom for the next segment
            for (int i = 0; i < bottom.length; i++) bottom[i] += segments[g][i];
        }

        ax.setXTicks(x, categories);
        ax.setTitle("Number of penguins with above average body mass");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
