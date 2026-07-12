package com.marmanis.jMatplot.backend;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import com.marmanis.jMatplot.core.Colormap;
import com.marmanis.jMatplot.core.Normalize;

/**
 * The Backend interface abstracts the rendering logic, allowing jMatplot
 * to support multiple output formats (e.g., Swing, PDF, PNG).
 * This is the Java equivalent of Matplotlib's RendererBase.
 */
public interface Backend {
    
    /**
     * Set the current viewport (drawing area) on the canvas.
     * @param x Relative X start (0.0 to 1.0).
     * @param y Relative Y start (0.0 to 1.0).
     * @param width Relative width.
     * @param height Relative height.
     */
    void setViewport(double x, double y, double width, double height);
    
    /**
     * Set the current data limits for coordinate transformation.
     * @param xMin Minimum X data value.
     * @param xMax Maximum X data value.
     * @param yMin Minimum Y data value.
     * @param yMax Maximum Y data value.
     */
    void setLimits(double xMin, double xMax, double yMin, double yMax);

    /**
     * Draw a line between two points.
     * @param x1 Start X coordinate.
     * @param y1 Start Y coordinate.
     * @param x2 End X coordinate.
     * @param y2 End Y coordinate.
     * @param color Color of the line.
     * @param lineWidth Width of the line.
     * @param dashPattern Optional dash pattern for the line (null for solid).
     * @param joinStyle Join style (e.g. BasicStroke.JOIN_MITER).
     * @param capStyle Cap style (e.g. BasicStroke.CAP_BUTT).
     */
    void drawLine(double x1, double y1, double x2, double y2, Color color, float lineWidth, float[] dashPattern, int joinStyle, int capStyle);
    
    /**
     * Draw a marker at a specific location.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param markerType Type of marker (e.g., "o", "s", "d").
     * @param size Size of the marker.
     * @param color Color of the marker.
     */
    void drawMarker(double x, double y, String markerType, float size, Color color);
    
    /**
     * Draw a path (e.g., for polygons or complex lines).
     * @param path The path to draw.
     * @param edgeColor Color of the path's edge.
     * @param faceColor Color of the path's interior (fill).
     * @param lineWidth Width of the edge line.
     */
    void drawPath(Path2D path, Color edgeColor, Color faceColor, float lineWidth);
    
    /**
     * Draw a rectangle.
     * @param rect The rectangle to draw.
     * @param edgeColor Color of the rectangle's edge.
     * @param faceColor Color of the rectangle's interior (fill).
     * @param lineWidth Width of the edge line.
     */
    void drawRectangle(Rectangle2D rect, Color edgeColor, Color faceColor, float lineWidth);
    
    /**
     * Draw text at a specific location.
     * @param text The text string.
     * @param x The X coordinate for the text origin.
     * @param y The Y coordinate for the text origin.
     * @param color Color of the text.
     * @param fontName Name of the font family.
     * @param fontSize Size of the font.
     */
    void drawText(String text, double x, double y, Color color, String fontName, int fontSize);
    
    /**
     * Clear the canvas and prepare for a new frame.
     */
    void clear();

    /**
     * Draw an image.
     * @param data 2D array of data.
     * @param colormap Colormap to use.
     * @param normalize Normalization to use.
     */
    void drawImage(double[][] data, Colormap colormap, Normalize normalize);

    /**
     * Draw a line in absolute screen coordinates.
     */
    void drawScreenLine(double x1, double y1, double x2, double y2, Color color, float lineWidth);

    /**
     * Draw text in absolute screen coordinates.
     */
    void drawScreenText(String text, double x, double y, Color color, String fontName, int fontSize);

    /**
     * Draw text in absolute screen coordinates, rotated about the given centre point.
     *
     * <p>After rotation, the string is centred both horizontally and vertically at
     * ({@code x}, {@code y}), making it straightforward to position axis labels alongside
     * their respective axes.
     *
     * <p>A {@code angleDegrees} of {@code -90} rotates the text so that it reads
     * bottom-to-top — the standard orientation for a y-axis label in matplotlib.
     *
     * @param text         the string to render
     * @param x            absolute screen x of the text centre (after rotation)
     * @param y            absolute screen y of the text centre (after rotation)
     * @param angleDegrees rotation in degrees (positive = clockwise in screen space)
     * @param color        text colour
     * @param fontName     font family name
     * @param fontSize     point size
     */
    void drawRotatedScreenText(String text, double x, double y, double angleDegrees,
                               Color color, String fontName, int fontSize);

    /**
     * Transform data X to screen X.
     */
    double transformX(double x);

    /**
     * Transform data Y to screen Y.
     */
    double transformY(double y);

