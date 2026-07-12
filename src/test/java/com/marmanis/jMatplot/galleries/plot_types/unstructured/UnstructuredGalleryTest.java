package com.marmanis.jMatplot.galleries.plot_types.unstructured;

import com.marmanis.jMatplot.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all plot_types.unstructured gallery examples.
 * Verifies that each example creates a valid Figure with at least one Axes.
 */
public class UnstructuredGalleryTest {

    /** Verify TricontourPlot creates a valid Figure. */
    @Test
    public void testTricontourPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.unstructured.TricontourPlot().create();
        assertNotNull(fig, "TricontourPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "TricontourPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify TricontourfPlot creates a valid Figure. */
    @Test
    public void testTricontourfPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.unstructured.TricontourfPlot().create();
        assertNotNull(fig, "TricontourfPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "TricontourfPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify TripcolorPlot creates a valid Figure. */
    @Test
    public void testTripcolorPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.unstructured.TripcolorPlot().create();
        assertNotNull(fig, "TripcolorPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "TripcolorPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify TriplotPlot creates a valid Figure. */
    @Test
    public void testTriplotPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.unstructured.TriplotPlot().create();
        assertNotNull(fig, "TriplotPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "TriplotPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

}
