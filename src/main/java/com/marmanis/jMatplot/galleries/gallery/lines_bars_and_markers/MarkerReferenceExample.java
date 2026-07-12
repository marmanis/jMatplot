package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Line2D;
import java.awt.Color;
/**
 * jMatplot equivalent of the matplotlib "Marker Reference" gallery example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/marker_reference.html">
 * marker_reference.html</a>
 *
 * <p>Call {@link #createFigure()} to obtain a configured {@link Figure}
 * that can be displayed with {@code fig.show()} or saved with
 * {@code fig.savefig(path, width, height)}.
 */
public class MarkerReferenceExample {

    /**
     * Build and return the marker-reference figure.
     * Renders one horizontal line per marker symbol with the symbol as a label.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        String[] markers = {"o", "s", "d", "^", "v", "+", "x"};
        Color[] cols = {new Color(0x1F77B4), new Color(0xFF7F0E), new Color(0x2CA02C),
                new Color(0xD62728), new Color(0x9467BD),
                new Color(0x8C564B), new Color(0xE377C2)};
        for (int i = 0; i < markers.length; i++) {
            Line2D l = ax.plot(new double[]{0, 1, 2}, new double[]{i, i, i});
            l.setColor(cols[i]);
            l.setMarker(markers[i]);
            l.setMarkerSize(10f);
            ax.text(-0.5, i - 0.1, markers[i]);
        }
        ax.setXLim(-0.8, 2.8);
        ax.setTitle("Marker Reference");
        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
