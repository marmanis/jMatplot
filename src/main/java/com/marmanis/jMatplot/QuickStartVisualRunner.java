package com.marmanis.jMatplot;

import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.PlotPanel;
import javax.swing.*;
import java.awt.*;

/**
 * A Swing application that provides a visual runner for jMatplot Quick Start examples.
 */
public class QuickStartVisualRunner extends JFrame {
    private final PlotPanel plotPanel;

    public QuickStartVisualRunner() {
        setTitle("jMatplot Quick Start Visual Runner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // Plot area
        plotPanel = new PlotPanel(QuickStartExamples.simpleExample());
        add(plotPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        addButton(controlPanel, "Simple", () -> plotPanel.setFigure(QuickStartExamples.simpleExample()));
        addButton(controlPanel, "OO-Style", () -> plotPanel.setFigure(QuickStartExamples.ooStylePlot()));
        addButton(controlPanel, "Styling", () -> plotPanel.setFigure(QuickStartExamples.styling()));
        addButton(controlPanel, "Scatter", () -> plotPanel.setFigure(QuickStartExamples.scatter()));
        addButton(controlPanel, "Bar Chart", () -> plotPanel.setFigure(QuickStartExamples.barChart()));
        addButton(controlPanel, "Subplots", () -> plotPanel.setFigure(QuickStartExamples.subplots()));
        addButton(controlPanel, "Annotations", () -> plotPanel.setFigure(QuickStartExamples.annotations()));

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuickStartVisualRunner runner = new QuickStartVisualRunner();
            runner.setLocationRelativeTo(null);
            runner.setVisible(true);
        });
    }
}
