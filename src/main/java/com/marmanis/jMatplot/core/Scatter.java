package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A scatter plot artist — the Java equivalent of
 * {@code matplotlib.collections.PathCollection} produced by {@code ax.scatter()}.
 *
 * <h2>Uniform vs. per-point styling</h2>
 * <p>Two modes are supported:
 * <ol>
 *   <li><b>Uniform</b> — all markers share the same {@link #color} and
 *       {@link #markerSize}.  This is the default mode and mirrors matplotlib's
 *       behaviour when scalar {@code c} and {@code s} values are passed.</li>
 *   <li><b>Per-point</b> — each marker gets its own diameter ({@link #perSizes})
 *       and/or colour ({@link #perColors}).  This mirrors matplotlib when array
 *       {@code c} and {@code s} are passed to {@code ax.scatter()}.  Set
 *       {@link #globalAlpha} to apply a uniform alpha on top of the per-point
 *       colours.</li>
 * </ol>
 *
 * <h2>Size convention</h2>
 * <p>Both {@link #markerSize} and {@link #perSizes} are <em>diameters in screen
 * pixels</em>.  To approximate matplotlib's {@code s} parameter (area in
 * typographic points²), convert with:
 * <pre>
 *   diameter_px = 2 * sqrt(s_pt2 / π) * pxPerPt
 * </pre>
 * where {@code pxPerPt ≈ 1.0} for typical on-screen rendering in jMatplot.
 */
public class Scatter extends Artist {

    private final double[] xData;
    private final double[] yData;

    // ── Uniform styling ───────────────────────────────────────────────────────
    /** Uniform fill colour (used when {@link #perColors} is {@code null}). */
    private Color  color      = Color.BLUE;
    /** Marker shape — {@code "o"} (circle), {@code "s"} (square), etc. */
    private String marker     = "o";
    /** Uniform marker diameter in screen pixels. */
    private float  markerSize = 6.0f;

    // ── Per-point styling (optional) ──────────────────────────────────────────
    /**
     * Per-point marker diameters in screen pixels.
     * When non-{@code null}, overrides {@link #markerSize} for each point.
     */
    private float[] perSizes  = null;

    /**
     * Per-point opaque colours (alpha channel is ignored; apply
     * {@link #globalAlpha} instead).
     * When non-{@code null}, overrides {@link #color} for each point.
     */
    private Color[] perColors = null;

    /**
     * Alpha applied uniformly on top of per-point colours.
     * Ignored when {@link #perColors} is {@code null} (use
     * {@link #setAlpha(float)} to modify the uniform colour instead).
     */
    private float globalAlpha = 1.0f;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Create a scatter artist for the given data arrays.
     *
     * @param xData x coordinates
     * @param yData y coordinates (must have the same length as {@code xData})
     */
    public Scatter(double[] xData, double[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw each marker using the back-end.
     *
     * <p>When per-point arrays are set ({@link #perSizes}, {@link #perColors}),
     * each point is rendered individually with its own size and/or colour.
     * {@link #globalAlpha} is applied to the per-point colour's alpha channel.
     *
     * @param backend rendering back-end
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;

        int alpha255 = Math.max(0, Math.min(255, Math.round(globalAlpha * 255)));

        for (int i = 0; i < xData.length; i++) {
            // resolve colour for this point
            Color c;
            if (perColors != null) {
                Color base = perColors[i];
                c = new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha255);
            } else {
                c = color;   // already has alpha baked in via setAlpha()
            }

            // resolve size for this point
            float sz = (perSizes != null) ? perSizes[i] : markerSize;

            backend.drawMarker(xData[i], yData[i], marker, sz, c);
        }
    }

    // ── Data accessors (used by Legend for placement density sampling) ───────

    /** @return x coordinates of the scatter points */
    public double[] getXData() { return xData; }

    /** @return y coordinates of the scatter points */
    public double[] getYData() { return yData; }

    // ── Uniform-style accessors ───────────────────────────────────────────────

    /** @return the uniform fill colour */
    public Color getColor() { return color; }

    /**
     * Set the uniform fill colour for all markers.
     * Has no effect when {@link #perColors} is set.
     *
     * @param color fill colour
     */
    public void setColor(Color color) { this.color = color; }

    /**
     * Set the transparency of all markers (uniform mode).
     * Modifies the alpha channel of the current {@link #color}.
     * In per-point mode, use {@link #setGlobalAlpha(float)} instead.
     *
     * @param alpha 0.0 (fully transparent) to 1.0 (fully opaque)
     */
    public void setAlpha(float alpha) {
        int a = Math.max(0, Math.min(255, Math.round(alpha * 255)));
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    /** @param marker marker shape code ({@code "o"}, {@code "s"}, …) */
    public void setMarker(String marker) { this.marker = marker; }

    /** @param size uniform marker diameter in screen pixels */
    public void setMarkerSize(float size) { this.markerSize = size; }

    // ── Per-point accessors ───────────────────────────────────────────────────

    /**
     * Set per-point marker diameters in screen pixels.
     * Must have the same length as the data arrays, or {@code null} to revert
     * to uniform sizing.
     *
     * @param perSizes per-point diameters, or {@code null}
     */
    public void setPerSizes(float[] perSizes)   { this.perSizes  = perSizes;  }

    /**
     * Set per-point opaque marker colours.
     * Must have the same length as the data arrays, or {@code null} to revert
     * to uniform colour.  The alpha channel of each colour is overridden by
     * {@link #globalAlpha}.
     *
     * @param perColors per-point colours, or {@code null}
     */
    public void setPerColors(Color[] perColors) { this.perColors = perColors; }

    /**
     * Set the global alpha applied to per-point colours.
     * Has no effect when {@link #perColors} is {@code null}.
     *
     * @param alpha 0.0 (fully transparent) to 1.0 (fully opaque)
     */
    public void setGlobalAlpha(float alpha) {
        this.globalAlpha = Math.max(0.0f, Math.min(1.0f, alpha));
    }

    /** @return per-point diameters, or {@code null} if in uniform mode */
    public float[] getPerSizes()  { return perSizes;  }

    /** @return per-point colours, or {@code null} if in uniform mode */
    public Color[] getPerColors() { return perColors; }

    /** @return global alpha value applied to per-point colours */
    public float getGlobalAlpha() { return globalAlpha; }
}
