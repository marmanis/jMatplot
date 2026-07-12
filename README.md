# jMatplot

A Java/Swing port of the [matplotlib](https://matplotlib.org/) plotting library.  
jMatplot mirrors matplotlib's object-oriented API — `Figure`, `Axes`, and `Artist` subclasses — while rendering entirely through Java 2D (AWT/Swing), with no native dependencies.

---

## Requirements

| Requirement | Version |
|---|---|
| Java | 17+ |
| Build tool | Maven 3.6+ |

No third-party runtime dependencies. Test scope only: JUnit 5.10, Mockito 5.5.

---

## Building

```bash
mvn clean package
```

Run the unit tests:

```bash
mvn test
```

---

## Quick start

```java
import com.marmanis.jMatplot.core.Figure;
import com.marmanis.jMatplot.core.Axes;

Figure fig = new Figure();
Axes ax = fig.addAxes();

double[] x = {1, 2, 3, 4};
double[] y = {1, 4, 2, 3};
ax.plot(x, y);
ax.setTitle("My first plot");

fig.show();           // opens a Swing window
fig.savefig("out.jpg"); // or save to disk
```

See `QuickStartExamples.java` for additional worked examples (OO-style multi-line plot, styled lines, scatter, bar chart, subplots, and annotations).

---

## Package overview

### `com.marmanis.jMatplot.core`

The library's public API.

| Class | Matplotlib equivalent | Description |
|---|---|---|
| `Figure` | `matplotlib.figure.Figure` | Top-level container. Manages one or more `Axes` and owns the canvas. |
| `Axes` | `matplotlib.axes.Axes` | A single plot region. Owns the coordinate system, ticks, axis labels, title, and a z-ordered list of artists. |
| `Artist` | `matplotlib.artist.Artist` | Abstract base for every visible element. Carries `visible`, `alpha`, `zorder`, and `label`. |
| `Line2D` | `matplotlib.lines.Line2D` | A polyline with optional per-point markers. Supports solid, dashed, and custom dash patterns. |
| `Scatter` | `matplotlib.collections.PathCollection` | A scatter plot (independent marker per point, variable size/color). |
| `Bar` / `BarH` | `matplotlib.patches.Rectangle` | Vertical and horizontal bar charts. |
| `GradientBar` | — | Bar with a linear colour gradient fill. |
| `FillBetween` | `matplotlib.patches.Polygon` | Filled area between two curves (or a curve and a scalar). |
| `FilledPolygon` | `matplotlib.patches.Polygon` | Arbitrary filled polygon defined by explicit vertex arrays. |
| `EventPlot` | `matplotlib.collections.EventCollection` | Parallel tick-mark rows for event-sequence data; supports horizontal and vertical orientation. |
| `ContourArtist` | `matplotlib.contour.QuadContourSet` | Iso-contour lines or filled contours over a 2-D scalar field. |
| `PcolormeshArtist` | `matplotlib.collections.QuadMesh` | Pseudo-colour mesh for 2-D array data. |
| `QuiverArtist` | `matplotlib.quiver.Quiver` | Vector-field arrows. |
| `TriArtist` | `matplotlib.tri.*` | Triangulation-based plots (tricontour, tripcolor, triplot). |
| `ImageArtist` | `matplotlib.image.AxesImage` | Raster image displayed within an axes. |
| `Polyline` | — | Low-level polyline primitive used internally. |
| `TickedLine` | — | Line segment with perpendicular tick marks. |
| `Text` | `matplotlib.text.Text` | Text annotation placed in data or axes coordinates. |
| `Legend` | `matplotlib.legend.Legend` | Auto-built legend from labelled artists. |
| `PlotPanel` | — | Swing `JPanel` that renders a `Figure` and repaints on `setFigure()`. |

#### Normalization classes

Used to map data values to the `[0, 1]` colour range for colormapped artists.

| Class | Matplotlib equivalent |
|---|---|
| `Normalize` | `matplotlib.colors.Normalize` (abstract) |
| `LinearNorm` | `matplotlib.colors.Normalize` |
| `LogNorm` | `matplotlib.colors.LogNorm` |
| `PowerNorm` | `matplotlib.colors.PowerNorm` |
| `SymLogNorm` | `matplotlib.colors.SymLogNorm` |
| `BoundaryNorm` | `matplotlib.colors.BoundaryNorm` |
| `CustomNorm` | — |

#### Colormaps

`Colormap` holds named colormaps and resolves a normalised `[0, 1]` value to an `awt.Color`. Built-in maps include the standard matplotlib defaults (viridis, plasma, jet, etc.).

### `com.marmanis.jMatplot.backend`

Rendering abstraction layer. `Backend` defines the drawing primitives (`drawLine`, `drawPolygon`, `fillPolygon`, `drawText`, …). `SwingBackend` implements them on a Java 2D `Graphics2D` context, enabling both on-screen display and off-screen image export.

### `com.marmanis.jMatplot.style`

`StyleSheet` manages named plot styles, analogous to `matplotlib.style`. A style can override line colors, line widths, background color, font sizes, and other visual defaults.

---

## Layout model

Axes positions are specified as a normalised rectangle `(x, y, width, height)` in the range `[0, 1]` relative to the figure canvas, with `(0, 0)` at the **bottom-left** corner — the same convention as matplotlib.

```java
// Full-width axes occupying the top third of the figure
Axes ax = fig.addAxes(0.07, 0.67, 0.86, 0.28);
```

When axes are added without an explicit position, `fig.subplots(rows, cols)` or the single-axes `fig.addAxes()` shortcut fills the canvas automatically.

Per-axes margins (as fractions of the cell size) leave room for tick labels, axis labels, and the title:

| Side | Default margin |
|---|---|
| Left | 13 % |
| Right | 3 % |
| Bottom | 12 % |
| Top | 8 % |

---

## `Axes` plotting methods

```java
// Lines
Line2D   line   = ax.plot(x, y);
Line2D   line   = ax.plot(x, y, fmt);   // fmt: "r--", "bo-", etc.

// Scatter
Scatter  sc     = ax.scatter(x, y);

// Bars
Bar      bar    = ax.bar(labels, values);
BarH     barh   = ax.barh(labels, values);

// Fill between curves
FillBetween fb  = ax.fillBetween(x, y1, y2);
FillBetween fb  = ax.fillBetween(x, y1, scalar);

// Arbitrary filled polygon
FilledPolygon fp = ax.fill(xs, ys);

// Event plot
EventPlot ep    = ax.eventplot(positions, colors, lineOffsets, lineLengths);
EventPlot ep    = ax.eventplot(positions, colors, lineOffsets, lineLengths, "vertical");

// Reference lines
ax.axhline(y);
ax.axvline(x);
ax.hlines(y, xmin, xmax);
ax.vlines(x, ymin, ymax);

// Text annotation
ax.text(x, y, "label");
```

### Axis configuration

```java
ax.setXLim(xMin, xMax);
ax.setYLim(yMin, yMax);
ax.setXLabel("Frequency (Hz)");
ax.setYLabel("Amplitude");
ax.setTitle("My axes title");
ax.setXTicks(positions, labels);
ax.legend();
```

---

## Saving figures

```java
fig.savefig("output.jpg");   // JPEG
fig.savefig("output.png");   // PNG
```

The image dimensions default to 800 × 600 px. Pass an explicit size to the `Figure` constructor or override it per-figure when needed.

---

## Running tests

```bash
mvn test
```

Test classes live under `src/test/java/com/marmanis/jMatplot/core/` and cover `Figure`, `Axes`, and rendering output.

---

## Project structure

```
jMatplot/
├── pom.xml
└── src/
    └── main/
        └── java/com/marmanis/jMatplot/
            ├── QuickStartExamples.java      # Self-contained worked examples
            ├── QuickStartVisualRunner.java  # Swing runner for the quick-start examples
            ├── backend/
            │   ├── Backend.java             # Rendering primitive interface
            │   └── SwingBackend.java        # Java 2D implementation
            ├── core/                        # Public API (Figure, Axes, artists, …)
            ├── data/                        # Static math / signal-processing helpers
            └── style/                       # Named plot-style support
```

---

## License

Apache License 2.0
