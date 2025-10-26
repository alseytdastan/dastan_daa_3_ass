package com.mst.io;

import com.google.gson.*;
import com.mst.model.Graph;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Handles reading and writing graph data to/from JSON files.
 */
public class GraphIO {
    
    /**
     * Reads a graph from a JSON file.
     * Expected JSON format:
     * {
     *   "vertices": 5,
     *   "edges": [
     *     {"source": 0, "destination": 1, "weight": 10},
     *     ...
     *   ]
     * }
     */
    public static Graph readGraphFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            
            int vertices = jsonObject.get("vertices").getAsInt();
            Graph graph = new Graph(vertices);
            
            JsonArray edgesArray = jsonObject.getAsJsonArray("edges");
            for (JsonElement element : edgesArray) {
                JsonObject edgeObj = element.getAsJsonObject();
                int source = edgeObj.get("source").getAsInt();
                int destination = edgeObj.get("destination").getAsInt();
                int weight = edgeObj.get("weight").getAsInt();
                
                graph.addEdge(source, destination, weight);
            }
            
            return graph;
        }
    }

    /**
     * Writes a graph to a JSON file.
     */
    public static void writeGraphToFile(Graph graph, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("vertices", graph.getVertices());
        
        JsonArray edgesArray = new JsonArray();
        for (com.mst.model.Edge edge : graph.getEdges()) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("source", edge.getSource());
            edgeObj.addProperty("destination", edge.getDestination());
            edgeObj.addProperty("weight", edge.getWeight());
            edgesArray.add(edgeObj);
        }
        jsonObject.add("edges", edgesArray);
        
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(jsonObject, writer);
        }
    }

    /**
     * Reads multiple graphs from a JSON file.
     * Expected format:
     * {
     *   "graphs": [
     *     {"name": "graph1", "vertices": 5, "edges": [...]},
     *     ...
     *   ]
     * }
     */
    public static Graph[] readGraphsFromFile(String filePath) throws IOException {
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
                for (JsonElement element : edgesArray) {
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
}

