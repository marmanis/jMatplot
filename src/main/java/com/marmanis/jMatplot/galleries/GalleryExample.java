package com.marmanis.jMatplot.galleries;

import com.marmanis.jMatplot.core.Figure;

/**
 * Marker interface for all jMatplot gallery examples.
 *
 * <p>Every class in the {@code galleries} package hierarchy implements this interface.
 * The contract mirrors the pattern used throughout the matplotlib gallery:
 * <ol>
 *   <li>Apply a style sheet ({@link com.marmanis.jMatplot.style.StyleSheet}).</li>
 *   <li>Generate data (via {@link com.marmanis.jMatplot.data.DataGenerator}).</li>
 *   <li>Create a {@link Figure} and add one or more {@link com.marmanis.jMatplot.core.Axes}.</li>
 *   <li>Configure the axes and return the figure.</li>
 * </ol>
 *
 * <p>Implementations are also usable as unit-test fixtures: call {@link #create()},
 * assert the returned figure is non-null and contains at least one axes.
 *
 * <p>Equivalent to the self-contained Python script pattern used by every file in
 * {@code galleries/gallery/} and {@code galleries/plot_types/}.
 */
public interface GalleryExample {

    /**
     * Build and return the fully configured {@link Figure} for this example.
     *
     * <p>This method must be side-effect free with respect to display — it must
     * <em>not</em> call {@code plt.show()} or open a window.  Callers decide how
     * to render or inspect the figure.
     *
     * @return a non-null, fully populated Figure
     */
    Figure create();
}
