package com.example.graphguru;

import java.util.List;
import java.util.Vector;

public class DirectedGraph extends Graph{

    public DirectedGraph(boolean weighted)
    {
        super(weighted);
    }
    public DirectedGraph()
    {
        super();
    }
    public DirectedGraph(int nrNodes){
        super(nrNodes);
    }
    public DirectedGraph(List<List<Edge>> adj)
    {
        super(adj);
    }

    public void AddEdge(int index1, int index2, float weight,  boolean CheckForRepeated) {
        super.AddEdge(index1, index2,weight, CheckForRepeated);

        boolean doesadjListContainNode = false ;
        try {
            doesadjListContainNode = DoesAdjListContainNode(index1,index2) ;
        }
        catch (Exception e)
        {
            doesadjListContainNode = false ;
        }

        if(CheckForRepeated == false || (CheckForRepeated == true && !doesadjListContainNode)) {
            adj.get(index1).add(nodes.get(index2)) ;
            edgeWeight.get(index1).add(weight) ;
        }
    }
}
