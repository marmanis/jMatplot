package com.marmanis.jMatplot.galleries.plot_types.stats;

import com.marmanis.jMatplot.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all plot_types.stats gallery examples.
 * Verifies that each example creates a valid Figure with at least one Axes.
 */
public class StatsGalleryTest {

    /** Verify HistPlot creates a valid Figure. */
    @Test
    public void testHistPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.HistPlot().create();
        assertNotNull(fig, "HistPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "HistPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify BoxPlot creates a valid Figure. */
    @Test
    public void testBoxPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.BoxPlot().create();
        assertNotNull(fig, "BoxPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "BoxPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify ErrorBar creates a valid Figure. */
    @Test
    public void testErrorBar() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.ErrorBar().create();
        assertNotNull(fig, "ErrorBar.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ErrorBar should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify ViolinPlot creates a valid Figure. */
    @Test
    public void testViolinPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.ViolinPlot().create();
        assertNotNull(fig, "ViolinPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ViolinPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify EventPlot creates a valid Figure. */
    @Test
    public void testEventPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.EventPlot().create();
        assertNotNull(fig, "EventPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "EventPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Hist2dPlot creates a valid Figure. */
    @Test
    public void testHist2dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.Hist2dPlot().create();
        assertNotNull(fig, "Hist2dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Hist2dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify HexbinPlot creates a valid Figure. */
    @Test
    public void testHexbinPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.HexbinPlot().create();
        assertNotNull(fig, "HexbinPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "HexbinPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify PiePlot creates a valid Figure. */
    @Test
    public void testPiePlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.PiePlot().create();
        assertNotNull(fig, "PiePlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "PiePlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify EcdfPlot creates a valid Figure. */
    @Test
    public void testEcdfPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.stats.EcdfPlot().create();
        assertNotNull(fig, "EcdfPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "EcdfPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

}
