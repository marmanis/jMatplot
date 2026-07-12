package com.marmanis.jMatplot.data;

import java.util.Random;
import java.util.function.DoubleUnaryOperator;

/**
 * Numerical data generation utilities for jMatplot gallery examples.
 *
 * <p>This class provides the Java equivalent of the NumPy functions used in every
 * matplotlib gallery script to generate sample data before plotting.  The common
 * pattern in Python is:
 *
 * <pre>{@code
 * # Python (from galleries/plot_types/basic/plot.py)
 * x = np.linspace(0, 10, 100)
 * y = 4 + 1 * np.sin(2 * x)
 * }</pre>
 *
 * The Java equivalent:
 *
 * <pre>{@code
 * double[] x = DataGenerator.linspace(0, 10, 100);
 * double[] y = DataGenerator.apply(x, v -> 4 + Math.sin(2 * v));
 * }</pre>
 *
 * <h2>API coverage</h2>
 * <ul>
 *   <li>{@link #linspace} — evenly-spaced values in a closed interval (numpy.linspace)</li>
 *   <li>{@link #arange} — integer range as double[] (numpy.arange)</li>
 *   <li>{@link #arange(double, double, double)} — stepped range (numpy.arange with step)</li>
 *   <li>{@link #apply} — element-wise function application (numpy ufuncs)</li>
 *   <li>{@link #meshgrid} — 2-D coordinate grids (numpy.meshgrid)</li>
 *   <li>{@link #normalRandom} — seeded Gaussian samples (numpy.random.normal)</li>
 *   <li>{@link #uniformRandom} — seeded uniform samples (numpy.random.uniform)</li>
 *   <li>{@link #mean(double[], double[])} — element-wise mean of two arrays</li>
 *   <li>{@link #zeros} / {@link #ones} — constant arrays</li>
 * </ul>
 *
 * @see GalleryData
 */
public final class DataGenerator {

    /** Prevent instantiation. */
    private DataGenerator() {}

    // ─── 1-D generation ───────────────────────────────────────────────────────

    /**
     * Return {@code num} evenly spaced values over the closed interval {@code [start, stop]}.
     *
     * <p>Equivalent to {@code numpy.linspace(start, stop, num)}.
     *
     * @param start first value
     * @param stop  last value (inclusive)
     * @param num   number of points; must be &ge; 1
     * @return array of length {@code num}
     * @throws IllegalArgumentException if {@code num < 1}
     */
    public static double[] linspace(double start, double stop, int num) {
        if (num < 1) throw new IllegalArgumentException("num must be >= 1, got " + num);
        if (num == 1) return new double[]{start};
        double[] result = new double[num];
        double step = (stop - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            result[i] = start + i * step;
        }
        return result;
    }

    /**
     * Return integer values from {@code 0} (inclusive) to {@code stop} (exclusive)
     * as a {@code double[]}.
     *
     * <p>Equivalent to {@code numpy.arange(stop)}.
     *
     * @param stop exclusive upper bound
     * @return array {@code [0, 1, ..., stop-1]}
     */
    public static double[] arange(int stop) {
        return arange(0, stop);
    }

    /**
     * Return integer values from {@code start} (inclusive) to {@code stop} (exclusive).
     *
     * <p>Equivalent to {@code numpy.arange(start, stop)}.
     *
     * @param start inclusive lower bound
     * @param stop  exclusive upper bound
     * @return array {@code [start, start+1, ..., stop-1]}
     */
    public static double[] arange(int start, int stop) {
        int n = stop - start;
        if (n <= 0) return new double[0];
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = start + i;
        return result;
    }

    /**
     * Return values from {@code start} to {@code stop} (exclusive) with the given {@code step}.
     *
     * <p>Equivalent to {@code numpy.arange(start, stop, step)}.
     *
     * @param start first value
     * @param stop  exclusive upper bound
     * @param step  spacing between consecutive values; must be non-zero
     * @return array of sampled values
     */
    public static double[] arange(double start, double stop, double step) {
        if (step == 0) throw new IllegalArgumentException("step must not be zero");
        int n = (int) Math.ceil((stop - start) / step);
        if (n <= 0) return new double[0];
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = start + i * step;
        return result;
    }

    // ─── Element-wise operations ───────────────────────────────────────────────

