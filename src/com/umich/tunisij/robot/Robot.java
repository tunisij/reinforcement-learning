package com.umich.tunisij.robot;

import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class Robot {

    private static final double SENSING_CONFIDENCE_OBSTACLE = .9;
    private static final double SENSING_CONFIDENCE_OPEN_SQUARE = .95;
    private static final double MOVING_DRIFT_PROBABILITY_X = .1;

    private Node[][] prior;

    public Robot(Node[][] prior) {
        this.prior = prior;
    }

    public void sense() {

    }

    public void move() {

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
}

