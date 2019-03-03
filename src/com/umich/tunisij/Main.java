package com.umich.tunisij;

import com.umich.tunisij.environment.MazeContext;
import com.umich.tunisij.robot.Robot;

public class Main {

    public static void main(String[] args) {
        MazeContext mazeContext = new MazeContext();
        Robot robot = new Robot(mazeContext.getPrior());

        System.out.println(robot.getPosterior());

        robot.move();
        System.out.println(mazeContext.toString());
    }
}
