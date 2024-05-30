package com.example.graphguru;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Test;

import java.util.List;

public class GraphTest {

    @Test
    public void addNode() {
    }

    @Test
    public void AddEdge() {
    }

    @Test
    public void TestRemoveNode()
    {
        UndirectedGraph g = new UndirectedGraph() ;
        g.AddEdge(0,1,true);
        g.AddEdge(2,1,true);
        g.AddEdge(3,4,true);
        g.RemoveNode(2);
        System.out.println(g.toString());
        g.RemoveNode(3);
        System.out.println(g.toString());
        g.RemoveNode(0);
        System.out.println(g.toString());
    }

    @Test
    public void testDoesAdjContainNode() {
        UndirectedGraph g1 = new UndirectedGraph() ;
        g1.AddEdge(0,0,true);
        assertEquals(true, g1.DoesAdjListContainNode(0, 0));
        assertEquals(false, g1.DoesAdjListContainNode(0, 3));
        assertEquals(false,g1.DoesAdjListContainNode(4,1)) ;

    }

    @Test
    public void testAddEdgeUndirected() {
        UndirectedGraph g1 = new UndirectedGraph() ;
        for(int i = 0 ; i < 3 ; i++)
            for(int j = 0 ; j < 3 ; j++)
                g1.AddEdge(i,j,true);

        //System.out.println(g1.PrintEdges()) ;

        for(int i = 0 ; i < 3 ; i++) {
            int[] freq = new int[3] ;
            for(int a = 0 ; a < 3 ; a++)
                freq[a] = 0 ;
            for (int j = 0; j < g1.adj.get(i).size(); j++)
                freq[g1.adj.get(i).get(j).value] += 1 ;
            for(int node = 0 ; node < 3 ; node++)
                assertEquals(1, freq[node]);

        }

    }

    @Test
    public void testToString() {
    }

    @Test
    public void testListConstructor()
    {
        UndirectedGraph g1 = new UndirectedGraph() ;
        for(int i = 0 ; i < 3 ; i++)
            for(int j = 0 ; j < 3 ; j++)
                g1.AddEdge(i,j,true);

        UndirectedGraph g2 = new UndirectedGraph(List.of(
                List.of(new Edge(0,0), new Edge(0,1), new Edge(0,2)),
                List.of(new Edge(1,0), new Edge(1,1), new Edge(1,2)),
                List.of(new Edge(2,0), new Edge(2,1), new Edge(2,2))
        )) ;

        /*System.out.println(g1.PrintEdges());
        System.out.println(g2.PrintEdges());

        System.out.println(g1.nodes.size());
        System.out.println(g2.nodes.size());

        for(Node node : g1.nodes)
            System.out.print(node.value + " ");

        System.out.println();

        for(Node node : g2.nodes)
            System.out.print(node.value + " ");*/

        assertEquals(0,g1.compareTo(g2));
        assertEquals(0,g2.compareTo(g1));
    }

    @Test
    public void testNrNodesConstructor()
    {
        UndirectedGraph g1 = new UndirectedGraph(5) ;
        assertEquals(5,g1.nodes.size());
        assertEquals(5,g1.adj.size());
        DirectedGraph g2 = new DirectedGraph(3) ;
        assertEquals(3,g2.nodes.size());
        assertEquals(3,g2.adj.size());
    }

    @Test
    public void testCompareUndirected()
    {
        UndirectedGraph g1 = new UndirectedGraph() ;
        UndirectedGraph g2 = new UndirectedGraph() ;
        for(int i = 0 ; i < 3 ; i++)
            for(int j = 0 ; j < 3 ; j++)
            {
                g1.AddEdge(i,j,true);
                g2.AddEdge(i,j,true);
            }

        assertEquals(0,g1.compareTo(g1));
        assertEquals(0,g1.compareTo(g2));
        assertEquals(0,g2.compareTo(g1));
        assertEquals(-1,g1.compareTo(new Integer(1)));
        g1.AddEdge(0,3,true);
        assertEquals(-1,g1.compareTo(g2));
        assertEquals(-1,g2.compareTo(g1));
        g2.AddEdge(0,3,true);
        assertEquals(0,g1.compareTo(g2));
        assertEquals(0,g2.compareTo(g1));

        g1.AddEdge(0,4,true);
        g1.AddEdge(0,5,true);

        g2.AddEdge(0,5,true);
        g2.AddEdge(0,4,true);

        System.out.println(g1.toString());
        System.out.println(g2.toString());

        assertEquals(0,g1.compareTo(g2));
        assertEquals(0,g2.compareTo(g1));
    }
}