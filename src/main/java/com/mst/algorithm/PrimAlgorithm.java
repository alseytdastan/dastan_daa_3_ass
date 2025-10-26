package com.mst.algorithm;

import com.mst.model.Edge;
import com.mst.model.Graph;

import java.util.*;

/**
 * Implementation of Prim's algorithm for finding Minimum Spanning Tree.
 * Uses a priority queue to efficiently select minimum weight edges.
 */
public class PrimAlgorithm {
    /**
     * Result container for MST computation results.
     */
    public static class PrimResult {
        private List<Edge> mstEdges;
        private int totalCost;
        private long executionTime;
        private int comparisons;
        private int operations;

        public PrimResult(List<Edge> mstEdges, int totalCost, long executionTime, int comparisons, int operations) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.executionTime = executionTime;
            this.comparisons = comparisons;
            this.operations = operations;
        }

        public List<Edge> getMstEdges() { return mstEdges; }
        public int getTotalCost() { return totalCost; }
        public long getExecutionTime() { return executionTime; }
        public int getComparisons() { return comparisons; }
        public int getOperations() { return operations; }
    }

    /**
     * Finds the Minimum Spanning Tree using Prim's algorithm.
     * @param graph the input graph
     * @return result containing MST edges, cost, time, and operation counts
     */
    public PrimResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int[] comparisons = new int[1]; // Use array to allow modification in lambda
        int operations = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int vertices = graph.getVertices();
        
        if (vertices == 0) {
            return new PrimResult(mstEdges, 0, 0, 0, 0);
        }

        // Initialize arrays
        boolean[] inMST = new boolean[vertices];
        int[] key = new int[vertices];
        int[] parent = new int[vertices];
        
        for (int i = 0; i < vertices; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        operations += 2 * vertices;

        // Priority queue: (vertex, key)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            comparisons[0]++;
            return Integer.compare(a[1], b[1]);
        });

        // Start from vertex 0
        key[0] = 0;
        pq.offer(new int[]{0, 0});
        operations++;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            operations++;

            if (inMST[u]) {
                comparisons[0]++;
                continue;
            }

            inMST[u] = true;

            // If not the starting vertex, add the edge to MST
            if (parent[u] != -1) {
                Edge edge = findEdge(graph, parent[u], u);
                if (edge != null) {
                    mstEdges.add(edge);
                }
            }

            // Process all adjacent vertices
            for (Edge edge : graph.getEdgesForVertex(u)) {
                int v = edge.getSource() == u ? edge.getDestination() : edge.getSource();
                int weight = edge.getWeight();

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                    pq.offer(new int[]{v, key[v]});
                    operations += 2;
                }
            }
            operations++;
        }

        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        return new PrimResult(mstEdges, totalCost, executionTime, comparisons[0], operations);
    }

    /**
     * Helper method to find an edge between two vertices.
     */
    private Edge findEdge(Graph graph, int u, int v) {
        for (Edge edge : graph.getEdgesForVertex(u)) {
            int other = edge.getSource() == u ? edge.getDestination() : edge.getSource();
            if (other == v) {
                return edge;
            }
        }
        return null;
    }
}

