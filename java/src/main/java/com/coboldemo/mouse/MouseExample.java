package com.coboldemo.mouse;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.MouseActionType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * MouseExample - A simple terminal-based paint program.
 *
 * Migrated from mouse/mouse_example.cbl (GnuCOBOL).
 *
 * <h2>COBOL Mouse Handling vs Lanterna Approach</h2>
 *
 * <p>In GnuCOBOL, mouse support is configured through environment variables
 * (COB_MOUSE_FLAGS, COB_SCREEN_EXCEPTIONS, etc.) and the ACCEPT/DISPLAY
 * statements with the CRT STATUS special register. Mouse events are delivered
 * as CRT status codes (e.g., COB-SCR-LEFT-PRESSED, COB-SCR-LEFT-RELEASED)
 * alongside keyboard input, and the cursor position is tracked via the
 * CURSOR IS special-name. The COBOL program uses a polling loop with a
 * timeout on ACCEPT to periodically check for both keyboard and mouse input.</p>
 *
 * <p>In Lanterna, terminal mouse support is enabled explicitly via
 * {@code terminal.setMouseCaptureMode()}. Input (both keyboard and mouse)
 * is read through {@code screen.pollInput()} or {@code screen.readInput()}.
 * Mouse events arrive as {@link MouseAction} objects with action type and
 * position, while keyboard events arrive as {@link KeyStroke} objects.
 * Lanterna abstracts away the platform-specific escape sequences for mouse
 * reporting, providing a clean Java API. Drawing is done by setting
 * {@link TextCharacter} cells on the {@link Screen} back-buffer and then
 * calling {@code screen.refresh()}.</p>
 *
 * <p>The COBOL program uses 1-based row/column addressing with DISPLAY AT,
 * while Lanterna uses 0-based coordinates. The COBOL color indices (0-7)
 * map to standard ANSI terminal colors in both environments.</p>
 *
 * Original COBOL author: Erik Eriksen (2022-04-13)
 */
public class MouseExample {

    // Drawing area: rows 0-17, columns 0-79 (matching COBOL rows 1-18, cols 1-80)
    private static final int DRAW_AREA_MAX_ROW = 17;
    private static final int DRAW_AREA_MAX_COL = 79;

    // Status line at row 18 (COBOL row 19)
    private static final int STATUS_ROW = 18;
    // Instruction line at row 19 (COBOL row 20)
    private static final int INSTRUCTION_ROW = 19;

    // Column where the color indicator block is displayed on the status line
    // In COBOL: "Current Draw color:" at col 1, color block at col 21
    private static final int COLOR_INDICATOR_COL = 20;

    /**
     * Maps COBOL color indices (0-7) to Lanterna ANSI colors.
     *
     * COBOL color mapping:
     *   0 = Black, 1 = Blue, 2 = Green, 3 = Cyan,
     *   4 = Red, 5 = Magenta, 6 = Yellow/Brown, 7 = White
     */
    private static final TextColor[] COBOL_COLORS = {
        TextColor.ANSI.BLACK,       // 0 - COB-COLOR-BLACK
        TextColor.ANSI.BLUE,        // 1 - COB-COLOR-BLUE
        TextColor.ANSI.GREEN,       // 2 - COB-COLOR-GREEN
        TextColor.ANSI.CYAN,        // 3 - COB-COLOR-CYAN
        TextColor.ANSI.RED,         // 4 - COB-COLOR-RED
        TextColor.ANSI.MAGENTA,     // 5 - COB-COLOR-MAGENTA
        TextColor.ANSI.YELLOW,      // 6 - COB-COLOR-YELLOW (COBOL "brown")
        TextColor.ANSI.WHITE        // 7 - COB-COLOR-WHITE
    };

    public static void main(String[] args) throws IOException {
        // In COBOL, environment variables configure screen and mouse behavior:
        //   SET ENVIRONMENT "COB_SCREEN_EXCEPTIONS" TO 'Y'
        //   SET ENVIRONMENT "COB_SCREEN_ESC" TO 'Y'
        //   SET ENVIRONMENT "COB_MOUSE_FLAGS" TO ws-mouse-flags
        // Lanterna handles all of this through its own API.

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        // Enable mouse capture in the terminal factory
        terminalFactory.setMouseCaptureMode(com.googlecode.lanterna.terminal.MouseCaptureMode.CLICK_RELEASE);

        Terminal terminal = terminalFactory.createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null); // Hide the cursor

        // COBOL: move 1 to ws-draw-color
        int drawColor = 1;

        // Track whether the left mouse button is currently held down
        // COBOL uses ws-mouse-clicked-sw ('Y'/'N') toggled by
        // COB-SCR-LEFT-PRESSED and COB-SCR-LEFT-RELEASED events
        boolean mousePressed = false;

        // Draw initial UI elements
        drawStatusLine(screen, drawColor);
        drawInstructionLine(screen);
        screen.refresh();

