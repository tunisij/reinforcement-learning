package com.umich.tunisij.robot;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class Robot {

    private Node[][] maze;
    private Node[][] prior;
    private double[][] transitionMatrixMovingNorth;
    private double[][] transitionMatrixMovingEast;

    public Robot(MazeContext mazeContext) {
        this.maze = mazeContext.getMaze();
        this.prior = mazeContext.getPrior();

        setTransitionMatrixMovingNorth();
        setTransitionMatrixMovingEast();

        mazeContext.print(transitionMatrixMovingNorth);
        mazeContext.print(transitionMatrixMovingEast);
    }

    //returns itself if blocked
    public Node getNodeByDirection(int i, int j, Direction direction) {
        if (direction.equals(Direction.NORTH)) {
            return i - 1 < 0 ? maze[i][j] : maze[i-1][j].getValue().equals("##") ? maze[i][j] : maze[i-1][j];
        } else if (direction.equals(Direction.EAST)) {
            return j + 1 > maze.length ? maze[i][j] : maze[i][j+1].getValue().equals("##") ? maze[i][j] : maze[i][j+1];
        } else if (direction.equals(Direction.WEST)) {
            return j - 1 < 0 ? maze[i][j] : maze[i][j-1].getValue().equals("##") ? maze[i][j] : maze[i][j-1];
        } else if (direction.equals(Direction.SOUTH)) {
            return i + 1 >= maze.length ? maze[i][j] : maze[i+1][j].getValue().equals("##") ? maze[i][j] : maze[i+1][j];
        }
        return maze[i][j];
    }

    private void setTransitionMatrixMovingNorth() {
        transitionMatrixMovingNorth = new double[prior.length * prior[0].length][prior.length * prior[0].length];
        initializeTransitionMatrix(transitionMatrixMovingNorth);

        //iterates 88 times
        for (int i = 0; i < transitionMatrixMovingNorth.length; i++) {
            setTransitionMatrixMovingNorthRowForNode(i); //for each position in transition matrix, set index
        }
    }

    private void setTransitionMatrixMovingNorthRowForNode(int rowIndex) {
        int mazeRow = rowIndex / maze[0].length;
        int mazeColumn = rowIndex % maze[0].length;

        Node east = getNodeByDirection(mazeRow, mazeColumn, Direction.EAST);
        Node west = getNodeByDirection(mazeRow, mazeColumn, Direction.WEST);
        Node north = getNodeByDirection(mazeRow, mazeColumn, Direction.NORTH);

        int eastPosition = east.getPosition().getKey() * maze[0].length + east.getPosition().getValue();
        int westPosition = west.getPosition().getKey() * maze[0].length + west.getPosition().getValue();
        int northPosition = north.getPosition().getKey() * maze[0].length + north.getPosition().getValue();

        transitionMatrixMovingNorth[rowIndex][eastPosition] += 0.1;
        transitionMatrixMovingNorth[rowIndex][westPosition] += 0.1;
        transitionMatrixMovingNorth[rowIndex][northPosition] += 0.8;
    }




    private void setTransitionMatrixMovingEast() {
        transitionMatrixMovingEast = new double[prior.length * prior[0].length][prior.length * prior[0].length];
        initializeTransitionMatrix(transitionMatrixMovingEast);

        //iterates 88 times
        for (int i = 0; i < transitionMatrixMovingEast.length; i++) {
            setTransitionMatrixMovingEastRowForNode(i); //for each position in transition matrix, set index
        }
    }

    private void setTransitionMatrixMovingEastRowForNode(int rowIndex) {
        int mazeRow = rowIndex / maze[0].length;
        int mazeColumn = rowIndex % maze[0].length;

        Node north = getNodeByDirection(mazeRow, mazeColumn, Direction.NORTH);
        Node south = getNodeByDirection(mazeRow, mazeColumn, Direction.SOUTH);
        Node east = getNodeByDirection(mazeRow, mazeColumn, Direction.EAST);

        int northPosition = north.getPosition().getKey() * maze[0].length + north.getPosition().getValue();
        int southPosition = south.getPosition().getKey() * maze[0].length + south.getPosition().getValue();
        int eastPosition = east.getPosition().getKey() * maze[0].length + east.getPosition().getValue();

        transitionMatrixMovingEast[rowIndex][northPosition] += 0.1;
        transitionMatrixMovingEast[rowIndex][southPosition] += 0.1;
        transitionMatrixMovingEast[rowIndex][eastPosition] += 0.8;
    }

    public void sense(boolean west, boolean north, boolean east, boolean south) {

    }

    public void move(Direction direction) {

    }

    public String getPosterior() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < this.prior.length; row++) {
            for (int column = 0; column < this.prior[row].length; column++) {
                sb.append(this.prior[row][column].getValue() + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void initializeTransitionMatrix(double[][] transitionMatrix) {
        for (int i = 0; i < prior.length; i++) {
            for (int j = 0; j < prior[i].length; j++) {
                transitionMatrix[i][j] = 0.0;
            }
        }
    }
}

