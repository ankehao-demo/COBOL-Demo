package com.coboldemo.mouse;

/**
 * Port of {@code mouse/mouse_example.cbl} — the COBOL version was a terminal
 * "paint" program built on top of the screen-mode {@code CRT STATUS} register
 * and ncurses/pdcurses mouse events.
 *
 * <p>Java's standard library has no portable terminal mouse event API. To
 * keep the dependency footprint small we just print an explanation here;
 * see the project README for notes on optionally adding Lanterna.
 */
public final class MouseExample {

    private MouseExample() {
    }

    public static void main(String[] args) {
        System.out.println("COBOL mouse example (mouse/mouse_example.cbl)");
        System.out.println("---------------------------------------------");
        System.out.println("The original program implements a terminal paint tool:");
        System.out.println("  * Esc exits the program.");
        System.out.println("  * Number keys 0-7 change the draw color.");
        System.out.println("  * Left mouse button draws a colored cell at the cursor.");
        System.out.println();
        System.out.println("GnuCOBOL drives these events through ncurses via the");
        System.out.println("COB_AUTO_MOUSE_HANDLING screenio configuration. Java does");
        System.out.println("not expose mouse events on the standard console, so this");
        System.out.println("migration does not implement the interactive behaviour.");
        System.out.println();
        System.out.println("To keep the feature functional, add a terminal-UI library");
        System.out.println("such as Lanterna and handle MouseAction events from a");
        System.out.println("Terminal screen. See the module README for an example.");
    }
}
