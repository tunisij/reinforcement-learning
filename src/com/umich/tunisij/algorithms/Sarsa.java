package com.umich.tunisij.algorithms;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.environment.Node;

public class Sarsa extends Algorithm {

    @Override
    public void run(MazeContext mazeContext) {
        mazeContext.setSource();

        while (true) {
            Node currentPosition = mazeContext.getCurrentPosition();
            Direction currentAction = mazeContext.getCurrentAction() != null ? mazeContext.getCurrentAction() : chooseAction(mazeContext, currentPosition.getPosition());
            Node nextPosition = mazeContext.simulateMove(currentPosition, currentAction);
            Direction nextAction = chooseAction(mazeContext, currentPosition.getPosition());
            currentPosition.incrementAccessFrequency(currentAction);

            sarsaUpdate(currentPosition, currentAction, nextPosition, nextAction, mazeContext);

            mazeContext.setCurrentPosition(nextPosition);
            mazeContext.setCurrentAction(nextAction);

            if (currentPosition.isGoal()) {
                break;
            }
        }
    }

    public void sarsaUpdate(Node currentPosition, Direction currentAction, Node nextPosition, Direction nextAction, MazeContext mazeContext) {
        if (!currentPosition.isGoal()) {
            Double qValue = currentPosition.getQValueAsDouble(currentAction) + getLearningRate(currentPosition, currentAction) * (mazeContext.getCost(currentAction) +
                    (gamma * (nextPosition.getQValueAsDouble(nextAction))) - currentPosition.getQValueAsDouble(currentAction));

            currentPosition.setQValue(currentAction, qValue.toString());
        }
    }

}
