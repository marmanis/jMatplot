package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.BarH;
import com.marmanis.jMatplot.core.Figure;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Barh" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/barh.html">
 * barh.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class BarhExample {

    /**
     * Build and return the horizontal bar chart figure.
     *
     * <p>Matches matplotlib's example: bars carry horizontal error bars
     * ({@code xerr}). {@code Axes} has no {@code errorbar()}/{@code xerr}
     * primitive, so each error bar is drawn manually as a black whisker line
     * plus two short vertical end-caps, at the same y-position as its bar.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] people = {"Tom", "Dick", "Harry", "Slim", "Jim"};
        double[] perf   = {5, 7, 6, 4, 9};
        double[] error  = {0.2, 0.4, 0.3, 0.6, 0.2};

        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        BarH bars = ax.barh(people, perf);
        bars.setColor(new Color(0x4C72B0));

        double capHalf = 0.15;
        for (int i = 0; i < people.length; i++) {
            double y = i;
            double xLo = perf[i] - error[i], xHi = perf[i] + error[i];
            ax.plot(new double[]{xLo, xHi}, new double[]{y, y}).setColor(Color.BLACK);
            ax.plot(new double[]{xLo, xLo}, new double[]{y - capHalf, y + capHalf}).setColor(Color.BLACK);
            ax.plot(new double[]{xHi, xHi}, new double[]{y - capHalf, y + capHalf}).setColor(Color.BLACK);
        }

        ax.setTitle("How fast do you want to go today?");
        ax.setXLabel("Performance");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
