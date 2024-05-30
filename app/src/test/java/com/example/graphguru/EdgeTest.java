package com.example.graphguru;

import static org.junit.Assert.*;

import org.junit.Test;

public class EdgeTest {

    @Test
    public void compareTo() {
        Edge e1 = new Edge(1,2,3) ;
        Edge e2 = new Edge(1,2,3) ;
        Edge e3 = new Edge(1,2,1) ;
        Integer a = 5 ;

        assertEquals(e1.Equals(e1),true);
        assertEquals(e1.Equals(e2),true);
        assertEquals(e1.Equals(e3),false);
        assertEquals(e1.Equals(a),false);
    }
}