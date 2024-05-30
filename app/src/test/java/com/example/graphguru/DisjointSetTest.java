package com.example.graphguru;

import static org.junit.Assert.*;

import org.junit.Test;

public class DisjointSetTest {


    @Test
    public void constructor() {
        DisjointSet d = new DisjointSet(5) ;
        assertEquals(5,d.parent.length);
        for(int i = 0 ; i < d.parent.length ; i++)
            assertEquals(-1,d.parent[i]);
    }

    @Test
    public void union() {
        DisjointSet d = new DisjointSet(5) ;
        assertEquals(true,d.Union(0,1)) ;//ret true daca nu au acelasi parinte initial
        assertEquals(-2,d.parent[0]);
        assertEquals(0,d.parent[1]);
        assertEquals(false,d.Union(0,1));
        assertEquals(true,d.Union(1,2));
        assertEquals(-3,d.parent[0]);
        assertEquals(0,d.parent[2]);
    }

    @Test
    public void findSet() {
        DisjointSet d = new DisjointSet(5) ;
        for(int i = 0 ; i < 5 ; i++)
            assertEquals(i,d.FindSet(i));
    }
}