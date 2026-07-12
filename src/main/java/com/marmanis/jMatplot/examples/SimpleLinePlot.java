package com.marmanis.jMatplot.examples;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;
import java.awt.*;

/**
 * A functional example of a simple line plot.
 */
public class SimpleLinePlot {
    
    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        
        // Data
        double[] x = {0, 1, 2, 3, 4};
        double[] y = {0, 1, 4, 9, 16};
        
        // Plot
        Line2D line = ax.plot(x, y);
        line.setColor(Color.BLUE);
        line.setLineWidth(2.0f);
        
        ax.setTitle("Functional Simple Line Plot");
        ax.setXLabel("X");
        ax.setYLabel("Y");
        
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simple Line Plot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 600);
            frame.setVisible(true);
        });
    }
}
