package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.FilledPolygon;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Filled polygon" gallery example.
 *
 * <p>See:
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/fill.html">
 * fill.html</a>
 *
 * <p>The example demonstrates {@code ax.fill(x, y)}, which draws a closed filled
 * polygon from vertex arrays.  The Koch snowflake fractal is used as the example
 * polygon because its intricate boundary provides a compelling visual test of the
 * fill renderer.
 *
 * <p>This jMatplot implementation shows the order-5 Koch snowflake in three styles:
 * <ol>
 *   <li>Default fill (tab:blue).</li>
 *   <li>Custom face colour ({@code lightsalmon}) with a thick orange-red edge.</li>
 *   <li>Edge-only — no fill, purple outline.</li>
 * </ol>
 *
 * <h2>Koch snowflake algorithm</h2>
 * <p>The snowflake is generated recursively.  At order 0 an equilateral triangle
 * is constructed at 90°, 210°, 330°.  At each subsequent order every edge segment
 * is replaced by four segments inserting a triangular bump on the middle third.
 * Complex numbers are represented as parallel real/imaginary arrays.
 *
 * <h2>Key Python → Java translations</h2>
 * <ul>
 *   <li>{@code ax.fill(x, y)} → {@link Axes#fill(double[], double[])} returns a
 *       {@link FilledPolygon}.</li>
 *   <li>{@code facecolor='none'} → {@link FilledPolygon#setFaceColor(Color) setFaceColor(null)}.</li>
 * </ul>
 */
public class FillExample {

    private static final Color LIGHTSALMON = new Color(0xFF, 0xA0, 0x7A);
    private static final Color ORANGERED   = new Color(0xFF, 0x45, 0x00);
    private static final Color PURPLE      = new Color(0x80, 0x00, 0x80);

    /**
     * Build and return the 1 x 3 Koch snowflake figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig  = new Figure();
        Axes[][] grid = fig.subplots(1, 3);

        double[][] xy = kochSnowflake(5, 10.0);
        double[] x = xy[0], y = xy[1];

        // Panel 1: default fill
        grid[0][0].fill(x, y);
        grid[0][0].setTitle("fill");

        // Panel 2: custom face/edge colours
        grid[0][1].fill(x, y)
                  .setFaceColor(LIGHTSALMON)
                  .setEdgeColor(ORANGERED)
                  .setLineWidth(3f);
        grid[0][1].setTitle("fill with facecolor / edgecolor");

        // Panel 3: edge only (no fill)
        grid[0][2].fill(x, y)
                  .setFaceColor(null)
                  .setEdgeColor(PURPLE)
                  .setLineWidth(3f);
        grid[0][2].setTitle("fill outline only");

        return fig;
    }

    // ── Koch snowflake ────────────────────────────────────────────────────────

    /**
     * Return the x and y vertex arrays of a Koch snowflake of the given order.
     *
     * @param order recursion depth (0 = equilateral triangle; 5 = fine snowflake)
     * @param scale edge-length of the base triangle
     * @return {@code {x, y}} vertex coordinate arrays
     */
    public static double[][] kochSnowflake(int order, double scale) {
        return kochSnowflakeComplex(order, scale);
    }

    /**
     * Recursive helper — returns {@code {re[], im[]}} (real/imaginary parts).
     *
     * <p>The construction constant {@code ZR = 0.5 − (√3/6)j} maps a segment
     * vector {@code dp} to the apex of the equilateral bump.
     */
    private static double[][] kochSnowflakeComplex(int order, double scale) {
        if (order == 0) {
            int[]    deg = {90, 210, 330};
            double   r   = scale / Math.sqrt(3.0);
            double[] re  = new double[3], im = new double[3];
            for (int i = 0; i < 3; i++) {
                double rad = Math.toRadians(deg[i]);
                re[i] = r * Math.cos(rad);
                im[i] = r * Math.sin(rad);
            }
            return new double[][]{re, im};
        }
        double[][] prev = kochSnowflakeComplex(order - 1, scale);
        double[] p1r = prev[0], p1i = prev[1];
        int n = p1r.length;
        final double ZRr =  0.5;
        final double ZRi = -0.5 * Math.sqrt(3.0) / 3.0;
        double[] newRe = new double[n * 4], newIm = new double[n * 4];
        for (int i = 0; i < n; i++) {
            int    next = (i + 1) % n;
            double dpr  = p1r[next] - p1r[i];
            double dpi  = p1i[next] - p1i[i];
            newRe[4*i]     = p1r[i];
            newIm[4*i]     = p1i[i];
            newRe[4*i + 1] = p1r[i] + dpr / 3.0;
            newIm[4*i + 1] = p1i[i] + dpi / 3.0;
            newRe[4*i + 2] = p1r[i] + (dpr * ZRr - dpi * ZRi);
            newIm[4*i + 2] = p1i[i] + (dpr * ZRi + dpi * ZRr);
            newRe[4*i + 3] = p1r[i] + dpr * 2.0 / 3.0;
            newIm[4*i + 3] = p1i[i] + dpi * 2.0 / 3.0;
        }
        return new double[][]{newRe, newIm};
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) { createFigure().show(); }
}
