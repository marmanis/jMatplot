package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A line artist that can carry both a connecting line and per-point markers.
 *
 * <p>This is the Java equivalent of {@code matplotlib.lines.Line2D}.
 *
 * <h2>markevery modes</h2>
 * <p>Three mutually-exclusive modes control which data points receive a marker:
 * <ol>
 *   <li><strong>Integer step</strong> ({@link #setMarkevery(int)}) — draw a marker
 *       at every {@code N}-th data point starting at index 0.</li>
 *   <li><strong>Explicit indices</strong> ({@link #setMarkeveryIndices(int[])}) —
 *       draw markers only at the listed data indices.  Negative indices are
 *       resolved Python-style (−1 = last element).</li>
 *   <li><strong>Float / diagonal fraction</strong>
 *       ({@link #setMarkeveryFloat(double)} / {@link #setMarkeveryFloat(double, double)}) —
 *       place markers at intervals of {@code period × axes_diagonal} pixels, measured
 *       as cumulative screen-space arc length along the <em>full</em> data path.
 *       This reproduces matplotlib's float {@code markevery} semantics exactly,
 *       including correct behaviour on zoomed axes.</li>
 * </ol>
 */
public class Line2D extends Artist {

    /** The X coordinates of the data points. */
    private final double[] xData;

    /** The Y coordinates of the data points. */
    private final double[] yData;

    /** The color of the line. Default is Blue. */
    private Color color = Color.BLUE;

    /** The width of the line. Default is 1.5. */
    private float lineWidth = 1.5f;

    /** The dash pattern for the line. {@code null} for solid. */
    private float[] dashPattern = null;

    /** The type of marker to draw at each point. {@code null} for no markers. */
    private String marker = null;

    /** The size of the markers in pixels. */
    private float markerSize = 6.0f;

    /** The join style for the line segments ({@code BasicStroke.JOIN_…}). */
    private int joinStyle = java.awt.BasicStroke.JOIN_MITER;

    /** The cap style for the line ends ({@code BasicStroke.CAP_…}). */
    private int capStyle = java.awt.BasicStroke.CAP_BUTT;

    // ── markevery state ───────────────────────────────────────────────────────

    /** Integer-step markevery: draw a marker at every {@code markevery}-th point. */
    private int markevery = 1;

    /**
     * Explicit index list.  When non-{@code null}, overrides {@link #markevery}
     * and the float-mode fields.
     */
    private int[] markeveryIndices = null;

    /**
     * Period for float-based markevery, as a fraction of the axes diagonal in
     * display pixels.  {@link Double#NaN} means float mode is inactive.
     *
     * <p>The marker interval is {@code markeveryPeriod × sqrt(vw² + vh²)} pixels,
     * measured along the cumulative screen-space arc length of the <em>full</em>
     * data path — even points outside the current axis limits still contribute to
     * the cumulative length.  This is the exact semantics of matplotlib's float
     * {@code markevery}, and it ensures that zoomed views show the correct subset
     * of markers rather than the index-fraction approximation used in earlier
     * jMatplot versions.
     */
    private double markeveryPeriod = Double.NaN;

    /**
     * Start offset for float-based markevery, as a fraction of the axes diagonal.
     * {@code 0.0} → first threshold is at the very beginning of the data path.
     */
    private double markeveryStart = 0.0;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Create a new line from the provided X and Y data.
     *
     * @param xData Array of X coordinates.
     * @param yData Array of Y coordinates (must have the same length as xData).
     */
    public Line2D(double[] xData, double[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    // ── markevery setters ─────────────────────────────────────────────────────

    /**
     * Integer-step markevery: draw a marker at every {@code markevery}-th data point.
     * Equivalent to passing an integer to matplotlib's {@code markevery} parameter.
     *
     * <p>Calling this method clears any float or explicit-index overrides.
     *
     * @param markevery step size; 1 marks every point, 8 marks every 8th, etc.
     */
    public void setMarkevery(int markevery) {
        this.markevery        = markevery;
        this.markeveryIndices = null;
        this.markeveryPeriod  = Double.NaN;
    }

    /**
     * Explicit-index markevery: draw markers only at the listed data indices.
     *
     * <p>Equivalent to passing a Python {@code list} or {@code slice} to
     * matplotlib's {@code markevery} parameter.  Negative indices are resolved
     * Python-style (e.g. {@code -1} means the last data point).
     *
     * <p>Calling this method clears any float-mode setting.
     *
     * @param indices array of data indices at which to draw markers
     */
    public void setMarkeveryIndices(int[] indices) {
        this.markeveryIndices = indices;
        this.markeveryPeriod  = Double.NaN;
    }

    /**
     * Float markevery: place markers at intervals of {@code period} times the
     * axes diagonal, measured as cumulative screen-space arc length.
     *
     * <p>Equivalent to passing a {@code float} to matplotlib's {@code markevery}
     * parameter.  Unlike the integer-step mode, markers are positioned in display
     * space at render time, so they respond correctly to axis limits (zoom), log
     * scales, and different figure sizes.
     *
     * <p>Calling this method clears any explicit-index override.
     *
     * @param period fraction of the axes diagonal between consecutive markers
     */
    public void setMarkeveryFloat(double period) {
        this.markeveryPeriod  = period;
        this.markeveryStart   = 0.0;
        this.markeveryIndices = null;
    }

    /**
     * Float markevery with a start offset: equivalent to matplotlib's
     * {@code markevery = (start, period)} tuple where both values are floats.
     *
     * <p>The first marker is placed at the point where the cumulative screen-space
     * arc length first reaches {@code start × diagonal} pixels; subsequent markers
     * follow at every additional {@code period × diagonal} pixels.
     *
     * <p>Calling this method clears any explicit-index override.
     *
     * @param start  fraction of the axes diagonal at which to place the first marker
     * @param period fraction of the axes diagonal between subsequent markers
     */
    public void setMarkeveryFloat(double start, double period) {
        this.markeveryPeriod  = period;
        this.markeveryStart   = start;
        this.markeveryIndices = null;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw the line (and optional markers) using the provided backend.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible() || xData.length == 0) return;

        // ── Line segments ────────────────────────────────────────────────────
        if (xData.length >= 2) {
            for (int i = 0; i < xData.length - 1; i++) {
                backend.drawLine(xData[i], yData[i], xData[i + 1], yData[i + 1],
                                 color, lineWidth, dashPattern, joinStyle, capStyle);
            }
        }

        // ── Markers ──────────────────────────────────────────────────────────
        if (marker == null) return;

        if (!Double.isNaN(markeveryPeriod)) {
            // Float mode: position markers by screen-space arc length (see Javadoc).
            drawMarkersFloat(backend);
        } else if (markeveryIndices != null) {
            // Explicit-index mode
            for (int idx : markeveryIndices) {
                int resolved = (idx < 0) ? xData.length + idx : idx;
                if (resolved >= 0 && resolved < xData.length) {
                    backend.drawMarker(xData[resolved], yData[resolved], marker, markerSize, color);
                }
            }
        } else {
            // Integer-step mode
            for (int i = 0; i < xData.length; i += markevery) {
                backend.drawMarker(xData[i], yData[i], marker, markerSize, color);
            }
        }
    }

    /**
     * Place markers using the float markevery algorithm.
     *
     * <h3>Algorithm (matches matplotlib exactly)</h3>
     * <ol>
     *   <li>Compute the axes diagonal: {@code diag = sqrt(vw² + vh²)} pixels.</li>
     *   <li>Convert fractions to pixels:
     *       {@code startPx = markeveryStart × diag},
     *       {@code periodPx = markeveryPeriod × diag}.</li>
     *   <li>Transform <em>all</em> data points to screen coordinates via the
     *       backend's current axis transforms (including points outside the current
     *       axis limits — those map to screen positions far outside the viewport
     *       but still contribute to the cumulative arc length).</li>
     *   <li>Walk the cumulative arc length; whenever it crosses the next threshold
     *       ({@code startPx + k × periodPx}), emit a marker at that data point.
     *       The viewport clip applied by {@link Axes#draw} automatically discards
     *       markers that fall outside the visible area.</li>
     * </ol>
     *
     * <p>This produces correct behaviour on zoomed axes: the full data path (not
     * just the visible portion) sets the arc-length baseline, so the markers that
     * happen to land inside the zoom window are identical to what matplotlib shows.
     *
     * @param backend the rendering backend (must have setViewport already called)
     */
    private void drawMarkersFloat(Backend backend) {
        final int n = xData.length;
        if (n == 0) return;

        // ── 1. Axes diagonal ─────────────────────────────────────────────────
        double vw   = backend.getVw();
        double vh   = backend.getVh();
        double diag = Math.sqrt(vw * vw + vh * vh);
        if (diag < 1.0) diag = 1.0;   // guard against zero-size viewport

        double startPx  = markeveryStart  * diag;
        double periodPx = Math.max(1.0, markeveryPeriod * diag);

        // ── 2. Screen coordinates for the full data path ──────────────────────
        // We compute these lazily (one at a time) to avoid a large temporary array.
        // prevSx/prevSy hold the screen position of the previous data point.
        double prevSx = backend.transformX(xData[0]);
        double prevSy = backend.transformY(yData[0]);

        // ── 3. Walk cumulative arc length, emit markers at threshold crossings ─
        double cumLen   = 0.0;
        int    k        = 0;             // number of thresholds already crossed
        double threshold = startPx;     // next crossing level (startPx + k × periodPx)

        // Check the very first point (handles the common startPx == 0 case)
        if (cumLen >= threshold) {
            backend.drawMarker(xData[0], yData[0], marker, markerSize, color);
            k++;
            threshold = startPx + k * periodPx;
        }

        for (int i = 1; i < n; i++) {
            double sx = backend.transformX(xData[i]);
            double sy = backend.transformY(yData[i]);

            double dx = sx - prevSx;
            double dy = sy - prevSy;
            cumLen += Math.sqrt(dx * dx + dy * dy);

            prevSx = sx;
            prevSy = sy;

            if (cumLen >= threshold) {
                backend.drawMarker(xData[i], yData[i], marker, markerSize, color);
                k++;
                threshold = startPx + k * periodPx;
            }
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return the x data array */
    public double[] getXData() { return xData; }

    /** @return the y data array */
    public double[] getYData() { return yData; }

    /** @return the current line colour */
    public Color getColor() { return color; }

    /** @param color the new line colour */
    public void setColor(Color color) { this.color = color; }

    /** @return the current stroke width */
    public float getLineWidth() { return lineWidth; }

    /** @param lineWidth the new stroke width */
    public void setLineWidth(float lineWidth) { this.lineWidth = lineWidth; }

    /** @return the dash pattern ({@code null} = solid) */
    public float[] getDashPattern() { return dashPattern; }

    /** @param dashPattern the dash pattern */
    public void setDashPattern(float[] dashPattern) { this.dashPattern = dashPattern; }

    /** @return the marker type string (e.g. {@code "o"}, {@code "s"}) */
    public String getMarker() { return marker; }

    /** @param marker the marker type string */
    public void setMarker(String marker) { this.marker = marker; }

    /** @return the marker size in pixels */
    public float getMarkerSize() { return markerSize; }

    /** @param markerSize the marker size in pixels */
    public void setMarkerSize(float markerSize) { this.markerSize = markerSize; }

    /** @return the join style ({@code BasicStroke.JOIN_…}) */
    public int getJoinStyle() { return joinStyle; }

    /** @param joinStyle the join style */
    public void setJoinStyle(int joinStyle) { this.joinStyle = joinStyle; }

    /** @return the cap style ({@code BasicStroke.CAP_…}) */
    public int getCapStyle() { return capStyle; }

    /** @param capStyle the cap style */
    public void setCapStyle(int capStyle) { this.capStyle = capStyle; }
}
