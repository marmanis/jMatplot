package com.marmanis.jMatplot.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AxesTest {

    @Test
    public void testPlotAndLimits() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        
        double[] x = {0, 1, 2};
        double[] y = {0, 1, 4};
        
        Line2D line = ax.plot(x, y);
        
        assertNotNull(line);
        // Limits are expanded by 5% padding
        // x range 2, padding 0.1 -> -0.1 to 2.1
        // y range 4, padding 0.2 -> -0.2 to 4.2
        assertTrue(ax.getXMin() < 0);
        assertTrue(ax.getXMax() > 2);
        assertTrue(ax.getYMin() < 0);
        assertTrue(ax.getYMax() > 4);
    }
}
