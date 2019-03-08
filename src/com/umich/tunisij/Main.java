package com.umich.tunisij;

import com.umich.tunisij.environment.Direction;
import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.robot.Robot;

public class Main {

    public static void main(String[] args) {
        MazeContext mazeContext = new MazeContext();
        Robot robot = new Robot(mazeContext);

        robot.sense(false, false, false, false);
        System.out.println(robot.getPosterior());
        robot.move(Direction.NORTH);
        System.out.println(robot.getPosterior());
        robot.sense(false, false, false, false);
        System.out.println(robot.getPosterior());
        robot.move(Direction.NORTH);
        System.out.println(robot.getPosterior());
        robot.sense(false, false, true, false);
        System.out.println(robot.getPosterior());
        robot.move(Direction.NORTH);
        System.out.println(robot.getPosterior());
        robot.sense(false, false, false, false);
        System.out.println(robot.getPosterior());
        robot.move(Direction.EAST);
        System.out.println(robot.getPosterior());
        robot.sense(false, false, true, true);
        System.out.println(robot.getPosterior());
        robot.move(Direction.NORTH);
        System.out.println(robot.getPosterior());
        robot.sense(false, false, true, false);
        System.out.println(robot.getPosterior());
    }
}
