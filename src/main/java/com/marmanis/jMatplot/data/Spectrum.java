package com.marmanis.jMatplot.data;

/**
 * Frequency-spectrum helpers, mirroring the subset of
 * {@code matplotlib.mlab}'s spectral functions used by
 * {@code Axes.magnitude_spectrum}, {@code Axes.angle_spectrum}, and
 * {@code Axes.phase_spectrum}.
 *
 * <p>Each method computes a one-sided spectrum of a real-valued signal: the
 * signal is first multiplied by a length-matched Hann ("Hanning") window,
 * then transformed with a direct discrete Fourier transform (no zero
 * padding — matching matplotlib's default {@code pad_to = len(x)}), and only
 * the non-negative frequency bins {@code [0, N/2]} are kept.
 *
 * <p>This intentionally uses a direct O(N²) DFT rather than a radix-2 FFT,
 * since the signal lengths used in jMatplot's gallery examples are small
 * (hundreds to low thousands of samples) and need not be a power of two.
 */
public final class Spectrum {

    private Spectrum() {}

    /**
     * Magnitude spectrum of {@code x}, linear scale.
     *
     * @param x  real-valued signal
     * @param fs sampling frequency
     * @return {@code {freqs, magnitude}}, both of length {@code x.length/2 + 1}
     */
    public static double[][] magnitudeSpectrum(double[] x, double fs) {
        double[][] s = computeSpectrum(x, fs);
        return new double[][]{s[0], s[1]};
    }

    /**
     * Magnitude spectrum of {@code x} in decibels ({@code 20*log10(magnitude)}),
     * matching {@code Axes.magnitude_spectrum(..., scale='dB')}.
     *
     * @param x  real-valued signal
     * @param fs sampling frequency
     * @return {@code {freqs, magnitudeDb}}, both of length {@code x.length/2 + 1}
     */
    public static double[][] magnitudeSpectrumDb(double[] x, double fs) {
        double[][] s = computeSpectrum(x, fs);
        double[] db = new double[s[1].length];
        for (int i = 0; i < db.length; i++) db[i] = 20.0 * Math.log10(s[1][i]);
        return new double[][]{s[0], db};
    }

    /**
     * Angle (wrapped phase, in radians) spectrum of {@code x}.
     *
     * @param x  real-valued signal
     * @param fs sampling frequency
     * @return {@code {freqs, angle}}, both of length {@code x.length/2 + 1}
     */
    public static double[][] angleSpectrum(double[] x, double fs) {
        double[][] s = computeSpectrum(x, fs);
        return new double[][]{s[0], s[2]};
    }

    /**
     * Phase (unwrapped angle, in radians) spectrum of {@code x}.
     *
     * @param x  real-valued signal
     * @param fs sampling frequency
     * @return {@code {freqs, phase}}, both of length {@code x.length/2 + 1}
     */
    public static double[][] phaseSpectrum(double[] x, double fs) {
        double[][] s = computeSpectrum(x, fs);
        return new double[][]{s[0], unwrap(s[2])};
    }

    /**
     * Compute the shared one-sided spectrum data: frequencies, magnitude
     * (normalised by the window sum), and wrapped angle.
     *
     * @return {@code {freqs, magnitude, angle}}
     */
    private static double[][] computeSpectrum(double[] x, double fs) {
        int n = x.length;
        double[] window = hanning(n);
        double windowSum = 0;
        for (double w : window) windowSum += w;

        double[] xw = new double[n];
        for (int i = 0; i < n; i++) xw[i] = x[i] * window[i];

        int numFreqs = (n % 2 == 0) ? n / 2 + 1 : (n + 1) / 2;
        double[] freqs = new double[numFreqs];
        double[] magnitude = new double[numFreqs];
        double[] angle = new double[numFreqs];

        for (int k = 0; k < numFreqs; k++) {
            double re = 0, im = 0;
            for (int t = 0; t < n; t++) {
                double theta = 2 * Math.PI * k * t / n;
                re += xw[t] * Math.cos(theta);
                im -= xw[t] * Math.sin(theta);
            }
            freqs[k] = k * fs / n;
            magnitude[k] = Math.sqrt(re * re + im * im) / windowSum;
            angle[k] = Math.atan2(im, re);
        }
        return new double[][]{freqs, magnitude, angle};
    }

    /** Hann window of length {@code n}, matching {@code numpy.hanning(n)}. */
    private static double[] hanning(int n) {
        double[] w = new double[n];
        if (n == 1) { w[0] = 1.0; return w; }
        for (int i = 0; i < n; i++) {
            w[i] = 0.5 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1));
        }
        return w;
    }

    /**
     * Unwrap a sequence of phase angles (radians) by adding/subtracting
     * multiples of 2π wherever consecutive values jump by more than π,
     * matching {@code numpy.unwrap}.
     */
    private static double[] unwrap(double[] p) {
        double[] result = new double[p.length];
        if (p.length == 0) return result;
        result[0] = p[0];
        for (int i = 1; i < p.length; i++) {
            double d = p[i] - p[i - 1];
            while (d > Math.PI) d -= 2 * Math.PI;
            while (d <= -Math.PI) d += 2 * Math.PI;
            result[i] = result[i - 1] + d;
        }
        return result;
    }
}
