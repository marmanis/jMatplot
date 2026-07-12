package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A legend artist that displays labels (with coloured line swatches) for the
 * labelled artists on an {@link Axes}.
 *
 * <p>This is a lightweight equivalent of {@code matplotlib.legend.Legend}.
 * Artists are eligible for the legend when they carry a non-empty label set
 * via {@link Artist#setLabel(String)}.
 *
 * <h2>Rendering</h2>
 * <p>Each entry shows:
 * <ol>
 *   <li>A short horizontal coloured line swatch (using the artist's colour).</li>
 *   <li>For {@link TickedLine} artists, two perpendicular tick marks on the swatch
 *       to indicate the ticked-stroke style.</li>
 *   <li>The label text immediately to the right of the swatch.</li>
 * </ol>
 * A near-white filled box with a thin border is drawn behind all entries.
 *
 * <h2>Placement</h2>
 * <p>The legend tries four candidate corner positions (upper-right, upper-left,
 * lower-left, lower-right) and places the box in the corner whose area contains
 * the fewest samples from all plotted paths.  This minimises visual overlap with
 * the plotted data in the common case.
 */
public class Legend extends Artist {

    /** Artists included in the legend (only those with a non-empty label). */
    private final List<Artist> artists;

    /**
     * All artists on the axes — used only for the placement density check so
     * the legend box can avoid regions that are visually occupied by lines.
     */
    private final List<Artist> allArtists;

    private final Axes axes;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Construct a Legend from the axes' complete artist list.
     *
     * @param axes    the axes that owns this legend
     * @param artists the complete artist list of the axes; only artists whose
     *                label is non-empty are shown in the legend, but the full
     *                list is used for the placement density check
     */
    public Legend(Axes axes, List<Artist> artists) {
        this.axes = axes;
        this.allArtists = new ArrayList<>(artists);   // full list for density
        this.artists    = new ArrayList<>();           // filtered for display
        for (Artist a : artists) {
            if (a.getLabel() != null && !a.getLabel().isEmpty()) {
                this.artists.add(a);
            }
        }
        setZorder(100); // draw on top of everything
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw the legend box and all its entries.
     *
     * <p>Coordinates are computed in data space and forwarded to the backend.
     * Tick marks for {@link TickedLine} swatches are placed in screen pixels via
     * {@link Backend#drawScreenLine}.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible() || artists.isEmpty()) return;

        final int n   = artists.size();
        double xMin   = axes.getXMin();
        double xMax   = axes.getXMax();
        double yMin   = axes.getYMin();
        double yMax   = axes.getYMax();
        double xRange = xMax - xMin;
        double yRange = yMax - yMin;

        // ── Legend box geometry (tight, in data units) ────────────────────────
        double swatchW = xRange * 0.08;
        double padX    = xRange * 0.015;
        double padY    = yRange * 0.020;
        double rowH    = yRange * 0.070;
        // Box: [padX] [swatchW] [padX] [textW] [padX]
        double textW   = xRange * 0.15;
        double boxW    = padX + swatchW + padX + textW + padX;
        double boxH    = padY + rowH * n + padY;

        // ── Pick the least-occupied corner ────────────────────────────────────
        double inset = 0.025;
        // Each entry: {boxLeft, boxBottom}
        double[][] corners = {
            // upper-right
            { xMax - boxW - inset * xRange,  yMax - boxH - inset * yRange },
            // upper-left
            { xMin + inset * xRange,          yMax - boxH - inset * yRange },
            // lower-left
            { xMin + inset * xRange,          yMin + inset * yRange         },
            // lower-right
            { xMax - boxW - inset * xRange,  yMin + inset * yRange         },
        };

        double bestCost = Double.MAX_VALUE;
        double boxLeft   = corners[0][0];
        double boxBottom = corners[0][1];

        for (double[] c : corners) {
            double bl = c[0], bb = c[1];
            double cost = sampleDensity(allArtists, bl, bb, bl + boxW, bb + boxH);
            if (cost < bestCost) {
                bestCost = cost;
                boxLeft   = bl;
                boxBottom = bb;
            }
        }
        double boxTop = boxBottom + boxH;

        // ── Background box ────────────────────────────────────────────────────
        Color bgColor     = new Color(0xF8, 0xF8, 0xF8); // near-white
        Color borderColor = new Color(0xC0, 0xC0, 0xC0); // light grey border
        backend.drawRectangle(
            new java.awt.geom.Rectangle2D.Double(boxLeft, boxBottom, boxW, boxH),
            borderColor, bgColor, 0.8f);

        // ── Entries ───────────────────────────────────────────────────────────
        // y-centre of first entry (top entry)
        double entryY = boxTop - padY - rowH * 0.5;

        for (Artist a : artists) {
            Color  swatchColor = resolveColor(a);
            String label       = a.getLabel();

            // Swatch line endpoints (data coords)
            double swatchX1 = boxLeft  + padX;
            double swatchX2 = swatchX1 + swatchW;

            if (a instanceof Scatter) {
                // Scatter swatches show a filled circle at the swatch centre,
                // mirroring how matplotlib renders scatter legend handles.
                double cx = (swatchX1 + swatchX2) / 2.0;
                backend.drawMarker(cx, entryY, "o", 8.0f, swatchColor);
            } else if (a instanceof FillBetween || a instanceof FilledPolygon) {
                // Filled-area swatches show a small filled rectangle.
                // For FilledPolygon with no fill (outline-only), draw the rect
                // as outline only so the swatch matches the plot appearance.
                double halfH = rowH * 0.30;
                Path2D.Double rectPath = new Path2D.Double();
                rectPath.moveTo(swatchX1, entryY - halfH);
                rectPath.lineTo(swatchX2, entryY - halfH);
                rectPath.lineTo(swatchX2, entryY + halfH);
                rectPath.lineTo(swatchX1, entryY + halfH);
                rectPath.closePath();
                if (a instanceof FilledPolygon) {
                    FilledPolygon fp = (FilledPolygon) a;
                    Color ec = fp.getEdgeColor();
                    Color fc = fp.getColor();  // may be null (no fill)
                    backend.drawPath(rectPath, ec, fc, fp.getLineWidth());
                } else {
                    backend.drawPath(rectPath, null, swatchColor, 0f);
                }
            } else {
                backend.drawLine(swatchX1, entryY, swatchX2, entryY,
                                 swatchColor, 1.5f, null,
                                 java.awt.BasicStroke.JOIN_MITER,
                                 java.awt.BasicStroke.CAP_BUTT);
                // For TickedLine artists, draw 2 tick marks on the swatch
                if (a instanceof TickedLine) {
                    drawSwatchTicks(backend, swatchX1, swatchX2, entryY,
                                    swatchColor, (TickedLine) a);
                }
            }

            // Label text
            backend.drawText(label,
                             swatchX2 + padX,
                             entryY + yRange * 0.010,
                             Color.BLACK, "SansSerif", 10);

            entryY -= rowH;
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Draw two small tick marks on a horizontal legend swatch in screen
     * coordinates, matching the style of the supplied {@link TickedLine}.
     *
     * <p>The ticks are centred perpendicular to the (horizontal) swatch at the
     * 30 % and 70 % positions along the swatch.  Exact angleDeg reproduction
     * is not attempted; the ticks are always vertical in screen space, which
     * is correct for a horizontal swatch.
     *
     * @param backend   renderer
     * @param swatchX1  left data-coord of swatch
     * @param swatchX2  right data-coord of swatch
     * @param swatchY   y data-coord of swatch
     * @param color     tick colour (same as swatch)
     * @param tl        the source {@link TickedLine}
     */
    private static void drawSwatchTicks(Backend backend,
                                        double swatchX1, double swatchX2, double swatchY,
                                        Color color, TickedLine tl) {
        double x1s = backend.transformX(swatchX1);
        double x2s = backend.transformX(swatchX2);
        double ys  = backend.transformY(swatchY);
        double swLen = x2s - x1s;

        // Half-tick height in screen pixels (smaller than the real ticks)
        double halfTick = Math.min(tl.getTickLengthPx() / 2.0, 5.0);
        float  tw       = Math.max(tl.getLineWidth(), 1.0f);

        for (double frac : new double[]{ 0.30, 0.70 }) {
            double txs = x1s + frac * swLen;
            backend.drawScreenLine(txs, ys - halfTick, txs, ys + halfTick, color, tw);
        }
    }

    /**
     * Estimate how much of the given data-coordinate box is occupied by the
     * paths of the supplied artists.  Each segment between consecutive data
     * points is sampled at {@code max(10, ceil(50 * euclidean_length))} equally
     * spaced positions; the count of positions that fall inside the box is
     * returned.  A higher count means more visual content in that region.
     *
     * <p>The method dispatches on artist type:
     * <ul>
     *   <li>{@link Line2D}, {@link TickedLine}, {@link Polyline} — path segments
     *       are sampled and positions inside the box are counted.</li>
     *   <li>{@link FillBetween} — <em>both</em> boundary curves are sampled.
     *       This is the key case for stacked area charts: without it, a tall
     *       filled band (e.g. the Asia region in a population stackplot) would
     *       score zero at every corner and the legend would land arbitrarily in
     *       the occupied upper-right instead of the empty upper-left.</li>
     *   <li>{@link Scatter} — each data point is tested directly.</li>
     * </ul>
     *
     * @param all  all artists on the axes (not just the labelled ones)
     * @param x1   left edge of candidate legend box (data coords)
     * @param y1   bottom edge of candidate legend box (data coords)
     * @param x2   right edge of candidate legend box (data coords)
     * @param y2   top edge of candidate legend box (data coords)
     * @return     total number of samples that fall inside the box
     */
    private static int sampleDensity(List<Artist> all,
                                     double x1, double y1, double x2, double y2) {
        int count = 0;
        for (Artist a : all) {
            if (a instanceof Line2D) {
                count += samplePath(((Line2D)     a).getXData(), ((Line2D)     a).getYData(), x1, y1, x2, y2);
            } else if (a instanceof TickedLine) {
                count += samplePath(((TickedLine) a).getXData(), ((TickedLine) a).getYData(), x1, y1, x2, y2);
            } else if (a instanceof Polyline) {
                count += samplePath(((Polyline)   a).getXData(), ((Polyline)   a).getYData(), x1, y1, x2, y2);
            } else if (a instanceof FillBetween) {
                // Sample both boundary curves so filled regions are visible to
                // the placement algorithm — critical for stackplot legends.
                FillBetween fb = (FillBetween) a;
                count += samplePath(fb.getX(), fb.getY1(), x1, y1, x2, y2);
                count += samplePath(fb.getX(), fb.getY2(), x1, y1, x2, y2);
            } else if (a instanceof Scatter) {
                // Scatter points are discrete — test each one directly.
                double[] xd = ((Scatter) a).getXData();
                double[] yd = ((Scatter) a).getYData();
                for (int i = 0; i < xd.length; i++) {
                    if (xd[i] >= x1 && xd[i] <= x2 && yd[i] >= y1 && yd[i] <= y2) count++;
                }
            } else if (a instanceof FilledPolygon) {
                // Sample the polygon boundary path
                count += samplePath(((FilledPolygon) a).getXData(),
                                    ((FilledPolygon) a).getYData(), x1, y1, x2, y2);
            }
        }
        return count;
    }

    /**
     * Sample a polyline and count how many positions fall inside the
     * axis-aligned bounding box {@code [x1, x2] × [y1, y2]}.
     *
     * <p>Each segment is sampled at {@code max(10, ceil(50 * euclidean_length))}
     * equally-spaced positions.  Segments with fewer than two points are skipped.
     *
     * @param xd  x coordinates of the path
     * @param yd  y coordinates of the path (same length as {@code xd})
     * @param x1  left edge of the test box
     * @param y1  bottom edge of the test box
     * @param x2  right edge of the test box
     * @param y2  top edge of the test box
     * @return    count of sampled positions that lie inside the box
     */
    private static int samplePath(double[] xd, double[] yd,
                                   double x1, double y1, double x2, double y2) {
        if (xd == null || xd.length < 2) return 0;
        int count = 0;
        for (int i = 0; i < xd.length - 1; i++) {
            double ddx    = xd[i + 1] - xd[i];
            double ddy    = yd[i + 1] - yd[i];
            double segLen = Math.sqrt(ddx * ddx + ddy * ddy);
            int    n      = Math.max(10, (int) Math.ceil(50.0 * segLen));
            for (int j = 0; j <= n; j++) {
                double t  = j / (double) n;
                double xi = xd[i] + t * ddx;
                double yi = yd[i] + t * ddy;
                if (xi >= x1 && xi <= x2 && yi >= y1 && yi <= y2) count++;
            }
        }
        return count;
    }

    /**
     * Extract the primary colour from an artist.
     * Falls back to dark grey for unrecognised types.
     *
     * @param a the artist
     * @return the artist's primary colour
     */
    private static Color resolveColor(Artist a) {
        if (a instanceof Line2D)       return ((Line2D)       a).getColor();
        if (a instanceof TickedLine)   return ((TickedLine)   a).getColor();
        if (a instanceof Polyline)     return ((Polyline)     a).getColor();
        if (a instanceof Scatter)      return ((Scatter)      a).getColor();
        if (a instanceof Bar)          return ((Bar)          a).getColor();
        if (a instanceof BarH)         return ((BarH)         a).getDefaultColor();
        if (a instanceof FillBetween)   return ((FillBetween)   a).getColor();
        if (a instanceof FilledPolygon) {
            Color c = ((FilledPolygon) a).getColor();
            return (c != null) ? c : ((FilledPolygon) a).getEdgeColor();
        }
        return new Color(0x40, 0x40, 0x40); // dark grey fallback
    }
}
