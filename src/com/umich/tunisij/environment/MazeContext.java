package com.umich.tunisij.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MazeContext {

    protected Maze maze = new Maze(ROWS, COLUMNS, getWallPositions());

    private static final int ROWS = 8;
    private static final int COLUMNS = 11;
    protected static final int GOAL_ROW = 0;
    protected static final int GOAL_COLUMN = 10;
    private static final int DRIFT_FORWARD_PROBABILITY = 80;
    private static final int DRIFT_SIDE_PROBABILITY = 10;
    private Node sourcePosition;
    private Node currentPosition;
    private Direction currentAction;

    public MazeContext() {
        setMazeState();
    }

    public List<Map.Entry<Integer, Integer>> getAdjacentPositions(Map.Entry<Integer, Integer> node) {
        List<Map.Entry<Integer, Integer>> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(Map.entry(node.getKey(), node.getValue() - 1)); //west
        adjacentPositions.add(Map.entry(node.getKey() - 1, node.getValue())); //north
        adjacentPositions.add(Map.entry(node.getKey(), node.getValue() + 1)); //east
        adjacentPositions.add(Map.entry(node.getKey() + 1, node.getValue())); //south


        adjacentPositions = adjacentPositions.stream().filter(position -> {
            if (position.getKey() < 0 || position.getValue() < 0 || position.getKey() >= maze.getHeight() || position.getValue() >= maze.getLength()) {
                return false;
            }
            String value = maze.getMaze()[position.getKey()][position.getValue()].getQValue(Direction.NORTH);
            if (value.equals("####") || value.equals("GGGG")) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return adjacentPositions;
    }

    public Node getNodeByDirection(int i, int j, Direction direction) {
        if (direction.equals(Direction.NORTH)) {
            return i - 1 < 0 ? maze.getMaze()[i][j] : maze.getMaze()[i-1][j].getOptimalAction().equals("####") ? maze.getMaze()[i][j] : maze.getMaze()[i-1][j];
        } else if (direction.equals(Direction.EAST)) {
            return j + 1 >= maze.getMaze()[i].length ? maze.getMaze()[i][j] : maze.getMaze()[i][j+1].getOptimalAction().equals("####") ? maze.getMaze()[i][j] : maze.getMaze()[i][j+1];
        } else if (direction.equals(Direction.WEST)) {
            return j - 1 < 0 ? maze.getMaze()[i][j] : maze.getMaze()[i][j-1].getOptimalAction().equals("####") ? maze.getMaze()[i][j] : maze.getMaze()[i][j-1];
        } else if (direction.equals(Direction.SOUTH)) {
            return i + 1 >= maze.getMaze().length ? maze.getMaze()[i][j] : maze.getMaze()[i+1][j].getOptimalAction().equals("####") ? maze.getMaze()[i][j] : maze.getMaze()[i+1][j];
        }
        return maze.getMaze()[i][j];
    }

    public Node simulateMove(Node currentPosition, Direction action) {
        int random = ThreadLocalRandom.current().nextInt(1, 101);

        Direction directionAfterDrift = action;
        if (random > DRIFT_FORWARD_PROBABILITY && random <= DRIFT_FORWARD_PROBABILITY + DRIFT_SIDE_PROBABILITY) {
            directionAfterDrift = getLeftDirection(action);
        } else if (random > DRIFT_FORWARD_PROBABILITY + DRIFT_SIDE_PROBABILITY && random <= DRIFT_FORWARD_PROBABILITY + (DRIFT_SIDE_PROBABILITY * 2)) {
            directionAfterDrift = getRightDirection(action);
        }
        return getNodeByDirection(currentPosition.getPosition().getKey(), currentPosition.getPosition().getValue(), directionAfterDrift);
    }

    private Direction getRightDirection(Direction direction) {
        if (Direction.NORTH.equals(direction)) {
            return Direction.EAST;
        } else if (Direction.EAST.equals(direction)) {
            return Direction.SOUTH;
        } else if (Direction.SOUTH.equals(direction)) {
            return Direction.WEST;
        } else {
            return Direction.NORTH;
        }
    }

    private Direction getLeftDirection(Direction direction) {
        if (Direction.NORTH.equals(direction)) {
            return Direction.WEST;
        } else if (Direction.EAST.equals(direction)) {
            return Direction.NORTH;
        } else if (Direction.SOUTH.equals(direction)) {
            return Direction.EAST;
        } else {
            return Direction.SOUTH;
        }
    }

    public int getCost(Direction direction) {
        if (Direction.NORTH.equals(direction)) {
            return -1;
        } else if (Direction.WEST.equals(direction) || Direction.EAST.equals(direction)) {
            return -2;
        }
        return -3;
    }

    public void setMazeState() {
        maze = new Maze(ROWS, COLUMNS, getWallPositions());
        maze.setGoalPosition(GOAL_ROW, GOAL_COLUMN);
        setSource();
    }

    public void setSource() {
        boolean isSourceSet = false;

        if (sourcePosition != null) {
            sourcePosition.setSource(false);
        }

        while (!isSourceSet) {
            int row = ThreadLocalRandom.current().nextInt(0, ROWS);
            int column = ThreadLocalRandom.current().nextInt(0, COLUMNS);

            Node node = maze.getMaze()[row][column];
            if (!node.isWall() && !node.isGoal()) {
                node.setSource(true);
                isSourceSet = true;
                currentPosition = node;
                sourcePosition = node;
            }
        }
    }

    public List<Map.Entry<Integer, Integer>> getWallPositions() {
        List<Map.Entry<Integer, Integer>> wallPositions = new ArrayList<>();
        wallPositions.add(Map.entry(2, 4));
        wallPositions.add(Map.entry(2, 5));
        wallPositions.add(Map.entry(2, 6));
        wallPositions.add(Map.entry(2, 7));
        wallPositions.add(Map.entry(3, 4));
        wallPositions.add(Map.entry(3, 7));
        wallPositions.add(Map.entry(4, 3));
        wallPositions.add(Map.entry(4, 4));
        wallPositions.add(Map.entry(4, 7));
        wallPositions.add(Map.entry(5, 6));
        wallPositions.add(Map.entry(5, 7));

        return wallPositions;
    }

    public Node[][] getMaze() {
        return maze.getMaze();
    }

    public String getOptimalAction() {
        return maze.getOptimalAction();
    }

    public String getQValues() {
        return maze.getQValues();
    }

    public String getAccessFrequency() {
        return maze.getAccessFrequency();
    }

    public Node getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Node currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Direction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Direction currentAction) {
        this.currentAction = currentAction;
    }
}
