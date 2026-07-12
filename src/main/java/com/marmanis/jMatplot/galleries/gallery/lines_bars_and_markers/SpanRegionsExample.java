package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.FillBetween;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Span Regions" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/span_regions.html">
 * span_regions.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class SpanRegionsExample {

    /**
     * Build and return the span-regions figure.
     * Positive and negative half-cycles of a sine wave are shaded
     * in green and red respectively using {@link Axes#fillBetween}.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] t = DataGenerator.linspace(0, 2, 201);
        double[] s = DataGenerator.apply(t, v -> Math.sin(2 * Math.PI * v));
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(t, s).setColor(java.awt.Color.BLACK);
        double[] zeros = DataGenerator.zeros(t.length);
        double[] pos   = DataGenerator.apply(s, v -> Math.max(0, v));
        double[] neg   = DataGenerator.apply(s, v -> Math.min(0, v));
        FillBetween fbPos = ax.fillBetween(t, zeros, pos);
        fbPos.setColor(new Color(0x2CA02C));
        fbPos.setAlpha(0.3f);
        FillBetween fbNeg = ax.fillBetween(t, neg, zeros);
        fbNeg.setColor(new Color(0xD62728));
        fbNeg.setAlpha(0.3f);
        ax.setTitle("Shade Regions with fill_between");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
