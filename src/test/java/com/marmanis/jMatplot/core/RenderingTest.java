package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;

public class RenderingTest {

    @Test
    public void testRenderingPipeline() {
        Backend mockBackend = Mockito.mock(Backend.class);
        
        Figure fig = new Figure();
        Axes ax = fig.addAxes();
        
        double[] x = {0, 1};
        double[] y = {0, 1};
        ax.plot(x, y);
        
        fig.draw(mockBackend);
        
        // Verify that drawLine was called by the Line2D artist
        Mockito.verify(mockBackend, Mockito.atLeastOnce()).drawLine(
                anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(), anyFloat(), any(), anyInt(), anyInt()
        );
    }
}
