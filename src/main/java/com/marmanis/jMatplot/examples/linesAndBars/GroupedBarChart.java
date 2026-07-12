package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Grouped Bar Chart</b></p>
 * <p>Demonstrates a grouped bar chart.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/barchart_demo.html">Matplotlib Gallery</a></p>
 */
public class GroupedBarChart {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        String[] labels = {"G1", "G2", "G3", "G4", "G5"};
        double[] menMeans = {20, 35, 30, 35, 27};
        double[] womenMeans = {25, 32, 34, 20, 25};
        
        // Grouped bars: manual positioning for now
        double[] x = {0, 1, 2, 3, 4};
        double[] x2 = {0.3, 1.3, 2.3, 3.3, 4.3};
        
        Bar bar1 = ax.bar(x, menMeans, 0.3);
        bar1.setColor(Color.BLUE);
        bar1.setLabel("Men");
        
        Bar bar2 = ax.bar(x2, womenMeans, 0.3);
        bar2.setColor(Color.ORANGE);
        bar2.setLabel("Women");

        ax.setTitle("Grouped Bar Chart");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Grouped Bar Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
