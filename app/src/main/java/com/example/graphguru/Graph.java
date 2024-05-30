package com.example.graphguru;
import java.util.List;
import java.util.ArrayList;

public class Graph implements Comparable<Object>{
    public String name = "Default_Graph_Name";
    protected ArrayList<Node> nodes;
    protected ArrayList<ArrayList<Node>> adj ;
    protected ArrayList<ArrayList<Float>> edgeWeight ;

    protected boolean weighted = true;

    public Graph(String name, ArrayList<Node> nodes, ArrayList<ArrayList<Node>> adj, boolean weighted)
    {
        this.name = name ;
        this.nodes = (ArrayList<Node>) nodes.clone();
        this.adj = (ArrayList<ArrayList<Node>>) adj.clone() ;
        this.weighted = weighted ;
        edgeWeight = new ArrayList<>(nodes.size()) ;
        for(int i = 0 ; i < nodes.size() ; i++)
            edgeWeight.add(new ArrayList<>(nodes.size())) ;
        for(int i = 0 ; i < nodes.size() ; i++)
            for(int j = 0 ; j < nodes.size() ; j++)
                edgeWeight.get(i).set(j,0f) ;
    }

    public int GetNodeIndex(Node node)
    {
        for(int i = 0 ; i < nodes.size() ; i++)
            if(node == nodes.get(i))
                return i ;
        return -1 ;
    }
    public Graph(List<List<Edge>> adj)
    {
        int nrOfElements = 0 ;
        nodes = new ArrayList<>() ;
        for(List<Edge> edges : adj)
            for (Edge e : edges)
                nrOfElements = Math.max(nrOfElements, Math.max(e.from,e.to));
        nrOfElements++ ;
        edgeWeight = new ArrayList<>(nrOfElements) ;
        for(int i = 0 ; i < nrOfElements ; i++)
            edgeWeight.add(new ArrayList<>(nrOfElements)) ;
        this.adj = new ArrayList<>(nrOfElements) ;
        for(List<Edge> edges : adj)
            for (Edge e : edges)
                AddEdge(e.from,e.to,e.weight,true);
    }
    public Graph()
    {
        nodes = new ArrayList<>() ;
        adj = new ArrayList<>() ;
        edgeWeight = new ArrayList<>() ;
    }

    public Graph(int nrNodes)
    {
        nodes = new ArrayList<>(nrNodes) ;
        adj = new ArrayList<>(nrNodes) ;
        edgeWeight = new ArrayList<>(nrNodes) ;
        for(int i = 0 ; i < nrNodes ; i++)
        {
            nodes.add(new Node(i)) ;
            adj.add(new ArrayList<>()) ;
            edgeWeight.add(new ArrayList<>()) ;
        }

    }

    public int GetNodeIndexInAdjList(int node_value, int from_node_index)
    {
        for(int i = 0 ; i < adj.get(from_node_index).size() ; i++)
            if(adj.get(from_node_index).get(i).value == node_value)
                return i ;
        return -1 ;
    }

    public int GetNodeIndex(int node_value)
    {
        for(int i = 0 ; i < nodes.size() ; i++)
            if(nodes.get(i).value == node_value)
                return i ;
        return -1 ;
    }

    public Graph(boolean weighted)
    {
        nodes = new ArrayList<>() ;
        adj = new ArrayList<>() ;
        edgeWeight = new ArrayList<>() ;
        this.weighted = weighted ;
    }

    public int GetSmallestNonExistingNodeValue()
    {
        int freq[] = new int[100] ;
        for(int i = 0 ; i < nodes.size() ; i++)
            freq[i] = 0 ;
        for(int i = 0 ; i < nodes.size() ; i++)
            freq[nodes.get(i).value]++ ;
        for(int i = 0 ; i < 100; i++)
            if(freq[i] == 0)
                return i ;
        return nodes.size();
    }

    public void SetWeighted(boolean val)
    {
        weighted = val ;
    }
    public void AddNode(int val)
    {
        Node newNode = new Node(val) ;
        adj.add(new ArrayList<>()) ;
        edgeWeight.add(new ArrayList<>()) ;
        nodes.add(newNode) ;
    }

