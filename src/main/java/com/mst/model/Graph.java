package com.mst.model;

import java.util.*;

/**
 * Represents a weighted undirected graph for the transportation network.
 * Vertices represent city districts, edges represent potential roads.
 */
public class Graph {
    private int vertices;
    private List<Edge> edges;
    private List<List<Edge>> adjacencyList;

    /**
     * Constructs a graph with the given number of vertices.
     * @param vertices number of vertices (city districts)
     */
    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * Adds an undirected edge to the graph.
     * @param source source vertex
     * @param destination destination vertex
     * @param weight edge weight (construction cost)
     */
    public void addEdge(int source, int destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);
        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(edge);
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Gets all edges connected to a specific vertex.
     * @param vertex the vertex index
     * @return list of edges connected to this vertex
     */
    public List<Edge> getEdgesForVertex(int vertex) {
        return adjacencyList.get(vertex);
    }

    /**
     * Calculates the total number of edges in the graph.
     * @return number of edges
     */
    public int getEdgeCount() {
        return edges.size();
    }

    /**
     * Checks if the graph is connected (all vertices are reachable).
     * @return true if graph is connected
     */
    public boolean isConnected() {
        if (vertices == 0) return true;
        
        boolean[] visited = new boolean[vertices];
        dfs(0, visited);
        
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for (Edge edge : adjacencyList.get(vertex)) {
            int neighbor = edge.getSource() == vertex ? edge.getDestination() : edge.getSource();
            if (!visited[neighbor]) {
                dfs(neighbor, visited);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{vertices=").append(vertices).append(", edges=").append(edges.size()).append("}\n");
        sb.append("Edges: ");
        for (Edge edge : edges) {
            sb.append(edge).append(" ");
        }
        return sb.toString();
    }
}
