package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * A text artist for placing labels on the plot.
 */
public class Text extends Artist {
    private final String text;
    private final double x;
    private final double y;
    private Color color = Color.BLACK;
    private String fontName = "SansSerif";
    private int fontSize = 12;

    public Text(String text, double x, double y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        backend.drawText(text, x, y, color, fontName, fontSize);
    }

    public void setColor(Color color) { this.color = color; }
    public void setFontSize(int size) { this.fontSize = size; }
}
