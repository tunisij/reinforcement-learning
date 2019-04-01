package com.umich.tunisij.environment;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MazeContext {

    protected Maze maze = new Maze(ROWS, COLUMNS, getWallPositions());

    private static final int ROWS = 8;
    private static final int COLUMNS = 11;
    protected static final int GOAL_ROW = 0;
    protected static final int GOAL_COLUMN = 10;

    public MazeContext() {
        setMazeState();
    }

//    public List<Map.Entry<Integer, Integer>> getAdjacentPositions(Map.Entry<Integer, Integer> node) {
//        List<Map.Entry<Integer, Integer>> adjacentPositions = new ArrayList<>();
//        adjacentPositions.add(Map.entry(node.getKey(), node.getValue() - 1)); //west
//        adjacentPositions.add(Map.entry(node.getKey() - 1, node.getValue())); //north
//        adjacentPositions.add(Map.entry(node.getKey(), node.getValue() + 1)); //east
//        adjacentPositions.add(Map.entry(node.getKey() + 1, node.getValue())); //south
//
//
//        adjacentPositions = adjacentPositions.stream().filter(position -> {
//            if (position.getKey() < 0 || position.getValue() < 0 || position.getKey() >= maze.getHeight() || position.getValue() >= maze.getLength()) {
//                return false;
//            }
//            String value = maze.getMaze()[position.getKey()][position.getValue()].getValue();
//            if (value.equals("[]") || value.equals("GG")) {
//                return true;
//            }
//            return false;
//        }).collect(Collectors.toList());
//        return adjacentPositions;
//    }

    public void setMazeState() {
        maze = new Maze(ROWS, COLUMNS, getWallPositions());
        maze.setGoalPosition(GOAL_ROW, GOAL_COLUMN);
        setSource();
    }

    public void setSource() {
        boolean isSourceSet = false;

        while (!isSourceSet) {
            int row = ThreadLocalRandom.current().nextInt(0, ROWS);
            int column = ThreadLocalRandom.current().nextInt(0, COLUMNS);

            Node node = maze.getMaze()[row][column];
            if (!node.isWall() && !node.isGoal()) {
                node.setSource(true);
                isSourceSet = true;
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

}
