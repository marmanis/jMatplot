package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.data.GalleryData;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Dashed line style configuration</b></p>
 * <p>Demonstrates how to configure the dash pattern of a line.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/dashed_line_style.html">Matplotlib Gallery</a></p>
 */
public class DashedLineStyle {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = GalleryData.linspace(0, 5, 6);
        double[] y1 = {0, 1, 0, 1, 0, 1};
        double[] y2 = {1, 2, 1, 2, 1, 2};

        // Dashed
        Line2D l1 = ax.plot(x, y1);
        l1.setDashPattern(new float[]{10.0f, 5.0f});
        l1.setLabel("Dashed (10, 5)");

        // Dotted
        Line2D l2 = ax.plot(x, y2);
        l2.setDashPattern(new float[]{2.0f, 2.0f});
        l2.setLabel("Dotted (2, 2)");

        ax.setTitle("Dashed Line Configuration");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dashed Line Style");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
