package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Bar;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Hat Graph" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/hat_graph.html">
 * hat_graph.html</a>
 *
 * <p>A "hat graph" compares a second group's value against a first group's
 * value by drawing the first group as a zero-height, transparent-fill
 * "brim" marker sitting exactly at its value, and the second group as a
 * solid bar that floats from the first group's value up to its own —
 * a colored "hat" perched on the brim. matplotlib builds this with
 * {@code ax.grouped_bar(values - values[0], bottom=values[0],
 * colors=['none', ...])}; jMatplot has no grouped-bar primitive, so the
 * same geometry is reproduced directly with per-category {@link Bar} calls.
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class HatGraphExample {

    /**
     * Build and return the hat-graph figure.
     *
     * <p>Five games (categories I–V) compare two players' scores: Player A's
     * value is marked by a hollow brim at its height; Player B's value is
     * shown as a solid bar rising from Player A's brim to Player B's score.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        String[] xlabels = {"I", "II", "III", "IV", "V"};
        double[] playerA = {5, 15, 22, 20, 25};
        double[] playerB = {25, 32, 34, 30, 27};

        double[] x = DataGenerator.arange(xlabels.length);
        double width = 0.35;

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        for (int i = 0; i < xlabels.length; i++) {
            double xA = x[i] - 0.2, xB = x[i] + 0.2;

            // Player A: zero-height, transparent-fill "brim" marking its value.
            Bar brim = ax.bar(new double[]{xA}, new double[]{0}, width, new double[]{playerA[i]});
            brim.setColor(new Color(0, 0, 0, 0));
            if (i == 0) brim.setLabel("Player A");
            ax.text(xA - 0.1, playerA[i] + 1, String.valueOf((int) playerA[i]));

            // Player B: solid "hat" floating from Player A's brim to Player B's value.
            Bar hat = ax.bar(new double[]{xB}, new double[]{playerB[i] - playerA[i]}, width,
                    new double[]{playerA[i]});
            hat.setColor(new Color(0x1F77B4));
            if (i == 0) hat.setLabel("Player B");
            ax.text(xB - 0.1, playerB[i] + 1, String.valueOf((int) playerB[i]));
        }

        ax.setXTicks(x, xlabels);
        ax.setXLabel("Games");
        ax.setYLabel("Score");
        ax.setYLim(0, 60);
        ax.setTitle("Scores by number of game and players");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
