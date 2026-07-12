package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;

import java.awt.Color;
import java.time.LocalDate;

/**
 * jMatplot equivalent of the matplotlib "Timeline with Lines, Dates, and Text"
 * gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/timeline.html">
 * timeline.html</a>
 *
 * <p>Reproduces matplotlib's release-history timeline (using the same
 * fallback release/date list the original script falls back to when it has
 * no network access): a stem per release, alternating above/below a y=0
 * baseline by minor-version group, with stem length decreasing for later
 * patch releases, white-filled circles for patch releases and red-filled
 * circles for feature ({@code x.y.0}) releases, and the version string
 * annotated near each stem tip.
 *
 * <p>{@code Axes} has no datetime axis, per-vertex {@code vlines} colors, or
 * filled/edged marker primitive, so dates are mapped to epoch-day doubles,
 * each stem is drawn individually via {@link Axes#plot}, and the
 * white/red-on-black marker look is faked by layering a larger black circle
 * behind a smaller colored one.
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class TimelineExample {

    private static final String[] DATES = {
        "2014-08-26", "2014-10-18", "2014-10-26", "2015-02-16", "2015-10-29",
        "2016-01-10", "2016-07-03", "2016-09-09", "2017-01-17", "2017-05-02",
        "2017-05-10", "2017-10-07", "2017-12-10", "2018-01-18", "2018-03-06",
        "2018-03-16", "2018-03-17", "2018-08-10", "2018-09-18", "2018-11-10",
        "2018-11-10", "2019-02-26", "2019-02-26"
    };
    private static final String[] RELEASES = {
        "1.4.0", "1.4.1", "1.4.2", "1.4.3", "1.5.0",
        "1.5.1", "1.5.2", "1.5.3", "2.0.0", "2.0.1",
        "2.0.2", "2.1.0", "2.1.1", "2.1.2", "2.2.0",
        "2.2.1", "2.2.2", "2.2.3", "3.0.0", "3.0.1",
        "3.0.2", "2.2.4", "3.0.3"
    };
    // level = +/-(1 + 0.8*(5 - micro)), sign alternates by (major, minor) group;
    // pre-computed here in the same way matplotlib's source derives it.
    private static final double[] LEVELS = {
         5.0,  4.2,  3.4,  2.6, -5.0,
        -4.2, -3.4, -2.6,  5.0,  4.2,
         3.4, -5.0, -4.2, -3.4,  5.0,
         4.2,  3.4,  2.6, -5.0, -4.2,
        -3.4,  1.8, -2.6
    };
    // is_feature(release): true for x.y.0 releases.
    private static final boolean[] IS_FEATURE = {
         true, false, false, false,  true,
        false, false, false,  true, false,
        false,  true, false, false,  true,
        false, false, false,  true, false,
        false, false, false
    };

    private static final Color TAB_RED = new Color(0xD6, 0x27, 0x28);

    /**
     * Build and return the timeline figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = new double[DATES.length];
        for (int i = 0; i < DATES.length; i++) {
            x[i] = LocalDate.parse(DATES[i]).toEpochDay();
        }

        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        // One stem per release, colored tab:red — full opacity for feature
        // releases, half opacity for patch releases.
        for (int i = 0; i < x.length; i++) {
            int alpha = IS_FEATURE[i] ? 255 : 128;
            Color c = new Color(TAB_RED.getRed(), TAB_RED.getGreen(), TAB_RED.getBlue(), alpha);
            ax.plot(new double[]{x[i], x[i]}, new double[]{0, LEVELS[i]}).setColor(c);
        }

        // Baseline at y=0.
        double xMin = x[0], xMax = x[0];
        for (double xi : x) { xMin = Math.min(xMin, xi); xMax = Math.max(xMax, xi); }
        ax.plot(new double[]{xMin, xMax}, new double[]{0, 0}).setColor(Color.BLACK);

        // Markers at y=0: white-filled circles for patch releases, red-filled
        // for feature releases — both with a black "edge" faked by a larger
        // black circle drawn first.
        plotEdgedMarkers(ax, x, Color.BLACK, Color.WHITE, false);
        plotEdgedMarkers(ax, x, Color.BLACK, TAB_RED, true);

        // Version annotations near each stem tip.
        for (int i = 0; i < x.length; i++) {
            double offset = LEVELS[i] >= 0 ? 0.3 : -0.6;
            int fontSize = IS_FEATURE[i] ? 11 : 9;
            var label = ax.text(x[i], LEVELS[i] + offset, RELEASES[i]);
            label.setFontSize(fontSize);
        }

        // X axis: one tick per year.
        int firstYear = LocalDate.parse(DATES[0]).getYear();
        int lastYear = firstYear;
        for (String d : DATES) lastYear = Math.max(lastYear, LocalDate.parse(d).getYear());
        int nYears = lastYear - firstYear + 1;
        double[] yearTicks = new double[nYears];
        String[] yearLabels = new String[nYears];
        for (int y = 0; y < nYears; y++) {
            yearTicks[y] = LocalDate.of(firstYear + y, 1, 1).toEpochDay();
            yearLabels[y] = String.valueOf(firstYear + y);
        }
        ax.setXTicks(yearTicks, yearLabels);

        ax.setYLim(-6.5, 6.5);
        ax.setYTickMarksVisible(false);
        ax.setYTickLabelsVisible(false);
        ax.setTitle("Matplotlib release dates");
        return fig;
    }

    /**
     * Draw a larger {@code edgeColor} circle behind a smaller {@code fillColor}
     * circle at each (x, 0), for either the feature or the non-feature subset
     * of releases — approximating an edged/filled marker.
     */
    private static void plotEdgedMarkers(Axes ax, double[] x, Color edgeColor,
                                          Color fillColor, boolean featureSubset) {
        int count = 0;
        for (boolean f : IS_FEATURE) if (f == featureSubset) count++;
        double[] xs = new double[count];
        int j = 0;
        for (int i = 0; i < x.length; i++) {
            if (IS_FEATURE[i] == featureSubset) xs[j++] = x[i];
        }
        double[] zeros = new double[count];
        var edge = ax.scatter(xs, zeros);
        edge.setMarker("o");
        edge.setMarkerSize(11f);
        edge.setColor(edgeColor);
        var fill = ax.scatter(xs, zeros);
        fill.setMarker("o");
        fill.setMarkerSize(7f);
        fill.setColor(fillColor);
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
