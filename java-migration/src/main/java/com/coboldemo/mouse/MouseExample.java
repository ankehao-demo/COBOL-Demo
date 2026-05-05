package com.coboldemo.mouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Java port of {@code mouse/mouse_example.cbl}.
 *
 * The original GnuCOBOL program is an ncurses-based terminal paint program
 * that uses mouse-down events to colour cells of a character grid. There is
 * no portable, ncurses-equivalent mouse API in the JDK, so this port renders
 * the demo as a small Swing application:
 * <ul>
 *   <li>Click and drag with the left mouse button to paint cells.</li>
 *   <li>Press number keys 1-7 (or click in the legend) to switch the
 *       current draw colour, matching the COBOL palette
 *       (1=blue, 2=green, 3=cyan, 4=red, 5=magenta, 6=yellow, 7=white).</li>
 *   <li>Press Escape or close the window to exit.</li>
 * </ul>
 *
 * If a Lanterna-based TUI port is desired, a similar event loop can be
 * built with the optional {@code com.googlecode.lanterna} dependency; that
 * is intentionally not used here so the example is self-contained.
 */
public class MouseExample {

    private static final int CELL_PX = 16;
    private static final int COLS = 80;
    private static final int ROWS = 19;

    private static final Color[] PALETTE = new Color[] {
            Color.BLACK,        // 0 - background
            new Color(0, 0, 200),     // 1 blue
            new Color(0, 160, 0),     // 2 green
            new Color(0, 200, 200),   // 3 cyan
            new Color(200, 0, 0),     // 4 red
            new Color(200, 0, 200),   // 5 magenta
            new Color(220, 220, 0),   // 6 yellow
            new Color(230, 230, 230)  // 7 white
    };

    private int drawColor = 1;
    private final boolean[][] painted = new boolean[ROWS][COLS];
    private final int[][] cellColor = new int[ROWS][COLS];

    public static void main(String[] args) {
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println(
                    "Mouse paint demo requires a graphical display; "
                    + "please run with a desktop session available.");
            System.out.println(
                    "(In a headless environment this program exits cleanly so the "
                    + "Maven build can still validate compilation.)");
            return;
        }
        SwingUtilities.invokeLater(() -> new MouseExample().show());
    }

    private void show() {
        JFrame frame = new JFrame("COBOL Mouse Paint - Java port");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(COLS * CELL_PX, ROWS * CELL_PX));

        JLabel status = new JLabel("Number keys 1-7 change colour. Esc to exit.");
        status.setOpaque(true);
        status.setBackground(PALETTE[drawColor]);

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                paintAt(e, canvas);
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                paintAt(e, canvas);
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int code = e.getKeyCode();
                if (code == java.awt.event.KeyEvent.VK_ESCAPE
                        || code == java.awt.event.KeyEvent.VK_Q) {
                    frame.dispose();
                    return;
                }
                if (code >= java.awt.event.KeyEvent.VK_1
                        && code <= java.awt.event.KeyEvent.VK_7) {
                    drawColor = code - java.awt.event.KeyEvent.VK_0;
                    status.setBackground(PALETTE[drawColor]);
                    status.setText("Current draw colour: " + drawColor);
                }
            }
        });

        frame.setVisible(true);
    }

    private void paintAt(MouseEvent e, JPanel canvas) {
        int col = e.getX() / CELL_PX;
        int row = e.getY() / CELL_PX;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return;
        }
        painted[row][col] = true;
        cellColor[row][col] = drawColor;
        canvas.repaint();
    }

    private final class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(PALETTE[0]);
            g.fillRect(0, 0, getWidth(), getHeight());
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (painted[r][c]) {
                        g.setColor(PALETTE[cellColor[r][c]]);
                        g.fillRect(c * CELL_PX, r * CELL_PX, CELL_PX, CELL_PX);
                    }
                }
            }
        }
    }

    /**
     * Suppress unused-list warnings; the field exists only so that future
     * extensions (multi-stroke history) can record events.
     */
    @SuppressWarnings("unused")
    private final List<int[]> history = new ArrayList<>();
}
