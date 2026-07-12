package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An Axes is the region of the figure that contains the actual plot.
 *
 * <p>It owns the coordinate system (data limits), tick marks, axis labels,
 * the axes title, and a z-ordered list of {@link Artist} objects (lines, patches, etc.).
 *
 * <p>This is the Java equivalent of {@code matplotlib.axes.Axes}.
 *
 * <h2>Coordinate flow</h2>
 * <ol>
 *   <li>Data coordinates (user values) are recorded as xMin/xMax/yMin/yMax.</li>
 *   <li>{@link #setPosition} records the normalised position on the figure.</li>
 *   <li>{@link #draw} tells the backend about the viewport and limits, then draws
 *       spines, ticks, labels, title, and finally all artists.</li>
 * </ol>
 */
public class Axes extends Artist {

    // ── Styling constants ────────────────────────────────────────────────────

    private static final Color  SPINE_COLOR      = new Color(0x20, 0x20, 0x20);
    private static final Color  TICK_COLOR       = new Color(0x20, 0x20, 0x20);
    private static final Color  GRID_COLOR       = new Color(0xCC, 0xCC, 0xCC);
    private static final Color  BACKGROUND_COLOR = Color.WHITE;
    private static final Color  LABEL_COLOR      = new Color(0x20, 0x20, 0x20);
    private static final String FONT_NAME        = "SansSerif";
    private static final int    TICK_LABEL_SIZE  = 9;
    private static final int    AXIS_LABEL_SIZE  = 11;
    private static final int    TITLE_SIZE       = 12;
    private static final float  SPINE_WIDTH      = 1.2f;
    private static final float  TICK_LENGTH      = 5.0f;
    private static final int    MAX_TICKS        = 6;

    // ── State ────────────────────────────────────────────────────────────────

    private final Figure figure;

    /** Normalised position on the figure canvas (x, y, w, h) in 0..1 range. */
    private java.awt.geom.Rectangle2D position =
        new java.awt.geom.Rectangle2D.Double(0.1, 0.1, 0.8, 0.8);

    /**
     * When {@code true} the position was set explicitly by the caller and
     * {@link com.marmanis.jMatplot.core.Figure#draw} will not overwrite it
     * with an automatically-computed grid position.
     */
    private boolean positionManual = false;

    private final List<Artist> artists = new ArrayList<>();

    // Data limits (expanded lazily as artists are added)
    private double xMin = Double.POSITIVE_INFINITY, xMax = Double.NEGATIVE_INFINITY;
    private double yMin = Double.POSITIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY;

    // Explicit overrides (set via setXLim / setYLim or ax.set())
    private Double xLimMin = null, xLimMax = null;
    private Double yLimMin = null, yLimMax = null;

    // Labels / title
    private String xlabel = "";
    private String ylabel = "";
    private String title  = "";

    // ── Tick customisation ───────────────────────────────────────────────────

    /** Custom X-tick positions (data coordinates). {@code null} = auto. */
    private double[] customXTickPositions = null;

    /** Custom X-tick labels (parallel to {@link #customXTickPositions}). {@code null} = numeric. */
    private String[] customXTickLabels = null;

    /** Custom Y-tick positions (data coordinates). {@code null} = auto. */
    private double[] customYTickPositions = null;

    /** Custom Y-tick labels (parallel to {@link #customYTickPositions}). {@code null} = numeric. */
    private String[] customYTickLabels = null;

    /** When {@code false}, X-tick marks are not drawn. */
    private boolean showXTickMarks   = true;

    /** When {@code false}, X-tick labels are not drawn. */
    private boolean showXTickLabels  = true;

    /** When {@code false}, Y-tick marks are not drawn. */
    private boolean showYTickMarks   = true;

    /** When {@code false}, Y-tick labels are not drawn. */
    private boolean showYTickLabels  = true;

    /** When {@code false}, grid lines are not drawn. */
    private boolean showGrid         = true;

    // ── Log-scale flags ──────────────────────────────────────────────────────

    /**
     * When {@code true}, the x axis uses a logarithmic (base-10) scale.
     * Equivalent to {@code ax.set_xscale('log')} in matplotlib.
     */
    private boolean logX = false;

    /**
     * When {@code true}, the y axis uses a logarithmic (base-10) scale.
     * Equivalent to {@code ax.set_yscale('log')} in matplotlib.
     */
    private boolean logY = false;

    // ── Title-only mode ──────────────────────────────────────────────────────

    /**
     * When {@code true}, {@link #draw} renders only a tinted background strip
     * and the axes title, with no spines, ticks, or artists.  Used for
     * section-header rows in combined figures (e.g. the markevery demo).
     */
    private boolean titleOnly = false;

    // ── Construction ─────────────────────────────────────────────────────────

    /**
     * Create a new Axes owned by the given Figure.
     *
     * @param figure the parent figure
     */
    public Axes(Figure figure) {
        this.figure = figure;
    }

    // ── Plot methods ─────────────────────────────────────────────────────────

    /**
     * Plot y versus x as lines (and/or markers).
     * Equivalent to {@code ax.plot(x, y)} in matplotlib.
     *
     * @param x x data array
     * @param y y data array (must have same length as x)
     * @return the created {@link Line2D} artist
     */
    public Line2D plot(double[] x, double[] y) {
        Line2D line = new Line2D(x, y);
        addArtist(line);
        updateLimits(x, y);
        return line;
    }

    /**
     * Create a scatter plot.
     * Equivalent to {@code ax.scatter(x, y)} in matplotlib.
     *
     * @param x x data array
     * @param y y data array
     * @return the created {@link Scatter} artist
     */
    public Scatter scatter(double[] x, double[] y) {
        Scatter scatter = new Scatter(x, y);
        addArtist(scatter);
        updateLimits(x, y);
        return scatter;
    }

    /**
     * Create a scatter plot with per-point marker sizes and colours.
     *
     * <p>This mirrors matplotlib's {@code ax.scatter(x, y, s=sizes, c=colors, alpha=alpha)}
     * where {@code s} is an array of per-point marker areas (in typographic points²) and
     * {@code c} is a colour array produced by mapping values through a colormap.
     *
     * <p>In jMatplot, {@code sizes} are <em>diameters in screen pixels</em>.  To convert
     * from matplotlib's points² format: {@code diameter_px = 2 * sqrt(s_pt2 / π)}.
     *
     * @param x      x data array
     * @param y      y data array (same length as x)
     * @param sizes  per-point marker diameters in screen pixels (same length as x)
     * @param colors per-point marker colours; alpha channel is overridden by {@code alpha}
     * @param alpha  global opacity for all markers (0.0 = transparent, 1.0 = opaque)
     * @return the created {@link Scatter} artist
     */
    public Scatter scatter(double[] x, double[] y, float[] sizes, Color[] colors, float alpha) {
        Scatter scatter = new Scatter(x, y);
        scatter.setPerSizes(sizes);
        scatter.setPerColors(colors);
        scatter.setGlobalAlpha(alpha);
        addArtist(scatter);
        updateLimits(x, y);
        return scatter;
    }

    /**
     * Make a bar plot with string category labels.
     *
     * @param labels category labels
     * @param values bar heights
     * @return the created {@link Bar} artist
     */
    public Bar bar(String[] labels, double[] values) {
        Bar bar = new Bar(labels, values);
        addArtist(bar);
        // Expand x limits by half-width so bar edges don't overflow the axes domain.
        // Also always include y=0 so that bar bottoms sit on the spine.
        double hw = bar.getWidth() / 2.0;
        updateLimits(new double[]{-hw, labels.length - 1 + hw}, new double[]{0});
        updateLimits(new double[]{0}, values);
        // Register the category labels as custom x-tick labels at integer positions.
        double[] pos = new double[labels.length];
        for (int i = 0; i < labels.length; i++) pos[i] = i;
        setXTicks(pos, labels);
        return bar;
    }

    /**
     * Make a bar plot with numeric x positions.
     *
     * @param x      x positions
     * @param values bar heights
     * @param width  bar width
     * @return the created {@link Bar} artist
     */
    public Bar bar(double[] x, double[] values, double width) {
        Bar bar = new Bar(x, values);
        bar.setWidth(width);
        addArtist(bar);
        double hw = width / 2.0;
        updateLimits(new double[]{x[0] - hw, x[x.length - 1] + hw}, new double[]{0});
        updateLimits(new double[]{0}, values);
        return bar;
    }

    /**
     * Make a gradient-filled bar chart.
     *
     * <p>Each bar is drawn with a vertical linear gradient running from
     * {@code colorBottom} at the base to {@code colorTop} at the tip —
     * equivalent to the {@code AxesImage}-based gradient emulation used in
     * matplotlib's {@code gradient_bar.py} gallery example.
     *
     * @param x      x centre positions in data coordinates
     * @param values bar heights
     * @param width  bar width in data units
     * @return the created {@link GradientBar} artist
     */
    public GradientBar gradientBar(double[] x, double[] values, double width) {
        GradientBar gb = new GradientBar(x, values, width);
        addArtist(gb);
        double hw = width / 2.0;
        updateLimits(new double[]{x[0] - hw, x[x.length - 1] + hw}, new double[]{0});
        updateLimits(new double[]{0}, values);
        return gb;
    }

    /**
     * Make a stacked bar plot.
     *
     * @param x      x positions
     * @param values bar heights
     * @param width  bar width
     * @param bottom bottom offsets for stacking
     * @return the created {@link Bar} artist
     */
    public Bar bar(double[] x, double[] values, double width, double[] bottom) {
        Bar bar = new Bar(x, values);
        bar.setWidth(width);
        bar.setBottom(bottom);
        addArtist(bar);
        double hw = width / 2.0;
        // Include bar outer edges and the stacking baseline
        double[] tops = new double[values.length];
        double yBase = 0;
        for (int i = 0; i < values.length; i++) {
            double b = (bottom != null) ? bottom[i] : 0;
            tops[i] = b + values[i];
            yBase = Math.min(yBase, b);
        }
        updateLimits(new double[]{x[0] - hw, x[x.length - 1] + hw}, new double[]{yBase});
        updateLimits(new double[]{0}, tops);
        return bar;
    }

    /**
     * Make a horizontal bar plot.
     *
     * @param labels category labels
     * @param values bar widths
     * @return the created {@link BarH} artist
     */
    public BarH barh(String[] labels, double[] values) {
        BarH bar = new BarH(labels, values);
        addArtist(bar);
        double hh = bar.getHeight() / 2.0;
        // Include bar outer edges and x=0 baseline
        updateLimits(new double[]{0}, new double[]{-hh, labels.length - 1 + hh});
        updateLimits(values, new double[]{0});
        // Register the category labels as custom y-tick labels at integer positions.
        double[] pos = new double[labels.length];
        for (int i = 0; i < labels.length; i++) pos[i] = i;
        setYTickLabels(pos, labels);
        return bar;
    }

    /**
     * Make a horizontal bar plot with numeric y positions.
     *
     * @param y      y positions
     * @param values bar widths
     * @param height bar height
     * @return the created {@link BarH} artist
     */
    public BarH barh(double[] y, double[] values, double height) {
        BarH bar = new BarH(y, values);
        bar.setHeight(height);
        addArtist(bar);
        double hh = height / 2.0;
        updateLimits(new double[]{0}, new double[]{y[0] - hh, y[y.length - 1] + hh});
        updateLimits(values, new double[]{0});
        return bar;
    }

    /** Place a legend on the Axes. */
    public Legend legend() {
        Legend legend = new Legend(this, artists);
        addArtist(legend);
        return legend;
    }

    /**
     * Add a text annotation.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param s text string
     * @return the created {@link Text} artist
     */
    public Text text(double x, double y, String s) {
        Text t = new Text(s, x, y);
        addArtist(t);
        return t;
    }

    /**
     * Annotate a data point.
     *
     * @param s annotation text
     * @param x x coordinate
     * @param y y coordinate
     * @return the created {@link Text} artist
     */
    public Text annotate(String s, double x, double y) {
        return text(x, y, s);
    }

    /**
     * Fill the area between two horizontal curves.
     *
     * <p>Equivalent to {@code ax.fill_between(x, y1, y2)} in matplotlib.
     *
     * @param x  x coordinates
     * @param y1 first curve y values
     * @param y2 second curve y values
     * @return the created {@link FillBetween} artist
     */
    public FillBetween fillBetween(double[] x, double[] y1, double[] y2) {
        FillBetween fill = new FillBetween(x, y1, y2);
        addArtist(fill);
        updateLimits(x, y1);
        updateLimits(x, y2);
        return fill;
    }

    /**
     * Fill the area between a curve and a constant horizontal boundary.
     *
     * <p>Equivalent to {@code ax.fill_between(x, y1, scalar)} in matplotlib,
     * e.g. {@code ax.fill_between(x, y1, 1)} fills between {@code y1} and
     * the horizontal line {@code y = 1}.
     *
     * @param x      x coordinates
     * @param y1     curve y values
     * @param scalar constant y value for the second boundary
     * @return the created {@link FillBetween} artist
     */
    public FillBetween fillBetween(double[] x, double[] y1, double scalar) {
        double[] y2 = new double[x.length];
        java.util.Arrays.fill(y2, scalar);
        return fillBetween(x, y1, y2);
    }

    /**
     * Fill the area between a curve and {@code y = 0}.
     *
     * <p>Equivalent to {@code ax.fill_between(x, y1)} in matplotlib (where
     * {@code y2} defaults to zero).
     *
     * @param x  x coordinates
     * @param y1 curve y values
     * @return the created {@link FillBetween} artist
     */
    public FillBetween fillBetween(double[] x, double[] y1) {
        return fillBetween(x, y1, 0.0);
    }

    // ── Stackplot ─────────────────────────────────────────────────────────────

    /**
     * Create a stacked area chart (equivalent to {@code ax.stackplot()} in matplotlib).
     *
     * <p>Each layer in {@code ys} is stacked on top of the previous one.  The filled
     * polygon for layer {@code k} spans from the cumulative sum up to layer {@code k-1}
     * (the "lower" boundary) to the cumulative sum up to layer {@code k} (the "upper"
     * boundary).  Each polygon is drawn as a {@link FillBetween} artist and added to
     * this axes.
     *
     * <p>Python equivalent:
     * <pre>{@code
     * ax.stackplot(x, ys, labels=labels, alpha=alpha, baseline=baseline)
     * }</pre>
     *
     * <h3>Baseline modes</h3>
     * <ul>
     *   <li>{@code "zero"} — the stack grows upward from y = 0 (default stackplot).</li>
     *   <li>{@code "wiggle"} — the stack is vertically centred so the total area is
     *       balanced around y = 0, producing a streamgraph.  The baseline at each x
     *       is {@code -0.5 * total(x)}, where {@code total(x)} is the sum of all
     *       series values at that position.</li>
     * </ul>
     *
     * @param x        shared x coordinates (same length as each row of {@code ys})
     * @param ys       data layers — {@code ys[k]} holds the y values for the k-th series
     * @param labels   legend labels, one per layer (may be {@code null}; shorter arrays
     *                 are accepted — extra layers receive no label)
     * @param alpha    global opacity applied to every layer (0.0 transparent – 1.0 opaque);
     *                 matplotlib's default stackplot alpha is 0.8
     * @param baseline {@code "zero"} for a standard stackplot or {@code "wiggle"} for
     *                 a centred streamgraph
     * @return list of {@link FillBetween} artists, one per layer ordered bottom-to-top
     */
    public List<FillBetween> stackplot(double[] x, double[][] ys,
                                       String[] labels, float alpha, String baseline) {
        int n = x.length;
        int k = ys.length;

        // Compute per-position baseline
        double[] base = new double[n];
        if ("wiggle".equals(baseline)) {
            // Centre the stack: base[i] = -0.5 * sum_of_all_series[i]
            for (int i = 0; i < n; i++) {
                double total = 0.0;
                for (double[] y : ys) total += y[i];
                base[i] = -0.5 * total;
            }
        }
        // "zero" baseline: base[] remains all 0.0

        // matplotlib tab10 default colour cycle (first five entries)
        Color[] defaultColors = {
            new Color(0x1F, 0x77, 0xB4),  // C0  tab:blue
            new Color(0xFF, 0x7F, 0x0E),  // C1  tab:orange
            new Color(0x2C, 0xA0, 0x2C),  // C2  tab:green
            new Color(0xD6, 0x27, 0x28),  // C3  tab:red
            new Color(0x94, 0x67, 0xBD),  // C4  tab:purple
        };
        int alphaInt = Math.max(0, Math.min(255, Math.round(alpha * 255)));

        List<FillBetween> layers = new ArrayList<>();
        double[] lower = base.clone();

        for (int layer = 0; layer < k; layer++) {
            // upper[i] = lower[i] + ys[layer][i]  (this layer's top boundary)
            double[] upper = new double[n];
            for (int i = 0; i < n; i++) upper[i] = lower[i] + ys[layer][i];

            Color c = defaultColors[layer % defaultColors.length];
            // Pass a clone of lower so each FillBetween owns independent boundary arrays
            FillBetween fb = new FillBetween(x, lower.clone(), upper);
            fb.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alphaInt));
            if (labels != null && layer < labels.length && labels[layer] != null) {
                fb.setLabel(labels[layer]);
            }
            addArtist(fb);
            updateLimits(x, upper);
            updateLimits(x, lower);
            layers.add(fb);

            lower = upper;  // this layer's top becomes the next layer's bottom
        }
        return layers;
    }

    /**
     * Create a stacked area chart using default alpha (0.8) and {@code "zero"} baseline.
     *
     * @param x      shared x coordinates
     * @param ys     data layers (one row per series)
     * @param labels legend labels, one per layer (may be {@code null})
     * @return list of {@link FillBetween} artists, bottom-to-top
     */
    public List<FillBetween> stackplot(double[] x, double[][] ys, String[] labels) {
        return stackplot(x, ys, labels, 0.8f, "zero");
    }

    /**
     * Create a stacked area chart with no legend labels.
     *
     * @param x  shared x coordinates
     * @param ys data layers (one row per series)
     * @return list of {@link FillBetween} artists, bottom-to-top
     */
    public List<FillBetween> stackplot(double[] x, double[][] ys) {
        return stackplot(x, ys, null, 0.8f, "zero");
    }

    // ── Filled polygon ────────────────────────────────────────────────────────

    /**
     * Draw a filled polygon defined by vertex arrays {@code x} and {@code y}.
     *
     * <p>This is the Java equivalent of {@code ax.fill(x, y)} in matplotlib.
     * The polygon is closed automatically (last vertex connected back to first)
     * and filled with the default tab-blue colour.  Use the fluent setters on
     * the returned {@link FilledPolygon} to override fill colour, edge colour,
     * and line width:
     *
     * <pre>{@code
     * // default fill
     * ax.fill(x, y);
     *
     * // custom colours
     * ax.fill(x, y).setFaceColor(LIGHTSALMON)
     *               .setEdgeColor(ORANGERED)
     *               .setLineWidth(3f);
     *
     * // outline only (facecolor='none')
     * ax.fill(x, y).setFaceColor(null)
     *               .setEdgeColor(PURPLE)
     *               .setLineWidth(3f);
     * }</pre>
     *
     * @param x x coordinates of the polygon vertices
     * @param y y coordinates of the polygon vertices (same length as {@code x})
     * @return the created {@link FilledPolygon} artist for further configuration
     */
    public FilledPolygon fill(double[] x, double[] y) {
        FilledPolygon fp = new FilledPolygon(x, y);
        addArtist(fp);
        updateLimits(x, y);
        return fp;
    }

    /**
     * Plot a line with perpendicular tick marks (hash-marks) drawn at regular
     * screen-space intervals along the path.
     *
     * <p>This is the jMatplot equivalent of matplotlib's
     * {@code ax.plot(..., path_effects=[patheffects.withTickedStroke(...)])}.
     * The tick marks are computed in screen-pixel coordinates so they remain
     * visually uniform regardless of the data range or axis aspect ratio.
     *
     * <p>The returned {@link TickedLine} exposes setters for all tick parameters
     * (color, line width, spacing, tick length, angle offset) and the inherited
     * {@link Artist#setLabel} for legend support.
     *
     * @param x x data array (at least 2 points)
     * @param y y data array (same length as x)
     * @return the created {@link TickedLine} artist
     */
    public TickedLine plotTicked(double[] x, double[] y) {
        TickedLine tl = new TickedLine(x, y);
        addArtist(tl);
        updateLimits(x, y);
        return tl;
    }

    /**
     * Plot a multi-segment path with full control over join style.
     *
     * <p>Unlike {@link #plot(double[], double[])} which uses {@link Line2D} and
     * draws each segment independently (losing join-style geometry at shared
     * vertices), this method creates a {@link Polyline} artist that draws the
     * entire path as a single {@link java.awt.BasicStroke} — which means the
     * AWT renderer correctly applies miter, round, or bevel joins at each vertex.
     *
     * <p>Equivalent to drawing a matplotlib {@code Line2D} with a custom
     * {@code solid_joinstyle} property.
     *
     * @param x x data array (at least 2 points)
     * @param y y data array (same length as x)
     * @return the created {@link Polyline} artist (configure color, width, join style, etc.)
     */
    public Polyline plotPath(double[] x, double[] y) {
        Polyline p = new Polyline(x, y);
        addArtist(p);
        updateLimits(x, y);
        return p;
    }

    /** Plot horizontal lines at each y from xmin to xmax. */
    public void hlines(double[] y, double xmin, double xmax) {
        for (double val : y) plot(new double[]{xmin, xmax}, new double[]{val, val});
    }

    /** Plot vertical lines at each x from ymin to ymax. */
    public void vlines(double[] x, double ymin, double ymax) {
        for (double val : x) plot(new double[]{val, val}, new double[]{ymin, ymax});
    }

    /**
     * Draw a collection of parallel event tick marks.
     * Equivalent to {@code ax.eventplot(positions, colors=colors,
     * lineoffsets=lineOffsets, linelengths=lineLengths, orientation=orientation)}
     * in matplotlib.
     *
     * @param positions   jagged array; {@code positions[i]} holds the event
     *                    values for row {@code i}
     * @param colors      per-row color, same length as {@code positions}
     * @param lineOffsets per-row offset (centre of each row's segments),
     *                    same length as {@code positions}
     * @param lineLengths per-row segment length, same length as {@code positions}
     * @param orientation {@code "horizontal"} (default) or {@code "vertical"}
     * @return the created {@link EventPlot} artist
     */
    public EventPlot eventplot(double[][] positions, Color[] colors, double[] lineOffsets,
                                double[] lineLengths, String orientation) {
        EventPlot ep = new EventPlot(positions);
        ep.setColors(colors);
        ep.setLineOffsets(lineOffsets);
        ep.setLineLengths(lineLengths);
        ep.setOrientation(orientation);
        addArtist(ep);

        boolean vertical = "vertical".equals(orientation);
        double dataMin = Double.POSITIVE_INFINITY, dataMax = Double.NEGATIVE_INFINITY;
        for (double[] row : positions) for (double v : row) {
            dataMin = Math.min(dataMin, v);
            dataMax = Math.max(dataMax, v);
        }
        double offMin = Double.POSITIVE_INFINITY, offMax = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < lineOffsets.length; i++) {
            double half = lineLengths[i] / 2.0;
            offMin = Math.min(offMin, lineOffsets[i] - half);
            offMax = Math.max(offMax, lineOffsets[i] + half);
        }
        if (vertical) {
            updateLimits(new double[]{offMin, offMax}, new double[]{dataMin, dataMax});
        } else {
            updateLimits(new double[]{dataMin, dataMax}, new double[]{offMin, offMax});
        }
        return ep;
    }

    /**
     * Draw a collection of parallel event tick marks using the default
     * horizontal orientation.
     *
     * @param positions   jagged array; {@code positions[i]} holds the event
     *                    values for row {@code i}
     * @param colors      per-row color, same length as {@code positions}
     * @param lineOffsets per-row offset, same length as {@code positions}
     * @param lineLengths per-row segment length, same length as {@code positions}
     * @return the created {@link EventPlot} artist
     */
    public EventPlot eventplot(double[][] positions, Color[] colors, double[] lineOffsets,
                                double[] lineLengths) {
        return eventplot(positions, colors, lineOffsets, lineLengths, "horizontal");
    }

    /**
     * Display a 2-D array as an image.
     *
     * @param data      2-D data array
     * @param colormap  colormap to apply
     * @param normalize normalisation to apply
     * @return the created {@link ImageArtist}
     */
    public ImageArtist imshow(double[][] data, Colormap colormap, Normalize normalize) {
        ImageArtist img = new ImageArtist(data, colormap, normalize);
        addArtist(img);
        updateLimits(new double[]{0, data[0].length}, new double[]{0, data.length});
        return img;
    }

    /** {@code imshow} with default colormap and normalisation. */
    public ImageArtist imshow(double[][] data) {
        return imshow(data, new Colormap(Color.BLUE, Color.YELLOW), new LinearNorm(0, 1));
    }

    /**
     * Create a pseudocolor plot.
     *
     * @param X         x grid
     * @param Y         y grid
     * @param C         color data
     * @param colormap  colormap
     * @param normalize normalisation
     * @return the created {@link PcolormeshArtist}
     */
    public PcolormeshArtist pcolormesh(double[][] X, double[][] Y, double[][] C,
                                       Colormap colormap, Normalize normalize) {
        PcolormeshArtist pcm = new PcolormeshArtist(X, Y, C, colormap, normalize);
        addArtist(pcm);
        updateLimits(X, Y);
        return pcm;
    }

    /**
     * Draw contour lines.
     *
     * @param Z      2-D data
     * @param levels contour levels
     * @return the created {@link ContourArtist}
     */
    public ContourArtist contour(double[][] Z, double[] levels) {
        ContourArtist c = new ContourArtist(Z, levels, false);
        addArtist(c);
        return c;
    }

    /** Draw filled contours. */
    public ContourArtist contourf(double[][] Z) {
        ContourArtist c = new ContourArtist(Z, new double[]{0.0}, true);
        addArtist(c);
        return c;
    }

    /**
     * Draw a 2-D vector field (quiver plot).
     *
     * @param X x positions
     * @param Y y positions
     * @param U u components
     * @param V v components
     * @return the created {@link QuiverArtist}
     */
    public QuiverArtist quiver(double[][] X, double[][] Y, double[][] U, double[][] V) {
        QuiverArtist q = new QuiverArtist(X, Y, U, V);
        addArtist(q);
        return q;
    }

    /** Add a colorbar (stub — not yet rendered). */
    public void colorbar(Object pcm, String label) { /* placeholder */ }

    /** Plot an unstructured triangular grid (stub). */
    public TriArtist tricontour(double[] x, double[] y, double[] z) { return null; }

    // ── Artist management ────────────────────────────────────────────────────

    /**
     * Add an Artist to this Axes. Artists are kept sorted by z-order.
     *
     * @param artist the artist to add
     */
    public void addArtist(Artist artist) {
        artists.add(artist);
        artists.sort(Comparator.comparingInt(Artist::getZorder));
    }

    // ── Limit helpers ────────────────────────────────────────────────────────

    private void updateLimits(double[] x, double[] y) {
        for (double v : x) { xMin = Math.min(xMin, v); xMax = Math.max(xMax, v); }
        for (double v : y) { yMin = Math.min(yMin, v); yMax = Math.max(yMax, v); }
    }

    private void updateLimits(double[][] X, double[][] Y) {
        for (double[] row : X) for (double v : row) { xMin = Math.min(xMin, v); xMax = Math.max(xMax, v); }
        for (double[] row : Y) for (double v : row) { yMin = Math.min(yMin, v); yMax = Math.max(yMax, v); }
    }

    /**
     * Effective xMin, honouring explicit limits and adding padding.
     *
     * <p>For a linear axis a 5 % linear pad is added; for a log axis a 10 %
     * multiplicative factor is used ({@code xMin * 0.9}) so the padded limit
     * stays strictly positive, which is required for the log transform.
     */
    private double effectiveXMin() {
        if (xLimMin != null) return xLimMin;
        if (logX && xMin > 0) return xMin * 0.9;
        double range = (xMax == xMin) ? 1.0 : (xMax - xMin);
        return xMin - 0.05 * range;
    }

    /** Effective xMax with symmetric padding. */
    private double effectiveXMax() {
        if (xLimMax != null) return xLimMax;
        if (logX && xMax > 0) return xMax * 1.1;
        double range = (xMax == xMin) ? 1.0 : (xMax - xMin);
        return xMax + 0.05 * range;
    }

    /** Effective yMin with axis-type-appropriate padding. */
    private double effectiveYMin() {
        if (yLimMin != null) return yLimMin;
        if (logY && yMin > 0) return yMin * 0.9;
        double range = (yMax == yMin) ? 1.0 : (yMax - yMin);
        return yMin - 0.05 * range;
    }

    /** Effective yMax with axis-type-appropriate padding. */
    private double effectiveYMax() {
        if (yLimMax != null) return yLimMax;
        if (logY && yMax > 0) return yMax * 1.1;
        double range = (yMax == yMin) ? 1.0 : (yMax - yMin);
        return yMax + 0.05 * range;
    }

    // ── Rendering ────────────────────────────────────────────────────────────

    /**
     * Draw this Axes and all its artists.
     *
     * <p>Drawing order:
     * <ol>
     *   <li>Background fill</li>
     *   <li>Grid lines</li>
     *   <li>All artist objects (lines, patches, etc.) in z-order</li>
     *   <li>Spines (axis borders)</li>
     *   <li>Ticks and tick labels</li>
     *   <li>Axis labels and title</li>
     * </ol>
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;

        // ── Title-only mode (section-header strips) ──────────────────────────
        // A titleOnly axes renders just a tinted background and a centred section
        // heading — no spines, ticks, or data artists.  It exists so that combined
        // figures (e.g. the markevery demo) can label each section group visually.
        if (titleOnly) {
            backend.setViewport(position.getX(), position.getY(),
                                position.getWidth(), position.getHeight());
            // Neutral limits: entire unit-square maps to the strip viewport.
            backend.setLimits(0, 1, 0, 1);
            // Reset log flags so the drawRectangle call uses linear coords (0..1).
            backend.setLogScales(false, false);
            // Tinted background strip
            backend.drawRectangle(
                new java.awt.geom.Rectangle2D.Double(0, 0, 1, 1),
                null, new Color(0xE8, 0xE8, 0xF0), 0);
            if (!title.isEmpty()) {
                // Centre the label in the strip using screen coords
                double sx0 = backend.transformX(0);
                double sx1 = backend.transformX(1);
                // Y: strip bottom (data y=0) and top (data y=1); average = centre
                double sy0 = backend.transformY(0); // larger screen-y value (bottom)
                double sy1 = backend.transformY(1); // smaller screen-y value (top)
                double sxCenter = (sx0 + sx1) / 2.0;
                double syCenter = (sy0 + sy1) / 2.0;
                double titleX = sxCenter - title.length() * 4.5;
                backend.drawScreenText(title, titleX, syCenter + 5,
                                       new Color(0x1a, 0x1a, 0x70),
                                       FONT_NAME, TITLE_SIZE + 2);
            }
            return;
        }

        // Skip only when there is truly nothing to draw: no artist ever expanded
        // the auto-range AND the caller didn't supply explicit limits on both axes
        // (e.g. via setXLim/setYLim) to define a viewport on their own. Checking
        // the raw auto-range field alone would wrongly skip axes whose only
        // artists are Text annotations, since Text never updates xMin/xMax/yMin/yMax.
        boolean hasData = xMin != Double.POSITIVE_INFINITY;
        boolean hasExplicitLimits = xLimMin != null && xLimMax != null && yLimMin != null && yLimMax != null;
        if (!hasData && !hasExplicitLimits) return;

        double exMin = effectiveXMin(), exMax = effectiveXMax();
        double eyMin = effectiveYMin(), eyMax = effectiveYMax();

        // Tell the backend our normalised viewport, data limits, and scale type.
        // setLogScales must be called after setLimits so the backend has the
        // correct xMin/xMax/yMin/yMax before any transform calls.
        backend.setViewport(position.getX(), position.getY(),
                            position.getWidth(), position.getHeight());
        backend.setLimits(exMin, exMax, eyMin, eyMax);
        backend.setLogScales(logX, logY);

        // ── 1. Background ────────────────────────────────────────────────────
        backend.drawRectangle(
            new java.awt.geom.Rectangle2D.Double(exMin, eyMin, exMax - exMin, eyMax - eyMin),
            null, BACKGROUND_COLOR, 0);

        // ── 2. Grid lines ────────────────────────────────────────────────────
        // Use log-spaced ticks on logarithmic axes (matching matplotlib's LogLocator).
        double[] xTicks = (customXTickPositions != null) ? customXTickPositions
                        : logX ? niceLogTicks(exMin, exMax) : niceTicks(exMin, exMax, MAX_TICKS);
        double[] yTicks = logY ? niceLogTicks(eyMin, eyMax) : niceTicks(eyMin, eyMax, MAX_TICKS);

        if (showGrid) {
            for (double xt : xTicks) {
                if (xt < exMin || xt > exMax) continue;
                backend.drawLine(xt, eyMin, xt, eyMax, GRID_COLOR, 0.6f, null,
                                 java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
            }
            for (double yt : yTicks) {
                if (yt < eyMin || yt > eyMax) continue;
                backend.drawLine(exMin, yt, exMax, yt, GRID_COLOR, 0.6f, null,
                                 java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
            }
        }

        // ── 3. Artists (clipped to the data area) ────────────────────────────
        // Activate the viewport clip before drawing any data artist so that
        // lines, markers, and patches cannot escape the axes boundary.  This
        // is essential when explicit axis limits are set (e.g. a zoomed plot)
        // because data points outside the visible range map to screen coordinates
        // that are far outside the axes — without clipping they would overwrite
        // neighbouring panels.  The clip is deactivated after all artists are
        // drawn so that spines, tick marks, and labels can be positioned in the
        // margin area without being truncated.
        backend.setDataClip(true);
        for (Artist a : artists) a.draw(backend);
        backend.setDataClip(false);

        // ── 4. Spines ────────────────────────────────────────────────────────
        // bottom
        backend.drawLine(exMin, eyMin, exMax, eyMin, SPINE_COLOR, SPINE_WIDTH, null,
                         java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
        // top
        backend.drawLine(exMin, eyMax, exMax, eyMax, SPINE_COLOR, SPINE_WIDTH, null,
                         java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
        // left
        backend.drawLine(exMin, eyMin, exMin, eyMax, SPINE_COLOR, SPINE_WIDTH, null,
                         java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
        // right
        backend.drawLine(exMax, eyMin, exMax, eyMax, SPINE_COLOR, SPINE_WIDTH, null,
                         java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);

        // ── 5. Ticks and tick labels ─────────────────────────────────────────
        // Use absolute screen coords via transformX/Y, then pass to drawScreenLine/Text
        // (drawScreenLine/Text do NOT add viewport offset — they expect absolute coords)
        double sxMin  = backend.transformX(exMin);
        double sxMax  = backend.transformX(exMax);
        double syMin  = backend.transformY(eyMin);
        double syMax  = backend.transformY(eyMax);

        java.awt.FontMetrics fm = null; // we approximate character widths numerically

        // X ticks (bottom spine) — use custom positions/labels when provided
        for (int ti = 0; ti < xTicks.length; ti++) {
            double xt = xTicks[ti];
            if (xt < exMin || xt > exMax) continue;
            double sx = backend.transformX(xt);
            if (showXTickMarks) {
                backend.drawScreenLine(sx, syMin, sx, syMin + TICK_LENGTH, TICK_COLOR, 1.0f);
            }
            if (showXTickLabels) {
                String raw = (customXTickLabels != null && ti < customXTickLabels.length)
                             ? customXTickLabels[ti] : formatTick(xt);
                // Support multiline labels (split on \n); strip basic LaTeX delimiters.
                String[] lines = raw.split("\n", -1);
                double lineY = syMin + TICK_LENGTH + 11;
                for (String line : lines) {
                    String rendered = renderTickLabel(line);
                    double labelX = sx - rendered.length() * 3.0; // crude centring
                    backend.drawScreenText(rendered, labelX, lineY,
                                           LABEL_COLOR, FONT_NAME, TICK_LABEL_SIZE);
                    lineY += TICK_LABEL_SIZE + 2; // advance one line height
                }
            }
        }

        // Y ticks (left spine) — use custom positions/labels when provided
        double[] effectiveYTicks = (customYTickPositions != null) ? customYTickPositions : yTicks;
        for (int ti = 0; ti < effectiveYTicks.length; ti++) {
            double yt = effectiveYTicks[ti];
            if (yt < eyMin || yt > eyMax) continue;
            double sy = backend.transformY(yt);
            if (showYTickMarks) {
                backend.drawScreenLine(sxMin - TICK_LENGTH, sy, sxMin, sy, TICK_COLOR, 1.0f);
            }
            // Pick the label: custom string when available, otherwise numeric
            if (showYTickLabels) {
                String label = (customYTickLabels != null && ti < customYTickLabels.length)
                               ? customYTickLabels[ti] : formatTick(yt);
                double labelX = sxMin - TICK_LENGTH - label.length() * 6.0 - 2;
                backend.drawScreenText(label, labelX, sy + 4, LABEL_COLOR, FONT_NAME, TICK_LABEL_SIZE);
            }
        }

        // ── 6. Axis labels and title ─────────────────────────────────────────
        double centerSX = (sxMin + sxMax) / 2.0;
        double centerSY = (syMin + syMax) / 2.0;

        if (!xlabel.isEmpty()) {
            double labelX = centerSX - xlabel.length() * 3.5;
            backend.drawScreenText(xlabel, labelX, syMin + TICK_LENGTH + 26,
                                   LABEL_COLOR, FONT_NAME, AXIS_LABEL_SIZE);
        }

        if (!ylabel.isEmpty()) {
            // Rotate 90° counterclockwise so the label reads bottom-to-top, parallel to the axis.
            // Position the centre of the rotated text to the left of the tick-label column.
            // TICK_LENGTH (5 px) + approx tick-label column width (40 px) + gap (4 px) + half
            // font-height (~6 px) gives a comfortable clearance from the spine for any label length.
            double labelX = sxMin - TICK_LENGTH - 50;
            backend.drawRotatedScreenText(ylabel, labelX, centerSY,
                                          -90.0, LABEL_COLOR, FONT_NAME, AXIS_LABEL_SIZE);
        }

        if (!title.isEmpty()) {
            double titleX = centerSX - title.length() * 3.5;
            backend.drawScreenText(title, titleX, syMax - 6,
                                   LABEL_COLOR, FONT_NAME, TITLE_SIZE);
        }
    }

    // ── Tick helpers ─────────────────────────────────────────────────────────

    /**
     * Generate "nice" tick positions within [lo, hi] — at most {@code maxTicks} ticks,
     * snapped to clean multiples (1, 2, 2.5, 5, 10, ...).
     *
     * <p>Equivalent to matplotlib's AutoLocator behaviour.
     *
     * @param lo       lower data bound
     * @param hi       upper data bound
     * @param maxTicks maximum number of ticks
     * @return sorted array of tick positions
     */
    static double[] niceTicks(double lo, double hi, int maxTicks) {
        if (hi <= lo) return new double[]{lo};
        double range   = hi - lo;
        double rough   = range / maxTicks;
        double mag     = Math.pow(10, Math.floor(Math.log10(rough)));
        double[] niceSteps = {1, 2, 2.5, 5, 10};
        double step = mag;
        for (double ns : niceSteps) {
            if (mag * ns >= rough) { step = mag * ns; break; }
        }
        double start = Math.ceil(lo / step) * step;
        List<Double> ticks = new ArrayList<>();
        for (double t = start; t <= hi + 1e-10 * step; t += step) {
            ticks.add(t);
        }
        double[] arr = new double[ticks.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = ticks.get(i);
        return arr;
    }

    /**
     * Generate tick positions for a logarithmic axis.
     *
     * <p>Ticks are placed at values of the form {@code m × 10^e} where {@code m ∈ {1, 2, 5}}
     * and {@code e} ranges across powers of ten that fall within [{@code lo}, {@code hi}].
     * This matches matplotlib's default {@code LogLocator} with {@code subs=(1, 2, 5)}.
     *
     * <p>Falls back to {@link #niceTicks} if either bound is non-positive.
     *
     * @param lo lower data bound (must be &gt; 0 for log to apply)
     * @param hi upper data bound (must be &gt; 0 for log to apply)
     * @return sorted array of tick positions
     */
    static double[] niceLogTicks(double lo, double hi) {
        if (lo <= 0 || hi <= 0 || hi <= lo) return niceTicks(lo, hi, MAX_TICKS);
        List<Double> ticks = new ArrayList<>();
        int startExp = (int) Math.floor(Math.log10(lo));
        int endExp   = (int) Math.ceil (Math.log10(hi));
        for (int e = startExp; e <= endExp; e++) {
            for (double m : new double[]{1.0, 2.0, 5.0}) {
                double t = m * Math.pow(10, e);
                // Small tolerance to include boundary values that are exact powers
                if (t >= lo * 0.999 && t <= hi * 1.001) {
                    ticks.add(t);
                }
            }
        }
        if (ticks.isEmpty()) return niceTicks(lo, hi, MAX_TICKS);
        // Deduplicate and sort (the generation order is already sorted, but be safe)
        double[] arr = new double[ticks.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = ticks.get(i);
        return arr;
    }

    /**
     * Format a tick value compactly: integers without decimal point,
     * small decimals with 1–2 significant figures.
     *
     * @param v tick value
     * @return formatted string
     */
    static String formatTick(double v) {
        if (v == 0) return "0";
        if (v == Math.floor(v) && Math.abs(v) < 1e6) return String.valueOf((long) v);
        double abs = Math.abs(v);
        if (abs >= 0.1 && abs < 1000) return String.format("%.2g", v);
        return String.format("%.1e", v);
    }

    /**
     * Render a single tick-label line for display.
     *
     * <p>Strips LaTeX math delimiters ({@code $}) and converts a small set of
     * common LaTeX commands to their Unicode equivalents so that labels like
     * {@code "$\\mu=$3700.66g"} display as {@code "μ=3700.66g"}.
     *
     * @param raw one line of a (possibly multiline) tick label
     * @return display-ready string
     */
    static String renderTickLabel(String raw) {
        return raw
            .replace("\\mu",    "μ")   // μ
            .replace("\\sigma", "σ")   // σ
            .replace("\\alpha", "α")   // α
            .replace("\\beta",  "β")   // β
            .replace("$", "")               // strip remaining $ delimiters
            .trim();
    }

    // ── Accessors ────────────────────────────────────────────────────────────

    public double getXMin() { return effectiveXMin(); }
    public double getXMax() { return effectiveXMax(); }
    public double getYMin() { return effectiveYMin(); }
    public double getYMax() { return effectiveYMax(); }

    public void setXLabel(String label) { this.xlabel = label; }
    public void setYLabel(String label) { this.ylabel = label; }
    public void setTitle(String title)  { this.title  = title; }

    public String getXLabel() { return xlabel; }
    public String getYLabel() { return ylabel; }
    public String getTitle()  { return title;  }

    /** Explicitly set x-axis limits. */
    public void setXLim(double min, double max) { this.xLimMin = min; this.xLimMax = max; }

    /** Explicitly set y-axis limits. */
    public void setYLim(double min, double max) { this.yLimMin = min; this.yLimMax = max; }

    /**
     * Override the auto-generated X-tick positions and labels.
     *
     * <p>Equivalent to {@code ax.set_xticks(positions, labels)} in matplotlib.
     *
     * @param ticks  tick positions in data coordinates
     * @param labels string labels to display (may be {@code null} for numeric formatting)
     */
    public void setXTicks(double[] ticks, String[] labels) {
        this.customXTickPositions = ticks;
        this.customXTickLabels    = labels;
    }

    public java.awt.geom.Rectangle2D getPosition() { return position; }

    /**
     * Set the normalised position of this Axes and mark it as manually positioned.
     * Once set manually, {@link com.marmanis.jMatplot.core.Figure#draw} will not
     * overwrite the position with an auto-computed grid cell.
     *
     * @param pos rectangle {@code (x, y, width, height)} with values in 0–1
     */
    public void setPosition(java.awt.geom.Rectangle2D pos) {
        this.position = pos;
        this.positionManual = true;
    }

    /**
     * Set the position without raising the manual-positioning flag.
     * Called internally by {@link com.marmanis.jMatplot.core.Figure#draw}
     * during automatic grid layout; not intended for external use.
     *
     * @param pos rectangle in normalised figure coordinates
     */
    void setPositionAuto(java.awt.geom.Rectangle2D pos) {
        this.position = pos;
        // positionManual is deliberately NOT set here
    }

    /** @return {@code true} if the position was set explicitly via {@link #setPosition}. */
    public boolean isPositionManual() { return positionManual; }

    // ── Tick customisation accessors ─────────────────────────────────────────

    /**
     * Replace the auto-generated Y-tick positions and labels.
     *
     * <p>Equivalent to {@code ax.set_yticks(positions, labels)} in matplotlib.
     * The labels appear in place of the numeric tick values and are drawn to the
     * left of the left spine, making this the standard way to show a legend-style
     * row of named items (e.g. linestyles, colours, markers).
     *
     * @param positions Y data-coordinate of each tick
     * @param labels    text to render next to each tick (parallel array)
     */
    public void setYTickLabels(double[] positions, String[] labels) {
        this.customYTickPositions = positions;
        this.customYTickLabels    = labels;
    }

    /**
     * Show or hide the X-axis tick marks.
     * @param visible {@code false} to suppress the small tick lines on the bottom spine
     */
    public void setXTickMarksVisible(boolean visible)  { this.showXTickMarks  = visible; }

    /**
     * Show or hide the X-axis tick labels (the numeric values below the bottom spine).
     * @param visible {@code false} to suppress tick labels without removing tick marks
     */
    public void setXTickLabelsVisible(boolean visible) { this.showXTickLabels = visible; }

    /**
     * Show or hide the Y-axis tick marks.
     * @param visible {@code false} to suppress the small tick lines on the left spine
     */
    public void setYTickMarksVisible(boolean visible)  { this.showYTickMarks  = visible; }

    /**
     * Show or hide the Y-axis tick labels (the numeric values left of the left spine).
     * @param visible {@code false} to suppress tick labels without removing tick marks
     */
    public void setYTickLabelsVisible(boolean visible) { this.showYTickLabels = visible; }

    /**
     * Show or hide the grid lines.
     *
     * <p>When {@code false}, no grid lines are drawn over the axes background.
     * This is useful for clean subplots where the data geometry should be the
     * only visual element (e.g. the JoinStyle demo where the line shape itself
     * is the focus).
     *
     * @param visible {@code false} to disable grid lines
     */
    public void setGridVisible(boolean visible) { this.showGrid = visible; }

    // ── Log-scale accessors ──────────────────────────────────────────────────

    /**
     * Enable logarithmic (base-10) scaling on one or both axes.
     *
     * <p>Equivalent to calling {@code ax.set_xscale('log')} and/or
     * {@code ax.set_yscale('log')} in matplotlib.  All data values on a log axis
     * must be strictly positive; non-positive values are skipped by the transform
     * and will not be plotted correctly.
     *
     * <p>Log scaling changes both the coordinate mapping (so data are positioned
     * correctly on the log scale) and the tick locations (which are placed at
     * decade-related multiples via {@link #niceLogTicks}).
     *
     * @param logX {@code true} to use a log x axis
     * @param logY {@code true} to use a log y axis
     */
    public void setLogScale(boolean logX, boolean logY) {
        this.logX = logX;
        this.logY = logY;
    }

    // ── Title-only mode accessor ─────────────────────────────────────────────

    /**
     * Switch this Axes into title-only (section-header) mode.
     *
     * <p>In title-only mode {@link #draw} renders only a tinted background strip
     * and the text set via {@link #setTitle}, with no data, spines, or tick marks.
     * This is used to add visible section headings to combined multi-section figures
     * (e.g. the markevery demo which stacks four 3×3 grids into a single tall image).
     *
     * @param titleOnly {@code true} to enable header mode
     */
    public void setTitleOnly(boolean titleOnly) {
        this.titleOnly = titleOnly;
    }

    public List<Artist> getArtists() { return new ArrayList<>(artists); }
}
