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
 * Migrated from mouse/mouse_example.cbl
 * A simple paint program demonstrating mouse functionality using Lanterna.
 * Number keys 0-7 change the draw color. Left mouse click draws. Esc/Q to quit.
 */
public class MouseExample {

    private static final TextColor.ANSI[] COLORS = {
            TextColor.ANSI.BLACK,
            TextColor.ANSI.RED,
            TextColor.ANSI.GREEN,
            TextColor.ANSI.YELLOW,
            TextColor.ANSI.BLUE,
            TextColor.ANSI.MAGENTA,
            TextColor.ANSI.CYAN,
            TextColor.ANSI.WHITE
    };

    public static void main(String[] args) {
        Terminal terminal = null;
        Screen screen = null;

        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            terminal = factory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.setCursorPosition(null); // hide cursor

            int drawColor = 1; // default draw color (RED)
            boolean mouseDown = false;

            // Draw status bar at row 19
            drawStatusBar(screen, drawColor);

            // Draw instructions at row 20
            String instructions = "Esc to exit. Number keys to change cursor draw color. Left mouse down to draw current color.";
            for (int i = 0; i < instructions.length() && i < 80; i++) {
                screen.setCharacter(i, 20,
                        new TextCharacter(instructions.charAt(i),
                                TextColor.ANSI.WHITE, TextColor.ANSI.BLUE, SGR.BOLD));
            }

            screen.refresh();

            boolean running = true;
            while (running) {
                KeyStroke keyStroke = screen.pollInput();

                if (keyStroke == null) {
                    Thread.sleep(50);
                    continue;
                }

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    running = false;
                    continue;
                }

                if (keyStroke.getKeyType() == KeyType.Character) {
                    char c = keyStroke.getCharacter();

                    if (c == 'q' || c == 'Q') {
                        running = false;
                        continue;
                    }

                    if (c >= '0' && c <= '7') {
                        drawColor = c - '0';
                        drawStatusBar(screen, drawColor);
                        screen.refresh();
                    }
                }

                if (keyStroke.getKeyType() == KeyType.MouseEvent) {
                    MouseAction mouse = (MouseAction) keyStroke;
                    int row = mouse.getPosition().getRow();
                    int col = mouse.getPosition().getColumn();

                    if (mouse.getActionType() == MouseActionType.CLICK_DOWN) {
                        mouseDown = true;
                    } else if (mouse.getActionType() == MouseActionType.CLICK_RELEASE) {
                        mouseDown = false;
                    }

                    // Draw in the drawable area (rows 0-18, cols 0-79)
                    if (mouseDown && row < 19 && col < 80) {
                        screen.setCharacter(col, row,
                                new TextCharacter(' ', COLORS[drawColor], COLORS[drawColor]));
                        screen.refresh();
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Terminal error: " + e.getMessage());
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    private static void drawStatusBar(Screen screen, int drawColor) {
        String status = "Current Draw color:";
        for (int i = 0; i < status.length(); i++) {
            screen.setCharacter(i, 19,
                    new TextCharacter(status.charAt(i), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        // Color preview
        screen.setCharacter(status.length(), 19,
                new TextCharacter(' ', COLORS[drawColor], COLORS[drawColor]));
    }
}
