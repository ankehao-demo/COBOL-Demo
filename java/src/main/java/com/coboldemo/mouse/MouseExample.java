package com.coboldemo.mouse;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.MouseActionType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * Java equivalent of mouse/mouse_example.cbl
 *
 * A simple paint program demonstrating mouse functionality. The COBOL program
 * uses GnuCOBOL's screen mode with mouse flags. In Java, Lanterna provides
 * terminal-based mouse event handling.
 *
 * COBOL Mapping:
 *   COB-AUTO-MOUSE-HANDLING + flags   → Lanterna mouse input mode
 *   COB-SCR-LEFT-PRESSED             → MouseActionType.CLICK_DOWN
 *   COB-SCR-LEFT-RELEASED            → MouseActionType.CLICK_RELEASE
 *   COB-SCR-ESC                      → KeyType.Escape
 *   BACKGROUND-COLOR                 → TextColor.ANSI
 *   ws-cursor-position               → MouseAction.getPosition()
 *
 * Note: Requires a real terminal with mouse support. Will not work in most IDEs.
 */
public class MouseExample {

    // GnuCOBOL color order: 0=Black, 1=Blue, 2=Green, 3=Cyan, 4=Red, 5=Magenta, 6=Yellow, 7=White
    private static final TextColor.ANSI[] COLORS = {
        TextColor.ANSI.BLACK,
        TextColor.ANSI.BLUE,
        TextColor.ANSI.GREEN,
        TextColor.ANSI.CYAN,
        TextColor.ANSI.RED,
        TextColor.ANSI.MAGENTA,
        TextColor.ANSI.YELLOW,
        TextColor.ANSI.WHITE
    };

    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();

            int drawColor = 1;
            boolean mouseClicked = false;

            // Display instructions
            terminal.setCursorPosition(0, 18);
            putString(terminal, 0, 18, "Current Draw color:");
            putColorBlock(terminal, 20, 18, COLORS[drawColor]);
            putString(terminal, 0, 19,
                    "Esc to exit. Number keys to change cursor draw color. Left mouse down to draw.");

            terminal.flush();

            boolean running = true;
            while (running) {
                KeyStroke keyStroke = terminal.pollInput();
                if (keyStroke == null) {
                    Thread.sleep(50);
                    continue;
                }

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    running = false;
                    continue;
                }

                if (keyStroke.getKeyType() == KeyType.Character) {
                    char c = Character.toUpperCase(keyStroke.getCharacter());
                    if (c == 'Q') {
                        running = false;
                        continue;
                    }
                    if (Character.isDigit(c)) {
                        drawColor = Math.min(c - '0', 7);
                        putColorBlock(terminal, 20, 18, COLORS[drawColor]);
                        terminal.flush();
                    }
                }

                if (keyStroke instanceof MouseAction) {
                    MouseAction mouseAction = (MouseAction) keyStroke;
                    if (mouseAction.getActionType() == MouseActionType.CLICK_DOWN) {
                        mouseClicked = true;
                    } else if (mouseAction.getActionType() == MouseActionType.CLICK_RELEASE) {
                        mouseClicked = false;
                    }

                    if (mouseClicked) {
                        TerminalPosition pos = mouseAction.getPosition();
                        if (pos.getRow() < 18 && pos.getColumn() < 80) {
                            terminal.setCursorPosition(pos.getColumn(), pos.getRow());
                            terminal.putCharacter(new TextCharacter(' ',
                                    TextColor.ANSI.DEFAULT, COLORS[drawColor]).getCharacter());
                            terminal.flush();
                        }
                    }
                }
            }

            terminal.exitPrivateMode();
            terminal.close();

        } catch (IOException | InterruptedException e) {
            System.err.println("Terminal error: " + e.getMessage());
            System.err.println("This program requires a real terminal with mouse support.");
        }
    }

    private static void putString(Terminal terminal, int col, int row, String text) throws IOException {
        terminal.setCursorPosition(col, row);
        for (char c : text.toCharArray()) {
            terminal.putCharacter(c);
        }
    }

    private static void putColorBlock(Terminal terminal, int col, int row, TextColor.ANSI color) throws IOException {
        terminal.setCursorPosition(col, row);
        terminal.setBackgroundColor(color);
        terminal.putCharacter(' ');
        terminal.resetColorAndSGR();
    }
}
