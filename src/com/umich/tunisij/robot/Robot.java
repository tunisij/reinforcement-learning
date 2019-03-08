package com.umich.tunisij.robot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class Robot {

    private MazeContext mazeContext;
    private Node[][] maze;
    private Node[][] posterior;
    private double[][] preNormalized;
    private double[][] transitionMatrixMovingNorth;
    private double[][] transitionMatrixMovingEast;

    private static final double FALSE_POSITIVE_SENSOR_RATE = .05;
    private static final double POSITIVE_SENSOR_RATE = .9;

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
            return j + 1 >= maze[i].length ? maze[i][j] : maze[i][j+1].getValue().equals("##") ? maze[i][j] : maze[i][j+1];
        } else if (direction.equals(Direction.WEST)) {
            return j - 1 < 0 ? maze[i][j] : maze[i][j-1].getValue().equals("##") ? maze[i][j] : maze[i][j-1];
        } else if (direction.equals(Direction.SOUTH)) {
            return i + 1 >= maze.length ? maze[i][j] : maze[i+1][j].getValue().equals("##") ? maze[i][j] : maze[i+1][j];
        }
        return maze[i][j];
    }

    private void setTransitionMatrixMovingNorth() {
        transitionMatrixMovingNorth = new double[posterior.length * posterior[0].length][posterior.length * posterior[0].length];
        initializeMatrix(posterior.length, posterior[0].length, transitionMatrixMovingNorth);

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
        initializeMatrix(posterior.length, posterior[0].length, transitionMatrixMovingEast);

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

    private boolean hasObstacle(int i, int j, Direction direction) {
        if (direction == null) {
            return maze[i][j].getValue().equals("##");
        } else if (direction.equals(Direction.NORTH)) {
            return i - 1 < 0 ? true : maze[i-1][j].getValue().equals("##") ? true : false;
        } else if (direction.equals(Direction.EAST)) {
            return j + 1 >= maze[i].length ? true : maze[i][j+1].getValue().equals("##") ? true : false;
        } else if (direction.equals(Direction.WEST)) {
            return j - 1 < 0 ? true : maze[i][j-1].getValue().equals("##") ? true : false;
        } else if (direction.equals(Direction.SOUTH)) {
            return i + 1 >= maze.length ? true : maze[i+1][j].getValue().equals("##") ? true : false;
        }
        return false;
    }

    private double getSensingValue(boolean sensed, boolean hasObstacle) {
        double value;

        if (sensed && hasObstacle) {
            value = POSITIVE_SENSOR_RATE;
        } else if (sensed && !hasObstacle) {
            value = 1 - POSITIVE_SENSOR_RATE;
        } else if (!sensed && !hasObstacle) {
            value = 1 - FALSE_POSITIVE_SENSOR_RATE;
        } else {
            value = FALSE_POSITIVE_SENSOR_RATE;
        }
        return value;
    }

    public void sense(boolean westSensed, boolean northSensed, boolean eastSensed, boolean southSensed) {
        double[][] evidence = new double[maze.length * maze[0].length][maze.length * maze[0].length];
        List<String> normalizationList = new ArrayList<>();
        preNormalized = new double[maze.length][maze[0].length];
        for (int i = 0; i < posterior.length; i++) {
            for (int j = 0; j < posterior[i].length; j++) {
                //don't update actual obstacles
                if (hasObstacle(i, j, null)) {
                    continue;
                }
                //obstacle: if sense && map shows one in that direction, use .9
                //obstacle: if sense && map show differently, use 1 - correct (.1)
                //no ob: if sense && map shows one in that direction, use .95
                //no ob: if sense && map shows differently, use 1 - correct (.05)

                //WNES is order
                //P(Zt=(-,-,-,-)|St=0,0) = .05 * .05 * .95 * .95 = .0023

                boolean westHasObstacle = hasObstacle(i, j, Direction.WEST);
                boolean northHasObstacle = hasObstacle(i, j, Direction.NORTH);
                boolean eastHasObstacle = hasObstacle(i, j, Direction.EAST);
                boolean southHasObstacle = hasObstacle(i, j, Direction.SOUTH);

                double westValue = getSensingValue(westSensed, westHasObstacle);
                double northValue = getSensingValue(northSensed, northHasObstacle);
                double eastValue = getSensingValue(eastSensed, eastHasObstacle);
                double southValue = getSensingValue(southSensed, southHasObstacle);

                evidence[i][j] = westValue * northValue * eastValue * southValue;

                //either sensed (-,-,-,-) or it didn't
                //loop through again and check if criteria match exactly or not (-,-,-,-)
                //then P(S1|Z1=d) alpha P(Z1=d|S1)P(S1) which is same as 1:33:17 example
                //TODO: how to normalize??? do we do for each combo of 4 booleans?
                String entry = Double.toString(evidence[i][j] * Double.parseDouble(posterior[i][j].getValue()));
                normalizationList.add(entry.substring(0, entry.length() - 1 < 7 ? entry.length() - 1 : 7));
            }
        }

        double denominator = 0.0;
        List<String> unique = normalizationList.stream().distinct().collect(Collectors.toList());
        for (String uniqueItem : unique) {
            int frequency = Collections.frequency(normalizationList, uniqueItem);
            denominator += frequency * Double.parseDouble(uniqueItem);
        }

        for (int i = 0; i < posterior.length; i++) {
            for (int j = 0; j < posterior[i].length; j++) {
                posterior[i][j].setValue(Double.toString(evidence[i][j] / denominator));
                //                posterior[i][j].setValue(Double.toString(preNormalized[i][j]));
            }
        }

        System.out.println(getPosterior());
    }

//    public void sense(boolean westSensed, boolean northSensed, boolean eastSensed, boolean southSensed) {
//        for (int i = 0; i < posterior.length; i++) {
//            for (int j = 0; j < posterior[i].length; j++) {
//                //don't update actual obstacles
//                if (hasObstacle(i, j, null)) {
//                    continue;
//                }
//                //obstacle: if sense && map shows one in that direction, use .9
//                //obstacle: if sense && map show differently, use 1 - correct (.1)
//                //no ob: if sense && map shows one in that direction, use .95
//                //no ob: if sense && map shows differently, use 1 - correct (.05)
//
//                //WNES is order
//                //P(Zt=(-,-,-,-)|St=0,0) = .05 * .05 * .95 * .95 = .0023
//
//                boolean westHasObstacle = hasObstacle(i, j, Direction.WEST);
//                boolean northHasObstacle = hasObstacle(i, j, Direction.NORTH);
//                boolean eastHasObstacle = hasObstacle(i, j, Direction.EAST);
//                boolean southHasObstacle = hasObstacle(i, j, Direction.SOUTH);
//
//                double westValue = getSensingValue(westSensed, westHasObstacle);
//                double northValue = getSensingValue(northSensed, northHasObstacle);
//                double eastValue = getSensingValue(eastSensed, eastHasObstacle);
//                double southValue = getSensingValue(southSensed, southHasObstacle);
//
//                double evidence = westValue * northValue * eastValue * southValue;
//
//                //then P(S1|Z1=d) alpha P(Z1=d|S1)P(S1) which is same as 1:33:17 example
//                //TODO: how to normalize??? do we do for each combo of 4 booleans?
//                posterior[i][j].setValue(Double.toString(evidence * Double.parseDouble(posterior[i][j].getValue())));
//            }
//        }
//        System.out.println(getPosterior());
//    }

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
        //TODO: multiply by 100, round 2 decimal
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < this.posterior.length; row++) {
            for (int column = 0; column < this.posterior[row].length; column++) {
                sb.append((new BigDecimal(this.posterior[row][column].getValue()).multiply(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP) + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void initializeMatrix(int rows, int columns, double[][] transitionMatrix) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transitionMatrix[i][j] = 0.0;
            }
        }
    }
}

