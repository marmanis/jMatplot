package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Scatter;
import com.marmanis.jMatplot.data.DataGenerator;
import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Scatter plot with histograms" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_hist.html">
 * scatter_hist.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class ScatterHistExample {

    /**
     * Build and return the scatter-with-marginal-histograms figure.
     * 500 points drawn from two standard normal distributions are plotted as a
     * semi-transparent scatter, with histograms of the x and y distributions
     * aligned along the top and right edges of the scatter axes.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.normalRandom(42, 0, 1, 500);
        double[] y = DataGenerator.normalRandom(43, 0, 1, 500);

        Figure fig = new Figure();

        // Layout mirrors matplotlib's example: a large scatter axes with a
        // narrower histogram axes above (sharing x) and to the right (sharing y).
        double left = 0.1, bottom = 0.1;
        double scatterSize = 0.6, spacing = 0.02, histSize = 0.2;

        Axes axScatter = fig.addAxes(left, bottom, scatterSize, scatterSize);
        Axes axHistX = fig.addAxes(left, bottom + scatterSize + spacing, scatterSize, histSize);
        Axes axHistY = fig.addAxes(left + scatterSize + spacing, bottom, histSize, scatterSize);

        Scatter sc = axScatter.scatter(x, y);
        sc.setColor(new Color(0x1F77B4));
        sc.setAlpha(0.3f);

        // Determine binning limits, rounded outward to the nearest binwidth,
        // matching the matplotlib example's "bins of width 0.25" approach.
        double maxExtent = Math.max(maxAbs(x), maxAbs(y));
        double binwidth = 0.25;
        double xymax = Math.ceil(maxExtent / binwidth) * binwidth;
        int nBins = (int) Math.round(2 * xymax / binwidth);

        double[] xCenters = new double[nBins];
        double[] xCounts = histogram(x, -xymax, xymax, nBins, xCenters);
        double[] yCenters = new double[nBins];
        double[] yCounts = histogram(y, -xymax, xymax, nBins, yCenters);

        Color histColor = new Color(0x1F77B4);
        axHistX.bar(xCenters, xCounts, binwidth).setColor(histColor);
        axHistX.setXLim(-xymax, xymax);
        axHistX.setXTicks(new double[0], new String[0]);

        axHistY.barh(yCenters, yCounts, binwidth).setColor(histColor);
        axHistY.setYLim(-xymax, xymax);
        axHistY.setYTickLabels(new double[0], new String[0]);

        axScatter.setXLim(-xymax, xymax);
        axScatter.setYLim(-xymax, xymax);

        return fig;
    }

    private static double maxAbs(double[] values) {
        double max = 0;
        for (double v : values) max = Math.max(max, Math.abs(v));
        return max;
    }

    /**
     * Bin {@code values} into {@code nBins} equal-width bins covering
     * {@code [min, max]} and return the per-bin counts. The bin-center
     * positions are written into {@code centersOut}.
     */
    private static double[] histogram(double[] values, double min, double max, int nBins, double[] centersOut) {
        double binwidth = (max - min) / nBins;
        double[] counts = new double[nBins];
        for (double v : values) {
            int bin = (int) Math.floor((v - min) / binwidth);
            if (bin < 0) bin = 0;
            if (bin >= nBins) bin = nBins - 1;
            counts[bin]++;
        }
        for (int i = 0; i < nBins; i++) {
            centersOut[i] = min + (i + 0.5) * binwidth;
        }
        return counts;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
