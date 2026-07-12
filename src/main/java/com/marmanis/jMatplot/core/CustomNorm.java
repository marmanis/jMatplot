package com.marmanis.jMatplot.core;

import java.util.function.Function;

public class CustomNorm implements Normalize {
    private Function<Double, Double> func;
    public CustomNorm(Function<Double, Double> func) { this.func = func; }
    public double normalize(double value) { return func.apply(value); }
}
