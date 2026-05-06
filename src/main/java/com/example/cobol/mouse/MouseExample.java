package com.example.cobol.mouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
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
 * <p>The original program is a tiny ncurses-based paint program that
 * tracked the mouse and drew filled "pixels" in the current color. Java
 * has no portable mouse-on-the-terminal story, so we instead build the
 * smallest possible Swing equivalent: a window the user can drag the mouse
 * across and number keys (1–7) to change the active drawing color.
 */
public final class MouseExample {

    private static final Color[] PALETTE = {
        Color.BLACK, Color.RED, Color.GREEN, Color.YELLOW,
        Color.BLUE, Color.MAGENTA, Color.CYAN, Color.WHITE
    };

    private MouseExample() {}

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Mouse example requires a graphical environment;");
            System.out.println("running headless — skipping. (Set DISPLAY to launch the GUI.)");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mouse Paint (1-7 = color, Esc = quit)");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            PaintPanel panel = new PaintPanel();
            JLabel status = new JLabel("Color: 1 (red)");
            frame.setLayout(new BorderLayout());
            frame.add(panel, BorderLayout.CENTER);
            frame.add(status, BorderLayout.SOUTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.setOnColorChange(c -> status.setText("Color: " + c
                    + " (" + colorName(PALETTE[c]) + ")"));
            panel.setOnQuit(frame::dispose);
        });
    }

    private static String colorName(Color c) {
        if (c.equals(Color.RED)) return "red";
        if (c.equals(Color.GREEN)) return "green";
        if (c.equals(Color.YELLOW)) return "yellow";
        if (c.equals(Color.BLUE)) return "blue";
        if (c.equals(Color.MAGENTA)) return "magenta";
        if (c.equals(Color.CYAN)) return "cyan";
        if (c.equals(Color.WHITE)) return "white";
        return "black";
    }

    private static final class PaintPanel extends JPanel {
        private final List<int[]> dots = new ArrayList<>();
        private int colorIndex = 1;
        private java.util.function.IntConsumer onColorChange;
        private Runnable onQuit;

        PaintPanel() {
            setPreferredSize(new Dimension(640, 480));
            setBackground(Color.BLACK);
            setFocusable(true);
            requestFocusInWindow();

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dots.add(new int[] {e.getX(), e.getY(), colorIndex});
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    requestFocusInWindow();
                    dots.add(new int[] {e.getX(), e.getY(), colorIndex});
                    repaint();
                }
            });

            addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(java.awt.event.KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code == java.awt.event.KeyEvent.VK_ESCAPE
                            || e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
                        if (onQuit != null) {
                            onQuit.run();
                        }
                        return;
                    }
                    char c = e.getKeyChar();
                    if (c >= '1' && c <= '7') {
                        colorIndex = c - '0';
                        if (onColorChange != null) {
                            onColorChange.accept(colorIndex);
                        }
                    }
                }
            });
        }

        void setOnColorChange(java.util.function.IntConsumer cb) {
            this.onColorChange = cb;
        }

        void setOnQuit(Runnable cb) {
            this.onQuit = cb;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int[] dot : dots) {
                g.setColor(PALETTE[dot[2]]);
                g.fillRect(dot[0], dot[1], 4, 4);
            }
        }
    }
}
