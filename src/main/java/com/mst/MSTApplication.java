package com.mst;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mst.algorithm.KruskalAlgorithm;
import com.mst.algorithm.PrimAlgorithm;
import com.mst.io.ResultsWriter;
import com.mst.model.Graph;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application for MST analysis.
 * Reads test data, runs both algorithms, and generates comparison results.
 */
public class MSTApplication {
    
    public static void main(String[] args) {
        String inputFile = "data/assign_3_input.json";
        String outputFile = "output/assign_3_output.json";
        
        System.out.println("=".repeat(80));
        System.out.println("MINIMUM SPANNING TREE ALGORITHM COMPARISON");
        System.out.println("=".repeat(80));
        
        try {
            // Read graphs from input file
            System.out.println("\nReading input graphs from: " + inputFile);
            Graph[] graphs = readGraphsFromFile(inputFile);
            System.out.println("Loaded " + graphs.length + " graphs\n");
            
            // Process each graph
            List<ResultsWriter.ResultEntry> results = new ArrayList<>();
            
            for (int i = 0; i < graphs.length; i++) {
                Graph graph = graphs[i];
                String graphName = "graph_" + (i + 1);
                
                System.out.println("-".repeat(80));
                System.out.println("Processing " + graphName);
                System.out.println("Vertices: " + graph.getVertices() + ", Edges: " + graph.getEdgeCount());
                
                // Check if graph is connected
                if (!graph.isConnected()) {
                    System.out.println("WARNING: Graph is not connected. Skipping MST computation.");
                    continue;
                }
                
                // Run Prim's algorithm
                System.out.println("Running Prim's algorithm...");
                PrimAlgorithm primAlgorithm = new PrimAlgorithm();
                PrimAlgorithm.PrimResult primResult = primAlgorithm.findMST(graph);
                
                System.out.println("  MST Cost: " + primResult.getTotalCost());
                System.out.println("  Execution Time: " + primResult.getExecutionTime() + " ms");
                System.out.println("  Operations: " + primResult.getOperations());
                System.out.println("  Comparisons: " + primResult.getComparisons());
                
                // Run Kruskal's algorithm
                System.out.println("Running Kruskal's algorithm...");
                KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm();
                KruskalAlgorithm.KruskalResult kruskalResult = kruskalAlgorithm.findMST(graph);
                
                System.out.println("  MST Cost: " + kruskalResult.getTotalCost());
                System.out.println("  Execution Time: " + kruskalResult.getExecutionTime() + " ms");
                System.out.println("  Unions: " + kruskalResult.getUnions());
                System.out.println("  Comparisons: " + kruskalResult.getComparisons());
                
                // Verify correctness
                if (primResult.getTotalCost() == kruskalResult.getTotalCost()) {
                    System.out.println("✓ Correctness: Both algorithms produce identical MST cost");
                } else {
                    System.out.println("✗ ERROR: Algorithms produced different costs!");
                }
                
                // Add result
                ResultsWriter.ResultEntry entry = new ResultsWriter.ResultEntry(
                    graphName,
                    graph.getVertices(),
                    graph.getEdgeCount(),
                    primResult.getTotalCost(),
                    primResult.getExecutionTime(),
                    primResult.getComparisons(),
                    primResult.getOperations(),
                    kruskalResult.getTotalCost(),
                    kruskalResult.getExecutionTime(),
                    kruskalResult.getComparisons(),
                    kruskalResult.getUnions()
                );
                results.add(entry);
                
                System.out.println();
            }
            
            // Write results to output file
            System.out.println("\nWriting results to: " + outputFile);
            ResultsWriter.writeResults(outputFile, results);
            
            // Generate summary statistics
            generateSummary(results);
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("ANALYSIS COMPLETE");
            System.out.println("=".repeat(80));
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reads graphs from JSON file.
     */
    private static Graph[] readGraphsFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray graphsArray = jsonObject.getAsJsonArray("graphs");
            
            Graph[] graphs = new Graph[graphsArray.size()];
            
            for (int i = 0; i < graphsArray.size(); i++) {
                JsonObject graphObj = graphsArray.get(i).getAsJsonObject();
                
                int vertices = graphObj.get("vertices").getAsInt();
                graphs[i] = new Graph(vertices);
                
                JsonArray edgesArray = graphObj.getAsJsonArray("edges");
                for (var element : edgesArray) {
                    JsonObject edgeObj = element.getAsJsonObject();
                    int source = edgeObj.get("source").getAsInt();
                    int destination = edgeObj.get("destination").getAsInt();
                    int weight = edgeObj.get("weight").getAsInt();
                    
                    graphs[i].addEdge(source, destination, weight);
                }
            }
            
            return graphs;
        }
    }
    
    /**
     * Generates and displays summary statistics.
     */
    private static void generateSummary(List<ResultsWriter.ResultEntry> results) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SUMMARY STATISTICS");
        System.out.println("=".repeat(80));
        
        if (results.isEmpty()) {
            System.out.println("No results to summarize.");
            return;
        }
        
        long avgPrimTime = 0;
        long avgKruskalTime = 0;
        int correctResults = 0;
        
        for (ResultsWriter.ResultEntry entry : results) {
            avgPrimTime += entry.getPrimTime();
            avgKruskalTime += entry.getKruskalTime();
            
            if (entry.getPrimCost() == entry.getKruskalCost()) {
                correctResults++;
            }
        }
        
        avgPrimTime /= results.size();
        avgKruskalTime /= results.size();
        
        System.out.println("Total graphs analyzed: " + results.size());
        System.out.println("Correctness: " + correctResults + "/" + results.size() + " graphs");
        System.out.println("Average Prim execution time: " + avgPrimTime + " ms");
        System.out.println("Average Kruskal execution time: " + avgKruskalTime + " ms");
        
        if (avgPrimTime < avgKruskalTime) {
            System.out.println("Winner: Prim's algorithm (" + 
                String.format("%.2f%% faster", (double)(avgKruskalTime - avgPrimTime) / avgKruskalTime * 100));
        } else {
            System.out.println("Winner: Kruskal's algorithm (" + 
                String.format("%.2f%% faster", (double)(avgPrimTime - avgKruskalTime) / avgPrimTime * 100));
        }
    }
}

