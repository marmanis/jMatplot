package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Axes;
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.data.DataGenerator;
import com.marmanis.jMatplot.data.Spectrum;

import java.awt.Color;

/**
 * jMatplot equivalent of the matplotlib "Spectrum representations" gallery
 * example.
 *
 * <p>See the full gallery at
 * <a href="https://matplotlib.org/stable/gallery/lines_bars_and_markers/spectrum_demo.html">
 * spectrum_demo.html</a>
 *
 * <p>Reproduces the real example's {@code subplot_mosaic} layout — a full
 * width "Signal" panel on top, with four spectrum panels below it in a 2×2
 * grid (Magnitude Spectrum, Log. Magnitude Spectrum, Phase Spectrum, Angle
 * Spectrum) — built on the spectral helpers in {@link Spectrum}, since
 * {@code Axes} has no native {@code magnitude_spectrum}/{@code
 * angle_spectrum}/{@code phase_spectrum} methods.
 *
 * <p>The signal is a 2 Hz sine wave plus colored noise (white noise passed
 * through a decaying-exponential FIR filter), exactly as in the original.
 */
public class SpectrumDemoExample {

    /** Pixel width of the exported image (square, matching mpl's {@code figsize=(7,7)}). */
    public static final int WIDTH = 700;
    /** Pixel height of the exported image. */
    public static final int HEIGHT = 700;

    /**
     * Build and return the spectrum-representations figure.
     *
     * @return configured Figure
     */
    public static Figure createFigure() {
        double dt = 0.01;
        double fs = 1.0 / dt;
        double[] t = DataGenerator.arange(0.0, 10.0, dt);
        int n = t.length;

        double[] noise = DataGenerator.normalRandom(0L, 0.0, 1.0, n);
        double[] r = DataGenerator.apply(t, v -> Math.exp(-v / 0.05));
        double[] coloredNoise = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int k = 0; k <= i; k++) sum += noise[k] * r[i - k];
            coloredNoise[i] = sum * dt;
        }

        double[] s = new double[n];
        for (int i = 0; i < n; i++) s[i] = 0.1 * Math.sin(4 * Math.PI * t[i]) + coloredNoise[i];

        Color c0 = new Color(0x1F77B4);
        Color c1 = new Color(0xFF7F0E);
        Color c2 = new Color(0x2CA02C);

        double padX = 0.07, padY = 0.06, signalGap = 0.11;
        double cellW = (1.0 - 3 * padX) / 2.0;
        double cellH = (1.0 - 3 * padY - signalGap) / 3.0;
        double rowBottom2 = padY;                             // phase / angle
        double rowBottom1 = 2 * padY + cellH;                 // magnitude / log_magnitude
        double rowBottom0 = 2 * padY + 2 * cellH + signalGap; // signal (top, spans both columns)
        double col0 = padX;
        double col1 = padX + cellW + padX;

        Figure fig = new Figure();

        Axes signal = fig.addAxes(col0, rowBottom0, 1.0 - 2 * padX, cellH);
        signal.plot(t, s).setColor(c0);
        signal.setTitle("Signal");
        signal.setXLabel("Time (s)");
        signal.setYLabel("Amplitude");

        double[][] mag = Spectrum.magnitudeSpectrum(s, fs);
        Axes magnitude = fig.addAxes(col0, rowBottom1, cellW, cellH);
        magnitude.plot(mag[0], mag[1]).setColor(c1);
        magnitude.setTitle("Magnitude Spectrum");

        double[][] magDb = Spectrum.magnitudeSpectrumDb(s, fs);
        Axes logMagnitude = fig.addAxes(col1, rowBottom1, cellW, cellH);
        logMagnitude.plot(magDb[0], magDb[1]).setColor(c1);
        logMagnitude.setTitle("Log. Magnitude Spectrum");

        double[][] phase = Spectrum.phaseSpectrum(s, fs);
        Axes phaseAx = fig.addAxes(col0, rowBottom2, cellW, cellH);
        phaseAx.plot(phase[0], phase[1]).setColor(c2);
        phaseAx.setTitle("Phase Spectrum");

        double[][] angle = Spectrum.angleSpectrum(s, fs);
        Axes angleAx = fig.addAxes(col1, rowBottom2, cellW, cellH);
        angleAx.plot(angle[0], angle[1]).setColor(c2);
        angleAx.setTitle("Angle Spectrum");

        return fig;
    }

    /** Entry point for standalone execution. */
    public static void main(String[] args) {
        createFigure().show();
    }
}
