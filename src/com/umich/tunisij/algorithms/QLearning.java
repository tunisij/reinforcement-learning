package com.umich.tunisij.algorithms;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class QLearning extends Algorithm {

    public void run(MazeContext mazeContext) {
        mazeContext.setSource();

        while (true) {
            Node currentPosition = mazeContext.getCurrentPosition();
            Direction action = chooseAction(mazeContext, currentPosition.getPosition());
            Node nextPosition = mazeContext.simulateMove(currentPosition, action);
            currentPosition.incrementAccessFrequency(action);

            qLearningUpdate(currentPosition, action, nextPosition, mazeContext);

            mazeContext.setCurrentPosition(nextPosition);

            if (currentPosition.isGoal()) {
                break;
            }
        }
    }

    public void qLearningUpdate(Node currentPosition, Direction action, Node nextPosition, MazeContext mazeContext) {
        if (!currentPosition.isGoal()) {
            Double qValue = currentPosition.getQValueAsDouble(action) + getLearningRate(currentPosition, action) * (mazeContext.getCost(action) +
                    (gamma * (nextPosition.getMaxQValue())) - currentPosition.getQValueAsDouble(action));

            currentPosition.setQValue(action, qValue.toString());
        }
    }
}
