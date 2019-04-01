package com.umich.tunisij;

import com.umich.tunisij.algorithms.Algorithm;
import com.umich.tunisij.algorithms.QLearning;
import com.umich.tunisij.algorithms.Sarsa;
import com.umich.tunisij.environment.MazeContext;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        MazeContext mazeContext = new MazeContext();
        Main.run(new Sarsa());
//        Main.run(new QLearning());
    }

    public static void run(Algorithm algorithm) {
        MazeContext mazeContext = new MazeContext();

//        for(int i = 0; i < 10000; i++) {
//            if (i == 1000) {
//                Main.print(mazeContext);
//            }
//            algorithm.run(mazeContext);
//        }
        Main.print(mazeContext);
    }

    public static void print(MazeContext mazeContext) {
        System.out.println("John Tunisi " + new Date().toString());
        System.out.println(mazeContext.getOptimalAction());
        System.out.println(mazeContext.getQValues());
        System.out.println(mazeContext.getAccessFrequency());
    }
}
