package com.example.graphguru;

public class Edge {
    int to ;
    int from ;
    float weight ;

    public Edge(int from,int to, float weight)
    {
        this.from = from ;
        this.to = to ;
        this.weight = weight ;
    }

    public Edge(int from,int to)
    {
        this.from = from ;
        this.to = to ;
        weight = 0 ;
    }

    public String toString()
    {
        return String.valueOf(to) + "," + String.valueOf(weight) ;
    }

    public boolean Equals(Object o) {
        if(o.getClass() != this.getClass())
            return false ;
        Edge e = (Edge) o;
        if(e.to != to || e.weight != weight || e.from != from)
            return false ;
        return true;
    }
}
