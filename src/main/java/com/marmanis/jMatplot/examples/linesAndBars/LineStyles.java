package com.marmanis.jMatplot.examples.linesAndBars;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.data.GalleryData;
import javax.swing.*;
import java.awt.*;

/**
 * <p><b>Line Styles</b></p>
 * <p>Demonstrates various line styles and colors.</p>
 * <p>See: <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/linestyles.html">Matplotlib Gallery</a></p>
 */
public class LineStyles {

    public static Figure create() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();

        double[] t = GalleryData.linspace(0, 10, 100);
        
        // Solid
        Line2D solid = ax.plot(t, new double[100]);
        solid.setColor(Color.BLUE);
        solid.setLabel("Solid");

        // Dashed
        Line2D dashed = ax.plot(t, new double[100]);
        dashed.setColor(Color.RED);
        dashed.setDashPattern(new float[]{10.0f, 5.0f});
        dashed.setLabel("Dashed");
        
        // Dotted
        Line2D dotted = ax.plot(t, new double[100]);
        dotted.setColor(Color.GREEN);
        dotted.setDashPattern(new float[]{2.0f, 2.0f});
        dotted.setLabel("Dotted");
        
        ax.setTitle("Line Styles Demo");
        ax.legend();
        return fig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Styles");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PlotPanel(create()));
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }
}
