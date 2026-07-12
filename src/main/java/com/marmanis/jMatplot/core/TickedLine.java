package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A line artist that additionally draws short perpendicular tick marks
 * (hash-marks) at regular screen-space intervals along its path.
 *
 * <p>This is the jMatplot equivalent of matplotlib's
 * {@code patheffects.withTickedStroke} / {@code patheffects.TickedStroke}.
 * In matplotlib, ticked stroke is a <em>path effect</em> applied on top of a
 * {@code Line2D}; in jMatplot the two responsibilities are combined into a
 * single artist for simplicity.
 *
 * <h2>Tick geometry</h2>
 * <p>Ticks are placed by walking the cumulative screen-space arc length of the
 * line.  Every time the arc length advances by {@link #spacingPx} pixels a
 * tick is drawn at that point.  The tick is a short line segment centred on
 * the path, perpendicular to the local tangent direction (i.e. perpendicular
 * to the line segment that contains the tick position), with an optional
 * rotation given by {@link #angleDeg}.
 *
 * <p>Because the tick geometry is computed in screen coordinates (not data
 * coordinates), ticks always look visually uniform regardless of the data
 * range or axis aspect ratio.
 *
 * <h2>Legend support</h2>
 * <p>Set the artist label via the inherited {@link Artist#setLabel(String)}
 * method.  The label will appear in the legend created by
 * {@link Axes#legend()}, with a small coloured line sample beside it.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * TickedLine tl = ax.plotTicked(x, y);
 * tl.setColor(new Color(0x1F77B4));
 * tl.setSpacingPx(10.0f);
 * tl.setTickLengthPx(8.0f);
 * tl.setAngleDeg(0.0);     // 0 = perp to path (pointing "left" of travel)
 * tl.setLabel("Line");
 * }</pre>
 *
 * <h2>Relation to matplotlib parameters</h2>
 * <p>matplotlib's {@code TickedStroke} {@code spacing} is in <em>points</em>
 * (1 point ≈ 1.33 px at 96 dpi).  For a 640 × 480 canvas at 96 dpi:
 * {@code spacing=7} points ≈ {@code 9} px; {@code spacing=10} points ≈ {@code 13} px.
 * jMatplot's {@link #spacingPx} is directly in pixels.
 *
 * <p>matplotlib's {@code angle} parameter is the angle (in degrees) of the
 * tick measured anticlockwise from the <em>screen</em> x-axis.  This class
 * uses {@link #angleDeg} as a rotation applied to the path's left-perpendicular
 * before drawing each tick, which produces equivalent results for most cases.
 */
public class TickedLine extends Artist {

    /** X coordinates in data space. */
    private final double[] xData;

    /** Y coordinates in data space. */
    private final double[] yData;

    // ── Line appearance ───────────────────────────────────────────────────────

    /** Stroke colour for both the main line and the tick marks. */
    private Color color = new Color(0x1F, 0x77, 0xB4); // tab:blue default

    /** Main-line stroke width in pixels. */
    private float lineWidth = 1.5f;

    // ── Tick parameters ───────────────────────────────────────────────────────

    /**
     * Screen-space distance (pixels) between consecutive tick marks.
     * Equivalent to matplotlib's {@code spacing} parameter (in points).
     * Default: 10 px.
     */
    private float spacingPx = 10.0f;

    /**
     * Total tick length in screen pixels (the tick is drawn centred on the
     * path, so half this length extends to each side).
     * Default: 8 px (4 px each side).
     */
    private float tickLengthPx = 8.0f;

    /**
     * Rotation applied to the path's left-perpendicular before drawing each
     * tick, in degrees.  {@code 0°} → tick points to the left of the travel
     * direction (i.e. "above" a line going right).  Positive values rotate
     * anticlockwise in screen space.
     *
     * <p>To approximately replicate matplotlib's {@code angle=135} on a
     * 45° diagonal use {@code angleDeg=0} (the left perpendicular of a
     * lower-left→upper-right path is already upper-left).
     *
     * <p>Default: 0°.
     */
    private double angleDeg = 0.0;

    // ── Construction ─────────────────────────────────────────────────────────

    /**
     * Construct a TickedLine from data arrays.
     *
     * @param xData x data coordinates (at least 2 points)
     * @param yData y data coordinates (same length as xData)
     */
    public TickedLine(double[] xData, double[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw the ticked line.
     *
     * <ol>
     *   <li>Draw each data segment as a solid line (data coordinates).</li>
     *   <li>Walk the cumulative screen-space arc length and emit a tick mark
     *       every {@link #spacingPx} pixels.</li>
     *   <li>Each tick is centred on the path, oriented perpendicular to the
     *       local tangent (optionally rotated by {@link #angleDeg}), drawn via
     *       {@link Backend#drawScreenLine}.</li>
     * </ol>
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible() || xData.length < 2) return;

        final int n = xData.length;

        // ── 1. Draw main line ────────────────────────────────────────────────
        for (int i = 0; i < n - 1; i++) {
            backend.drawLine(xData[i], yData[i], xData[i + 1], yData[i + 1],
                             color, lineWidth, null,
                             java.awt.BasicStroke.JOIN_MITER,
                             java.awt.BasicStroke.CAP_BUTT);
        }

        // ── 2. Pre-compute screen coordinates ────────────────────────────────
        double[] sx = new double[n];
        double[] sy = new double[n];
        for (int i = 0; i < n; i++) {
            sx[i] = backend.transformX(xData[i]);
            sy[i] = backend.transformY(yData[i]);
        }

        // ── 3. Walk arc length, emit ticks ───────────────────────────────────
        // Start half a spacing in so the first tick doesn't sit on the
        // very first data point (matches matplotlib's default placement).
        double cumLen   = 0.0;
        double nextTick = spacingPx * 0.5;

        double cosA = Math.cos(Math.toRadians(angleDeg));
        double sinA = Math.sin(Math.toRadians(angleDeg));
        double halfTick = tickLengthPx / 2.0;
        float tickWidth = Math.max(lineWidth, 1.0f);

        for (int i = 1; i < n; i++) {
            double dx      = sx[i] - sx[i - 1];
            double dy      = sy[i] - sy[i - 1];
            double segLen  = Math.sqrt(dx * dx + dy * dy);
            if (segLen < 1e-10) continue;   // degenerate segment — skip

            // Emit all ticks that fall within this segment
            while (cumLen + segLen >= nextTick) {
                // Parametric position along segment [0,1]
                double t  = (nextTick - cumLen) / segLen;
                double tx = sx[i - 1] + t * dx;
                double ty = sy[i - 1] + t * dy;

                // Normalised tangent (screen space, y-down)
                double tnx = dx / segLen;
                double tny = dy / segLen;

                // Left perpendicular to the tangent: rotate tangent 90° CCW
                // (In screen space y is down, so CCW visually = (-tny, tnx))
                double lpx = -tny;
                double lpy =  tnx;

                // Apply optional rotation (angleDeg) around z in screen space
                double nx = lpx * cosA - lpy * sinA;
                double ny = lpx * sinA + lpy * cosA;

                // Draw tick centred on (tx, ty) along direction (nx, ny)
                backend.drawScreenLine(
                    tx - nx * halfTick, ty - ny * halfTick,
                    tx + nx * halfTick, ty + ny * halfTick,
                    color, tickWidth);

                nextTick += spacingPx;
            }
            cumLen += segLen;
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return the x data array */
    public double[] getXData() { return xData; }

    /** @return the y data array */
    public double[] getYData() { return yData; }

    /** @return stroke colour */
    public Color getColor() { return color; }

    /** @param color stroke colour for both the line and its ticks */
    public void setColor(Color color) { this.color = color; }

    /** @return main line stroke width */
    public float getLineWidth() { return lineWidth; }

    /** @param lineWidth stroke width in pixels */
    public void setLineWidth(float lineWidth) { this.lineWidth = lineWidth; }

    /** @return tick spacing in screen pixels */
    public float getSpacingPx() { return spacingPx; }

    /**
     * Set the tick spacing in screen pixels.
     * Equivalent to matplotlib's {@code spacing} parameter (in points).
     * @param spacingPx spacing in pixels (positive)
     */
    public void setSpacingPx(float spacingPx) { this.spacingPx = Math.max(1f, spacingPx); }

    /** @return total tick length in screen pixels */
    public float getTickLengthPx() { return tickLengthPx; }

    /**
     * Set the total tick length in screen pixels.  The tick is drawn centred
     * on the path so half this length extends to each side.
     * @param tickLengthPx total tick length in pixels
     */
    public void setTickLengthPx(float tickLengthPx) { this.tickLengthPx = Math.max(1f, tickLengthPx); }

    /** @return rotation offset applied to the path perpendicular (degrees) */
    public double getAngleDeg() { return angleDeg; }

    /**
     * Set the rotation applied to the path's left-perpendicular direction
     * before drawing each tick.  {@code 0°} is perpendicular to the path
     * pointing "left" relative to the direction of travel.
     * @param angleDeg rotation in degrees (positive = anticlockwise in screen space)
     */
    public void setAngleDeg(double angleDeg) { this.angleDeg = angleDeg; }
}
