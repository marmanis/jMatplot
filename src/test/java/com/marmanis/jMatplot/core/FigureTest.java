package com.marmanis.jMatplot.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FigureTest {

    @Test
    public void testAddAxes() {
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        
        assertNotNull(ax);
        assertEquals(1, fig.getAxes().size());
        assertTrue(fig.getAxes().contains(ax));
    }

    @Test
    public void testTitle() {
        Figure fig = new Figure();
        fig.setTitle("My Figure");
        assertEquals("My Figure", fig.getTitle());
    }
}
