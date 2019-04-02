package com.umich.tunisij.algorithms;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EGreedy {

    public static final double OPTIMAL_ACTION = 95;

    public Direction chooseAction(MazeContext mazeContext, Map.Entry<Integer, Integer> position) {
        if (ThreadLocalRandom.current().nextInt(1, 101) > OPTIMAL_ACTION) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            return random == 0 ? Direction.NORTH : random == 1 ? Direction.EAST : random == 2 ? Direction.WEST : random == 3 ? Direction.SOUTH : null;
        }
        return mazeContext.getMaze()[position.getKey()][position.getValue()].getOptimalDirection();
    }

}
