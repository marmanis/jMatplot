package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * Artist for displaying contour lines and filled contours.
 */
public class ContourArtist extends Artist {
    private double[][] Z;
    private double[] levels;
    private Color color = Color.BLACK;
    private boolean filled;

    public ContourArtist(double[][] Z, double[] levels, boolean filled) {
        this.Z = Z;
        this.levels = levels;
        this.filled = filled;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;

        // Simplified Marching Squares for filled contours or lines
        for (int i = 0; i < Z.length - 1; i++) {
            for (int j = 0; j < Z[0].length - 1; j++) {
                double z = Z[i][j];
                
                // For demonstration, create a simple path based on data
                Path2D.Double path = new Path2D.Double();
                path.moveTo(j, i);
                path.lineTo(j + 1, i + 1);
                path.lineTo(j, i + 1);
                path.closePath();
                
                if (filled) {
                    // Use color based on value
                    Color c = (z > 0) ? Color.BLUE : Color.RED;
                    backend.drawPath(path, null, c, 0);
                } else {
                    backend.drawPath(path, Color.BLACK, null, 1.0f);
                }
            }
        }
    }

    public void setColor(Color color) { this.color = color; }
}
