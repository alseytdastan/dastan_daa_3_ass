package com.mst.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mst.algorithm.PrimAlgorithm;
import com.mst.algorithm.KruskalAlgorithm;
import com.mst.model.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes MST algorithm results to JSON format.
 */
public class ResultsWriter {
    
    /**
     * Writes comparison results for multiple graphs to a JSON file.
     */
    public static void writeResults(String outputPath, List<ResultEntry> results) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject root = new JsonObject();
        JsonArray resultsArray = new JsonArray();
        
        for (ResultEntry entry : results) {
            JsonObject entryObj = new JsonObject();
            entryObj.addProperty("graphName", entry.getGraphName());
            entryObj.addProperty("vertices", entry.getVertices());
            entryObj.addProperty("edges", entry.getEdges());
            
            // Prim's results
            JsonObject primObj = new JsonObject();
            primObj.addProperty("mstCost", entry.getPrimCost());
            primObj.addProperty("executionTime", entry.getPrimTime());
            primObj.addProperty("comparisons", entry.getPrimComparisons());
            primObj.addProperty("operations", entry.getPrimOperations());
            entryObj.add("prim", primObj);
            
            // Kruskal's results
            JsonObject kruskalObj = new JsonObject();
            kruskalObj.addProperty("mstCost", entry.getKruskalCost());
            kruskalObj.addProperty("executionTime", entry.getKruskalTime());
            kruskalObj.addProperty("comparisons", entry.getKruskalComparisons());
            kruskalObj.addProperty("unions", entry.getKruskalUnions());
            entryObj.add("kruskal", kruskalObj);
            
            resultsArray.add(entryObj);
        }
        
        root.add("results", resultsArray);
        
        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(root, writer);
        }
    }
    
    /**
     * Represents a single result entry for a graph.
     */
    public static class ResultEntry {
        private String graphName;
        private int vertices;
        private int edges;
        private int primCost;
        private long primTime;
        private int primComparisons;
        private int primOperations;
        private int kruskalCost;
        private long kruskalTime;
        private int kruskalComparisons;
        private int kruskalUnions;

        public ResultEntry(String graphName, int vertices, int edges,
                          int primCost, long primTime, int primComparisons, int primOperations,
                          int kruskalCost, long kruskalTime, int kruskalComparisons, int kruskalUnions) {
            this.graphName = graphName;
            this.vertices = vertices;
            this.edges = edges;
            this.primCost = primCost;
            this.primTime = primTime;
            this.primComparisons = primComparisons;
            this.primOperations = primOperations;
            this.kruskalCost = kruskalCost;
            this.kruskalTime = kruskalTime;
            this.kruskalComparisons = kruskalComparisons;
            this.kruskalUnions = kruskalUnions;
        }

        // Getters
        public String getGraphName() { return graphName; }
        public int getVertices() { return vertices; }
        public int getEdges() { return edges; }
        public int getPrimCost() { return primCost; }
        public long getPrimTime() { return primTime; }
        public int getPrimComparisons() { return primComparisons; }
        public int getPrimOperations() { return primOperations; }
        public int getKruskalCost() { return kruskalCost; }
        public long getKruskalTime() { return kruskalTime; }
        public int getKruskalComparisons() { return kruskalComparisons; }
        public int getKruskalUnions() { return kruskalUnions; }
    }
}

