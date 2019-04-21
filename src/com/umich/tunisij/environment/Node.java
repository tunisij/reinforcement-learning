package com.umich.tunisij.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Node {

    private Map.Entry<Integer, Integer> position;
    private Map<Direction, String> qValueMap = new HashMap<>();
    private Map<Direction, String> accessFrequency = new HashMap<>();
    private boolean isGoal = false;
    private boolean isWall = false;
    private boolean isSource = false;
    private Direction currentOptimalDirection;

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
    }

    public double getMaxQValue() {
        Double maxQValue = null;

        for (String qValue : qValueMap.values()) {
            if (maxQValue == null || Double.compare(Double.parseDouble(qValue), maxQValue) == 1) {
                maxQValue = Double.parseDouble(qValue);
            }
        }
        return maxQValue;
    }

    public double getQValueAsDouble(Direction direction) {
        return Double.parseDouble(qValueMap.get(direction));
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

    public void incrementAccessFrequency(Direction direction) {
        if (!isGoal) {
            accessFrequency.put(direction, "" + (Integer.parseInt(accessFrequency.get(direction)) + 1));
        }
    }

    public Map.Entry<Integer, Integer> getPosition() {
        return position;
    }

    public String toString() {
        return qValueMap.toString();
    }

    protected String getOptimalAction() {
        String action;
        Direction direction = getOptimalDirection(false);

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

    public Direction getOptimalDirection(boolean updateOptimal) {
        if (!updateOptimal && currentOptimalDirection != null) {
            return currentOptimalDirection;
        }

        double maxValue = -999999;

        for (Map.Entry<Direction, String> entry : qValueMap.entrySet()) {
            if (Double.compare(Double.parseDouble(entry.getValue()), maxValue) == 1) {
                maxValue = Double.parseDouble(entry.getValue());
            }
        }

        List<Map.Entry<Direction, String>> maxEntries = new ArrayList<>();
        for (Map.Entry<Direction, String> entry : qValueMap.entrySet()) {
            if (Double.compare(Double.parseDouble(entry.getValue()), maxValue) == 0) {
                maxEntries.add(entry);
            }
        }

        if (maxEntries.size() == 0) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            return random == 0 ? Direction.NORTH : random == 1 ? Direction.EAST : random == 2 ? Direction.SOUTH : Direction.WEST;
        }
        currentOptimalDirection = maxEntries.get(ThreadLocalRandom.current().nextInt(0, maxEntries.size())).getKey();
        return currentOptimalDirection;
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
