package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * An artist that draws a collection of parallel "event" tick marks — the
 * Java equivalent of {@code matplotlib.collections.EventCollection} produced
 * by {@code matplotlib.axes.Axes.eventplot}.
 *
 * <p>The data is a jagged array of rows ({@link #positions}); each row
 * {@code i} is drawn as a set of short line segments, one per value in
 * {@code positions[i]}, perpendicular to the data axis. Each segment is
 * centered on that row's {@link #lineOffsets offset} with total length
 * {@link #lineLengths lineLengths}{@code [i]}, and colored with
 * {@link #colors}{@code [i]}.
 *
 * <h2>Orientation</h2>
 * <p>In {@code "horizontal"} orientation (the default), each value {@code v}
 * in row {@code i} becomes a <em>vertical</em> segment at {@code x = v}
 * spanning {@code y ∈ [offset - length/2, offset + length/2]} — matching
 * matplotlib's default. In {@code "vertical"} orientation the roles of x and
 * y are swapped: each value becomes a <em>horizontal</em> segment at
 * {@code y = v} spanning {@code x ∈ [offset - length/2, offset + length/2]}.
 *
 * <h2>Python equivalent</h2>
 * <pre>{@code
 * ax.eventplot(positions, colors=colors, lineoffsets=offsets,
 *              linelengths=lengths, orientation='horizontal')
 * }</pre>
 */
public class EventPlot extends Artist {

    private final double[][] positions;
    private Color[] colors;
    private double[] lineOffsets;
    private double[] lineLengths;
    private float lineWidth = 1.5f;
    private String orientation = "horizontal";

    /**
     * Construct an event plot from per-row event positions.
     *
     * <p>Defaults: every row drawn in tab:blue, offset equal to its row
     * index, length {@code 1.0}, horizontal orientation.
     *
     * @param positions jagged array; {@code positions[i]} holds the event
     *                  values for row {@code i}
     */
    public EventPlot(double[][] positions) {
        this.positions = positions;
        int n = positions.length;
        this.colors = new Color[n];
        this.lineOffsets = new double[n];
        this.lineLengths = new double[n];
        for (int i = 0; i < n; i++) {
            colors[i] = new Color(0x1F77B4);
            lineOffsets[i] = i;
            lineLengths[i] = 1.0;
        }
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        boolean vertical = "vertical".equals(orientation);

        for (int row = 0; row < positions.length; row++) {
            double half = lineLengths[row] / 2.0;
            double off = lineOffsets[row];
            Color c = colors[row];
            for (double v : positions[row]) {
                if (vertical) {
                    backend.drawLine(off - half, v, off + half, v, c, lineWidth, null,
                                     BasicStroke.JOIN_MITER, BasicStroke.CAP_BUTT);
                } else {
                    backend.drawLine(v, off - half, v, off + half, c, lineWidth, null,
                                     BasicStroke.JOIN_MITER, BasicStroke.CAP_BUTT);
                }
            }
        }
    }

    // ── Style setters ─────────────────────────────────────────────────────────

    /** Set a uniform color for every row. */
    public void setColor(Color color) {
        for (int i = 0; i < colors.length; i++) colors[i] = color;
    }

    /** Set a per-row color; must be the same length as the positions array. */
    public void setColors(Color[] colors) { this.colors = colors; }

    /** Set a uniform offset for every row. */
    public void setLineOffset(double offset) {
        for (int i = 0; i < lineOffsets.length; i++) lineOffsets[i] = offset;
    }

    /** Set a per-row offset; must be the same length as the positions array. */
    public void setLineOffsets(double[] lineOffsets) { this.lineOffsets = lineOffsets; }

    /** Set a uniform segment length for every row. */
    public void setLineLength(double length) {
        for (int i = 0; i < lineLengths.length; i++) lineLengths[i] = length;
    }

    /** Set a per-row segment length; must be the same length as the positions array. */
    public void setLineLengths(double[] lineLengths) { this.lineLengths = lineLengths; }

    /** Set the stroke width used for every segment. */
    public void setLineWidth(float lineWidth) { this.lineWidth = lineWidth; }

    /** Set the orientation: {@code "horizontal"} (default) or {@code "vertical"}. */
    public void setOrientation(String orientation) { this.orientation = orientation; }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public double[][] getPositions()   { return positions;   }
    public Color[]     getColors()      { return colors;      }
    public double[]    getLineOffsets() { return lineOffsets; }
    public double[]    getLineLengths() { return lineLengths; }
    public float       getLineWidth()   { return lineWidth;   }
    public String      getOrientation() { return orientation; }
}
