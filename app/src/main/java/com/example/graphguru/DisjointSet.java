package com.example.graphguru;

public class DisjointSet {
    int[] parent ;

    public DisjointSet(int nrOfnodeIndexs)
    {
        parent = new int[nrOfnodeIndexs] ;
        for(int i = 0 ; i < nrOfnodeIndexs ; i++)
            parent[i] = -1 ;
    }

    public boolean Union(int nodeIndex1, int nodeIndex2) throws RuntimeException
    {
        if(nodeIndex1 >= parent.length || nodeIndex2 >= parent.length)
            throw new RuntimeException("DISJOINTSET.UNION : nodeIndex OUT OF BOUNDS") ;
        int p1 = FindSet(nodeIndex1) ;
        int p2 = FindSet(nodeIndex2) ;
        if(p1 != p2)
        {
            if(-parent[p1] >= -parent[p2])
            {
                parent[p1] += parent[p2] ;
                parent[p2] = p1 ;
            }
            else
            {
                parent[p2] += parent[p1] ;
                parent[p1] = p2 ;
            }
            return true ;
        }
        return false ;
    }

    public int FindSet(int nodeIndex)
    {
        if(parent[nodeIndex] < 0)
            return nodeIndex ;
        parent[nodeIndex] = FindSet(parent[nodeIndex]) ;
        return parent[nodeIndex] ;
    }

}
