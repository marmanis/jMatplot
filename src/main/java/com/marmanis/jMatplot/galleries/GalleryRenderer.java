package com.marmanis.jMatplot.galleries;

import com.marmanis.jMatplot.core.*;
import com.marmanis.jMatplot.data.DataGenerator;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.JoinstyleExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.LinestylesExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.LinesWithTicksDemoExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.MarkeveryLinearExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.MarkeveryLogExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.MarkeveryZoomedExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.MarkeveryPolarExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.MarkerExamplesExample;
import com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers.SpectrumDemoExample;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Renders all 49 "Lines, Bars and Markers" gallery examples to JPEG files.
 *
 * <p>Run from the project root with:
 * <pre>
 *   mvn compile exec:java -Dexec.mainClass=com.marmanis.jMatplot.galleries.GalleryRenderer \
 *       -Djava.awt.headless=true
 * </pre>
 *
 * <p>Output is written to {@code HTML-gallery/images/} relative to the working directory.
 */
public class GalleryRenderer {

    private static final int W = 640;
    private static final int H = 480;

    public static void main(String[] args) throws IOException {
        // Force headless mode so the JVM doesn't need an X11/Quartz display.
        System.setProperty("java.awt.headless", "true");

        String outDir = "HTML-gallery/images";
        new File(outDir).mkdirs();

        int saved = 0;
        String[][] examples = examples();
        for (String[] ex : examples) {
            String slug = ex[0];
            try {
                Figure fig = buildFigure(slug);
                if (fig != null) {
                    int[] sz = figureSize(slug);
                    fig.savefig(outDir + "/" + slug + ".jpg", sz[0], sz[1]);
                    System.out.println("  ✓ " + slug);
                    saved++;
                } else {
                    System.out.println("  ✗ " + slug + " (no builder)");
                }
            } catch (Exception e) {
                System.err.println("  ✗ " + slug + ": " + e.getMessage());
            }
        }
        System.out.println("Done: " + saved + "/" + examples.length + " images written to " + outDir);

    }

    // ─── per-example image dimensions ────────────────────────────────────────

    /**
     * Return the pixel dimensions {@code {width, height}} for a specific gallery
     * example.  Most examples use the defaults {@link #W} × {@link #H}
     * (640 × 480 px).  Examples that need a different aspect ratio or more
     * space override the defaults here.
     *
     * @param slug the URL-style slug identifying the gallery example
     * @return {@code int[]{width, height}} for the saved image
     */
    private static int[] figureSize(String slug) {
        switch (slug) {
            case "linestyles":
                return new int[]{640, LinestylesExample.HEIGHT};   // tall panels
            case "markevery_linear":
                return new int[]{MarkeveryLinearExample.WIDTH, MarkeveryLinearExample.HEIGHT};
            case "markevery_log":
                return new int[]{MarkeveryLogExample.WIDTH, MarkeveryLogExample.HEIGHT};
            case "markevery_zoomed":
                return new int[]{MarkeveryZoomedExample.WIDTH, MarkeveryZoomedExample.HEIGHT};
            case "markevery_polar":
                return new int[]{MarkeveryPolarExample.WIDTH, MarkeveryPolarExample.HEIGHT};
            case "joinstyle":
                return new int[]{JoinstyleExample.WIDTH,
                        JoinstyleExample.HEIGHT};         // 700×1000 five-row 5×3 grid
            case "spectrum_demo":
                return new int[]{SpectrumDemoExample.WIDTH, SpectrumDemoExample.HEIGHT};
            default:
                return new int[]{W, H};
        }
    }

    // ─── dispatch ────────────────────────────────────────────────────────────