    /**
     * Apply {@code func} element-wise to {@code x} and return the result.
     *
     * <p>Equivalent to a NumPy universal function (ufunc) applied to an array, e.g.:
     * {@code y = np.sin(x)}.
     *
     * @param x    input array
     * @param func function to apply to each element
     * @return new array of the same length as {@code x}
     */
    public static double[] apply(double[] x, DoubleUnaryOperator func) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) result[i] = func.applyAsDouble(x[i]);
        return result;
    }

    /**
     * Return the element-wise mean of two same-length arrays.
     *
     * <p>Equivalent to {@code (y1 + y2) / 2}.
     *
     * @param y1 first array
     * @param y2 second array; must have the same length as {@code y1}
     * @return element-wise mean
     */
    public static double[] mean(double[] y1, double[] y2) {
        if (y1.length != y2.length) throw new IllegalArgumentException("Arrays must have the same length");
        double[] result = new double[y1.length];
        for (int i = 0; i < y1.length; i++) result[i] = (y1[i] + y2[i]) / 2.0;
        return result;
    }

    /**
     * Add a scalar offset to every element of an array.
     *
     * @param x      input array
     * @param offset value to add
     * @return new array with {@code x[i] + offset} for all {@code i}
     */
    public static double[] add(double[] x, double offset) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) result[i] = x[i] + offset;
        return result;
    }

    /**
     * Return an array of zeros of length {@code n}.
     *
     * <p>Equivalent to {@code numpy.zeros(n)}.
     *
     * @param n length
     * @return zero-filled array
     */
    public static double[] zeros(int n) {
        return new double[n];
    }

    /**
     * Return an array of ones of length {@code n}.
     *
     * <p>Equivalent to {@code numpy.ones(n)}.
     *
     * @param n length
     * @return one-filled array
     */
    public static double[] ones(int n) {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = 1.0;
        return result;
    }

    // ─── 2-D grid generation ──────────────────────────────────────────────────

    /**
     * Create 2-D coordinate matrices from two 1-D arrays (equivalent to
     * {@code numpy.meshgrid(x, y)}).
     *
     * <p>Returns {@code {X, Y}} where {@code X[i][j] = x[j]} and
     * {@code Y[i][j] = y[i]}, following NumPy's default {@code 'xy'} indexing.
     *
     * @param x 1-D x coordinates
     * @param y 1-D y coordinates
     * @return two-element array {@code {X, Y}}, each of shape {@code [y.length][x.length]}
     */
    public static double[][][] meshgrid(double[] x, double[] y) {
        int rows = y.length;
        int cols = x.length;
        double[][] X = new double[rows][cols];
        double[][] Y = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                X[r][c] = x[c];
                Y[r][c] = y[r];
            }
        }
        return new double[][][]{X, Y};
    }

    // ─── Random data ───────────────────────────────────────────────────────────

    /**
     * Generate {@code n} samples from a Gaussian distribution with the given
     * {@code mean} and {@code stddev}, using a fixed {@code seed} for reproducibility.
     *
     * <p>Equivalent to {@code numpy.random.seed(seed); numpy.random.normal(mean, stddev, n)}.
     *
     * @param seed   random seed
     * @param mean   distribution mean
     * @param stddev standard deviation; must be &ge; 0
     * @param n      number of samples
     * @return array of {@code n} samples
     */
    public static double[] normalRandom(long seed, double mean, double stddev, int n) {
        Random rng = new Random(seed);
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = mean + stddev * rng.nextGaussian();
        return result;
    }

    /**
     * Generate {@code n} samples from a Gaussian distribution with multiple
     * {@code means} and corresponding {@code stddevs}.
     *
     * <p>Produces interleaved samples matching {@code numpy.random.normal(means, stddevs, (n, k))}
     * where {@code k = means.length}.  The resulting 2-D array has shape {@code [n][k]}.
     *
     * @param seed    random seed
     * @param means   per-group means
     * @param stddevs per-group standard deviations
     * @param n       number of samples per group
     * @return 2-D array of shape {@code [n][means.length]}
     */
    public static double[][] normalRandom2d(long seed, double[] means, double[] stddevs, int n) {
        if (means.length != stddevs.length) throw new IllegalArgumentException("means and stddevs must have the same length");
        Random rng = new Random(seed);
        int k = means.length;
        double[][] result = new double[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                result[i][j] = means[j] + stddevs[j] * rng.nextGaussian();
            }
        }
        return result;
    }

    /**
     * Generate {@code n} samples uniformly distributed in {@code [low, high)}.
     *
     * <p>Equivalent to {@code numpy.random.seed(seed); numpy.random.uniform(low, high, n)}.
     *
     * @param seed random seed
     * @param low  lower bound (inclusive)
     * @param high upper bound (exclusive)
     * @param n    number of samples
     * @return array of {@code n} uniform samples
     */
    public static double[] uniformRandom(long seed, double low, double high, int n) {
        Random rng = new Random(seed);
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = low + (high - low) * rng.nextDouble();
        return result;
    }

    // ─── Convenience delegates ─────────────────────────────────────────────────

    /**
     * Compute {@code sqrt(x^2 + y^2)} element-wise on 2-D grids.
     *
     * @param X 2-D x-coordinate grid
     * @param Y 2-D y-coordinate grid
     * @return 2-D array of radii
     */
    public static double[][] hypot(double[][] X, double[][] Y) {
        int rows = X.length;
        int cols = X[0].length;
        double[][] R = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                R[r][c] = Math.hypot(X[r][c], Y[r][c]);
            }
        }
        return R;
    }

    /**
     * Apply a {@link DoubleUnaryOperator} element-wise to a 2-D array.
     *
     * @param Z    input 2-D array
     * @param func function to apply
     * @return new 2-D array of the same shape
     */
    public static double[][] apply2d(double[][] Z, DoubleUnaryOperator func) {
        int rows = Z.length;
        int cols = Z[0].length;
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = func.applyAsDouble(Z[r][c]);
            }
        }
        return result;
    }

    /**
     * Return the minimum value in an array.
     *
     * @param x input array; must not be empty
     * @return minimum value
     */
    public static double min(double[] x) {
        double m = x[0];
        for (double v : x) if (v < m) m = v;
        return m;
    }

    /**
     * Return the maximum value in an array.
     *
     * @param x input array; must not be empty
     * @return maximum value
     */
    public static double max(double[] x) {
        double m = x[0];
        for (double v : x) if (v > m) m = v;
        return m;
    }
}
