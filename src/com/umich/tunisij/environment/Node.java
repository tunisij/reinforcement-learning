package com.umich.tunisij.environment;

import java.util.Map;

public class Node {

    private Map.Entry<Integer, Integer> position;
    private String value;

    public Node(Map.Entry<Integer, Integer> position) {
        this.position = position;
    }

    public Map.Entry<Integer, Integer> getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return getValue();
    }
}
