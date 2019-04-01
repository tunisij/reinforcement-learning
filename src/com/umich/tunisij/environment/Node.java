package com.umich.tunisij.environment;

import java.util.HashMap;
import java.util.Map;

public class Node {

    private Map.Entry<Integer, Integer> position;
    private Map<Direction, String> qValueMap = new HashMap<>();
    private Map<Direction, String> accessFrequency = new HashMap<>();
    private boolean isGoal = false;
    private boolean isWall = false;
    private boolean isSource = false;

    public Node(Map.Entry<Integer, Integer> position) {
        this.position = position;

        this.qValueMap.put(Direction.NORTH, "0");
        this.qValueMap.put(Direction.EAST, "0");
        this.qValueMap.put(Direction.WEST, "0");
        this.qValueMap.put(Direction.SOUTH, "0");

        this.accessFrequency.put(Direction.NORTH, "0");
        this.accessFrequency.put(Direction.EAST, "0");
        this.accessFrequency.put(Direction.WEST, "0");
        this.accessFrequency.put(Direction.SOUTH, "0");
    }

    public void setQValue(Direction direction, String value) {
        qValueMap.put(direction, value);
        accessFrequency.put(direction, accessFrequency.get(direction) + 1);
    }

    public String getQValue(Direction direction) {
        return isWall ? "####" : qValueMap.get(direction);
    }

    public String getAccessFrequency(Direction direction) {
        return isWall ? "####" : accessFrequency.get(direction);
    }

    protected void setAccessFrequency(Direction direction, String frequency) {
        accessFrequency.put(direction, frequency);
    }

    public Map.Entry<Integer, Integer> getPosition() {
        return position;
    }

    public String toString() {
        return qValueMap.toString();
    }

    public String getOptimalAction() {
        String action;
        Direction direction = getOptimalDirection();

        if (isSource) {
            action = "SSSS";
        } else if (isWall) {
            action = "####";
        } else if (isGoal) {
            action = "GGGG";
        } else if (Direction.NORTH.equals(direction)) {
            action = "^^^^";
        } else if (Direction.WEST.equals(direction)) {
            action = "<<<<";
        } else if (Direction.EAST.equals(direction)) {
            action = ">>>>";
        } else {
            action = "VVVV";
        }
        return action;
    }

    private Direction getOptimalDirection() {
        Map.Entry<Direction, String> maxEntry = null;

        for (Map.Entry<Direction, String> entry : qValueMap.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }

    public void setGoal(boolean goal) {
        this.isGoal = goal;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setWall(boolean wall) {
        this.isWall = wall;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setSource(boolean source) {
        this.isSource = source;
    }

    public boolean isSource() {
        return isSource;
    }
}
