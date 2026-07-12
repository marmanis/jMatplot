package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Polyline;
import com.marmanis.jMatplot.data.DataGenerator;
/**
 * jMatplot equivalent of the matplotlib "Line Demo Dash Control" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/line_demo_dash_control.html">
 * line_demo_dash_control.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class LineDemoDashControlExample {

    /**
     * Build and return the dash-control figure.
     *
     * <p>Three copies of {@code sin(x)}, each offset further downward, are drawn
     * with distinct dash patterns. Each is rendered as a single
     * {@link Polyline} (one continuous {@link java.awt.BasicStroke} over the
     * whole path) rather than per-segment strokes — with 500 points spanning
     * only 10 data units, a per-segment dash phase resets on every ~1px-long
     * segment and the dash pattern never gets far enough to reach a gap,
     * making the line look solid. Drawing one continuous path keeps the dash
     * phase running the full length of the curve. The dash/gap lengths are
     * also scaled up (in pixels) from matplotlib's point-based values so the
     * pattern reads clearly at this figure's resolution, with extra emphasis
     * on the gap length per the "increase the break" request.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.linspace(0, 10, 500);
        double[] y = DataGenerator.apply(x, Math::sin);

        float[][] dashSeqs = {
                {18f, 10f},
                {9f, 6f},
                {15f, 8f, 4f, 8f}
        };
        String[] labels = {"18,10", "9,6", "15,8,4,8"};

        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        for (int i = 0; i < dashSeqs.length; i++) {
            double offset = i * 0.4;
            Polyline line = ax.plotPath(x, DataGenerator.add(y, -offset));
            line.setLineWidth(2.5f);
            line.setDashPattern(dashSeqs[i]);
            line.setLabel(labels[i]);
        }
        ax.setTitle("Dashed Line Style Configuration");
        ax.legend();
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
