package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * A bar plot artist — the Java equivalent of the
 * {@code matplotlib.container.BarContainer} produced by {@code ax.bar()}.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Vertical bars positioned at numeric x coordinates with an optional
 *       stacking offset ({@link #setBottom}).</li>
 *   <li>Optional value labels drawn at the centre of each bar segment,
 *       equivalent to matplotlib's {@code ax.bar_label(container,
 *       label_type='center')} when {@link #setShowValues(boolean) showValues}
 *       is {@code true}.</li>
 *   <li>A single overall bar colour ({@link #setColor}) or per-bar colours
 *       ({@link #setColors}).</li>
 * </ul>
 */
public class Bar extends Artist {

    /** X centre positions of the bars (data coordinates). */
    private double[] x;

    /** Heights of each bar segment (data coordinates). */
    private final double[] values;

    /**
     * Y coordinate of the base of each bar.  {@code null} means the base is
     * at {@code y = 0} (i.e. no stacking).
     */
    private double[] bottom;

    /**
     * Per-bar colours.  When {@code null}, {@link #defaultColor} is used for
     * every bar.
     */
    private Color[] colors;

    /** Fallback colour used when {@link #colors} is {@code null}. */
    private Color defaultColor = Color.BLUE;

    /** Width of each bar in data-coordinate units. */
    private double width = 0.8;

    /**
     * When {@code true}, each bar's numeric value is drawn at the vertical
     * centre of that bar segment — equivalent to
     * {@code ax.bar_label(container, label_type='center')}.
     */
    private boolean showValues = false;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Construct bars at integer positions 0, 1, 2, … from category labels.
     *
     * @param labels category names (stored in the parent {@link Axes} as
     *               x-tick labels; the {@code Bar} itself only keeps the count)
     * @param values bar heights
     */
    public Bar(String[] labels, double[] values) {
        this.values = values;
        this.x = new double[values.length];
        for (int i = 0; i < values.length; i++) this.x[i] = i;
    }

    /**
     * Construct bars at explicit numeric positions.
     *
     * @param x      x centre positions in data coordinates
     * @param values bar heights
     */
    public Bar(double[] x, double[] values) {
        this.x      = x;
        this.values = values;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw all bar rectangles and, optionally, their value labels.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;

        for (int i = 0; i < values.length; i++) {
            double xi    = x[i] - width / 2.0;
            double base  = (bottom != null) ? bottom[i] : 0.0;
            double hi    = values[i];

            Color faceColor = (colors != null && colors.length > i) ? colors[i] : defaultColor;

            Rectangle2D.Double rect = new Rectangle2D.Double(xi, base, width, hi);
            backend.drawRectangle(rect, Color.BLACK, faceColor, 1.0f);

            // Value label centred in the bar segment (screen-space placement)
            if (showValues) {
                String text = formatValue(hi);
                // Convert bar centre to screen coordinates for centering
                double sx = backend.transformX(x[i]);
                double sy = backend.transformY(base + hi / 2.0);
                // Crude horizontal centering: ~6px per character at 9pt
                double labelScreenX = sx - text.length() * 3.0;
                backend.drawScreenText(text, labelScreenX, sy + 4,
                                       Color.WHITE, "SansSerif", 9);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Format a bar value for display.  Integers are shown without a decimal
     * point; fractional values are shown to two decimal places.
     */
    private static String formatValue(double v) {
        return (v == Math.floor(v) && !Double.isInfinite(v))
               ? String.valueOf((long) v)
               : String.format("%.2f", v);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return the x centre positions array */
    public double[] getX() { return x; }

    /** @return the bar heights array */
    public double[] getValues() { return values; }

    /** @return the stacking base array, or {@code null} if not stacked */
    public double[] getBottom() { return bottom; }

    /** @param bottom y-base of each bar for stacked plots; {@code null} = zero base */
    public void setBottom(double[] bottom) { this.bottom = bottom; }

    /** @return the uniform bar colour */
    public Color getColor() { return defaultColor; }

    /** @param color colour to use for all bars */
    public void setColor(Color color) { this.defaultColor = color; }

    /** @param colors per-bar colours; must be at least as long as the values array */
    public void setColors(Color[] colors) { this.colors = colors; }

    /** @return bar width in data units */
    public double getWidth() { return width; }

    /** @param width bar width in data units */
    public void setWidth(double width) { this.width = width; }

    /**
     * Enable or disable value labels drawn at the centre of each bar.
     * Equivalent to {@code ax.bar_label(p, label_type='center')}.
     *
     * @param showValues {@code true} to draw numeric labels inside each bar
     */
    public void setShowValues(boolean showValues) { this.showValues = showValues; }

    /** @return {@code true} if value labels are drawn */
    public boolean isShowValues() { return showValues; }
}
