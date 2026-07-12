package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Step Demo" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/step_demo.html">
 * step_demo.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class StepDemoExample {

    /**
     * Build and return the step demo figure.
     * Plots {@code sin(x/2) + 2} as a pre-step (hold-left) staircase.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.arange(14);
        double[] y = DataGenerator.apply(x, v -> Math.sin(v / 2));
        // Expand into pre-step staircase: for each step hold value until next x
        double[] xs = new double[x.length * 2 - 1];
        double[] ys = new double[x.length * 2 - 1];
        for (int i = 0; i < x.length; i++) {
            xs[i * 2] = x[i];
            ys[i * 2] = y[i] + 2;
            if (i < x.length - 1) {
                xs[i * 2 + 1] = x[i + 1];
                ys[i * 2 + 1] = y[i] + 2;
            }
        }
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Line2D l = ax.plot(xs, ys);
        l.setColor(new Color(0x1F77B4));
        ax.setTitle("Step Demo (pre-step)");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