    public void AddEdge(int index1,int index2, boolean CheckForRepeated)
    {
        AddEdge(index1,index2,0,CheckForRepeated);
    }

    public String PrintEdges()
    {
        String res = "" ;
        for(int i = 0 ; i < adj.size() ; i++) {
            res += String.valueOf(i) + ": ";
            for (int j = 0; j < adj.get(i).size(); j++)
                res += adj.get(i).get(j).value + " ";
            res += "\n" ;
        }
        return res ;
    }

    public void RemoveNode(int nodeIndex)
    {
        if(nodeIndex < 0 || nodeIndex > nodes.size())
            return ;
        for(int i = 0 ; i < nodes.size() ; i++)
            if(i != nodeIndex)
            {
                int indexInAdj = -1 ;
                for(int j = 0 ; j < adj.get(i).size() ; j++)
                    if(adj.get(i).get(j) == nodes.get(nodeIndex))
                    {
                        indexInAdj = j ;
                        break ;
                    }
                if(indexInAdj != -1) {
                    adj.get(i).remove(indexInAdj);
                    edgeWeight.get(i).remove(indexInAdj) ;
                }
            }
        edgeWeight.remove(nodeIndex) ;
        adj.remove(nodeIndex) ;
        nodes.remove(nodeIndex) ;
    }

    public void RemoveEdge(int fromIndex,int toIndex)
    {
        int nodeIndexInAdj = -1 ;
        for(int i = 0 ; i < adj.get(fromIndex).size() ; i++)
            if(adj.get(fromIndex).get(i) == nodes.get(toIndex))
            {
                nodeIndexInAdj = i ;
                break ;
            }
        adj.get(fromIndex).remove(nodeIndexInAdj) ;
        edgeWeight.get(fromIndex).remove(nodeIndexInAdj);
        if(this instanceof UndirectedGraph) {
            int fromNodeIndexInAdj = -1 ;
            for(int i = 0 ; i < adj.get(toIndex).size() ; i++)
                if(adj.get(toIndex).get(i) == nodes.get(fromIndex)){
                    fromNodeIndexInAdj = i ;
                    break ;
                }
            adj.get(toIndex).remove(fromNodeIndexInAdj);
            edgeWeight.get(toIndex).remove(fromNodeIndexInAdj) ;
        }
    }

    public boolean DoesAdjListContainNode(int fromNodeIndex,int nodeIndex)
    {
        if(nodes.size() <= fromNodeIndex)
            return false ;
        if(nodes.size() <= nodeIndex)
            return false ;
        for(int i = 0 ; i < adj.get(fromNodeIndex).size() ; i++)
            if(adj.get(fromNodeIndex).get(i) == nodes.get(nodeIndex))
                return true ;
        return false ;
    }

    public void AddEdge(int index1,int index2,float weight, boolean CheckForRepeated)
    {
        if(index1 < nodes.size() && index2 < nodes.size())
            //HANDLUIT DE CLASELE DERIVATE
            return;
        //AICI ADAUG NODURILE CU INDEXURI INDEX1, SI INDEX2 DACA nu exista nodul cu index1 sau index2 in nodes
        int diff = Math.max(index1,index2) - nodes.size() ;

        for (int i = 1; i <= diff + 1; i++) {
            nodes.add(new Node(nodes.size())) ;
            adj.add(new ArrayList<>());
            edgeWeight.add(new ArrayList<>()) ;
        }
        nodes.get(index1).value = index1 ;
        nodes.get(index2).value = index2 ;
        adj.get(index1).add(nodes.get(index2)) ;
        edgeWeight.get(index1).add(weight) ;
        if(index1 != index2) {
            adj.get(index2).add(nodes.get(index1));
            edgeWeight.get(index2).add(weight) ;
        }
    }

