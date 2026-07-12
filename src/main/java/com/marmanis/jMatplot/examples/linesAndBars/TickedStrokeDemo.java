package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Ticked Stroke Demo</b></p>
 * <p>Demonstrates how to create a ticked line effect.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/tickedstroke_demo.html">Matplotlib Gallery</a></p>
 */
public class TickedStrokeDemo {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = {0, 1, 2, 3};
        double[] y = {0, 1, 0, 1};

        Line2D line = ax.plot(x, y);
        // Use a dash pattern to simulate ticks
        line.setDashPattern(new float[]{1.0f, 10.0f});
        line.setLineWidth(5.0f);
        line.setColor(Color.BLACK);

        ax.setTitle("Ticked Stroke Demo");
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ticked Stroke Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