    /**
     * Enable or disable clipping to the current viewport (data area).
     *
     * <p>When {@code clip} is {@code true}, all subsequent drawing calls are
     * constrained to the rectangle established by the most recent
     * {@link #setViewport} call.  This ensures that data artists (lines,
     * markers, patches) do not bleed outside the axes boundary when explicit
     * axis limits are in effect (e.g. a zoomed plot where most data points
     * fall outside the visible range).
     *
     * <p>Callers must always follow a {@code setDataClip(true)} with a
     * matching {@code setDataClip(false)} to restore unrestricted drawing so
     * that spines, tick marks, and labels can be drawn in the margin area.
     *
     * @param clip {@code true} to activate viewport clipping; {@code false} to restore
     */
    default void setDataClip(boolean clip) {
        // default: no-op (back-ends that do not support clipping are unaffected)
    }

    /**
     * Enable or disable logarithmic scaling on each axis.
     *
     * <p>When log scaling is active, the coordinate transformation uses
     * {@code log10} instead of linear interpolation, matching the behaviour
     * of {@code ax.set_xscale('log')} / {@code ax.set_yscale('log')} in
     * matplotlib.  Both data limits must be strictly positive for log scaling
     * to produce meaningful results.
     *
     * <p>This method must be called <em>after</em> {@link #setLimits} so the
     * backend knows the data range before any drawing calls.
     *
     * @param logX {@code true} to use a logarithmic x axis
     * @param logY {@code true} to use a logarithmic y axis
     */
    default void setLogScales(boolean logX, boolean logY) {
        // default: no-op (linear is always the base case)
    }

    /**
     * Get the current viewport X origin in pixels.
     */
    double getVx();

    /**
     * Get the current viewport Y origin in pixels.
     */
    double getVy();

    /**
     * Get the current viewport width in pixels.
     * Used by float-based {@code markevery} to compute the axes diagonal.
     */
    default double getVw() { return 0; }

    /**
     * Get the current viewport height in pixels.
     * Used by float-based {@code markevery} to compute the axes diagonal.
     */
    default double getVh() { return 0; }

    /**
     * Draw a multi-segment polyline as a single stroke, preserving the specified
     * join style at every interior vertex.
     *
     * <p>Unlike calling {@link #drawLine} once per segment (which draws each
     * segment independently and loses the join geometry at shared endpoints),
     * this method builds a single {@link java.awt.geom.Path2D} and applies one
     * {@link java.awt.BasicStroke} so the AWT rendering engine draws the correct
     * miter, round, or bevel corner at each vertex.
     *
     * <p>This is equivalent to drawing a {@code matplotlib.lines.Line2D} with a
     * custom {@code solid_joinstyle} property.
     *
     * @param x           x data coordinates (length ≥ 2)
     * @param y           y data coordinates (same length as x)
     * @param color       stroke colour
     * @param lineWidth   stroke width in pixels
     * @param dashPattern optional dash array ({@code null} for solid)
     * @param joinStyle   one of {@code BasicStroke.JOIN_MITER},
     *                    {@code JOIN_ROUND}, or {@code JOIN_BEVEL}
     * @param capStyle    one of {@code BasicStroke.CAP_BUTT},
     *                    {@code CAP_ROUND}, or {@code CAP_SQUARE}
     * @param miterLimit  maximum miter length ratio; if the miter would exceed
     *                    {@code miterLimit × lineWidth / 2}, it falls back to bevel
     */
    /**
     * Draw a gradient-filled bar in data coordinates.
     *
     * <p>This is the jMatplot equivalent of matplotlib's technique of filling a
     * bar with an {@code AxesImage} whose pixel values sweep through a colormap
     * (see the {@code gradient_bar.py} gallery example).  The bar is filled with
     * a linear gradient that runs from {@code colorBottom} at the base of the bar
     * ({@code y}) to {@code colorTop} at the top ({@code y + h}).  A thin black
     * outline is drawn around the bar.
     *
     * <p>Back-ends that do not override this method fall back to drawing a plain
     * filled rectangle using {@code colorTop} (the "dominant" colour).
     *
     * @param x           data-space x coordinate of the bar's left edge
     * @param y           data-space y coordinate of the bar's base
     * @param w           bar width in data units
     * @param h           bar height in data units
     * @param colorBottom fill colour at the bottom of the bar
     * @param colorTop    fill colour at the top of the bar
     */
    default void drawGradientBar(double x, double y, double w, double h,
                                  Color colorBottom, Color colorTop) {
        // Fallback: plain filled rectangle using the top colour
        drawRectangle(new Rectangle2D.Double(x, y, w, h),
                      new Color(0x40, 0x40, 0x40), colorTop, 0.8f);
    }

    default void drawPolyline(double[] x, double[] y, Color color, float lineWidth,
                               float[] dashPattern, int joinStyle, int capStyle,
                               float miterLimit) {
        // Fallback for back-ends that have not overridden this method:
        // draw as individual segments (join style is lost at segment boundaries).
        if (x.length < 2) return;
        for (int i = 0; i < x.length - 1; i++) {
            drawLine(x[i], y[i], x[i + 1], y[i + 1], color, lineWidth, dashPattern,
                     joinStyle, capStyle);
        }
    }
}
