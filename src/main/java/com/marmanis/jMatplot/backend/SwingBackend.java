package com.marmanis.jMatplot.backend;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import com.marmanis.jMatplot.core.Colormap;
import com.marmanis.jMatplot.core.Normalize;

/**
 * A backend that renders to a Java Swing {@link Graphics2D} object.
 *
 * <h2>Coordinate system</h2>
 * <ul>
 *   <li>Data coordinates  — the values supplied by the user (e.g. xMin..xMax).</li>
 *   <li>Viewport coordinates — normalised 0..1 fractions of the figure canvas.</li>
 *   <li>Screen coordinates — absolute pixels within the canvas.</li>
 * </ul>
 *
 * <p>{@link #transformX}/{@link #transformY} map data → screen.
 * {@link #drawScreenLine} and {@link #drawScreenText} accept <em>absolute screen
 * coordinates</em> (i.e. already transformed); they must <strong>not</strong> add
 * the viewport offset a second time.
 */
public class SwingBackend implements Backend {

    private final Graphics2D g2d;
    private final int width;
    private final int height;

    // Current data limits (set by setLimits)
    private double xMin, xMax, yMin, yMax;

    // Current viewport in pixels (set by setViewport)
    private double vx, vy, vw, vh;

    // Logarithmic-axis flags (set by setLogScales)
    private boolean logX = false;
    private boolean logY = false;

    // Saved clip shape, used by setDataClip to restore the previous clip region
    private java.awt.Shape savedClip = null;

