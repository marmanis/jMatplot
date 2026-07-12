package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * An artist that draws a single closed, filled polygon.
 *
 * <p>This is the Java equivalent of the {@link java.awt.Polygon} patches created
 * by {@code matplotlib.axes.Axes.fill(x, y)}.  The polygon is closed automatically
 * (the last vertex is connected back to the first) and rendered as a single filled
 * shape with an optional visible edge.
 *
 * <h2>Python equivalents</h2>
 * <pre>{@code
 * ax.fill(x, y)                                         # default blue fill
 * ax.fill(x, y, facecolor='lightsalmon',
 *              edgecolor='orangered', linewidth=3)        # custom colours
 * ax.fill(x, y, facecolor='none',
 *              edgecolor='purple',    linewidth=3)        # edge only
 * }</pre>
 *
 * The jMatplot equivalents use the fluent setters returned by
 * {@link Axes#fill(double[], double[])}:
 * <pre>{@code
 * ax.fill(x, y);                                        // default fill
 * ax.fill(x, y).setFaceColor(LIGHTSALMON)
 *               .setEdgeColor(ORANGERED)
 *               .setLineWidth(3f);
 * ax.fill(x, y).setFaceColor(null)                      // no fill
 *               .setEdgeColor(PURPLE)
 *               .setLineWidth(3f);
 * }</pre>
 *
 * <h2>Rendering</h2>
 * <p>The path is closed by a {@code closePath()} call; both {@code faceColor}
 * and {@code edgeColor} may be {@code null} to suppress fill or stroke
 * respectively (matching matplotlib's {@code facecolor='none'} behaviour).
 */
public class FilledPolygon extends Artist {

    private final double[] x;
    private final double[] y;

    /**
     * Fill colour.  {@code null} means no fill (transparent), which corresponds
     * to matplotlib's {@code facecolor='none'}.
     */
    private Color faceColor = new Color(0x1F, 0x77, 0xB4);  // default tab:blue

    /**
     * Edge (outline) colour.  {@code null} means no edge is drawn.
     */
    private Color edgeColor = null;

    /** Width of the edge stroke in screen pixels. */
    private float lineWidth = 1.0f;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Construct a filled polygon from vertex arrays.
     *
     * @param x x coordinates of the polygon vertices
     * @param y y coordinates of the polygon vertices (same length as {@code x})
     */
    public FilledPolygon(double[] x, double[] y) {
        this.x = x;
        this.y = y;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw the polygon.
     *
     * <p>A closed {@link Path2D} is built from the vertex arrays and forwarded to
     * {@link Backend#drawPath}.  If {@link #faceColor} is {@code null} the polygon
     * is not filled; if {@link #edgeColor} is {@code null} the outline is not drawn.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible() || x.length < 2) return;

        Path2D.Double path = new Path2D.Double();
        path.moveTo(x[0], y[0]);
        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
        }
        path.closePath();

        backend.drawPath(path, edgeColor, faceColor, lineWidth);
    }

    // ── Style setters (fluent) ────────────────────────────────────────────────

    /**
     * Set the fill colour.
     *
     * @param color fill colour, or {@code null} for no fill
     *              ({@code matplotlib facecolor='none'})
     * @return {@code this} for fluent chaining
     */
    public FilledPolygon setFaceColor(Color color) {
        this.faceColor = color;
        return this;
    }

    /**
     * Set the edge (outline) colour.
     *
     * @param color edge colour, or {@code null} for no edge
     * @return {@code this} for fluent chaining
     */
    public FilledPolygon setEdgeColor(Color color) {
        this.edgeColor = color;
        return this;
    }

    /**
     * Set the edge stroke width.
     *
     * @param lineWidth stroke width in screen pixels
     * @return {@code this} for fluent chaining
     */
    public FilledPolygon setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /**
     * Return the current face colour (may be {@code null}).
     *
     * <p>Used by {@link Legend} to resolve the swatch colour.
     *
     * @return fill colour
     */
    public Color getColor()     { return faceColor; }

    /** @return edge colour (may be {@code null}) */
    public Color getEdgeColor() { return edgeColor; }

    /** @return edge stroke width in screen pixels */
    public float getLineWidth() { return lineWidth; }

    /** @return x coordinates of the polygon vertices */
    public double[] getXData()  { return x; }

    /** @return y coordinates of the polygon vertices */
    public double[] getYData()  { return y; }
}
