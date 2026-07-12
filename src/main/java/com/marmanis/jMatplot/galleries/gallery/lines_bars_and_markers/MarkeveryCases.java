package com.marmanis.jMatplot.galleries.gallery.lines_bars_and_markers;

import com.marmanis.jMatplot.core.Line2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared helper applying one of the nine representative {@code markevery}
 * cases used throughout matplotlib's "Markevery Demo" gallery example
 * (linear, log, zoomed, and polar variants).
 */
final class MarkeveryCases {

    private MarkeveryCases() {}

    /** Apply the {@code caseIndex}-th representative {@code markevery} case to {@code line}. */
    static void apply(Line2D line, int caseIndex) {
        switch (caseIndex) {
            case 0: // None -> every point
                line.setMarkevery(1);
                break;
            case 1: // 8
                line.setMarkevery(8);
                break;
            case 2: // (30, 8) -> explicit start/step tuple
                line.setMarkeveryIndices(indicesFromStartStep(30, 8, 200));
                break;
            case 3: // [16, 24, 32]
                line.setMarkeveryIndices(new int[]{16, 24, 32});
                break;
            case 4: // [0, -1]
                line.setMarkeveryIndices(new int[]{0, -1});
                break;
            case 5: // slice(100, 200, 3)
                line.setMarkeveryIndices(indicesFromSlice(100, 200, 3));
                break;
            case 6: // 0.1
                line.setMarkeveryFloat(0.1);
                break;
            case 7: // 0.3
                line.setMarkeveryFloat(0.3);
                break;
            case 8: // 1.5
                line.setMarkeveryFloat(1.5);
                break;
            default:
                break;
        }
    }

    /** Build {@code [start, start+step, start+2*step, ...]} while {@code < length}. */
    private static int[] indicesFromStartStep(int start, int step, int length) {
        List<Integer> out = new ArrayList<>();
        for (int i = start; i < length; i += step) out.add(i);
        return out.stream().mapToInt(Integer::intValue).toArray();
    }

    /** Build indices equivalent to Python's {@code slice(start, stop, step)}. */
    private static int[] indicesFromSlice(int start, int stop, int step) {
        List<Integer> out = new ArrayList<>();
        for (int i = start; i < stop; i += step) out.add(i);
        return out.stream().mapToInt(Integer::intValue).toArray();
    }
}
