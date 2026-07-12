package com.marmanis.jMatplot;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.backend.Backend;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Replicates examples from https://matplotlib.org/stable/users/explain/quick_start.html
 * and ensures they are visually displayable.
 */
public class QuickStartTest {

    @Test
    public void testSimpleExample() {
        Figure fig = QuickStartExamples.simpleExample();
        Axes ax = fig.getAxes().get(0);
        assertEquals(1, fig.getAxes().size());
        assertEquals(1, ax.getXMin(), 0.5);
    }

    @Test
    public void testOOStylePlot() {
        Figure fig = QuickStartExamples.ooStylePlot();
        Axes ax = fig.getAxes().get(0);
        assertEquals(4, ax.getArtists().size()); // 3 lines + 1 legend
    }

    @Test
    public void testStyling() {
        Figure fig = QuickStartExamples.styling();
        Axes ax = fig.getAxes().get(0);
        Line2D l1 = (Line2D) ax.getArtists().get(0);
        assertNotNull(l1.getDashPattern());
    }

    @Test
    public void testScatter() {
        Figure fig = QuickStartExamples.scatter();
        Axes ax = fig.getAxes().get(0);
        assertTrue(ax.getXMax() >= 49);
    }

    @Test
    public void testBarChart() {
        Figure fig = QuickStartExamples.barChart();
        Axes ax = fig.getAxes().get(0);
        assertEquals(0, ax.getXMin(), 1.0);
    }

    @Test
    public void testSubplots() {
        Figure fig = QuickStartExamples.subplots();
        assertEquals(2, fig.getAxes().size());
    }

    @Test
    public void testAnnotations() {
        Figure fig = QuickStartExamples.annotations();
        Axes ax = fig.getAxes().get(0);
        assertEquals(3, ax.getArtists().size()); // 1 line + 2 texts
    }

    @Test
    public void testFullRenderingVerification() {
        Backend mockBackend = Mockito.mock(Backend.class);
        Figure fig = QuickStartExamples.ooStylePlot();
        fig.draw(mockBackend);
        
        Mockito.verify(mockBackend, Mockito.atLeastOnce()).setViewport(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        Mockito.verify(mockBackend, Mockito.atLeastOnce()).setLimits(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        Mockito.verify(mockBackend, Mockito.atLeastOnce()).drawLine(anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(), anyFloat(), any(), anyInt(), anyInt());
    }
}
