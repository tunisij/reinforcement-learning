package com.umich.tunisij.robot;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class Robot {

    private MazeContext mazeContext;
    private Node[][] maze;
    private Node[][] posterior;
    private double[][] transitionMatrixMovingNorth;
    private double[][] transitionMatrixMovingEast;

    private static final double FALSE_POSITIVE_SENSOR_RATE = 5.0;
    private static final double POSITIVE_SENSOR_RATE = 90.0;

    public Robot(MazeContext mazeContext) {
        this.mazeContext = mazeContext;
        this.maze = mazeContext.getMaze();
        this.posterior = mazeContext.getPrior();

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
        transitionMatrixMovingNorth = new double[posterior.length * posterior[0].length][posterior.length * posterior[0].length];
        initializeTransitionMatrix(transitionMatrixMovingNorth);

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

        transitionMatrixMovingNorth[rowIndex][eastPosition] += 10.0;
        transitionMatrixMovingNorth[rowIndex][westPosition] += 10.0;
        transitionMatrixMovingNorth[rowIndex][northPosition] += 80.0;
    }




    private void setTransitionMatrixMovingEast() {
        transitionMatrixMovingEast = new double[posterior.length * posterior[0].length][posterior.length * posterior[0].length];
        initializeTransitionMatrix(transitionMatrixMovingEast);

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

        transitionMatrixMovingEast[rowIndex][northPosition] += 10.0;
        transitionMatrixMovingEast[rowIndex][southPosition] += 10.0;
        transitionMatrixMovingEast[rowIndex][eastPosition] += 80.0;
    }

    public void sense(boolean west, boolean north, boolean east, boolean south) {
        filter(true);
        System.out.println(getPosterior());
        filter(north);
        System.out.println(getPosterior());
        filter(east);
        System.out.println(getPosterior());
        filter(south);
        System.out.println(getPosterior());
    }

    private boolean isObstacle(Direction direction) {
        return false;
    }

    private void filter(boolean sensed) {
        for (int i = 0; i < posterior.length; i++) {
            for (int j = 0; j < posterior[i].length; j++) {
                if (posterior[i][j].getValue().equals("0.0")) {
                    return;
                }

                //make sure to look at inverses
                double noObstacle = .05 * (Double.parseDouble(posterior[i][j].getValue()) / 100);
                double hasObstacle = .9 * (Double.parseDouble(posterior[i][j].getValue()) / 100);

                double value = 0.0;
                if (sensed) {
                    value = hasObstacle / ((noObstacle * mazeContext.getNonObstacleCount()) + (hasObstacle * mazeContext.getObstacleCount()));
                } else {
                    value = noObstacle / ((noObstacle * mazeContext.getNonObstacleCount()) + (hasObstacle * mazeContext.getObstacleCount()));
                }
                posterior[i][j].setValue(value + "");
            }
        }
    }

    public void move(Direction direction) {

    }

    public String getPosterior() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < this.posterior.length; row++) {
            for (int column = 0; column < this.posterior[row].length; column++) {
                sb.append(this.posterior[row][column].getValue() + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void initializeTransitionMatrix(double[][] transitionMatrix) {
        for (int i = 0; i < posterior.length; i++) {
            for (int j = 0; j < posterior[i].length; j++) {
                transitionMatrix[i][j] = 0.0;
            }
        }
    }
}