    /**
     * Construct a SwingBackend for a canvas of the given pixel dimensions.
     *
     * @param g2d    the Graphics2D context to draw into
     * @param width  canvas width in pixels
     * @param height canvas height in pixels
     */
    public SwingBackend(Graphics2D g2d, int width, int height) {
        this.g2d = g2d;
        this.width = width;
        this.height = height;
        this.vx = 0;
        this.vy = 0;
        this.vw = width;
        this.vh = height;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    // ── Viewport / limits ────────────────────────────────────────────────────

    /**
     * Set the current drawing viewport using normalised (0–1) fractions of the canvas.
     * The Y axis is flipped so that y=0 is the bottom of the canvas.
     *
     * @param x      left edge fraction
     * @param y      bottom edge fraction (before Y flip)
     * @param width  viewport width fraction
     * @param height viewport height fraction
     */
    @Override
    public void setViewport(double x, double y, double width, double height) {
        this.vx = x * this.width;
        // Flip Y: matplotlib's y=0 is bottom; screen y=0 is top
        this.vy = (1.0 - y - height) * this.height;
        this.vw = width  * this.width;
        this.vh = height * this.height;
    }

    /**
     * Set the data limits used for coordinate transformation.
     *
     * @param xMin minimum x data value
     * @param xMax maximum x data value
     * @param yMin minimum y data value
     * @param yMax maximum y data value
     */
    @Override
    public void setLimits(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Enable or disable clipping to the current viewport rectangle.
     *
     * <p>Activating the clip (passing {@code true}) saves the current {@link java.awt.Shape}
     * clip and intersects the active clip with the data-area rectangle
     * {@code (vx, vy, vw, vh)}.  All subsequent drawing calls are restricted to
     * that rectangle until {@code setDataClip(false)} is called.
     *
     * <p>Deactivating the clip (passing {@code false}) restores the previously saved
     * clip, allowing decorations (spines, tick marks, axis labels) to be drawn freely
     * in the margin area outside the data rectangle.
     *
     * @param clip {@code true} to constrain drawing to the viewport; {@code false} to restore
     */
    @Override
    public void setDataClip(boolean clip) {
        if (clip) {
            savedClip = g2d.getClip();
            // Use clip() rather than setClip() so we intersect with any outer clip
            // (e.g. a parent panel clip), rather than replacing it entirely.
            g2d.clip(new Rectangle2D.Double(vx, vy, vw, vh));
        } else {
            g2d.setClip(savedClip);
            savedClip = null;
        }
    }

    /**
     * Enable or disable logarithmic coordinate scaling.
     *
     * <p>When a log axis is active, {@link #transformX} / {@link #transformY} use
     * {@code log10} interpolation instead of linear interpolation.  Both the
     * corresponding data limit and the value being transformed must be strictly
     * positive; otherwise the method falls back to linear scaling silently.
     *
     * @param logX {@code true} to use a log-10 x axis
     * @param logY {@code true} to use a log-10 y axis
     */
    @Override
    public void setLogScales(boolean logX, boolean logY) {
        this.logX = logX;
        this.logY = logY;
    }

    // ── Coordinate transforms ────────────────────────────────────────────────

    /**
     * Map a data x-value to an absolute screen x-coordinate.
     *
     * <p>When {@link #logX} is {@code true} (set via {@link #setLogScales}), the mapping
     * uses log-10 interpolation between {@code xMin} and {@code xMax}, matching the visual
     * behaviour of {@code ax.set_xscale('log')} in matplotlib.  All of {@code x}, {@code xMin},
     * and {@code xMax} must be strictly positive for log scaling to apply; if any value is
     * non-positive the method silently falls back to linear interpolation.
     *
     * @param x data x value
     * @return screen x in pixels
     */
    @Override
    public double transformX(double x) {
        if (logX && x > 0 && xMin > 0 && xMax > 0) {
            double logVal = Math.log10(x);
            double logMin = Math.log10(xMin);
            double logMax = Math.log10(xMax);
            double range  = (logMax == logMin) ? 1.0 : (logMax - logMin);
            return vx + ((logVal - logMin) / range) * vw;
        }
        double range = (xMax == xMin) ? 1.0 : (xMax - xMin);
        return vx + ((x - xMin) / range) * vw;
    }

    /**
     * Map a data y-value to an absolute screen y-coordinate (Y-flipped).
     *
     * <p>When {@link #logY} is {@code true}, log-10 interpolation is used, mirroring
     * {@code ax.set_yscale('log')} in matplotlib.  Falls back to linear if any value is
     * non-positive.
     *
     * @param y data y value
     * @return screen y in pixels
     */
    @Override
    public double transformY(double y) {
        if (logY && y > 0 && yMin > 0 && yMax > 0) {
            double logVal = Math.log10(y);
            double logMin = Math.log10(yMin);
            double logMax = Math.log10(yMax);
            double range  = (logMax == logMin) ? 1.0 : (logMax - logMin);
            return vy + vh - ((logVal - logMin) / range) * vh;
        }
        double range = (yMax == yMin) ? 1.0 : (yMax - yMin);
        return vy + vh - ((y - yMin) / range) * vh;
    }

    @Override public double getVx() { return vx; }
    @Override public double getVy() { return vy; }
    @Override public double getVw() { return vw; }
    @Override public double getVh() { return vh; }

    // ── Primitive drawing — data coordinates ─────────────────────────────────

    @Override
    public void drawLine(double x1, double y1, double x2, double y2,
                         Color color, float lineWidth, float[] dashPattern,
                         int joinStyle, int capStyle) {
        g2d.setColor(color);
        if (dashPattern != null && dashPattern.length > 0) {
            g2d.setStroke(new java.awt.BasicStroke(lineWidth, capStyle, joinStyle,
                                                    10.0f, dashPattern, 0.0f));
        } else {
            g2d.setStroke(new java.awt.BasicStroke(lineWidth, capStyle, joinStyle));
        }
        g2d.draw(new Line2D.Double(transformX(x1), transformY(y1),
                                   transformX(x2), transformY(y2)));
    }

    @Override
    public void drawMarker(double x, double y, String markerType, float size, Color color) {
        g2d.setColor(color);
        double tx = transformX(x);
        double ty = transformY(y);
        double h  = size / 2.0;

        switch (markerType == null ? "." : markerType) {
            case "o":
                g2d.fill(new java.awt.geom.Ellipse2D.Double(tx - h, ty - h, size, size));
                break;
            case "s":
                g2d.fill(new Rectangle2D.Double(tx - h, ty - h, size, size));
                break;
            case "d":
            case "D": {
                Path2D.Double p = new Path2D.Double();
                p.moveTo(tx, ty - h);
                p.lineTo(tx + h, ty);
                p.lineTo(tx, ty + h);
                p.lineTo(tx - h, ty);
                p.closePath();
                g2d.fill(p);
                break;
            }
            case "^":
            case "v": {
                Path2D.Double p = new Path2D.Double();
                if ("^".equals(markerType)) {
                    p.moveTo(tx, ty - h); p.lineTo(tx + h, ty + h); p.lineTo(tx - h, ty + h);
                } else {
                    p.moveTo(tx, ty + h); p.lineTo(tx + h, ty - h); p.lineTo(tx - h, ty - h);
                }
                p.closePath();
                g2d.fill(p);
                break;
            }
            case "+":
                g2d.setStroke(new java.awt.BasicStroke(1.5f));
                g2d.draw(new Line2D.Double(tx - h, ty, tx + h, ty));
                g2d.draw(new Line2D.Double(tx, ty - h, tx, ty + h));
                break;
            case "x":
                g2d.setStroke(new java.awt.BasicStroke(1.5f));
                g2d.draw(new Line2D.Double(tx - h, ty - h, tx + h, ty + h));
                g2d.draw(new Line2D.Double(tx + h, ty - h, tx - h, ty + h));
                break;
            default:
                // Small filled circle for unknown marker types
                g2d.fill(new java.awt.geom.Ellipse2D.Double(tx - 2, ty - 2, 4, 4));
        }
    }

    @Override
    public void drawPath(Path2D path, Color edgeColor, Color faceColor, float lineWidth) {
        Path2D.Double tp = new Path2D.Double();
        java.awt.geom.PathIterator it = path.getPathIterator(null);
        double[] c = new double[6];
        while (!it.isDone()) {
            switch (it.currentSegment(c)) {
                case java.awt.geom.PathIterator.SEG_MOVETO:
                    tp.moveTo(transformX(c[0]), transformY(c[1])); break;
                case java.awt.geom.PathIterator.SEG_LINETO:
                    tp.lineTo(transformX(c[0]), transformY(c[1])); break;
                case java.awt.geom.PathIterator.SEG_QUADTO:
                    tp.quadTo(transformX(c[0]), transformY(c[1]),
                              transformX(c[2]), transformY(c[3])); break;
                case java.awt.geom.PathIterator.SEG_CUBICTO:
                    tp.curveTo(transformX(c[0]), transformY(c[1]),
                               transformX(c[2]), transformY(c[3]),
                               transformX(c[4]), transformY(c[5])); break;
                case java.awt.geom.PathIterator.SEG_CLOSE:
                    tp.closePath(); break;
            }
            it.next();
        }
        if (faceColor != null) { g2d.setColor(faceColor); g2d.fill(tp); }
        if (edgeColor != null) {
            g2d.setColor(edgeColor);
            g2d.setStroke(new java.awt.BasicStroke(lineWidth));
            g2d.draw(tp);
        }
    }

    @Override
    public void drawRectangle(Rectangle2D rect, Color edgeColor, Color faceColor, float lineWidth) {
        // rect is in data coordinates; transform top-left corner then scale
        double tx = transformX(rect.getX());
        double ty = transformY(rect.getY() + rect.getHeight()); // flip: top-left in screen
        double xRange = (xMax == xMin) ? 1.0 : (xMax - xMin);
        double yRange = (yMax == yMin) ? 1.0 : (yMax - yMin);
        double tw = (rect.getWidth()  / xRange) * vw;
        double th = (rect.getHeight() / yRange) * vh;
        Rectangle2D.Double sr = new Rectangle2D.Double(tx, ty, tw, th);
        if (faceColor != null) { g2d.setColor(faceColor); g2d.fill(sr); }
        if (edgeColor != null) {
            g2d.setColor(edgeColor);
            g2d.setStroke(new java.awt.BasicStroke(lineWidth));
            g2d.draw(sr);
        }
    }

    /**
     * Draw a gradient-filled bar using Java's {@link GradientPaint}.
     *
     * <p>The gradient runs vertically from {@code colorBottom} at the base of the
     * bar to {@code colorTop} at the top, closely matching the visual produced by
     * matplotlib's {@code AxesImage}-based gradient emulation (the {@code Blues_r}
     * colourmap sweep in the {@code gradient_bar.py} gallery example).
     *
     * <p>The bar is outlined with a thin dark stroke after filling.
     */
    @Override
    public void drawGradientBar(double x, double y, double w, double h,
                                 Color colorBottom, Color colorTop) {
        double xRange = (xMax == xMin) ? 1.0 : (xMax - xMin);
        double yRange = (yMax == yMin) ? 1.0 : (yMax - yMin);

        // Screen rectangle (ty is the TOP of the bar in screen/y-down coords)
        double tx = transformX(x);
        double ty = transformY(y + h);   // top-left corner in screen space
        double tw = (w / xRange) * vw;
        double th = (h / yRange) * vh;

        Rectangle2D.Double sr = new Rectangle2D.Double(tx, ty, tw, th);

        // GradientPaint: start point = bottom of bar in screen (ty+th),
        //                end   point = top   of bar in screen (ty).
        // colorBottom maps to the bar base; colorTop maps to the bar tip.
        Point2D ptBottom = new Point2D.Double(tx, ty + th);
        Point2D ptTop    = new Point2D.Double(tx, ty);
        GradientPaint gp = new GradientPaint(
                (float) ptBottom.getX(), (float) ptBottom.getY(), colorBottom,
                (float) ptTop.getX(),    (float) ptTop.getY(),    colorTop);

        java.awt.Paint savedPaint = g2d.getPaint();
        g2d.setPaint(gp);
        g2d.fill(sr);
        g2d.setPaint(savedPaint);

        // Thin dark outline
        g2d.setColor(new Color(0x30, 0x30, 0x30));
        g2d.setStroke(new java.awt.BasicStroke(0.8f));
        g2d.draw(sr);
    }

    @Override
    public void drawText(String text, double x, double y, Color color, String fontName, int fontSize) {
        g2d.setColor(color);
        g2d.setFont(new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize));
        g2d.drawString(text, (float) transformX(x), (float) transformY(y));
    }

    @Override
    public void drawImage(double[][] data, Colormap colormap, Normalize normalize) {
        int rows = data.length, cols = data[0].length;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                img.setRGB(j, i, colormap.getColor(normalize.normalize(data[i][j])).getRGB());
        g2d.drawImage(img, (int)vx, (int)vy, (int)vw, (int)vh, null);
    }

    // ── Primitive drawing — absolute screen coordinates ───────────────────────

    /**
     * Draw a line between two <em>absolute screen coordinates</em>.
     *
     * <p>Callers (e.g. {@code Axes.draw()}) first obtain screen positions via
     * {@link #transformX}/{@link #transformY} and then pass those values here.
     * This method must <strong>not</strong> add the viewport offset a second time.
     *
     * @param x1        absolute screen x of the start point
     * @param y1        absolute screen y of the start point
     * @param x2        absolute screen x of the end point
     * @param y2        absolute screen y of the end point
     * @param color     stroke colour
     * @param lineWidth stroke width in pixels
     */
    @Override
    public void drawScreenLine(double x1, double y1, double x2, double y2,
                               Color color, float lineWidth) {
        g2d.setColor(color);
        g2d.setStroke(new java.awt.BasicStroke(lineWidth));
        g2d.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    /**
     * Draw a string at an <em>absolute screen coordinate</em>.
     *
     * <p>Callers pass coordinates already in screen space; this method does not
     * add the viewport offset.
     *
     * @param text     string to render
     * @param x        absolute screen x (left edge of text baseline)
     * @param y        absolute screen y (text baseline)
     * @param color    text colour
     * @param fontName font family name
     * @param fontSize point size
     */
    @Override
    public void drawScreenText(String text, double x, double y,
                               Color color, String fontName, int fontSize) {
        g2d.setColor(color);
        g2d.setFont(new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize));
        g2d.drawString(text, (float) x, (float) y);
    }

    /**
     * Draw a string at an <em>absolute screen position</em>, rotated about that point.
     *
     * <p>The text is centred both along its baseline and in the ascent direction at
     * ({@code x}, {@code y}) after the rotation is applied.  For a y-axis label,
     * pass {@code angleDegrees = -90} and set {@code x} to the desired distance left
     * of the left spine, {@code y} to the vertical mid-point of the axes.
     *
     * <p>The current {@link java.awt.geom.AffineTransform} is saved and restored so
     * that subsequent drawing calls are unaffected.
     *
     * @param text         string to render
     * @param x            absolute screen x of the rotation centre
     * @param y            absolute screen y of the rotation centre
     * @param angleDegrees rotation in degrees (negative = counterclockwise)
     * @param color        text colour
     * @param fontName     font family name
     * @param fontSize     point size
     */
    @Override
    public void drawRotatedScreenText(String text, double x, double y, double angleDegrees,
                                      Color color, String fontName, int fontSize) {
        java.awt.Font font = new java.awt.Font(fontName, java.awt.Font.PLAIN, fontSize);
        g2d.setFont(font);
        java.awt.FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth  = fm.stringWidth(text);
        int textAscent = fm.getAscent();

        java.awt.geom.AffineTransform saved = g2d.getTransform();
        g2d.setColor(color);
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angleDegrees));
        // Centre the string: move left by half its width, up by half its ascent
        g2d.drawString(text, -textWidth / 2.0f, textAscent / 2.0f);
        g2d.setTransform(saved);
    }

    /**
     * Draw a multi-segment polyline as a <em>single</em> {@link java.awt.BasicStroke} path,
     * so that the join style is correctly applied at every interior vertex.
     *
     * <p>This is critical for the JoinStyle demo: calling {@link #drawLine} once per
     * segment produces independent strokes whose endpoints meet at a point but have
     * no join geometry.  Building a {@link Path2D.Double} and drawing it with one
     * stroke causes AWT to apply the miter, round, or bevel join where segments meet.
     *
     * @param x           x data coordinates
     * @param y           y data coordinates (parallel to x)
     * @param color       stroke colour
     * @param lineWidth   stroke width in pixels
     * @param dashPattern dash array ({@code null} for solid)
     * @param joinStyle   {@code BasicStroke.JOIN_MITER}, {@code JOIN_ROUND}, or {@code JOIN_BEVEL}
     * @param capStyle    {@code BasicStroke.CAP_BUTT}, {@code CAP_ROUND}, or {@code CAP_SQUARE}
     * @param miterLimit  miter-length ratio threshold; values ≥ 1.0 (typical: 10.0)
     */
    @Override
    public void drawPolyline(double[] x, double[] y, Color color, float lineWidth,
                              float[] dashPattern, int joinStyle, int capStyle,
                              float miterLimit) {
        if (x == null || x.length < 2) return;
        // Build a single path in screen coordinates
        Path2D.Double path = new Path2D.Double();
        path.moveTo(transformX(x[0]), transformY(y[0]));
        for (int i = 1; i < x.length; i++) {
            path.lineTo(transformX(x[i]), transformY(y[i]));
        }
        g2d.setColor(color);
        java.awt.BasicStroke stroke;
        if (dashPattern != null && dashPattern.length > 0) {
            stroke = new java.awt.BasicStroke(lineWidth, capStyle, joinStyle,
                                               miterLimit, dashPattern, 0.0f);
        } else {
            stroke = new java.awt.BasicStroke(lineWidth, capStyle, joinStyle, miterLimit);
        }
        g2d.setStroke(stroke);
        g2d.draw(path);
    }

    @Override
    public void clear() {
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);
    }
}
