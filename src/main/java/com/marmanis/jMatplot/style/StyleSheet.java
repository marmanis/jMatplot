package com.marmanis.jMatplot.style;

/**
 * Style sheet constants and application for jMatplot gallery examples.
 *
 * <p>This class is the Java equivalent of {@code plt.style.use(name)} in Python matplotlib.
 * Every gallery example calls {@link #use(String)} as its very first statement, before any
 * data generation or plotting.
 *
 * <h2>Gallery style sheets</h2>
 * <p>The two style constants used exclusively across the matplotlib gallery source files are:
 * <ul>
 *   <li>{@link #MPL_GALLERY} — default gallery style with a light grid, used by most examples.</li>
 *   <li>{@link #MPL_GALLERY_NO_GRID} — gallery style without grid lines, used for image, contour,
 *       and unstructured data plots where grid lines would obscure the data.</li>
 * </ul>
 *
 * <h2>Usage pattern (from every Python gallery file)</h2>
 * <pre>{@code
 * // Python:
 * plt.style.use('_mpl-gallery')
 *
 * // Java equivalent:
 * StyleSheet.use(StyleSheet.MPL_GALLERY);
 * }</pre>
 *
 * @see <a href="https://matplotlib.org/stable/gallery/style_sheets/style_sheets_reference.html">
 *      matplotlib style sheets reference</a>
 */
public final class StyleSheet {

    // ─── Public constants ──────────────────────────────────────────────────────

    /**
     * The default matplotlib gallery style ({@code _mpl-gallery}).
     * Applied by most plot_types and gallery examples that display line, bar, scatter,
     * statistical, and 3-D plots.
     */
    public static final String MPL_GALLERY = "_mpl-gallery";

    /**
     * The matplotlib gallery style without grid lines ({@code _mpl-gallery-nogrid}).
     * Applied by examples that render images, contour plots, pseudocolor grids,
     * and unstructured data, where grid lines would obscure the visual content.
     */
    public static final String MPL_GALLERY_NO_GRID = "_mpl-gallery-nogrid";

    /** Built-in Matplotlib {@code bmh} style. */
    public static final String BMH = "bmh";

    /** Built-in Matplotlib {@code dark_background} style. */
    public static final String DARK_BACKGROUND = "dark_background";

    /** Built-in Matplotlib {@code grayscale} style. */
    public static final String GRAYSCALE = "grayscale";

    /** Built-in Matplotlib {@code fivethirtyeight} style. */
    public static final String FIVETHIRTYEIGHT = "fivethirtyeight";

    /** Built-in Matplotlib {@code ggplot} style. */
    public static final String GGPLOT = "ggplot";

    /** Built-in Matplotlib {@code seaborn-v0_8} style. */
    public static final String SEABORN = "seaborn-v0_8";

    /** Built-in Matplotlib {@code Solarize_Light2} style. */
    public static final String SOLARIZE_LIGHT2 = "Solarize_Light2";

    // ─── Active style ──────────────────────────────────────────────────────────

    /** The currently active style name. Defaults to {@link #MPL_GALLERY}. */
    private static volatile String current = MPL_GALLERY;

    // ─── Private constructor ───────────────────────────────────────────────────

    /** Prevent instantiation — this is a utility class. */
    private StyleSheet() {}

    // ─── Public API ────────────────────────────────────────────────────────────

    /**
     * Activate the named style sheet.
     *
     * <p>This is the Java equivalent of {@code plt.style.use(name)}.
     * All subsequent figures created in the same thread will use the rendering
     * parameters associated with this style until {@code use()} is called again.
     *
     * @param name one of the style constants defined in this class, or any custom name
     * @throws IllegalArgumentException if {@code name} is {@code null} or blank
     */
    public static void use(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Style sheet name must not be null or blank");
        }
        current = name;
    }

    /**
     * Return the name of the currently active style sheet.
     *
     * @return the active style name; never {@code null}
     */
    public static String getCurrent() {
        return current;
    }

    /**
     * Return {@code true} if the currently active style suppresses grid lines.
     * Convenience helper used by rendering backends to decide whether to draw grid lines.
     *
     * @return {@code true} for {@link #MPL_GALLERY_NO_GRID}; {@code false} otherwise
     */
    public static boolean isNoGrid() {
        return MPL_GALLERY_NO_GRID.equals(current);
    }

    /**
     * Reset to the default gallery style ({@link #MPL_GALLERY}).
     * Useful in test tear-downs to restore a known baseline.
     */
    public static void reset() {
        current = MPL_GALLERY;
    }
}
