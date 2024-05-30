package com.example.graphguru;

public class Node {
    int value ;

    public Node(int val)
    {
        value = val ;
    }

    @Override
    public String toString()
    {
        return String.valueOf(value) ;
    }
}
