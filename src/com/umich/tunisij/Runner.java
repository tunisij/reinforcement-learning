package com.umich.tunisij;

import com.umich.tunisij.algorithms.Algorithm;
import com.umich.tunisij.algorithms.QLearning;
import com.umich.tunisij.algorithms.Sarsa;
import com.umich.tunisij.environment.MazeContext;

import java.util.Date;

public class Runner {

    public static void main(String[] args) {
        System.out.println("Sarsa");
        run(new Sarsa());
        System.out.println("-----------------------------------------------");
        System.out.println("QLearning");
        run(new QLearning());
    }

    public static void run(Algorithm algorithm) {
        MazeContext mazeContext = new MazeContext();
        print(mazeContext);
        for(int i = 0; i < 10000; i++) {
            if (i == 1000) {
                System.out.println("-----------------------------------------------");
                System.out.println("1000th trial results");
                print(mazeContext);
            }
            algorithm.run(mazeContext);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Final results");
        print(mazeContext);
    }

    public static void print(MazeContext mazeContext) {
        System.out.println("John Tunisi " + new Date().toString());
        System.out.println(mazeContext.getOptimalAction());
        System.out.println(mazeContext.getQValues());
        System.out.println(mazeContext.getAccessFrequency());
    }
}
