package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>JoinStyle</b></p>
 * <p>Demonstrates miter, round, and bevel join styles.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/joinstyle.html">Matplotlib Gallery</a></p>
 */
public class JoinStyle {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = {0, 1, 2};
        double[] y = {0, 1, 0};

        // Miter
        Line2D l1 = ax.plot(x, y);
        l1.setJoinStyle(BasicStroke.JOIN_MITER);
        l1.setLineWidth(15.0f);
        l1.setLabel("Miter");

        // Round
        Line2D l2 = ax.plot(x, new double[]{1, 2, 1});
        l2.setJoinStyle(BasicStroke.JOIN_ROUND);
        l2.setLineWidth(15.0f);
        l2.setLabel("Round");

        // Bevel
        Line2D l3 = ax.plot(x, new double[]{2, 3, 2});
        l3.setJoinStyle(BasicStroke.JOIN_BEVEL);
        l3.setLineWidth(15.0f);
        l3.setLabel("Bevel");

        ax.setTitle("Join Styles");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Join Style");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
