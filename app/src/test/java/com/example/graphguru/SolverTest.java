package com.example.graphguru;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Test;

import java.util.List;

public class SolverTest {

    @Test
    public void testIsGraphConnected() {
        UndirectedGraph g1 = new UndirectedGraph(5) ;
        assertEquals(false,Solver.IsConnected(g1));
        g1.AddEdge(0,1,true);
        assertEquals(false,Solver.IsConnected(g1));
        g1.AddEdge(1,2,true);
        assertEquals(false,Solver.IsConnected(g1));
        g1.AddEdge(2,3,true);
        assertEquals(false,Solver.IsConnected(g1));
        g1.AddEdge(3,4,true);
        assertEquals(true,Solver.IsConnected(g1));
    }

    @Test
    public void testGetMWSTDirected() {
        //region test1
        DirectedGraph g = new DirectedGraph(true) ;
        for(int i = 1 ; i <= 7  ; i++)
            g.AddNode(i);
        g.AddEdge(0,1,2, true);
        g.AddEdge(0,6,4, true);
        g.AddEdge(1,2,3, true);
        g.AddEdge(1,4,2, true);
        g.AddEdge(1,5,3, true);
        g.AddEdge(1,6,3, true);
        g.AddEdge(2,3,1, true);
        g.AddEdge(2,4,2, true);
        g.AddEdge(3,4,1, true);
        g.AddEdge(4,5,3, true);
        g.AddEdge(5,6,5, true);

        try {
            assertEquals(12d,Solver.GetMWST(g),0.001);
            fail();
        }
        catch (Exception e)
        {

        }
        //endregion

        //region test2
        DirectedGraph g1 = new DirectedGraph(true) ;
        for(int i = 1 ; i <= 7  ; i++)
            g1.AddNode(i);
        g1.AddEdge(0,1,2, true);
        g1.AddEdge(0,3,3, true);
        g1.AddEdge(0,6,4, true);
        g1.AddEdge(1,2,3, true);
        g1.AddEdge(1,4,2, true);
        g1.AddEdge(3,4,5, true);
        g1.AddEdge(4,5,7, true);
        g1.AddEdge(4,6,6, true);

        try {
            assertEquals(21,Solver.GetMWST(g1),0.001);
            fail();
        }
        catch (Exception e)
        {
        }
        //endregion

        //region test3
        DirectedGraph u = new DirectedGraph(true) ;
        for(int i = 0 ; i < 3  ; i++)
            u.AddNode(i);
        u.AddEdge(0,1,2, true);
        u.AddEdge(1,2,3, true);
        u.AddEdge(2,0,4, true);

        try {
            assertEquals(5,Solver.GetMWST(u),0.001);
            fail();
        }
        catch (Exception e)
        {
        }
        //endregion

        //region test4
        DirectedGraph v = new DirectedGraph(true) ;
        for(int i = 0 ; i < 3  ; i++)
            v.AddNode(i);
        v.AddEdge(0,1,2, true);
        v.AddEdge(1,2,3, true);

        try {
            assertEquals(5,Solver.GetMWST(v),0.001);
            fail();
        }
        catch (Exception e)
        {

        }
        //endregion
    }

    @Test
    public void testGetMWSTUndirected() { // TODO ADAUGA TEST CASEURI
        UndirectedGraph g1 = new UndirectedGraph(2) ;
        try {
            Solver.GetMWST(g1) ;
        }
        catch (Exception e){
            assertEquals(GraphNotConnectedException.class,e.getClass());
        }
        g1.AddEdge(1,0,true);
        try {
            Solver.GetMWST(g1) ;
        }
        catch (Exception e){
            fail();
        }
    }


}