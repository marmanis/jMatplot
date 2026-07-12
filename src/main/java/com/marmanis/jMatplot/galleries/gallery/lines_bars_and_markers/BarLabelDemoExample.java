package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Bar Label Demo" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/bar_label_demo.html">
 * bar_label_demo.html</a>
 *
 * <p>Demonstrates adding numeric labels at the top of each bar using
 * {@link Axes#text}.  Three bar groups show results for three test conditions.
 */
public class BarLabelDemoExample {

    /**
     * Build and return the bar-label demo figure.
     *
     * <p>Two grouped bars (group A and B) per category are drawn side by side.
     * The height of each bar is annotated with its value above the bar top.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] categories = {"Cat 1", "Cat 2", "Cat 3", "Cat 4", "Cat 5"};
        double[] groupA = {35, 30, 25, 40, 45};
        double[] groupB = {45, 40, 30, 25, 35};
        double w = 0.35;
        double[] x = DataGenerator.arange(categories.length);

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        Bar bA = ax.bar(DataGenerator.add(x, -w / 2), groupA, w);
        bA.setColor(new Color(0x4C72B0));

        Bar bB = ax.bar(DataGenerator.add(x, w / 2), groupB, w);
        bB.setColor(new Color(0xDD8452));

        // Add value labels above each bar
        for (int i = 0; i < groupA.length; i++) {
            ax.text(x[i] - w / 2, groupA[i] + 0.5,
                    String.valueOf((int) groupA[i]));
            ax.text(x[i] + w / 2, groupB[i] + 0.5,
                    String.valueOf((int) groupB[i]));
        }

        ax.setXTicks(x, categories);
        ax.setYLim(0, 55);
        ax.setTitle("Bar Label Demo");
        ax.setYLabel("Score");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
