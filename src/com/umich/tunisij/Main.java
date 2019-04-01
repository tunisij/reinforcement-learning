package com.umich.tunisij;

import com.umich.tunisij.environment.MazeContext;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        MazeContext mazeContext = new MazeContext();

        System.out.println("John Tunisi " + new Date().toString());
//        System.out.println(mazeContext.getOptimalAction());
//        System.out.println(mazeContext.getQValues());
        System.out.println(mazeContext.getAccessFrequency());
    }
}
