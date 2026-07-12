package com.marmanis.jMatplot;

import com.marmanis.jMatplot.core.*;
import java.awt.Color;
import java.util.Random;

/**
 * Encapsulates the plotting logic for Quick Start examples so they can be reused
 * by both tests and visual runners.
 */
public class QuickStartExamples {

    public static Figure simpleExample() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        double[] x = {1, 2, 3, 4};
        double[] y = {1, 4, 2, 3};
        ax.plot(x, y);
        ax.setTitle("Simple Example");
        return fig;
    }

    public static Figure ooStylePlot() {
        double[] x = new double[100];
        for (int i = 0; i < 100; i++) x[i] = i * 0.02;
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(x, x).setLabel("linear");
        double[] x2 = new double[100];
        for (int i = 0; i < 100; i++) x2[i] = x[i] * x[i];
        ax.plot(x, x2).setLabel("quadratic");
        double[] x3 = new double[100];
        for (int i = 0; i < 100; i++) x3[i] = x2[i] * x[i];
        ax.plot(x, x3).setLabel("cubic");
        ax.setXLabel("x label");
        ax.setYLabel("y label");
        ax.setTitle("OO Style Plot");
        ax.legend();
        return fig;
    }

    public static Figure styling() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        double[] x = {0, 1, 2, 3};
        Line2D l1 = ax.plot(x, new double[]{0, 1, 0, 1});
        l1.setColor(Color.BLUE);
        l1.setLineWidth(3.0f);
        l1.setDashPattern(new float[]{10.0f, 5.0f});
        Line2D l2 = ax.plot(x, new double[]{1, 0, 1, 0});
        l2.setColor(Color.ORANGE);
        l2.setMarker("o");
        ax.setTitle("Styling Example");
        return fig;
    }

    public static Figure scatter() {
        Random rand = new Random(19680801);
        double[] a = new double[50];
        double[] b = new double[50];
        for (int i = 0; i < 50; i++) {
            a[i] = i;
            b[i] = i + 10 * rand.nextGaussian();
        }
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.scatter(a, b);
        ax.setXLabel("entry a");
        ax.setYLabel("entry b");
        ax.setTitle("Scatter Plot");
        return fig;
    }

    public static Figure barChart() {
        String[] labels = {"turnips", "rutabaga", "cucumber", "pumpkins"};
        double[] values = {0.4, 0.9, 0.2, 0.7};
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.bar(labels, values);
        ax.setTitle("Bar Chart");
        return fig;
    }

    public static Figure subplots() {
        Figure fig = new Figure();
        Axes[][] axs = fig.subplots(1, 2);
        axs[0][0].plot(new double[]{0, 1}, new double[]{0, 1});
        axs[0][0].setTitle("Subplot 1");
        axs[0][1].scatter(new double[]{0, 1}, new double[]{1, 0});
        axs[0][1].setTitle("Subplot 2");
        fig.setTitle("Multiple Subplots");
        return fig;
    }

    public static Figure annotations() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        ax.plot(new double[]{0, 1, 2, 3}, new double[]{0, 1, 0, 1});
        ax.annotate("local max", 1, 1);
        ax.text(2, 0.5, "Label text");
        ax.setTitle("Annotations");
        return fig;
    }
}
