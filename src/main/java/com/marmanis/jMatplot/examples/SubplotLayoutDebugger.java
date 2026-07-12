package com.marmanis.jMatplot.examples;

import com.marmanis.jMatplot.core.*;
import javax.swing.*;

/**
 * A debugger to isolate subplot layout and metadata rendering issues.
 */
public class SubplotLayoutDebugger {
    
    public static Figure create() {
        Figure fig = new Figure();
        // 2x1 layout
        Axes[][] axes = fig.subplots(2, 1);
        
        // Plot 1
        axes[0][0].plot(new double[]{0, 1}, new double[]{0, 1});
        axes[0][0].setTitle("Subplot 1");
        axes[0][0].setXLabel("X1");
        
        // Plot 2
        axes[1][0].scatter(new double[]{0.1, 0.5}, new double[]{0.5, 0.2});
        axes[1][0].setTitle("Subplot 2");
        axes[1][0].setYLabel("Y2");
        
        fig.setTitle("Subplot Layout Debugger");
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Subplot Layout Debugger");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 800);
            frame.setVisible(true);
        });
    }
}