    private static Figure buildFigure(String slug) {
        switch (slug) {
            case "simple_plot":
                return simplePlot();
            case "linestyles":
                return linestyles();
            case "line_demo_dash_control":
                return lineDemoDashControl();
            case "markevery_linear":
                return markeveryLinear();
            case "markevery_log":
                return markeveryLog();
            case "markevery_zoomed":
                return markeveryZoomed();
            case "markevery_polar":
                return markeveryPolar();
            case "marker_examples":
                return markerExamples();
            case "joinstyle":
                return joinstyle();
            case "capstyle":
                return capstyle();
            case "marker_reference":
                return markerReference();
            case "axline":
                return axline();
            case "multicolored_line":
                return multicoloredLine();
            case "lines_with_ticks_demo":
                return linesWithTicksDemo();
            case "barchart":
                return barchart();
            case "bar_label_demo":
                return barLabelDemo();
            case "bar_stacked":
                return barStacked();
            case "bar_colors":
                return barColors();
            case "hat_graph":
                return hatGraph();
            case "gradient_bar":
                return gradientBar();
            case "barh":
                return barh();
            case "horizontal_barchart_distribution":
                return horizontalBarchartDist();
            case "broken_barh":
                return brokenBarh();
            case "scatter_demo2":
                return scatterDemo2();
            case "multivariate_marker_plot":
                return multivariateMarkerPlot();
            case "scatter_star_poly":
                return scatterStarPoly();
            case "scatter_with_legend":
                return scatterWithLegend();
            case "scatter_masked":
                return scatterMasked();
            case "scatter_hist":
                return scatterHist();
            case "stairs_demo":
                return stairsDemo();
            case "step_demo":
                return stepDemo();
            case "fill":
                return fill();
            case "fill_between_alpha":
                return fillBetweenAlpha();
            case "fill_between_demo":
                return fillBetweenDemo();
            case "fill_area_between_curves":
                return fillAreaBetweenCurves();
            case "confidence_bands":
                return confidenceBands();
            case "selective_filling_horizontal_regions":
                return selectiveFillingHorizontalRegions();
            case "selective_filling_horizontal_regions_whole_axes":
                return selectiveFillingHorizontalRegionsWholeAxes();
            case "stackplot_demo":
                return stackplotDemo();
            case "eventcollection_demo":
                return eventcollectionDemo();
            case "eventplot_demo":
                return eventplotDemo();
            case "timeline":
                return timeline();
            case "vline_hline_demo":
                return vlineHlineDemo();
            case "span_regions":
                return spanRegions();
            case "stem_plot":
                return stemPlot();
            case "categorical_variables":
                return categoricalVariables();
            case "masked_demo":
                return maskedDemo();
            case "spectrum_demo":
                return spectrumDemo();
            case "step_vs_stairs":
                return stepVsStairs();
            default:
                return null;
        }
    }

    // ─── example builders ────────────────────────────────────────────────────

