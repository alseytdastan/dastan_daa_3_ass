package com.mst.algorithm;

import com.mst.model.Edge;
import com.mst.model.Graph;

import java.util.*;

/**
 * Implementation of Kruskal's algorithm for finding Minimum Spanning Tree.
 * Uses Union-Find data structure with path compression for efficiency.
 */
public class KruskalAlgorithm {
    /**
     * Result container for MST computation results.
     */
    public static class KruskalResult {
        private List<Edge> mstEdges;
        private int totalCost;
        private long executionTime;
        private int comparisons;
        private int unions;

        public KruskalResult(List<Edge> mstEdges, int totalCost, long executionTime, int comparisons, int unions) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.executionTime = executionTime;
            this.comparisons = comparisons;
            this.unions = unions;
        }

        public List<Edge> getMstEdges() { return mstEdges; }
        public int getTotalCost() { return totalCost; }
        public long getExecutionTime() { return executionTime; }
        public int getComparisons() { return comparisons; }
        public int getUnions() { return unions; }
    }

    /**
     * Union-Find data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int operations = 0;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
            operations += size * 2;
        }

        public int find(int x) {
            operations++;
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
                operations++;
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            operations += 2;

            if (rootX == rootY) {
                return false; // Already in same set
            }

            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            operations++;
            return true;
        }

        public int getOperations() {
            return operations;
        }
    }

    /**
     * Finds the Minimum Spanning Tree using Kruskal's algorithm.
     * @param graph the input graph
     * @return result containing MST edges, cost, time, and operation counts
     */
    public KruskalResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int comparisons = 0;

        List<Edge> mstEdges = new ArrayList<>();
        int vertices = graph.getVertices();

        if (vertices == 0) {
            return new KruskalResult(mstEdges, 0, 0, 0, 0);
        }

        // Get all edges and sort them by weight
        List<Edge> allEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(allEdges);
        comparisons += allEdges.size() * (int)(Math.log(allEdges.size()) / Math.log(2));

        // Initialize Union-Find
        UnionFind uf = new UnionFind(vertices);
        int unions = 0;

        // Process edges in sorted order
        for (Edge edge : allEdges) {
            int source = edge.getSource();
            int dest = edge.getDestination();

            // Check if adding this edge creates a cycle
            if (uf.find(source) != uf.find(dest)) {
                mstEdges.add(edge);
                uf.union(source, dest);
                unions++;
                comparisons++;
            } else {
                comparisons++;
            }

            // Stop when we have V-1 edges
            if (mstEdges.size() == vertices - 1) {
                break;
            }
        }

        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        return new KruskalResult(mstEdges, totalCost, executionTime, comparisons, unions);
    }
}

