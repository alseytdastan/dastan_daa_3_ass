package com.mst.algorithm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mst.model.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Generates test data for the MST algorithms.
 * Creates graphs of various sizes and densities for testing.
 */
public class TestDataGenerator {
    
    /**
     * Generates a complete graph (all possible edges).
     */
    public static Graph generateCompleteGraph(int vertices) {
        Graph graph = new Graph(vertices);
        Random random = new Random(vertices); // Seed for reproducibility
        
        for (int i = 0; i < vertices; i++) {
            for (int j = i + 1; j < vertices; j++) {
                int weight = random.nextInt(100) + 1; // Weight between 1 and 100
                graph.addEdge(i, j, weight);
            }
        }
        
        return graph;
    }
    
    /**
     * Generates a sparse graph (minimum edges to ensure connectivity).
     */
    public static Graph generateSparseGraph(int vertices) {
        Graph graph = new Graph(vertices);
        Random random = new Random(vertices);
        
        // Minimum spanning tree edges
        for (int i = 1; i < vertices; i++) {
            int weight = random.nextInt(50) + 1;
            int parent = random.nextInt(i);
            graph.addEdge(parent, i, weight);
        }
        
        // Add a few extra random edges
        int extraEdges = vertices / 2;
        for (int i = 0; i < extraEdges; i++) {
            int source = random.nextInt(vertices);
            int dest = random.nextInt(vertices);
            if (source != dest) {
                int weight = random.nextInt(100) + 1;
                graph.addEdge(source, dest, weight);
            }
        }
        
        return graph;
    }
    
    /**
     * Generates a dense graph (many edges).
     */
    public static Graph generateDenseGraph(int vertices, double density) {
        Graph graph = new Graph(vertices);
        Random random = new Random(vertices);
        
        int maxEdges = vertices * (vertices - 1) / 2;
        int targetEdges = (int)(maxEdges * density);
        
        Set<String> addedEdges = new HashSet<>();
        int edgeCount = 0;
        
        while (edgeCount < targetEdges) {
            int source = random.nextInt(vertices);
            int dest = random.nextInt(vertices);
            
            if (source == dest) continue;
            
            String edgeKey = source < dest ? source + "-" + dest : dest + "-" + source;
            if (!addedEdges.contains(edgeKey)) {
                int weight = random.nextInt(100) + 1;
                graph.addEdge(source, dest, weight);
                addedEdges.add(edgeKey);
                edgeCount++;
            }
        }
        
        return graph;
    }
    
    /**
     * Generates all test datasets and saves to JSON.
     */
    public static void generateAllTestData(String outputPath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject root = new JsonObject();
        JsonArray graphsArray = new JsonArray();
        
        // Generate 5 small graphs (4-6 vertices)
        System.out.println("Generating small graphs...");
        for (int i = 0; i < 5; i++) {
            int vertices = 4 + i;
            Graph graph = generateDenseGraph(vertices, 0.6);
            addGraphToArray(graphsArray, "small_graph_" + (i + 1), graph);
        }
        
        // Generate 10 medium graphs (10-30 vertices, then scaled up)
        System.out.println("Generating medium graphs...");
        int[] mediumSizes = {10, 15, 20, 25, 30, 50, 75, 100, 150, 200};
        for (int i = 0; i < 10; i++) {
            int vertices = mediumSizes[i];
            double density = 0.3 + (i % 3) * 0.15; // Varying density
            Graph graph = generateDenseGraph(vertices, density);
            addGraphToArray(graphsArray, "medium_graph_" + (i + 1), graph);
        }
        
        // Generate large graphs (250-300 vertices)
        System.out.println("Generating large graphs...");
        int[] largeSizes = {250, 300};
        for (int i = 0; i < 2; i++) {
            int vertices = largeSizes[i];
            Graph graph = generateDenseGraph(vertices, 0.4);
            addGraphToArray(graphsArray, "large_graph_" + (i + 1), graph);
        }
        
        root.add("graphs", graphsArray);
        
        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(root, writer);
        }
        
        System.out.println("Test data generation complete. Saved to: " + outputPath);
    }
    
    private static void addGraphToArray(JsonArray array, String name, Graph graph) {
        JsonObject graphObj = new JsonObject();
        graphObj.addProperty("name", name);
        graphObj.addProperty("vertices", graph.getVertices());
        
        JsonArray edgesArray = new JsonArray();
        for (com.mst.model.Edge edge : graph.getEdges()) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("source", edge.getSource());
            edgeObj.addProperty("destination", edge.getDestination());
            edgeObj.addProperty("weight", edge.getWeight());
            edgesArray.add(edgeObj);
        }
        graphObj.add("edges", edgesArray);
        
        array.add(graphObj);
    }
    
    public static void main(String[] args) {
        try {
            generateAllTestData("data/assign_3_input.json");
        } catch (IOException e) {
            System.err.println("Error generating test data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

