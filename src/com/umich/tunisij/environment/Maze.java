package com.umich.tunisij.environment;

import java.util.List;
import java.util.Map;

public class Maze {

    private Node[][] maze;

    private int startRow;
    private int startColumn;
    private int goalRow;
    private int goalColumn;

    public Maze(final int lengthRow, final int lengthColumn) {
        initializeMaze(lengthRow, lengthColumn);
    }

    public Node[][] getMaze() {
        return maze;
    }

    public boolean setValueByPosition(String value, int row, int column) {
        if (maze[row][column].getValue().equals("[]")) {
            maze[row][column].setValue(value);
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[row].length; column++) {
                sb.append(maze[row][column].getValue() + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    protected void initializeMaze(int lengthRow, int lengthColumn) {
        maze = new Node[lengthRow][lengthColumn];

        for (int row = 0; row < lengthRow; row++) {
            for (int column = 0; column < lengthColumn; column++) {
                maze[row][column] = new Node(Map.entry(row, column));
                maze[row][column].setValue("[]");
            }
        }
    }

    public void setGoalPosition(int row, int column) {
        goalRow = row;
        goalColumn = column;
        maze[row][column].setValue("GG");
    }

    public void setWallPositions(List<Map.Entry<Integer, Integer>> wallPositions) {
        for (Map.Entry<Integer, Integer> wallPosition : wallPositions) {
            maze[wallPosition.getKey()][wallPosition.getValue()].setValue("##");
        }
    }

    public int getLength() {
        return this.maze[0].length;
    }

    public int getHeight() {
        return this.maze.length;
    }

    public Node getNode(Map.Entry<Integer, Integer> position) {
        return maze[position.getKey()][position.getValue()];
    }

}