        // COBOL: perform until ws-exit
        boolean running = true;
        while (running) {
            // Update the color indicator block on the status line
            // COBOL: display " " background-color ws-draw-color at 1921
            drawColorIndicator(screen, drawColor);
            screen.refresh();

            // COBOL: accept ws-kb-input with auto-skip no-echo timeout after 50
            // In Lanterna, pollInput() returns immediately (non-blocking),
            // so we add a small sleep to avoid busy-waiting (similar to COBOL timeout)
            KeyStroke keyStroke = screen.pollInput();

            if (keyStroke == null) {
                // No input available; sleep briefly to avoid busy-waiting
                // COBOL uses "timeout after 50" which at COB_TIMEOUT_SCALE=3
                // corresponds to ~50ms
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            // Handle mouse events
            // COBOL evaluates ws-crt-status for COB-SCR-LEFT-PRESSED / COB-SCR-LEFT-RELEASED
            if (keyStroke instanceof MouseAction) {
                MouseAction mouseAction = (MouseAction) keyStroke;
                MouseActionType actionType = mouseAction.getActionType();

                if (actionType == MouseActionType.CLICK_DOWN) {
                    // COBOL: when COB-SCR-LEFT-PRESSED -> set ws-mouse-clicked to true
                    mousePressed = true;

                    // Draw at the mouse position if within the drawing area
                    // COBOL: if ws-cursor-position not = zeros
                    //        and ws-cursor-line < 19 and ws-cursor-col <= 80
                    //        and ws-mouse-clicked
                    int row = mouseAction.getPosition().getRow();
                    int col = mouseAction.getPosition().getColumn();
                    if (row <= DRAW_AREA_MAX_ROW && col <= DRAW_AREA_MAX_COL) {
                        drawPixel(screen, row, col, drawColor);
                    }
                } else if (actionType == MouseActionType.CLICK_RELEASE) {
                    // COBOL: when COB-SCR-LEFT-RELEASED -> set ws-mouse-not-clicked to true
                    mousePressed = false;
                } else if (actionType == MouseActionType.DRAG && mousePressed) {
                    // Support drag-painting (mouse move while button held)
                    // COBOL enables COB-ALLOW-MOUSE-MOVE for this behavior
                    int row = mouseAction.getPosition().getRow();
                    int col = mouseAction.getPosition().getColumn();
                    if (row <= DRAW_AREA_MAX_ROW && col <= DRAW_AREA_MAX_COL) {
                        drawPixel(screen, row, col, drawColor);
                    }
                }
                continue;
            }

            // Handle keyboard events
            KeyType keyType = keyStroke.getKeyType();

            // COBOL: evaluate ws-crt-status when COB-SCR-ESC -> set ws-exit to true
            if (keyType == KeyType.Escape) {
                running = false;
                continue;
            }

            // COBOL: if ws-kb-input = 'Q' then stop run
            if (keyType == KeyType.Character) {
                char ch = keyStroke.getCharacter();

                if (ch == 'q' || ch == 'Q') {
                    running = false;
                    continue;
                }

                // COBOL: if ws-kb-input is numeric then
                //            move ws-kb-input to ws-draw-color
                //            if ws-draw-color > 7 then move 7 to ws-draw-color
                if (ch >= '0' && ch <= '9') {
                    int newColor = ch - '0';
                    // COBOL clamps to max of 7
                    if (newColor > 7) {
                        newColor = 7;
                    }
                    drawColor = newColor;
                    drawStatusLine(screen, drawColor);
                }
            }
        }

        // COBOL: stop run
        screen.stopScreen();
    }

    /**
     * Draws the status line at row 18.
     * COBOL: display "Current Draw color:" at 1901
     */
    private static void drawStatusLine(Screen screen, int drawColor) {
        String statusText = "Current Draw color:";
        for (int i = 0; i < statusText.length(); i++) {
            screen.setCharacter(i, STATUS_ROW,
                new TextCharacter(statusText.charAt(i),
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        drawColorIndicator(screen, drawColor);
    }

    /**
     * Draws the color indicator block at the status line.
     * COBOL: display " " background-color ws-draw-color at 1921
     */
    private static void drawColorIndicator(Screen screen, int drawColor) {
        // Display a space with the current draw color as background
        // Also show the color number for clarity
        screen.setCharacter(COLOR_INDICATOR_COL, STATUS_ROW,
            new TextCharacter(' ', TextColor.ANSI.WHITE, COBOL_COLORS[drawColor]));
        screen.setCharacter(COLOR_INDICATOR_COL + 1, STATUS_ROW,
            new TextCharacter(' ', TextColor.ANSI.WHITE, COBOL_COLORS[drawColor]));
        screen.setCharacter(COLOR_INDICATOR_COL + 2, STATUS_ROW,
            new TextCharacter(' ', TextColor.ANSI.WHITE, COBOL_COLORS[drawColor]));
    }

    /**
     * Draws the instruction line at row 19 with white text on blue background.
     * COBOL: display "Esc to exit. Number keys to change cursor draw color.
     *         Left mouse down to draw current color."
     *         foreground-color cob-color-white highlight
     *         background-color cob-color-blue at 2001
     */
    private static void drawInstructionLine(Screen screen) {
        String instructions = "Esc to exit. Number keys to change color. Click to draw.";
        // Pad to 80 chars to fill the entire line with blue background
        StringBuilder padded = new StringBuilder(instructions);
        while (padded.length() < 80) {
            padded.append(' ');
        }
        String line = padded.toString();
        for (int i = 0; i < line.length(); i++) {
            // COBOL uses foreground-color cob-color-white highlight (bright white)
            // and background-color cob-color-blue
            screen.setCharacter(i, INSTRUCTION_ROW,
                new TextCharacter(line.charAt(i),
                    TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLUE));
        }
    }

    /**
     * Draws a single "pixel" (colored space) at the given position.
     * COBOL: display " " background-color ws-draw-color at ws-cursor-position
     */
    private static void drawPixel(Screen screen, int row, int col, int drawColor) {
        screen.setCharacter(col, row,
            new TextCharacter(' ', TextColor.ANSI.WHITE, COBOL_COLORS[drawColor]));
    }
}
