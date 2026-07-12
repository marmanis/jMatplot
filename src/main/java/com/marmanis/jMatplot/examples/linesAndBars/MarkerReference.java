package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Marker Reference</b></p>
 * <p>Demonstrates the different marker styles available in jMatplot.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/marker_reference.html">Matplotlib Gallery</a></p>
 */
public class MarkerReference {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        String[] markers = {"o", "s", "d"};
        String[] labels = {"circle", "square", "diamond"};

        for (int i = 0; i < markers.length; i++) {
            Line2D line = ax.plot(new double[]{0, 1}, new double[]{i, i});
            line.setMarker(markers[i]);
            line.setMarkerSize(12.0f);
            line.setLabel(labels[i]);
        }

        ax.setTitle("Marker Reference");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Marker Reference");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
