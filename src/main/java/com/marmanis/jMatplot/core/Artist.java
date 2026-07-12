package com.marmanis.jMatplot.core;

import com.marmanis.jMatplot.backend.Backend;

/**
 * The Artist is the abstract base class for everything visible on the Figure.
 * It handles common properties like visibility, zorder, and alpha.
 * This is the Java equivalent of matplotlib.artist.Artist.
 */
public abstract class Artist {
    /** Whether the artist is visible and should be drawn. */
    private boolean visible = true;
    
    /** The drawing order of the artist. Higher values are drawn on top. */
    private int zorder = 0;
    
    /** The transparency of the artist, from 0.0 (transparent) to 1.0 (opaque). */
    private float alpha = 1.0f;
    
    /** The label for this artist, used in legends. */
    private String label = "";

    /**
     * Draw the artist using the provided backend.
     * This is the main entry point for rendering the artist.
     * 
     * @param backend The rendering backend to use for drawing primitives.
     */
    public abstract void draw(Backend backend);

    /**
     * @return true if the artist is visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the visibility of the artist.
     * @param visible true to make visible, false to hide.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return The drawing order (z-order).
     */
    public int getZorder() {
        return zorder;
    }

    /**
     * Set the drawing order. Artists with higher z-order are drawn on top.
     * @param zorder The z-order value.
     */
    public void setZorder(int zorder) {
        this.zorder = zorder;
    }

    /**
     * @return The transparency (alpha) value.
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Set the transparency (alpha) value.
     * @param alpha Value between 0.0 and 1.0.
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * @return The label for this artist.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label for this artist.
     * @param label The label string.
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
