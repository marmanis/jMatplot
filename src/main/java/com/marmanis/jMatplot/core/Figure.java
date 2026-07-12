package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import com.marmanis.jMatplot.backend.SwingBackend;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * The Figure is the top-level container for all plot elements.
 * It manages one or more {@link Axes} and controls how they are laid out on the canvas.
 *
 * <p>This is the Java equivalent of {@code matplotlib.figure.Figure}.
 *
 * <h2>Layout model</h2>
 * <p>Each Axes receives a normalised position rectangle {@code (x, y, w, h)} where all
 * values are in the range 0–1 relative to the canvas.  The layout algorithm uses
 * fixed per-side margins so that room is left for tick labels, axis labels and the title:
 * <ul>
 *   <li>Left margin 13 % — room for y-axis tick labels</li>
 *   <li>Right margin 3 %</li>
 *   <li>Bottom margin 12 % — room for x-axis tick labels and x-label</li>
 *   <li>Top margin 8 % — room for axes title</li>
 * </ul>
 * These values are applied <em>per axes cell</em> in a grid layout, not to the whole figure.
 */
public class Figure extends Artist {

    // Per-cell margins (fraction of cell size)
    private static final double MARGIN_LEFT   = 0.13;
    private static final double MARGIN_RIGHT  = 0.03;
    private static final double MARGIN_BOTTOM = 0.12;
    private static final double MARGIN_TOP    = 0.08;

    /** All Axes managed by this figure. */
    private final List<Axes> axesList = new ArrayList<>();

    /** Figure-level suptitle (displayed above all subplots). */
    private String title = "";

    /** Create an empty Figure. */
    public Figure() {}

    // ── Axes creation ─────────────────────────────────────────────────────────

    /**
     * Add a single Axes to this figure.
     *
     * <p>The Axes will be positioned automatically by the grid-layout algorithm
     * in {@link #draw}. To place the Axes at a specific location, use
     * {@link #addAxes(double, double, double, double)} instead.
     *
     * @return the newly created Axes
     */
    public Axes addAxes() {
        Axes axes = new Axes(this);
        axesList.add(axes);
        return axes;
    }

    /**
     * Add an Axes at an explicit normalised position, bypassing the automatic
     * grid layout.  Equivalent to {@code fig.add_axes([x, y, width, height])}
     * in matplotlib.
     *
     * <p>All values are fractions of the figure canvas in the range 0–1, where
     * {@code y = 0} is the bottom of the figure (matplotlib convention).
     *
     * @param x      left edge fraction
     * @param y      bottom edge fraction
     * @param width  axes width fraction
     * @param height axes height fraction
     * @return the newly created Axes
     */
    public Axes addAxes(double x, double y, double width, double height) {
        Axes axes = new Axes(this);
        axes.setPosition(new java.awt.geom.Rectangle2D.Double(x, y, width, height));
        axesList.add(axes);
        return axes;
    }

