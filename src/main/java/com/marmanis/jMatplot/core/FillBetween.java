package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * An artist that fills the area between two lines.
 * This is the Java equivalent of matplotlib.collections.PolyCollection created by fill_between().
 */
public class FillBetween extends Artist {
    private final double[] x;
    private final double[] y1;
    private final double[] y2;
    private Color faceColor = new Color(0, 0, 255, 128); // Semi-transparent blue
    private Color edgeColor = null;
    private float lineWidth = 1.0f;

    public FillBetween(double[] x, double[] y1, double[] y2) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible() || x.length < 2) return;

        Path2D.Double path = new Path2D.Double();
        // Forward along y1
        path.moveTo(x[0], y1[0]);
        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y1[i]);
        }
        // Backward along y2
        for (int i = x.length - 1; i >= 0; i--) {
            path.lineTo(x[i], y2[i]);
        }
        path.closePath();

        backend.drawPath(path, edgeColor, faceColor, lineWidth);
    }

    // ── Data accessors (used by Legend for placement density sampling) ───────

    /** Return the shared x coordinates. */
    public double[] getX()  { return x;  }

    /**
     * Return the first boundary curve.
     *
     * <p>In a {@link Axes#stackplot stackplot} layer this is the <em>lower</em>
     * boundary (cumulative sum up to the previous layer).
     */
    public double[] getY1() { return y1; }

    /**
     * Return the second boundary curve.
     *
     * <p>In a {@link Axes#stackplot stackplot} layer this is the <em>upper</em>
     * boundary (cumulative sum including this layer).
     */
    public double[] getY2() { return y2; }

    // ── Style setters ─────────────────────────────────────────────────────────

    public void setFaceColor(Color color) { this.faceColor = color; }
    public void setEdgeColor(Color color) { this.edgeColor = color; }

    /**
     * Set the fill colour (convenience alias for setFaceColor).
     * @param color fill colour
     */
    public void setColor(Color color) { this.faceColor = color; }

    /**
     * Return the current face (fill) colour, including any alpha that has been
     * applied via {@link #setAlpha(float)}.
     *
     * <p>Used by {@link Legend} to resolve the swatch colour for fill-between
     * entries.
     *
     * @return current face colour
     */
    public Color getColor() { return faceColor; }

    /**
     * Set the transparency of the filled region.
     * @param alpha 0.0 (fully transparent) to 1.0 (fully opaque)
     */
    public void setAlpha(float alpha) {
        int a = Math.max(0, Math.min(255, Math.round(alpha * 255)));
        this.faceColor = new Color(faceColor.getRed(), faceColor.getGreen(), faceColor.getBlue(), a);
    }
}
