package sr.unasat.services;

import com.sun.corba.se.impl.orbutil.graph.GraphImpl;

import java.util.Stack;

/**
 * Created by Mitchell Jones on 8/15/2018.
 */

class Distpar {
    public int distance;
    public int parentVert;

    public Distpar(int pv, int d) // constructor
    {
        distance = d;
        parentVert = pv;
    }
}

class Vertex{
        public char label;
        public boolean isIntree;

        public Vertex(char lab) {
            label=lab;
            isIntree= false;
        }
}

class Graph
{
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000000;
    private Vertex vertexList[];  // list of vertices
    private int adjMat[][];       // adjacency matrix
    private int nVerts;       // number of Verices
    private int currentVert;
    private Distpar sPath[];
    private int nTree;     // number of Verts in tree
    private int startToCurrent;

    public Graph() {
        vertexList = new Vertex[MAX_VERTS];
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        nTree = 0;
        for (int j = 0; j < MAX_VERTS; j++)  //set adjecency
            for (int k = 0; k < MAX_VERTS; k++)   // matrix
                adjMat[j][k] = INFINITY;    //to infinity
        sPath = new Distpar[MAX_VERTS];   // shortest paths
    }
    public void addVertex(char lab)
    {
        vertexList[nVerts++]= new Vertex(lab);
    }

    public void addEdge(int naam, int voornaam, int Beroep)
    {
        adjMat[naam][voornaam]=Beroep;  //directed
    }

    public  void path()
    {
        int startTree = 0;     // start a vertex
        vertexList[startTree].isIntree=true;
        nTree=1;

        // transfer row of distances from adjmat to spath
        for (int j=0; j<nVerts; j++){
            int tempDist=adjMat[startTree][j];
            sPath[j]= new Distpar(startTree,tempDist);
        }
        //until all vertices are in the tree

        while (nTree<nVerts)
        {
            int indexMin= getMin(); // get minimum from spath
            int minDist = sPath[indexMin].distance;

            if (minDist== INFINITY){
                System.out.println("there are unreachable vertices");
                break;
            }
            else
            {
                currentVert=indexMin;
                startToCurrent = sPath[indexMin].distance;
            }
            vertexList[currentVert].isIntree= true;
            nTree++;
            adjust_spath();
        }

        displayPaths();
        nTree= 0;
        for (int j =0; j<nVerts; j++)
            vertexList[j].isIntree = false;
    }
    public int getMin()
    {
        int minDist = INFINITY;
        int indexMin = 0;
        for(int j=1; j<nVerts; j++)
        {
            if (!vertexList[j].isIntree &&
                    sPath[j].distance<minDist)
            {
                minDist = sPath[j].distance;
                indexMin = j;
            }
        }
        return indexMin;
    }
    public void adjust_spath()
    {
        int column = 1;
        while (column< nVerts)
        {if (vertexList[column].isIntree)
        {
            column++;
            continue;
        }
        int currentToFringe = adjMat[currentVert][column];
        int startToFringe = startToCurrent + currentToFringe;
        int sPathDist = sPath[column].distance;

        if (startToFringe<sPathDist)
        {
            sPath[column].parentVert = currentVert;
            sPath[column].distance= startToFringe;
        }
        column++;
    }
}
public void displayPaths()
{
    for (int j=0; j<nVerts; j++)
    {
        System.out.println(vertexList[j].label+ " = ");
        if (sPath[j].distance== INFINITY)
            System.out.println("inf");
        else
            System.out.println(sPath[j].distance);
        char parent = vertexList[sPath[j].parentVert].label;
            System.out.println("("+ parent +")");

    }
    System.out.println(" ");
}

}

public class Path {
    public static void main(String[] args) {

        Graph theGraph = new Graph();
        theGraph.addVertex('A'); // 0
        theGraph.addVertex('B'); // 1
        theGraph.addVertex('C'); // 2
        theGraph.addVertex('D'); // 3
        theGraph.addVertex('E'); // 4


        theGraph.addEdge(0, 1, 50);  //AB 50
        theGraph.addEdge(0, 3, 80);  //AD 80
        theGraph.addEdge(1, 2, 60);  //BC 60
        theGraph.addEdge(1, 3, 90);  // BE 90
        theGraph.addEdge(2, 4, 40);  // CE 40
        theGraph.addEdge(3, 2, 20);  // CD 20
        theGraph.addEdge(3, 4, 70);  // DE 70
        theGraph.addEdge(4, 1, 50);  // EF 50

        System.out.println("shortest paths");
        theGraph.path();
        System.out.println();
    }
}
