package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * Artist for displaying vector fields using quivers.
 */
public class QuiverArtist extends Artist {
    private double[][] X, Y, U, V;

    public QuiverArtist(double[][] X, double[][] Y, double[][] U, double[][] V) {
        this.X = X;
        this.Y = Y;
        this.U = U;
        this.V = V;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        
        // Render each vector as an arrow (line + small marker)
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[0].length; j++) {
                double x = X[i][j];
                double y = Y[i][j];
                double u = U[i][j];
                double v = V[i][j];
                
                // Draw the arrow line
                backend.drawLine(x, y, x + u, y + v, Color.BLACK, 1.0f, null, java.awt.BasicStroke.JOIN_MITER, java.awt.BasicStroke.CAP_BUTT);
                
                // Draw arrow head as a small marker
                backend.drawMarker(x + u, y + v, "o", 3.0f, Color.RED);
            }
        }
    }
}
