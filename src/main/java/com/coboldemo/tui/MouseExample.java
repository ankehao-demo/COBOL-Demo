package com.coboldemo.tui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.MouseActionType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Migrated from: mouse/mouse_example.cbl
 *
 * A simple paint program demonstrating mouse functionality.
 * Uses the Lanterna library for terminal UI:
 * - Terminal for screen management
 * - Mouse event handling for left-click drawing
 * - Keyboard input for color changes and exit
 *
 * Note: Requires a terminal emulator that supports mouse events to run.
 */
public class MouseExample {

    private static final TextColor[] COLORS = {
            TextColor.ANSI.BLACK,
            TextColor.ANSI.RED,
            TextColor.ANSI.GREEN,
            TextColor.ANSI.YELLOW,
            TextColor.ANSI.BLUE,
            TextColor.ANSI.MAGENTA,
            TextColor.ANSI.CYAN,
            TextColor.ANSI.WHITE
    };

    // ANSI escape sequences to enable/disable mouse tracking
    private static final String ENABLE_MOUSE = "\033[?1000h\033[?1002h\033[?1006h";
    private static final String DISABLE_MOUSE = "\033[?1006l\033[?1002l\033[?1000l";

    public static void main(String[] args) throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        Terminal terminal = factory.createTerminal();

        try {
            terminal.enterPrivateMode();
            terminal.clearScreen();
            // Enable mouse tracking via ANSI escape sequences
            OutputStream out = System.out;
            out.write(ENABLE_MOUSE.getBytes());
            out.flush();

            int drawColor = 1;
            boolean mouseDown = false;

            // Draw status bar
            drawStatusBar(terminal, drawColor);

            terminal.flush();

            boolean running = true;
            while (running) {
                KeyStroke keyStroke = terminal.pollInput();
                if (keyStroke == null) {
                    Thread.sleep(50);
                    continue;
                }

                // Handle mouse events
                if (keyStroke instanceof MouseAction mouseAction) {
                    int row = mouseAction.getPosition().getRow();
                    int col = mouseAction.getPosition().getColumn();

                    if (mouseAction.getActionType() == MouseActionType.CLICK_DOWN) {
                        mouseDown = true;
                    } else if (mouseAction.getActionType() == MouseActionType.CLICK_RELEASE) {
                        mouseDown = false;
                    }

                    // Draw if mouse is down and within drawing area (rows 0-17)
                    if (mouseDown && row < 18 && col < 80) {
                        terminal.setCursorPosition(col, row);
                        terminal.setBackgroundColor(COLORS[drawColor]);
                        terminal.putCharacter(' ');
                        terminal.resetColorAndSGR();
                        terminal.flush();
                    }
                    continue;
                }

                // Handle keyboard events
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    running = false;
                } else if (keyStroke.getKeyType() == KeyType.Character) {
                    char c = Character.toUpperCase(keyStroke.getCharacter());

                    if (c == 'Q') {
                        running = false;
                    } else if (c >= '0' && c <= '7') {
                        drawColor = c - '0';
                        drawStatusBar(terminal, drawColor);
                        terminal.flush();
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Disable mouse tracking
            OutputStream out = System.out;
            out.write(DISABLE_MOUSE.getBytes());
            out.flush();
            terminal.exitPrivateMode();
            terminal.close();
        }
    }

    private static void drawStatusBar(Terminal terminal, int drawColor) throws IOException {
        TerminalSize size = terminal.getTerminalSize();
        int cols = Math.min(size.getColumns(), 80);

        // Status line at row 18
        terminal.setCursorPosition(0, 18);
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setBackgroundColor(TextColor.ANSI.DEFAULT);
        String statusMsg = "Current Draw color:";
        for (char c : statusMsg.toCharArray()) {
            terminal.putCharacter(c);
        }

        // Draw color indicator
        terminal.setBackgroundColor(COLORS[drawColor]);
        terminal.putCharacter(' ');
        terminal.resetColorAndSGR();

        // Instructions at row 19
        terminal.setCursorPosition(0, 19);
        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setBackgroundColor(TextColor.ANSI.BLUE);
        String instructions = "Esc to exit. Number keys to change cursor draw color. Left mouse down to draw current color.";
        for (int i = 0; i < cols && i < instructions.length(); i++) {
            terminal.putCharacter(instructions.charAt(i));
        }
        for (int i = instructions.length(); i < cols; i++) {
            terminal.putCharacter(' ');
        }
        terminal.resetColorAndSGR();
    }
}
