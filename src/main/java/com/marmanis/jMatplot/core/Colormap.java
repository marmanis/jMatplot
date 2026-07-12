package com.marmanis.jMatplot.core;

import java.awt.Color;

/**
 * Maps normalized values [0, 1] to colors using either a two-stop gradient or
 * a multi-stop piecewise-linear colormap.
 *
 * <h2>Two-stop (classic) usage</h2>
 * <pre>{@code
 * Colormap cm = new Colormap(Color.BLUE, Color.YELLOW);
 * Color c = cm.getColor(0.5);   // mid-point blue-yellow
 * }</pre>
 *
 * <h2>Multi-stop usage</h2>
 * <pre>{@code
 * Colormap viridis = Colormap.viridis();
 * Color c = viridis.getColor(0.75);
 * }</pre>
 */
public class Colormap {

    // ── Two-stop fields (kept for backward compat) ────────────────────────────
    private final Color startColor;
    private final Color endColor;

    // ── Multi-stop fields (null when using two-stop mode) ─────────────────────
    /** Stop positions in [0, 1], must be sorted ascending. */
    private final double[] stops;
    /** Colors at each stop position; same length as {@link #stops}. */
    private final Color[]  stopColors;

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Two-stop (classic) constructor.
     *
     * @param startColor colour at t = 0
     * @param endColor   colour at t = 1
     */
    public Colormap(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor   = endColor;
        this.stops      = null;
        this.stopColors = null;
    }

    /**
     * Multi-stop constructor.
     *
     * @param stops      sorted stop positions in [0, 1] (must have ≥ 2 entries)
     * @param stopColors colors at each stop; same length as {@code stops}
     */
    public Colormap(double[] stops, Color[] stopColors) {
        if (stops.length != stopColors.length || stops.length < 2) {
            throw new IllegalArgumentException(
                    "stops and stopColors must be parallel arrays with ≥ 2 entries");
        }
        this.stops      = stops;
        this.stopColors = stopColors;
        this.startColor = stopColors[0];
        this.endColor   = stopColors[stopColors.length - 1];
    }

    // ── Color look-up ─────────────────────────────────────────────────────────

    /**
     * Return the color at a normalized position {@code t} in [0, 1].
     *
     * <p>In multi-stop mode the value is linearly interpolated between the two
     * surrounding stop colors.  In two-stop mode it delegates to the original
     * linear blend.
     *
     * @param normalizedValue position in [0, 1]
     * @return interpolated {@link Color}
     */
    public Color getColor(double normalizedValue) {
        double t = Math.max(0.0, Math.min(1.0, normalizedValue));

        if (stops == null) {
            // classic two-stop linear blend
            return blend(startColor, endColor, t);
        }

        // Multi-stop: find the bracket [stops[i], stops[i+1]] containing t
        if (t <= stops[0])       return stopColors[0];
        if (t >= stops[stops.length - 1]) return stopColors[stops.length - 1];

        for (int i = 0; i < stops.length - 1; i++) {
            if (t <= stops[i + 1]) {
                double localT = (t - stops[i]) / (stops[i + 1] - stops[i]);
                return blend(stopColors[i], stopColors[i + 1], localT);
            }
        }
        return stopColors[stopColors.length - 1];   // safety fallback
    }

    // ── Factory colormaps ─────────────────────────────────────────────────────

    /**
     * Create a Viridis colormap approximation.
     *
     * <p>Viridis is the default sequential colormap used by matplotlib for scatter
     * plots ({@code ax.scatter(..., c=values)}).  The key control points are taken
     * directly from the matplotlib colormap data:
     * <ul>
     *   <li>0.00 → (68,  1, 84)   — deep purple</li>
     *   <li>0.25 → (59, 82, 139)  — blue</li>
     *   <li>0.50 → (33, 145, 140) — teal</li>
     *   <li>0.75 → (94, 201, 98)  — green</li>
     *   <li>1.00 → (253, 231, 37) — yellow</li>
     * </ul>
     *
     * @return a new Viridis {@link Colormap}
     */
    public static Colormap viridis() {
        return new Colormap(
            new double[]{ 0.00, 0.25, 0.50, 0.75, 1.00 },
            new Color[]{
                new Color( 68,   1,  84),   // deep purple
                new Color( 59,  82, 139),   // blue
                new Color( 33, 145, 140),   // teal
                new Color( 94, 201,  98),   // green
                new Color(253, 231,  37),   // yellow
            }
        );
    }

    /**
     * Create a Plasma colormap approximation (violet → orange → yellow).
     *
     * @return a new Plasma {@link Colormap}
     */
    public static Colormap plasma() {
        return new Colormap(
            new double[]{ 0.00, 0.25, 0.50, 0.75, 1.00 },
            new Color[]{
                new Color( 13,   8, 135),   // deep violet
                new Color(126,   3, 168),   // purple
                new Color(204,  71, 120),   // pink-red
                new Color(248, 149,  64),   // orange
                new Color(240, 249,  33),   // yellow
            }
        );
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Color blend(Color a, Color b, double t) {
        int r = clamp((int) Math.round(a.getRed()   + t * (b.getRed()   - a.getRed())));
        int g = clamp((int) Math.round(a.getGreen() + t * (b.getGreen() - a.getGreen())));
        int bv= clamp((int) Math.round(a.getBlue()  + t * (b.getBlue()  - a.getBlue())));
        int al= clamp((int) Math.round(a.getAlpha() + t * (b.getAlpha() - a.getAlpha())));
        return new Color(r, g, bv, al);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
