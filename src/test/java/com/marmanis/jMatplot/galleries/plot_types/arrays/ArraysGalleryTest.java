package com.marmanis.jMatplot.galleries.plot_types.arrays;

import com.marmanis.jMatplot.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all plot_types.arrays gallery examples.
 * Verifies that each example creates a valid Figure with at least one Axes.
 */
public class ArraysGalleryTest {

    /** Verify ImshowPlot creates a valid Figure. */
    @Test
    public void testImshowPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.ImshowPlot().create();
        assertNotNull(fig, "ImshowPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ImshowPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify PcolormeshPlot creates a valid Figure. */
    @Test
    public void testPcolormeshPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.PcolormeshPlot().create();
        assertNotNull(fig, "PcolormeshPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "PcolormeshPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify ContourPlot creates a valid Figure. */
    @Test
    public void testContourPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.ContourPlot().create();
        assertNotNull(fig, "ContourPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ContourPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify ContourfPlot creates a valid Figure. */
    @Test
    public void testContourfPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.ContourfPlot().create();
        assertNotNull(fig, "ContourfPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "ContourfPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify BarbsPlot creates a valid Figure. */
    @Test
    public void testBarbsPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.BarbsPlot().create();
        assertNotNull(fig, "BarbsPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "BarbsPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify QuiverPlot creates a valid Figure. */
    @Test
    public void testQuiverPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.QuiverPlot().create();
        assertNotNull(fig, "QuiverPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "QuiverPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify StreamplotPlot creates a valid Figure. */
    @Test
    public void testStreamplotPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.arrays.StreamplotPlot().create();
        assertNotNull(fig, "StreamplotPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "StreamplotPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

}
