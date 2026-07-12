package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>CapStyle</b></p>
 * <p>Demonstrates butt, round, and square cap styles.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/capstyle.html">Matplotlib Gallery</a></p>
 */
public class CapStyle {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] x = {0, 1};
        
        // Butt
        Line2D l1 = ax.plot(x, new double[]{0, 0});
        l1.setCapStyle(BasicStroke.CAP_BUTT);
        l1.setLineWidth(15.0f);
        l1.setLabel("Butt");

        // Round
        Line2D l2 = ax.plot(x, new double[]{1, 1});
        l2.setCapStyle(BasicStroke.CAP_ROUND);
        l2.setLineWidth(15.0f);
        l2.setLabel("Round");

        // Square (Projecting)
        Line2D l3 = ax.plot(x, new double[]{2, 2});
        l3.setCapStyle(BasicStroke.CAP_SQUARE);
        l3.setLineWidth(15.0f);
        l3.setLabel("Square");

        ax.setTitle("Cap Styles");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cap Style");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
