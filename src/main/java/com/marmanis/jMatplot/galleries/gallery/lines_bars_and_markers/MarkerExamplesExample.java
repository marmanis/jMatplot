package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Colormap;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Marker examples" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/scatter_star_poly.html">
 * scatter_star_poly.html</a>
 *
 * <p>Demonstrates several ways of specifying markers in a scatter plot — a
 * built-in shape code, a symbol-like glyph, a custom polygon outline, and
 * regular/star/asterisk polygons addressed by side count — against a 2×3
 * grid of panels, each coloring its ten random points by {@code z =
 * sqrt(x^2 + y^2)} via the viridis colormap.
 *
 * <p>{@code Scatter} only understands a small fixed set of marker codes (see
 * {@code SwingBackend#drawMarker}), so the custom shapes here (triangle,
 * polygon outline, pentagon, star, asterisk) are drawn manually as per-point
 * {@link Axes#fill} polygons / {@link Axes#plot} spokes rather than through
 * {@code Scatter}. The TeX symbol panel (&#9827;) is approximated with the
 * Unicode glyph directly, since jMatplot has no TeX marker renderer.
 */
public class MarkerExamplesExample {

    /**
     * Build and return the marker-examples figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double[] x = DataGenerator.uniformRandom(19680801L, 0, 1, 10);
        double[] y = DataGenerator.uniformRandom(19680802L, 0, 1, 10);
        double[] z = new double[x.length];
        for (int i = 0; i < x.length; i++) z[i] = Math.sqrt(x[i] * x[i] + y[i] * y[i]);

        Colormap viridis = Colormap.viridis();
        Color[] colors = new Color[x.length];
        for (int i = 0; i < x.length; i++) colors[i] = viridis.getColor(z[i] / Math.sqrt(2));

        Figure fig = new Figure();
        int cols = 3, rows = 2;
        double padX = 0.04, padY = 0.08;
        double cellW = (1.0 - padX * (cols + 1)) / cols;
        double cellH = (1.0 - padY * (rows + 1)) / rows;

        String[] titles = {
            "marker='>'", "marker=r'$\\clubsuit$'", "marker=verts",
            "marker=(5, 0)", "marker=(5, 1)", "marker=(5, 2)"
        };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int panel = r * cols + c;
                double left   = padX + c * (cellW + padX);
                double bottom = padY + (rows - 1 - r) * (cellH + padY);
                Axes ax = fig.addAxes(left, bottom, cellW, cellH);
                ax.setXLim(-0.1, 1.1);
                ax.setYLim(-0.1, 1.1);
                ax.setTitle(titles[panel]);
                ax.setXTickLabelsVisible(false);
                ax.setYTickLabelsVisible(false);

                double r2 = 0.05;
                for (int i = 0; i < x.length; i++) {
                    switch (panel) {
                        case 0: // marker='>' -- right-pointing triangle
                            drawPolygon(ax, regularPolygon(x[i], y[i], r2, 3, 0), colors[i]);
                            break;
                        case 1: // marker=r'$\clubsuit$' -- TeX glyph approximated with Unicode
                            ax.text(x[i] - 0.02, y[i] - 0.02, "♣").setColor(colors[i]);
                            break;
                        case 2: // marker=verts -- custom triangular path
                            drawPolygon(ax, customVerts(x[i], y[i], r2), colors[i]);
                            break;
                        case 3: // marker=(5, 0) -- regular pentagon
                            drawPolygon(ax, regularPolygon(x[i], y[i], r2, 5, 90), colors[i]);
                            break;
                        case 4: // marker=(5, 1) -- regular 5-pointed star
                            drawPolygon(ax, starPolygon(x[i], y[i], r2, r2 * 0.382, 5, 90), colors[i]);
                            break;
                        case 5: // marker=(5, 2) -- regular 5-pointed asterisk (open spokes)
                            drawAsterisk(ax, x[i], y[i], r2, 5, 90, colors[i]);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return fig;
    }

    /** Fill a closed polygon given as parallel {@code {xs, ys}} vertex arrays. */
    private static void drawPolygon(Axes ax, double[][] verts, Color color) {
        ax.fill(verts[0], verts[1]).setFaceColor(color).setEdgeColor(null);
    }

    /**
     * Vertices of a regular {@code n}-sided polygon centered at {@code (cx, cy)}
     * with circumradius {@code r}, with the first vertex at {@code startAngleDeg}
     * (measured counter-clockwise from the positive x-axis).
     */
    private static double[][] regularPolygon(double cx, double cy, double r, int n, double startAngleDeg) {
        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            double theta = Math.toRadians(startAngleDeg + 360.0 * i / n);
            xs[i] = cx + r * Math.cos(theta);
            ys[i] = cy + r * Math.sin(theta);
        }
        return new double[][]{xs, ys};
    }

    /**
     * Vertices of a regular {@code n}-pointed star outline centered at
     * {@code (cx, cy)}, alternating between {@code rOuter} (points) and
     * {@code rInner} (inner notches), with the first outer point at
     * {@code startAngleDeg}.
     */
    private static double[][] starPolygon(double cx, double cy, double rOuter, double rInner,
                                           int n, double startAngleDeg) {
        double[] xs = new double[2 * n];
        double[] ys = new double[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            double theta = Math.toRadians(startAngleDeg + 180.0 * i / n);
            double r = (i % 2 == 0) ? rOuter : rInner;
            xs[i] = cx + r * Math.cos(theta);
            ys[i] = cy + r * Math.sin(theta);
        }
        return new double[][]{xs, ys};
    }

    /** A small triangular "custom path" marker, matching mpl's {@code verts} example. */
    private static double[][] customVerts(double cx, double cy, double r) {
        return new double[][]{
            {cx - r, cy - r, cx + r, cx - r},
            {cy - r, cy + r, cy - r, cy - r}
        };
    }

    /** Draw an {@code n}-spoke open asterisk centered at {@code (cx, cy)}. */
    private static void drawAsterisk(Axes ax, double cx, double cy, double r, int n,
                                      double startAngleDeg, Color color) {
        for (int i = 0; i < n; i++) {
            double theta = Math.toRadians(startAngleDeg + 360.0 * i / n);
            double tipX = cx + r * Math.cos(theta);
            double tipY = cy + r * Math.sin(theta);
            ax.plot(new double[]{cx, tipX}, new double[]{cy, tipY}).setColor(color);
        }
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
