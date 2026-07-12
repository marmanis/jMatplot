package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Path2D;

/**
 * Artist for displaying unstructured triangular grids.
 */
public class TriArtist extends Artist {
    private double[] x, y;
    private int[][] triangles;
    private Color color = Color.BLACK;

    public TriArtist(double[] x, double[] y, int[][] triangles) {
        this.x = x;
        this.y = y;
        this.triangles = triangles;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        
        for (int[] tri : triangles) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(x[tri[0]], y[tri[0]]);
            path.lineTo(x[tri[1]], y[tri[1]]);
            path.lineTo(x[tri[2]], y[tri[2]]);
            path.closePath();
            backend.drawPath(path, color, null, 1.0f);
        }
    }

    public void setColor(Color color) { this.color = color; }
}