    static Figure simplePlot() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .SimplePlotExample.createFigure();
    }

    static Figure linestyles() {
        return LinestylesExample.createFigure();
    }

    static Figure lineDemoDashControl() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .LineDemoDashControlExample.createFigure();
    }

    static Figure markeveryLinear() {
        return MarkeveryLinearExample.createFigure();
    }

    static Figure markeveryLog() {
        return MarkeveryLogExample.createFigure();
    }

    static Figure markeveryZoomed() {
        return MarkeveryZoomedExample.createFigure();
    }

    static Figure markeveryPolar() {
        return MarkeveryPolarExample.createFigure();
    }

    static Figure markerExamples() {
        return MarkerExamplesExample.createFigure();
    }

    static Figure joinstyle() {
        return JoinstyleExample.createFigure();
    }

    static Figure capstyle() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .CapstyleExample.createFigure();
    }

    static Figure markerReference() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .MarkerReferenceExample.createFigure();
    }

    static Figure axline() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .AxlineExample.createFigure();
    }

    static Figure multicoloredLine() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .MulticoloredLineExample.createFigure();
    }

    static Figure linesWithTicksDemo() {
        return LinesWithTicksDemoExample.createFigure();
    }

    static Figure barchart() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BarchartExample.createFigure();
    }

    static Figure barLabelDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BarLabelDemoExample.createFigure();
    }

    static Figure barStacked() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BarStackedExample.createFigure();
    }

    static Figure barColors() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BarColorsExample.createFigure();
    }

    static Figure hatGraph() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .HatGraphExample.createFigure();
    }

    static Figure gradientBar() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .GradientBarExample.createFigure();
    }

    static Figure barh() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BarhExample.createFigure();
    }

    static Figure horizontalBarchartDist() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .HorizontalBarchartDistributionExample.createFigure();
    }

    static Figure brokenBarh() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .BrokenBarhExample.createFigure();
    }

    static Figure scatterDemo2() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ScatterDemo2Example.createFigure();
    }

    static Figure multivariateMarkerPlot() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .MultivariateMarkerPlotExample.createFigure();
    }

    static Figure scatterStarPoly() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ScatterStarPolyExample.createFigure();
    }

    static Figure scatterWithLegend() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ScatterWithLegendExample.createFigure();
    }

    static Figure scatterMasked() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ScatterMaskedExample.createFigure();
    }

    static Figure scatterHist() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ScatterHistExample.createFigure();
    }

    static Figure stairsDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .StairsDemoExample.createFigure();
    }

    static Figure stepDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .StepDemoExample.createFigure();
    }

    static Figure fill() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .FillExample.createFigure();
    }

    static Figure fillBetweenAlpha() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .FillBetweenAlphaExample.createFigure();
    }

    static Figure fillBetweenDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .FillBetweenDemoExample.createFigure();
    }

    static Figure fillAreaBetweenCurves() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .FillAreaBetweenCurvesExample.createFigure();
    }

    static Figure confidenceBands() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .ConfidenceBandsExample.createFigure();
    }

    static Figure selectiveFillingHorizontalRegions() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .SelectiveFillingHorizontalRegionsExample.createFigure();
    }

    static Figure selectiveFillingHorizontalRegionsWholeAxes() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .SelectiveFillingHorizontalRegionsWholeAxesExample.createFigure();
    }

    static Figure stackplotDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .StackplotDemoExample.createFigure();
    }

    static Figure eventcollectionDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .EventcollectionDemoExample.createFigure();
    }

    static Figure eventplotDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .EventplotDemoExample.createFigure();
    }

    static Figure timeline() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .TimelineExample.createFigure();
    }

    static Figure vlineHlineDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .VlineHlineDemoExample.createFigure();
    }

    static Figure spanRegions() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .SpanRegionsExample.createFigure();
    }

    static Figure stemPlot() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .StemPlotExample.createFigure();
    }

    static Figure categoricalVariables() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .CategoricalVariablesExample.createFigure();
    }

    static Figure maskedDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .MaskedDemoExample.createFigure();
    }

    static Figure spectrumDemo() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .SpectrumDemoExample.createFigure();
    }

    static Figure stepVsStairs() {
        return com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers
                .StepVsStairsExample.createFigure();
    }

    // ─── slug list ───────────────────────────────────────────────────────────

    private static String[][] examples() {
        return new String[][]{
                {"simple_plot"}, {"linestyles"}, {"line_demo_dash_control"},
                {"markevery_linear"}, {"markevery_log"}, {"markevery_zoomed"}, {"markevery_polar"},
                {"marker_examples"},
                {"joinstyle"}, {"capstyle"}, {"marker_reference"},
                {"axline"}, {"multicolored_line"}, {"lines_with_ticks_demo"},
                {"barchart"}, {"bar_label_demo"}, {"bar_stacked"}, {"bar_colors"},
                {"hat_graph"}, {"gradient_bar"}, {"barh"}, {"horizontal_barchart_distribution"},
                {"broken_barh"}, {"scatter_demo2"}, {"multivariate_marker_plot"},
                {"scatter_star_poly"}, {"scatter_with_legend"}, {"scatter_masked"},
                {"scatter_hist"}, {"stairs_demo"}, {"step_demo"}, {"fill"},
                {"fill_between_alpha"}, {"fill_between_demo"},
                {"fill_area_between_curves"}, {"confidence_bands"},
                {"selective_filling_horizontal_regions"},
                {"selective_filling_horizontal_regions_whole_axes"},
                {"stackplot_demo"}, {"eventcollection_demo"}, {"eventplot_demo"},
                {"timeline"}, {"vline_hline_demo"}, {"span_regions"}, {"stem_plot"},
                {"categorical_variables"}, {"masked_demo"}, {"spectrum_demo"},
                {"step_vs_stairs"},
        };
    }
}
