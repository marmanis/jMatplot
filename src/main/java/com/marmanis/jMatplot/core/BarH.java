package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * A horizontal bar plot artist.
 * This is the Java equivalent of matplotlib.container.BarContainer created by barh().
 */
public class BarH extends Artist {
    private double[] y;
    private final double[] values;
    private double[] left;
    private Color[] colors;
    private Color defaultColor = Color.BLUE;
    private double height = 0.8;

    public BarH(String[] labels, double[] values) {
        this.values = values;
        this.y = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.y[i] = i;
        }
    }

    public BarH(double[] y, double[] values) {
        this.y = y;
        this.values = values;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;

        for (int i = 0; i < values.length; i++) {
            double xi = (left != null) ? left[i] : 0;
            double yi = y[i] - height / 2.0;
            double wi = values[i];
            
            Color faceColor = (colors != null && colors.length > i) ? colors[i] : defaultColor;
            
            Rectangle2D.Double rect = new Rectangle2D.Double(xi, yi, wi, height);
            backend.drawRectangle(rect, Color.BLACK, faceColor, 1.0f);
        }
    }

    public void setColor(Color color)   { this.defaultColor = color; }
    public Color getColor()             { return defaultColor; }
    public Color getDefaultColor()      { return defaultColor; }
    public void setColors(Color[] colors) { this.colors = colors; }
    public void setHeight(double height) { this.height = height; }
    public double getHeight() { return height; }
    public void setLeft(double[] left) { this.left = left; }
}
