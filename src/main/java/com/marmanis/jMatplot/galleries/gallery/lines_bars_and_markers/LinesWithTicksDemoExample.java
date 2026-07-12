package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.TickedLine;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Lines with Ticks Demo" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/lines_with_ticks_demo.html">
 * lines_with_ticks_demo.html</a>
 *
 * <p>Demonstrates {@link TickedLine} — a line artist that draws small perpendicular
 * tick marks at regular intervals along its length.  This is useful for
 * distinguishing boundary curves on technical diagrams (e.g. geology cross-sections).
 */
public class LinesWithTicksDemoExample {

    /**
     * Build and return the ticked-lines demo figure.
     *
     * <p>Three ticked lines are plotted:
     * <ol>
     *   <li>A horizontal sine wave with ticks pointing upward.</li>
     *   <li>A diagonal line with ticks at 45°.</li>
     *   <li>A cosine curve with denser, shorter ticks.</li>
     * </ol>
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = DataGenerator.linspace(0, 2 * Math.PI, 100);

        // 1. Sine wave — ticks perpendicular to the line direction
        double[] y1 = DataGenerator.apply(x, Math::sin);
        TickedLine tl1 = ax.plotTicked(x, y1);
        tl1.setColor(new Color(0x1F77B4));
        tl1.setLineWidth(2f);
        tl1.setSpacingPx(16f);
        tl1.setTickLengthPx(8.0f);
        tl1.setLabel("sin(x)");

        // 2. Offset sine (acts like a roughly diagonal line segment by segment)
        double[] y2 = DataGenerator.apply(x, v -> 0.5 * Math.sin(v) + 0.5);
        TickedLine tl2 = ax.plotTicked(x, y2);
        tl2.setColor(new Color(0xFF7F0E));
        tl2.setLineWidth(2f);
        tl2.setSpacingPx(10f);
        tl2.setTickLengthPx(6.0f);
        tl2.setLabel("0.5·sin(x) + 0.5");

        // 3. Cosine — shorter, denser ticks
        double[] y3 = DataGenerator.apply(x, v -> Math.cos(v) - 0.5);
        TickedLine tl3 = ax.plotTicked(x, y3);
        tl3.setColor(new Color(0x2CA02C));
        tl3.setLineWidth(1.5f);
        tl3.setSpacingPx(6f);
        tl3.setTickLengthPx(4.0f);
        tl3.setLabel("cos(x) − 0.5");

        ax.setXLim(0, 2 * Math.PI);
        ax.setTitle("Lines with Ticks Demo");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