    /**
     * Create a grid of {@code nrows × ncols} subplots with explicit cell positions.
     *
     * <p>Each subplot is placed using the same per-cell margins as the automatic
     * grid layout ({@link #MARGIN_LEFT}, {@link #MARGIN_RIGHT}, {@link #MARGIN_BOTTOM},
     * {@link #MARGIN_TOP}).  Because the positions are set explicitly, the subplots
     * are not touched again by the auto-layout logic in {@link #draw}, which makes
     * it safe to mix manually-positioned and auto-positioned axes in the same figure.
     *
     * <p>Equivalent to {@code fig, axs = plt.subplots(nrows, ncols)} in matplotlib.
     *
     * @param nrows number of rows (vertical divisions)
     * @param ncols number of columns (horizontal divisions)
     * @return 2-D Axes array indexed as {@code [row][col]},
     *         where {@code [0][0]} is the <em>top-left</em> subplot
     */
    public Axes[][] subplots(int nrows, int ncols) {
        double cellW = 1.0 / ncols;
        double cellH = 1.0 / nrows;
        Axes[][] grid = new Axes[nrows][ncols];
        for (int r = 0; r < nrows; r++) {
            for (int c = 0; c < ncols; c++) {
                double cellX = c * cellW;
                // y=0 is figure bottom; row 0 is the top row
                double cellY = 1.0 - (r + 1) * cellH;
                double axX = cellX + MARGIN_LEFT   * cellW;
                double axY = cellY + MARGIN_BOTTOM * cellH;
                double axW = cellW * (1.0 - MARGIN_LEFT   - MARGIN_RIGHT);
                double axH = cellH * (1.0 - MARGIN_BOTTOM - MARGIN_TOP);
                grid[r][c] = addAxes(axX, axY, axW, axH);
            }
        }
        return grid;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /**
     * Draw the figure and all its contained Axes onto the given backend.
     *
     * <p>The Axes are arranged in a grid whose dimensions are computed as the
     * smallest enclosing rectangle for the number of axes (e.g. 4 axes → 2×2).
     * Each cell is inset by the per-cell margins before being assigned to an Axes.
     *
     * @param backend the rendering backend
     */
    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        int n = axesList.size();
        if (n == 0) return;

        // Count axes that need automatic grid placement (i.e. not manually positioned).
        long autoCount = axesList.stream().filter(a -> !a.isPositionManual()).count();

        // Determine grid dimensions for the auto-placed axes only.
        int cols = (autoCount > 0) ? (int) Math.ceil(Math.sqrt(autoCount)) : 1;
        int rows = (autoCount > 0) ? (int) Math.ceil((double) autoCount / cols) : 1;

        // Cell size in normalised figure coordinates
        double cellW = 1.0 / cols;
        double cellH = 1.0 / rows;

        int autoIdx = 0;
        for (int i = 0; i < n; i++) {
            Axes ax = axesList.get(i);

            if (!ax.isPositionManual()) {
                // Place this Axes in the next available grid cell
                int row = autoIdx / cols;
                int col = autoIdx % cols;

                // Cell origin (bottom-left, matplotlib convention: y=0 is bottom)
                double cellX = col * cellW;
                double cellY = 1.0 - (row + 1) * cellH;   // flip rows

                // Inset by per-cell margins to leave room for labels/ticks
                double axX = cellX + MARGIN_LEFT   * cellW;
                double axY = cellY + MARGIN_BOTTOM * cellH;
                double axW = cellW * (1.0 - MARGIN_LEFT - MARGIN_RIGHT);
                double axH = cellH * (1.0 - MARGIN_BOTTOM - MARGIN_TOP);

                // Use the internal setter so the manual-positioning flag stays false
                ax.setPositionAuto(
                    new java.awt.geom.Rectangle2D.Double(axX, axY, axW, axH));
                autoIdx++;
            }
            // Manually-positioned axes already have their position set; just draw.
            ax.draw(backend);
        }
    }

    // ── Export ────────────────────────────────────────────────────────────────

    /**
     * Render this figure to an image file (PNG or JPG).
     *
     * <p>The output format is inferred from the file extension: {@code .jpg} / {@code .jpeg}
     * produce a JPEG; anything else (typically {@code .png}) produces a PNG.
     *
     * <p>This method is equivalent to {@code matplotlib.figure.Figure.savefig(path)}.
     *
     * <p><strong>Headless usage:</strong> works without a display when the JVM is started
     * with {@code -Djava.awt.headless=true}.
     *
     * @param path     output file path (format inferred from extension)
     * @param widthPx  image width in pixels
     * @param heightPx image height in pixels
     * @throws IOException if the file cannot be written
     */
    public void savefig(String path, int widthPx, int heightPx) throws IOException {
        String lower = path.toLowerCase();
        boolean isJpg = lower.endsWith(".jpg") || lower.endsWith(".jpeg");

        // JPG requires TYPE_INT_RGB (no alpha channel); PNG can use ARGB.
        int type = isJpg ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage img = new BufferedImage(widthPx, heightPx, type);

        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // White background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, widthPx, heightPx);

        SwingBackend backend = new SwingBackend(g2d, widthPx, heightPx);
        draw(backend);
        g2d.dispose();

        File outFile = new File(path);
        // Ensure parent directories exist
        if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();

        String fmtStr = isJpg ? "jpg" : "png";
        ImageIO.write(img, fmtStr, outFile);
    }

    /**
     * Render this figure to an image file at the default size of 800 × 600 pixels.
     *
     * @param path output file path (format inferred from extension)
     * @throws IOException if the file cannot be written
     */
    public void savefig(String path) throws IOException {
        savefig(path, 800, 600);
    }

    /**
     * Display this figure in a Swing JFrame.
     *
     * <p>Equivalent to {@code plt.show()} in matplotlib.
     * This method blocks until the window is closed.
     */
    public void show() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("jMatplot");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            PlotPanel panel = new PlotPanel(this);
            panel.setPreferredSize(new java.awt.Dimension(800, 600));
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** @return a copy of the list of Axes in this figure */
    public List<Axes> getAxes() { return new ArrayList<>(axesList); }

    /** @return the figure suptitle */
    public String getTitle() { return title; }

    /** @param title the figure suptitle */
    public void setTitle(String title) { this.title = title; }
}
