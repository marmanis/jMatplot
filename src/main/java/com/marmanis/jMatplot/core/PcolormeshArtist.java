package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * Artist for displaying 2D pseudocolor plots (pcolormesh).
 */
public class PcolormeshArtist extends Artist {
    private double[][] X, Y, C;
    private Colormap colormap;
    private Normalize normalize;

    public PcolormeshArtist(double[][] X, double[][] Y, double[][] C, Colormap colormap, Normalize normalize) {
        this.X = X;
        this.Y = Y;
        this.C = C;
        this.colormap = colormap;
        this.normalize = normalize;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        
        // Simplified pcolormesh: draw a rectangle for each grid cell
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C[0].length; j++) {
                double x = X[i][j];
                double y = Y[i][j];
                double val = normalize.normalize(C[i][j]);
                Color color = colormap.getColor(val);
                
                // Draw cell as a small rectangle
                backend.drawRectangle(new Rectangle2D.Double(x, y, 1, 1), null, color, 0);
            }
        }
    }
}
