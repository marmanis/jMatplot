package com.marmanis.jMatplot.galleries.plot_types.threed;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import com.marmanis.jMatplot.galleries.GalleryExample;
import com.marmanis.jMatplot.style.StyleSheet;

/**
 * plot_surface(X, Y, Z)
 * <p>Plot a 3D surface.</p>
 * <p>Java equivalent of {@code galleries/plot_types/threed/surface3d_simple.py}</p>
 *
 * @see com.marmanis.jMatplot.core.Axes
 */
public class Surface3dPlot implements GalleryExample {

    /**
     * Create and return the Figure for this example.
     * @return the configured Figure ready to render
     */
    @Override
    public Figure create() {
        StyleSheet.use(StyleSheet.MPL_GALLERY);

        // Make data
        double[] x = DataGenerator.linspace(0, 10, 100);
        double[] y = DataGenerator.apply(x, v -> 4 + Math.sin(2 * v));

        // Plot
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(x, y);
        ax.setTitle("plot_surface(X, Y, Z)");

        return fig;
    }

    /**
     * Stand-alone entry point.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Figure fig = new Surface3dPlot().create();
        System.out.println("Figure: " + fig.getTitle() + " (" + fig.getAxes().size() + " axes)");
    }
}
