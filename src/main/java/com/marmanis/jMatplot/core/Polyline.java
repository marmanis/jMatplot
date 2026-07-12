package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A polyline artist that draws a multi-segment path with a consistent stroke
 * join style at every interior vertex.
 *
 * <h2>Why not use Line2D?</h2>
 * <p>{@link Line2D} draws each data segment as an independent
 * {@code Graphics2D.drawLine()} call.  Independent strokes share endpoints
 * but have no join geometry: the AWT rendering engine has no way to know
 * whether two adjacent segments form a corner that needs a miter, round, or
 * bevel treatment.  The resulting plot appears to have arbitrary end-caps at
 * shared vertices rather than the requested join style.
 *
 * <p>Polyline builds a single {@link java.awt.geom.Path2D.Double} from all
 * data points and draws it with one {@link java.awt.BasicStroke} whose
 * {@code joinStyle} parameter is forwarded to AWT.  This matches the behaviour
 * of matplotlib's {@code solid_joinstyle} line property.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * Polyline pl = ax.plotPath(xData, yData);
 * pl.setColor(Color.BLUE);
 * pl.setLineWidth(12.0f);
 * pl.setJoinStyle(BasicStroke.JOIN_ROUND);
 * }</pre>
 *
 * <p>This is the Java equivalent of passing a path to a matplotlib
 * {@code Line2D} or {@code PathPatch} with the desired {@code joinstyle}.
 */
public class Polyline extends Artist {

    /** X coordinates of the vertices in data space. */
    private final double[] xData;

    /** Y coordinates of the vertices in data space. */
    private final double[] yData;

    /** Stroke colour. Default: tab:blue. */
    private Color color = new Color(0x1F, 0x77, 0xB4);

    /** Stroke width in pixels. */
    private float lineWidth = 1.5f;

    /**
     * AWT join style constant — one of {@link java.awt.BasicStroke#JOIN_MITER},
     * {@link java.awt.BasicStroke#JOIN_ROUND}, or
     * {@link java.awt.BasicStroke#JOIN_BEVEL}.
     * <p>Default: {@code JOIN_MITER}.
     */
    private int joinStyle = java.awt.BasicStroke.JOIN_MITER;

    /**
     * AWT cap style constant — one of {@link java.awt.BasicStroke#CAP_BUTT},
     * {@link java.awt.BasicStroke#CAP_ROUND}, or
     * {@link java.awt.BasicStroke#CAP_SQUARE}.
     * <p>Default: {@code CAP_BUTT}.
     */
    private int capStyle = java.awt.BasicStroke.CAP_BUTT;

    /**
     * Miter limit: the maximum ratio of miter length to line width.
     * When the miter at a corner would exceed {@code miterLimit × lineWidth / 2},
     * AWT falls back to a bevel join for that corner only.
     * <p>The Java default ({@link java.awt.BasicStroke} 4-arg constructor) is
     * {@code 10.0f}, matching matplotlib's default.
     */
    private float miterLimit = 10.0f;

    /** Optional dash pattern (null = solid). */
    private float[] dashPattern = null;

    // ── Construction ─────────────────────────────────────────────────────────

    /**
     * Create a Polyline from the given data arrays.
     *
     * @param xData x coordinates (at least 2 points required to draw anything)
     * @param yData y coordinates (must be same length as xData)
     */
    public Polyline(double[] xData, double[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    // ── Rendering ────────────────────────────────────────────────────────────

    /**
     * Draw this polyline using the given backend.
     *
     * <p>Delegates to {@link Backend#drawPolyline}, which in
     * {@link com.marmanis.jMatplot.backend.SwingBackend} builds a single
     * {@code Path2D} and draws it with one {@code BasicStroke} — ensuring the
     * join style is applied at each interior vertex.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible() || xData.length < 2) return;
        backend.drawPolyline(xData, yData, color, lineWidth, dashPattern,
                             joinStyle, capStyle, miterLimit);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return the x data array */
    public double[] getXData() { return xData; }

    /** @return the y data array */
    public double[] getYData() { return yData; }

    /** @return the stroke colour */
    public Color getColor() { return color; }

    /**
     * Set the stroke colour.
     * @param color new colour (must not be null)
     */
    public void setColor(Color color) { this.color = color; }

    /** @return stroke width in pixels */
    public float getLineWidth() { return lineWidth; }

    /**
     * Set the stroke width.
     * @param lineWidth width in pixels (positive)
     */
    public void setLineWidth(float lineWidth) { this.lineWidth = lineWidth; }

    /** @return the AWT join-style constant */
    public int getJoinStyle() { return joinStyle; }

    /**
     * Set the join style.
     * @param joinStyle one of {@code BasicStroke.JOIN_MITER},
     *                  {@code JOIN_ROUND}, or {@code JOIN_BEVEL}
     */
    public void setJoinStyle(int joinStyle) { this.joinStyle = joinStyle; }

    /** @return the AWT cap-style constant */
    public int getCapStyle() { return capStyle; }

    /**
     * Set the cap style.
     * @param capStyle one of {@code BasicStroke.CAP_BUTT},
     *                 {@code CAP_ROUND}, or {@code CAP_SQUARE}
     */
    public void setCapStyle(int capStyle) { this.capStyle = capStyle; }

    /** @return the miter-length limit ratio */
    public float getMiterLimit() { return miterLimit; }

    /**
     * Set the miter limit.  When the miter at a corner would extend more than
     * {@code miterLimit × lineWidth / 2} pixels from the join point, AWT
     * automatically falls back to a bevel join for that specific corner.
     * Must be ≥ 1.0.
     *
     * @param miterLimit miter-length ratio (typical value: 10.0)
     */
    public void setMiterLimit(float miterLimit) { this.miterLimit = miterLimit; }

    /** @return the dash pattern ({@code null} = solid) */
    public float[] getDashPattern() { return dashPattern; }

    /** @param dashPattern optional dash array */
    public void setDashPattern(float[] dashPattern) { this.dashPattern = dashPattern; }
}
