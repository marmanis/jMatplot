package com.marmanis.jMatplot.galleries.plot_types.basic;

import com.marmanis.jMatplot.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all plot_types.basic gallery examples.
 * Verifies that each example creates a valid Figure with at least one Axes.
 */
public class BasicGalleryTest {

    /** Verify PlotXY creates a valid Figure. */
    @Test
    public void testPlotXY() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.PlotXY().create();
        assertNotNull(fig, "PlotXY.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "PlotXY should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify ScatterPlot creates a valid Figure. */
    @Test
    public void testScatterPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.ScatterPlot().create();
        assertNotNull(fig, "ScatterPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ScatterPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify BarPlot creates a valid Figure. */
    @Test
    public void testBarPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.BarPlot().create();
        assertNotNull(fig, "BarPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "BarPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify StemPlot creates a valid Figure. */
    @Test
    public void testStemPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.StemPlot().create();
        assertNotNull(fig, "StemPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "StemPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify FillBetween creates a valid Figure. */
    @Test
    public void testFillBetween() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.FillBetween().create();
        assertNotNull(fig, "FillBetween.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "FillBetween should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify StackplotBasic creates a valid Figure. */
    @Test
    public void testStackplotBasic() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.StackplotBasic().create();
        assertNotNull(fig, "StackplotBasic.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "StackplotBasic should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify StairsPlot creates a valid Figure. */
    @Test
    public void testStairsPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.basic.StairsPlot().create();
        assertNotNull(fig, "StairsPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "StairsPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

}
