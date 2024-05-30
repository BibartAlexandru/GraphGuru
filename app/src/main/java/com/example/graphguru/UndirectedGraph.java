package com.example.graphguru;

import java.util.List;
import java.util.Vector;

public class UndirectedGraph extends Graph{
    public UndirectedGraph(boolean weighted) {
        super(weighted);
    }
    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(List<List<Edge>> adj)
    {
        super(adj);
    }
    public UndirectedGraph(int nrNodes){
        super(nrNodes);
    }

    public void AddEdge(int index1, int index2, float weight, boolean CheckForRepeated) {
        super.AddEdge(index1, index2, weight, CheckForRepeated);
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

        try {
            doesadjListContainNode = DoesAdjListContainNode(index2,index1) ;
        }
        catch (Exception e)
        {
            doesadjListContainNode = false ;
        }

        if(index1 != index2 && (CheckForRepeated == false || (CheckForRepeated == true && !doesadjListContainNode))) {
            adj.get(index2).add(nodes.get(index1)) ;
            edgeWeight.get(index2).add(weight) ;
        }
    }

    public void AddEdge(int index1, int index2, boolean CheckForRepeated)
    {
        AddEdge(index1,index2,0,CheckForRepeated);
    }

}
