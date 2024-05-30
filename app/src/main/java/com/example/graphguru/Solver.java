package com.example.graphguru;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class Solver {

    public static class EdgeComparatorClass implements Comparator<Edge>
    {
        @Override
        public int compare(Edge e1, Edge e2) {
            return (int)(e1.weight - e2.weight) ;
        }
    };

    public static void DFS(int current_node_index, ArrayList<Boolean> visited, Graph graph)
    {
        visited.set(current_node_index,true) ;
        for(int i = 0 ; i < graph.adj.get(current_node_index).size() ; i++)
            if(visited.get(graph.GetNodeIndex(graph.adj.get(current_node_index).get(i))) == false)
                DFS(graph.GetNodeIndex(graph.adj.get(current_node_index).get(i)),visited,graph) ;
    }

    public static boolean IsConnected(Graph g)
    {
        if(g.nodes.size() == 0)
            return true ;
        if(g instanceof UndirectedGraph) {
            DisjointSet parents = new DisjointSet(g.nodes.size());
            for (int i = 0; i < g.adj.size(); i++)
                for (int j = 0; j < g.adj.get(i).size(); j++)
                    parents.Union(i, g.GetNodeIndex(g.adj.get(i).get(j)));

            for (int i = 0; i < g.nodes.size(); i++)
                if (parents.FindSet(i) != parents.FindSet(0))
                    return false;
        }
        else {
            ArrayList<Boolean> visited = new ArrayList<>(g.nodes.size());
            for (int i = 0; i < g.nodes.size(); i++) {
                for (int j = 0; j < g.nodes.size(); j++)
                    visited.set(j, false);
                DFS(i, visited, g);
                for (int j = 0; j < g.nodes.size(); j++)
                    if (visited.get(j) == false)
                        return false;
            }
        }
        return true ;
    }
    public static float GetMWST(Graph g) throws GraphNotWeightedException,GraphNotConnectedException
    {
        if(!g.weighted)
            throw new GraphNotWeightedException() ;
        if(!IsConnected(g))
            throw new GraphNotConnectedException() ;
        DisjointSet parents = new DisjointSet(g.nodes.size()) ;
        Vector<Edge> edges = new Vector<Edge>() ;
        for(int i = 0 ; i < g.adj.size() ; i++)
            for(int j = 0 ; j < g.adj.get(i).size() ; j++)
                edges.add(new Edge(i,g.GetNodeIndex(g.adj.get(i).get(j)),g.edgeWeight.get(i).get(j)));
        edges.sort(new EdgeComparatorClass());
        float sum = 0 ;
        for(Edge e : edges)
        {
            if(parents.Union(e.from,e.to) != false)
                sum += e.weight ;
        }
        return sum ;
    }
}
