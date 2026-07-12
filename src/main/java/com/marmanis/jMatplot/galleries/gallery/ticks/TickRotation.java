package com.marmanis.jMatplot.galleries.gallery.ticks;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import com.marmanis.jMatplot.galleries.GalleryExample;
import com.marmanis.jMatplot.style.StyleSheet;

/**
 * Rotating tick labels
 * <p>Rotate tick labels.</p>
 * <p>Java equivalent of {@code galleries/gallery/ticks/ticklabels_rotation.py}</p>
 *
 * @see com.marmanis.jMatplot.core.Axes
 * @see <a href="https://matplotlib.org/stable/gallery/ticks/ticklabels_rotation.html">matplotlib: Rotating tick labels</a>
 */
public class TickRotation implements GalleryExample {

    /**
     * Create and return the Figure for this example.
     * @return the configured Figure ready to render
     */
    @Override
    public Figure create() {
        StyleSheet.use(StyleSheet.MPL_GALLERY);

        // Make data
        double[] x = DataGenerator.linspace(0, 10, 50);
        double[] y = DataGenerator.apply(x, v -> Math.sin(v));

        // Plot
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(x, y);
        ax.setTitle("Rotating tick labels");
        ax.setXLabel("x");
        ax.setYLabel("y");

        return fig;
    }

    /**
     * Stand-alone entry point.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Figure fig = new TickRotation().create();
        System.out.println("Figure: " + fig.getTitle() + " (" + fig.getAxes().size() + " axes)");
    }
}
