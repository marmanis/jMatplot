package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.data.GalleryData;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Line Plot</b></p>
 * <p>A simple line plot demonstrating basic plotting functionality.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/line_plot.html">Matplotlib Gallery</a></p>
 */
public class LinePlot {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        // Data
        double[] t = GalleryData.linspace(0, 2 * Math.PI, 100);
        double[] s = new double[100];
        for(int i=0; i<100; i++) s[i] = Math.sin(t[i]);
        
        // Plot
        ax.plot(t, s);
        
        ax.setTitle("Simple Line Plot");
        ax.setXLabel("Time (s)");
        ax.setYLabel("Voltage (mV)");
        
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Plot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
