package com.umich.tunisij.environment;

import java.util.List;
import java.util.Map;

public class Maze {

    private Node[][] maze;

    public Maze(final int lengthRow, final int lengthColumn, List<Map.Entry<Integer, Integer>> wallPositions) {
        initializeMaze(lengthRow, lengthColumn, wallPositions);
    }

    public Node[][] getMaze() {
        return maze;
    }

    public String getOptimalAction() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[row].length; column++) {
                sb.append(maze[row][column].getOptimalAction() + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getQValues() {
        int maxStringLength = 10;
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[row].length; column++) {
                String qValue = maze[row][column].getQValue(Direction.NORTH);
                int leftPadding = ((maxStringLength - qValue.length()) / 2) + 1;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", qValue)) + "\t");
            }

            sb.append("\n");

            for (int column = 0; column < maze[row].length; column++) {
                String qValue = maze[row][column].getQValue(Direction.WEST)+ " " + maze[row][column].getQValue(Direction.EAST);
                int leftPadding = ((maxStringLength - qValue.length()) / 2) + 3;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", qValue)) + "\t");
            }

            sb.append("\n");

            for (int column = 0; column < maze[row].length; column++) {
                String qValue = maze[row][column].getQValue(Direction.SOUTH);
                int leftPadding = ((maxStringLength - qValue.length()) / 2) + 1;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", qValue)) + "\t");
            }

            sb.append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getAccessFrequency() {
        int maxStringLength = 10;
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < maze.length; row++) {
            for (int column = 0; column < maze[row].length; column++) {
                String accessFrequency = maze[row][column].getAccessFrequency(Direction.NORTH);
                int leftPadding = ((maxStringLength - accessFrequency.length()) / 2) + 1;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", accessFrequency)) + "\t");
            }

            sb.append("\n");

            for (int column = 0; column < maze[row].length; column++) {
                String accessFrequency = maze[row][column].getAccessFrequency(Direction.WEST)+ " " + maze[row][column].getAccessFrequency(Direction.EAST);
                int leftPadding = ((maxStringLength - accessFrequency.length()) / 2) + 3;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", accessFrequency)) + "\t");
            }

            sb.append("\n");

            for (int column = 0; column < maze[row].length; column++) {
                String accessFrequency = maze[row][column].getAccessFrequency(Direction.SOUTH);
                int leftPadding = ((maxStringLength - accessFrequency.length()) / 2) + 1;
                sb.append(String.format("%-" + maxStringLength + "s", String.format("%" + leftPadding + "s", accessFrequency)) + "\t");
            }

            sb.append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    protected void initializeMaze(int lengthRow, int lengthColumn, List<Map.Entry<Integer, Integer>> wallPositions) {
        maze = new Node[lengthRow][lengthColumn];

        for (int row = 0; row < lengthRow; row++) {
            for (int column = 0; column < lengthColumn; column++) {
                maze[row][column] = new Node(Map.entry(row, column));
            }
        }

        setWallPositions(wallPositions);
    }

    public void setGoalPosition(int row, int column) {
        maze[row][column].setQValue(Direction.NORTH, "50");
        maze[row][column].setQValue(Direction.EAST, "50");
        maze[row][column].setQValue(Direction.WEST, "50");
        maze[row][column].setQValue(Direction.SOUTH, "50");

        maze[row][column].setAccessFrequency(Direction.NORTH, "0");
        maze[row][column].setAccessFrequency(Direction.EAST, "0");
        maze[row][column].setAccessFrequency(Direction.WEST, "0");
        maze[row][column].setAccessFrequency(Direction.SOUTH, "0");

        maze[row][column].setGoal(true);
    }

    public void setWallPositions(List<Map.Entry<Integer, Integer>> wallPositions) {
        for (Map.Entry<Integer, Integer> wallPosition : wallPositions) {
            maze[wallPosition.getKey()][wallPosition.getValue()].setWall(true);
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
