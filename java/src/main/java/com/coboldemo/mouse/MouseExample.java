package com.coboldemo.mouse;

import com.googlecode.lanterna.TerminalSize;
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
 * Migrated from: mouse/mouse_example.cbl
 * Original author: Erik Eriksen (2022-04-13)
 * Purpose: A simple paint program to demo the mouse functionality.
 *
 * Uses the Lanterna library for terminal UI with mouse support.
 *
 * Controls:
 * - Left mouse button: draw at cursor position
 * - Number keys 0-7: change draw color
 * - Esc or Q: quit
 */
public class MouseExample {

    // Color palette mapping (COBOL color codes 0-7)
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

    public static void main(String[] args) throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        Terminal terminal = factory.createTerminal();
        Screen screen = new TerminalScreen(terminal);

        screen.startScreen();

        try {
            TerminalSize size = screen.getTerminalSize();
            int maxRow = size.getRows();
            int maxCol = size.getColumns();

            int drawColor = 1; // Start with red
            boolean mousePressed = false;

            // Draw status bar (row 19 equivalent)
            drawStatusBar(screen, maxCol, drawColor);

            // Draw instruction bar (row 20 equivalent)
            drawInstructionBar(screen, maxCol);

            screen.refresh();

            boolean running = true;
            while (running) {
                KeyStroke keyStroke = screen.pollInput();

                if (keyStroke != null) {
                    // Check for mouse events
                    if (keyStroke instanceof MouseAction) {
                        MouseAction mouseAction = (MouseAction) keyStroke;

                        if (mouseAction.getActionType() == MouseActionType.CLICK_DOWN) {
                            mousePressed = true;
                        } else if (mouseAction.getActionType() == MouseActionType.CLICK_RELEASE) {
                            mousePressed = false;
                        }

                        // Draw if mouse is pressed and within drawable area
                        if (mousePressed) {
                            int row = mouseAction.getPosition().getRow();
                            int col = mouseAction.getPosition().getColumn();
                            if (row < maxRow - 2 && col < maxCol) {
                                screen.setCharacter(col, row,
                                        new TextCharacter(' ',
                                                TextColor.ANSI.DEFAULT,
                                                COLORS[drawColor]));
                                screen.refresh();
                            }
                        }
                    } else {
                        // Keyboard input
                        if (keyStroke.getKeyType() == KeyType.Escape) {
                            running = false;
                        } else if (keyStroke.getKeyType() == KeyType.Character) {
                            char c = keyStroke.getCharacter();
                            if (c == 'q' || c == 'Q') {
                                running = false;
                            } else if (c >= '0' && c <= '7') {
                                drawColor = c - '0';
                                drawStatusBar(screen, maxCol, drawColor);
                                screen.refresh();
                            }
                        }
                    }
                } else {
                    // Small sleep to avoid busy-waiting
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } finally {
            screen.stopScreen();
        }
    }

    private static void drawStatusBar(Screen screen, int maxCol, int drawColor) {
        String statusText = "Current Draw color: ";
        TerminalSize size = screen.getTerminalSize();
        int statusRow = size.getRows() - 2;

        for (int col = 0; col < maxCol && col < statusText.length(); col++) {
            screen.setCharacter(col, statusRow,
                    new TextCharacter(statusText.charAt(col),
                            TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }

        // Draw color sample
        screen.setCharacter(statusText.length(), statusRow,
                new TextCharacter(' ', TextColor.ANSI.DEFAULT, COLORS[drawColor]));
        screen.setCharacter(statusText.length() + 1, statusRow,
                new TextCharacter(' ', TextColor.ANSI.DEFAULT, COLORS[drawColor]));
        screen.setCharacter(statusText.length() + 2, statusRow,
                new TextCharacter(' ', TextColor.ANSI.DEFAULT, COLORS[drawColor]));
    }

    private static void drawInstructionBar(Screen screen, int maxCol) {
        String instructions = "Esc to exit. Number keys to change cursor draw color. Left mouse down to draw current color.";
        TerminalSize size = screen.getTerminalSize();
        int instrRow = size.getRows() - 1;

        for (int col = 0; col < maxCol && col < instructions.length(); col++) {
            screen.setCharacter(col, instrRow,
                    new TextCharacter(instructions.charAt(col),
                            TextColor.ANSI.WHITE, TextColor.ANSI.BLUE));
        }

        // Fill rest of line with blue background
        for (int col = instructions.length(); col < maxCol; col++) {
            screen.setCharacter(col, instrRow,
                    new TextCharacter(' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLUE));
        }
    }
}
