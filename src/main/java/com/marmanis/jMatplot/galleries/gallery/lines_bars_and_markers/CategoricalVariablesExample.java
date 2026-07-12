package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Categorical Variables" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/categorical_variables.html">
 * categorical_variables.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class CategoricalVariablesExample {

    /**
     * Build and return the categorical-variables figure.
     * A simple bar chart whose x-axis uses string category labels.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] names  = {"apple", "orange", "lemon", "lime"};
        double[] values = {10, 15, 5, 20};
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        Bar bars = ax.bar(names, values);
        bars.setColor(new Color(0x4C72B0));
        ax.setTitle("Categorical Plotting");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
