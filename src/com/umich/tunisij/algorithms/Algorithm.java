package com.umich.tunisij.algorithms;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public abstract class Algorithm extends EGreedy {

    protected static final double gamma = 0.9;

    public double getLearningRate(Node node, Direction direction) {
        return 1.0 / (Double.parseDouble(node.getAccessFrequency(direction)) + 1.0);
    }

    public abstract void run(MazeContext mazeContext);
}
