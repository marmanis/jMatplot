package com.marmanis.jMatplot.galleries.plot_types.threed;

import com.marmanis.jMatplot.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all plot_types.threed gallery examples.
 * Verifies that each example creates a valid Figure with at least one Axes.
 */
public class ThreedGalleryTest {

    /** Verify Bar3dPlot creates a valid Figure. */
    @Test
    public void testBar3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Bar3dPlot().create();
        assertNotNull(fig, "Bar3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Bar3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify FillBetween3d creates a valid Figure. */
    @Test
    public void testFillBetween3d() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.FillBetween3d().create();
        assertNotNull(fig, "FillBetween3d.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "FillBetween3d should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Plot3dSimple creates a valid Figure. */
    @Test
    public void testPlot3dSimple() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Plot3dSimple().create();
        assertNotNull(fig, "Plot3dSimple.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Plot3dSimple should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Quiver3dPlot creates a valid Figure. */
    @Test
    public void testQuiver3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Quiver3dPlot().create();
        assertNotNull(fig, "Quiver3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Quiver3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Scatter3dPlot creates a valid Figure. */
    @Test
    public void testScatter3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Scatter3dPlot().create();
        assertNotNull(fig, "Scatter3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Scatter3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Stem3dPlot creates a valid Figure. */
    @Test
    public void testStem3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Stem3dPlot().create();
        assertNotNull(fig, "Stem3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Stem3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Surface3dPlot creates a valid Figure. */
    @Test
    public void testSurface3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Surface3dPlot().create();
        assertNotNull(fig, "Surface3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Surface3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Trisurf3dPlot creates a valid Figure. */
    @Test
    public void testTrisurf3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Trisurf3dPlot().create();
        assertNotNull(fig, "Trisurf3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Trisurf3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify VoxelsPlot creates a valid Figure. */
    @Test
    public void testVoxelsPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.VoxelsPlot().create();
        assertNotNull(fig, "VoxelsPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "VoxelsPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

    /** Verify Wireframe3dPlot creates a valid Figure. */
    @Test
    public void testWireframe3dPlot() {
        Figure fig = new com.marmanis.jMatplot.galleries.plot_types.threed.Wireframe3dPlot().create();
        assertNotNull(fig, "Wireframe3dPlot.create() should not return null");
        assertFalse(fig.getAxes().isEmpty(), "Wireframe3dPlot should have at least one Axes");
        assertNotNull(fig.getTitle());
    }

}
