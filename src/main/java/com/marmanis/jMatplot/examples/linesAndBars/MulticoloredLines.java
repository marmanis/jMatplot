package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Multicolored lines</b></p>
 * <p>Demonstrates creating a line with different colors for different segments.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/multicolored_line.html">Matplotlib Gallery</a></p>
 */
public class MulticoloredLines {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = new double[100];
        double[] y = new double[100];
        for (int i = 0; i < 100; i++) {
            x[i] = i;
            y[i] = Math.sin(i * 0.2);
        }

        // Segment the line into different colors
        for (int i = 0; i < 99; i++) {
            Color color = (y[i] > 0) ? Color.RED : Color.BLUE;
            Line2D line = ax.plot(new double[]{x[i], x[i+1]}, new double[]{y[i], y[i+1]});
            line.setColor(color);
        }

        ax.setTitle("Multicolored Line");
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Multicolored Lines");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
