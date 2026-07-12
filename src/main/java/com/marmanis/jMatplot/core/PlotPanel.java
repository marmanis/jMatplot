package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.SwingBackend;
import javax.swing.*;
import java.awt.*;

/**
 * A JPanel that renders a jMatplot Figure.
 */
public class PlotPanel extends JPanel {
    private Figure figure;

    public PlotPanel(Figure figure) {
        this.figure = figure;
        setBackground(Color.WHITE);
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (figure != null) {
            Graphics2D g2d = (Graphics2D) g;
            SwingBackend backend = new SwingBackend(g2d, getWidth(), getHeight());
            backend.clear();
            figure.draw(backend);
        }
    }
}
