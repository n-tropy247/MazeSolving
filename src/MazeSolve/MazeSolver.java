/* 
 * Copyright (C) 2018 Ryan Castelli
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package MazeSolve;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Maze Solving w/ Graphics
 *
 * @author NTropy
 * @version 1.0
 */
public class MazeSolver {

    private static final char MAZE[][] = {
        {'S', '#', '#', '#', '#', '#'},
        {'.', '.', '.', '.', '.', '#'},
        {'#', '.', '#', '#', '#', '#'},
        {'#', '.', '#', '#', '#', '#'},
        {'.', '.', '.', '#', '.', 'G'},
        {'#', '#', '.', '.', '.', '#'},};

    private static final int BALL_RADIUS = 4;
    private static final int RADIUS = 5;

    private static final JFrame FRAME = new JFrame();

    private static final JPanel PANEL = new Draw();

    private static boolean solved;

    private static int ballX;
    private static int ballY;
    private static int forCounterX;
    private static int forCounterY;
    private static int startX;
    private static int startY;

    private static int[] wallX;
    private static int[] wallY;

    /**
     * Creates JFrame
     *
     * @param args
     */
    public static void main(String args[]) {
        solved = false;

        FRAME.setResizable(false);

        FRAME.getContentPane().add(PANEL);
        PANEL.setPreferredSize(new Dimension(300, 200));
        PANEL.setFocusable(true);
        PANEL.requestFocusInWindow();

        FRAME.pack();
        FRAME.setBackground(Color.WHITE);
        FRAME.setLayout(null);
        FRAME.setVisible(true);
        FRAME.repaint();
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        wallX = new int[MAZE.length * MAZE[0].length];
        wallY = new int[MAZE.length * MAZE[0].length];
        forCounterX = 0;
        forCounterY = 0;
        for (int row = 0; row < MAZE.length; row++) {
            for (int col = 0; col < MAZE[0].length; col++) {
                if (MAZE[row][col] == '#') {
                    wallX[forCounterX++] = col * 20;
                    wallY[forCounterY++] = row * 10;
                }
            }
        }
        forCounterX = 0;
        FRAME.repaint();

        //find start
        for (int row = 0; row < MAZE.length; row++) {
            for (int col = 0; col < MAZE[0].length; col++) {
                if (MAZE[row][col] == 'S') {
                    ballX = col;
                    ballY = col;
                    startX = col;
                    startY = row;
                }
            }
        }
        FRAME.repaint();

        recursiveMaze(startX, startY, 'P');
    }

    /**
     * Maze solver
     *
     * @param x
     * @param y
     * @param lastDir
     */
    private static void recursiveMaze(int x, int y, char lastDir) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
        }
        if (MAZE[y][x] == 'G') {
            solved = true;
            ballX = x * 23;
            ballY = y * 11;
            FRAME.repaint();
        }

        if (!solved && y + 1 < MAZE.length && MAZE[y + 1][x] != '#' && lastDir != 'S') {
            MAZE[y][x] = '@';
            lastDir = 'N';
            ballX = x * 23;
            ballY = y * 11;
            FRAME.repaint();
            recursiveMaze(x, y + 1, lastDir);
        }

        if (!solved && y - 1 > 0 && MAZE[y - 1][x] != '#' && lastDir != 'N') {
            MAZE[y][x] = '@';
            lastDir = 'S';
            ballX = x * 23;
            ballY = y * 11;
            FRAME.repaint();
            recursiveMaze(x, y - 1, lastDir);
        }

        if (!solved && x + 1 < MAZE[0].length && MAZE[y][x + 1] != '#' && lastDir != 'W') {
            MAZE[y][x] = '@';
            lastDir = 'E';
            ballX = x * 23;
            ballY = y * 11;
            FRAME.repaint();
            recursiveMaze(x + 1, y, lastDir);
        }

        if (!solved && x - 1 > 0 && MAZE[y][x - 1] != '#' && lastDir != 'E') {
            MAZE[y][x] = '@';
            lastDir = 'W';
            ballX = x * 23;
            ballY = y * 11;
            FRAME.repaint();
            recursiveMaze(x - 1, y, lastDir);
        }
    }

    /**
     * Handles graphics for maze solver
     */
    private static class Draw extends JPanel {
        
    private static final int WIDTH_OVERRIDE = 5;

    /**
     * Paints in ball and walls
     * @param g 
     */
        @Override
        public void paintComponent(Graphics g) {
            try {
                g.setColor(Color.black);

                for (int row = 0; row < wallX.length; row++) {
                    if (wallX[row] == 0) {
                        g.fillRect(wallX[row], wallY[row], WIDTH_OVERRIDE + 2 * RADIUS, WIDTH_OVERRIDE);
                    } else {
                        g.fillRect(wallX[row], wallY[row], WIDTH_OVERRIDE + 2 * RADIUS, WIDTH_OVERRIDE);
                    }
                }

                g.setColor(Color.white);
                g.fillRect(0, 0, WIDTH_OVERRIDE + 2 * RADIUS, WIDTH_OVERRIDE);
                g.setColor(Color.black);

                g.fillRect(0, MAZE.length * 10, (WIDTH_OVERRIDE + 2 * RADIUS) * MAZE[0].length, 1);

                g.setColor(Color.red);

                g.fillOval(ballX - RADIUS + 1, ballY - RADIUS + 1, 2 * BALL_RADIUS, 2 * BALL_RADIUS - 1);
            } catch (ArrayIndexOutOfBoundsException aioob) {
                System.err.println("Array index DNE");
            }
        }
    }
}
