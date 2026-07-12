package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;
import java.awt.Color;

/**
 * Artist for displaying 2D images.
 */
public class ImageArtist extends Artist {
    private double[][] data;
    private Colormap colormap;
    private Normalize normalize;

    public ImageArtist(double[][] data, Colormap colormap, Normalize normalize) {
        this.data = data;
        this.colormap = colormap;
        this.normalize = normalize;
    }

    @Override
    public void draw(Backend backend) {
        if (!isVisible()) return;
        
        backend.drawImage(data, colormap, normalize);
    }
}
