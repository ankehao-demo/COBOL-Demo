package com.coboldemo.mouse;

/**
 * Java port of mouse/mouse_example.cbl.
 *
 * The original is a ncurses-based paint program that captures mouse events
 * inside a 3270-style character grid. Java's standard library doesn't expose
 * terminal mouse events; JLine3 or Lanterna can, and Swing/JavaFX would offer
 * a GUI equivalent.
 *
 * Rather than pull in an optional UI dependency we ship a stub that explains
 * the gap and documents how the original COBOL feature maps to each
 * alternative. Running this class just prints guidance to stdout.
 */
public final class MouseExample {

    private MouseExample() {
    }

    public static void main(String[] args) {
        System.out.println("mouse/mouse_example.cbl — terminal paint demo.");
        System.out.println();
        System.out.println("The original COBOL program used ncurses/pdcurses");
        System.out.println("screen mode to capture left-button mouse events");
        System.out.println("and draw colour blocks at the cursor position.");
        System.out.println();
        System.out.println("Java does not expose terminal mouse events in");
        System.out.println("the standard library. Equivalent approaches:");
        System.out.println();
        System.out.println("  - JLine3's `Terminal.trackMouse(...)` feed,");
        System.out.println("  - Lanterna's MouseAction listener,");
        System.out.println("  - A Swing/JavaFX canvas with MouseListener.");
        System.out.println();
        System.out.println("See the README for the full feature mapping.");
    }
}
