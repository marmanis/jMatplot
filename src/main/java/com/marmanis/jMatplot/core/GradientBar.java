package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A bar-chart artist where each bar is filled with a vertical linear gradient
 * instead of a flat colour.
 *
 * <h2>Relation to matplotlib</h2>
 * <p>matplotlib does not natively support gradient-filled bars.  The
 * {@code gradient_bar.py} gallery example emulates the effect by overlaying an
 * {@code AxesImage} (produced by {@code ax.imshow()}) whose pixel values sweep
 * through a colormap over the extent of each bar.  jMatplot replicates this
 * visually by using Java's {@link java.awt.GradientPaint} to fill each bar
 * rectangle, which is equivalent to a 1-D colormap sweep and avoids the need
 * for an explicit pixel-buffer.
 *
 * <h2>Default colour scheme</h2>
 * <p>The default colours approximate matplotlib's {@code Blues_r} colormap with
 * {@code cmap_range=(0, 0.8)}:
 * <ul>
 *   <li>{@link #colorBottom} — near-white light blue at the bar base
 *       ({@code #C6DCEF}, Blues_r ≈ 0.8)</li>
 *   <li>{@link #colorTop}    — deep blue at the bar tip
 *       ({@code #08519C}, Blues_r ≈ 0.0)</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * GradientBar gb = ax.gradientBar(x, values, 0.6);
 * gb.setColorBottom(Color.WHITE);
 * gb.setColorTop(new Color(0x08, 0x51, 0x9C));
 * }</pre>
 */
public class GradientBar extends Artist {

    /** X centre positions of the bars (data coordinates). */
    private final double[] x;

    /** Heights of each bar (data coordinates). */
    private final double[] values;

    /** Width of each bar in data units. */
    private double width;

    /**
     * Colour at the base of each bar.
     * Default: light blue {@code #C6DCEF} (Blues_r colourmap near 0.8).
     */
    private Color colorBottom = new Color(0xC6, 0xDC, 0xEF);

    /**
     * Colour at the tip of each bar.
     * Default: deep blue {@code #08519C} (Blues_r colourmap near 0.0).
     */
    private Color colorTop = new Color(0x08, 0x51, 0x9C);

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Construct gradient bars at explicit numeric x positions.
     *
     * @param x      x centre positions in data coordinates
     * @param values bar heights in data coordinates
     * @param width  bar width in data units
     */
    public GradientBar(double[] x, double[] values, double width) {
        this.x      = x;
        this.values = values;
        this.width  = width;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw each bar as a gradient-filled rectangle.
     *
     * <p>Each bar delegates to {@link Backend#drawGradientBar}, which in the
     * Swing back-end is implemented with a {@link java.awt.GradientPaint} running
     * vertically from {@link #colorBottom} at {@code y=0} to {@link #colorTop}
     * at {@code y=height}.
     *
     * @param backend the rendering back-end
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        for (int i = 0; i < values.length; i++) {
            double barX = x[i] - width / 2.0;
            double barH = values[i];
            backend.drawGradientBar(barX, 0, width, barH, colorBottom, colorTop);
        }
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return x centre positions */
    public double[] getX()      { return x; }

    /** @return bar heights */
    public double[] getValues() { return values; }

    /** @return bar width in data units */
    public double getWidth()    { return width; }

    /** @param width bar width in data units */
    public void setWidth(double width) { this.width = width; }

    /** @return the fill colour at the bottom of each bar */
    public Color getColorBottom() { return colorBottom; }

    /**
     * Set the fill colour at the base (bottom) of each bar.
     * @param colorBottom base colour
     */
    public void setColorBottom(Color colorBottom) { this.colorBottom = colorBottom; }

    /** @return the fill colour at the top of each bar */
    public Color getColorTop() { return colorTop; }

    /**
     * Set the fill colour at the tip (top) of each bar.
     * @param colorTop tip colour
     */
    public void setColorTop(Color colorTop) { this.colorTop = colorTop; }
}