    public String toString()
    {
        String res = "" ;
        if(nodes.size() == 0)
            return res ;
        res += nodes.size() + " nodes\n" ;
        for(int i = 0 ; i < nodes.size() ; i++)
            res += nodes.get(i).toString() + " " ;
        res += "\n" ;
        for(int i = 0 ; i < adj.size() ; i++)
        {
            res += "Adj of " + nodes.get(i) + ": " ;
            for(int j = 0 ; j < adj.get(i).size() ; j++) {
                res += adj.get(i).get(j).value ;
                if(weighted)
                    res += "," + edgeWeight.get(i).get(j) + " " ;
                else
                    res += " " ;
            }
            res += "\n" ;
        }
        return res ;
    }

    @Override
    public int compareTo(Object o) {
        if(o.getClass() != this.getClass())
            return -1 ;
        Graph c = (Graph)o;
        if(c.weighted != weighted || c.adj.size() != adj.size() || c.nodes.size() != nodes.size())
            return -1 ;
        ArrayList<Node> thisSortedNodes = new ArrayList<>(nodes) ;
        ArrayList<Node> otherSortedNodes = new ArrayList<>(c.nodes) ;
        ArrayList<ArrayList<Node>> thisSortedAdj = new ArrayList<>(adj) ;
        ArrayList<ArrayList<Node>> otherSortedAdj = new ArrayList<>(c.adj) ;

        //region Sortare arrayuri noduri + schimbare ordine liste adiacente in functie valoarea nodurilor
        for(int a = 0 ; a < nodes.size() ; a++)
            for(int b = a+1 ; b < nodes.size() ; b++) {
                if (thisSortedNodes.get(a).value > thisSortedNodes.get(a).value)
                {
                    Node aux = thisSortedNodes.get(a) ;
                    thisSortedNodes.set(a,thisSortedNodes.get(b)) ;
                    thisSortedNodes.set(b,aux) ;
                    ArrayList<Node> auxList = thisSortedAdj.get(a) ;
                    thisSortedAdj.set(a,thisSortedAdj.get(b)) ;
                    thisSortedAdj.set(b,auxList);
                }
                if (otherSortedNodes.get(a).value > otherSortedNodes.get(a).value)
                {
                    Node aux = otherSortedNodes.get(a) ;
                    otherSortedNodes.set(a,otherSortedNodes.get(b)) ;
                    otherSortedNodes.set(b,aux) ;
                    ArrayList<Node> auxList = otherSortedAdj.get(a) ;
                    otherSortedAdj.set(a,otherSortedAdj.get(b)) ;
                    otherSortedAdj.set(b,auxList);
                }
            }
        //endregion

        for(int i = 0 ; i < nodes.size() ; i++) {
            if (thisSortedNodes.get(i).value != otherSortedNodes.get(i).value)
                return -1;
        }
        for(int i = 0 ; i < adj.size() ; i++)
        {
            if(adj.get(i).size() != c.adj.get(i).size())
                return -1 ;

        //region Sortare nodurile din arrayul de vecini al fiecarui nod
        for(int k = 0 ; k < nodes.size() ; k++)
            for(int a = 0 ; a < adj.get(k).size() ; a++)
                for(int b = a+1 ; b < adj.get(k).size() ; b++)
                {
                    if(thisSortedAdj.get(k).get(a).value > thisSortedAdj.get(k).get(b).value)
                    {
                        Node aux = thisSortedAdj.get(k).get(a) ;
                        thisSortedAdj.get(k).set(a,thisSortedAdj.get(k).get(b)) ;
                        thisSortedAdj.get(k).set(b,aux) ;
                    }
                    if(otherSortedAdj.get(k).get(a).value > otherSortedAdj.get(k).get(b).value)
                    {
                        Node aux = otherSortedAdj.get(k).get(a) ;
                        otherSortedAdj.get(k).set(a,otherSortedAdj.get(k).get(b)) ;
                        otherSortedAdj.get(k).set(b,aux) ;
                    }
                }
        //endregion

            for(int j = 0 ; j < adj.get(i).size() ; j++)
                if(adj.get(i).get(j).value != c.adj.get(i).get(j).value)
                    return -1 ;
        }
        return 0;
    }
}
