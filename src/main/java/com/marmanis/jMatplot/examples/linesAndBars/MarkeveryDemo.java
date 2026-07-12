package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.data.GalleryData;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Markevery Demo</b></p>
 * <p>Demonstrates how to use 'markevery' to control marker placement.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/markevery_demo.html">Matplotlib Gallery</a></p>
 */
public class MarkeveryDemo {

    public static Figure create() {
        Figure fig = new Figure();
        // 4x4 layout
        Axes[][] axes = fig.subplots(4, 4);

        double[] x = GalleryData.linspace(0, 10, 30);
        double[] y = new double[30];
        for (int i = 0; i < 30; i++) y[i] = Math.sin(x[i]);

        // Fill subplots with different markevery settings
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                int markevery = r * 4 + c + 1;
                Line2D line = axes[r][c].plot(x, y);
                line.setMarker("o");
                line.setMarkerSize(5.0f);
                line.setMarkevery(markevery);
                axes[r][c].setTitle("markevery=" + markevery);
            }
        }

        fig.setTitle("Markevery Demo");
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Markevery Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(1000, 1000);
            frame.setVisible(true);
        });
    }
}
