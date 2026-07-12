package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;

/**
 * <p><b>Infinite lines</b></p>
 * <p>Demonstrates plotting horizontal and vertical lines that span the entire axis.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/horizontal_and_vertical_lines.html">Matplotlib Gallery</a></p>
 */
public class InfiniteLines {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        // Data to define the limits
        ax.plot(new double[]{0, 10}, new double[]{0, 10});

        // Horizontal line
        ax.hlines(new double[]{5}, 0, 10);
        
        // Vertical line
        ax.vlines(new double[]{5}, 0, 10);

        ax.setTitle("Infinite Lines (hlines/vlines)");
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Infinite Lines");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
