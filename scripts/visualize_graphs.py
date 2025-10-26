#!/usr/bin/env python3
"""
Graph Visualization Script
Generates visual representations of the test graphs
"""

import json
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import numpy as np

def load_graphs(filepath):
    """Load graphs from JSON file"""
    with open(filepath, 'r') as f:
        data = json.load(f)
    return data['graphs']

def create_circular_layout(vertices, radius=1.0):
    """Create circular layout for vertices"""
    n = vertices
    if n == 0:
        return {}
    
    positions = {}
    angle_step = 2 * np.pi / n
    
    for i in range(n):
        angle = i * angle_step - np.pi / 2  # Start from top
        x = radius * np.cos(angle)
        y = radius * np.sin(angle)
        positions[i] = (x, y)
    
    return positions

def visualize_graph(graph, save_path=None):
    """Visualize a single graph"""
    name = graph.get('name', 'graph')
    vertices = graph['vertices']
    edges = graph['edges']
    
    # Create figure
    fig, ax = plt.subplots(figsize=(10, 8))
    ax.set_aspect('equal')
    ax.axis('off')
    ax.set_title(f'Graph Visualization: {name}\nVertices: {vertices}, Edges: {len(edges)}', 
                 fontsize=14, fontweight='bold')
    
    # Get positions
    positions = create_circular_layout(vertices)
    
    # Draw edges
    for edge in edges:
        src = edge['source']
        dst = edge['destination']
        weight = edge['weight']
        
        x1, y1 = positions[src]
        x2, y2 = positions[dst]
        
        # Draw edge
        ax.plot([x1, x2], [y1, y2], 'b-', alpha=0.4, linewidth=1.5)
        
        # Add weight label
        mid_x, mid_y = (x1 + x2) / 2, (y1 + y2) / 2
        ax.text(mid_x, mid_y, str(weight), 
                fontsize=8, ha='center', va='center',
                bbox=dict(boxstyle='round,pad=0.3', facecolor='white', alpha=0.7))
    
    # Draw vertices
    for vertex, (x, y) in positions.items():
        ax.plot(x, y, 'ro', markersize=15, markerfacecolor='red', 
                markeredgecolor='darkred', markeredgewidth=2)
        ax.text(x, y, str(vertex), fontsize=10, ha='center', va='center', 
                color='white', fontweight='bold')
    
    # Save or show
    if save_path:
        plt.savefig(save_path, dpi=150, bbox_inches='tight')
        print(f"Saved graph visualization to: {save_path}")
    else:
        plt.show()
    
    plt.close()

def visualize_mst_comparison(graph, prim_edges, kruskal_edges, save_path=None):
    """Compare Prim's and Kruskal's MSTs side by side"""
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(16, 8))
    
    vertices = graph['vertices']
    all_edges = graph['edges']
    
    positions = create_circular_layout(vertices)
    
    # Helper function to draw graph on an axis
    def draw_graph_on_axis(ax, title, mst_edges_set, highlight_color='green'):
        ax.set_aspect('equal')
        ax.axis('off')
        ax.set_title(title, fontsize=12, fontweight='bold')
        
        # Draw all edges (light gray)
        for edge in all_edges:
            src = edge['source']
            dst = edge['destination']
            weight = edge['weight']
            
            x1, y1 = positions[src]
            x2, y2 = positions[dst]
            
            # Check if edge is in MST
            edge_tuple = tuple(sorted([src, dst]))
            if edge_tuple in mst_edges_set:
                # Highlight MST edge
                ax.plot([x1, x2], [y1, y2], color=highlight_color, 
                       linewidth=3, alpha=0.8)
            else:
                # Regular edge
                ax.plot([x1, x2], [y1, y2], 'lightgray', linewidth=1, alpha=0.3)
        
        # Draw vertices
        for vertex, (x, y) in positions.items():
            ax.plot(x, y, 'ro', markersize=12, markerfacecolor='red',
                   markeredgecolor='darkred', markeredgewidth=2)
            ax.text(x, y, str(vertex), fontsize=9, ha='center', va='center',
                   color='white', fontweight='bold')
    
    # Create MST edge sets for comparison
    prim_set = set()
    kruskal_set = set()
    
    for edge in prim_edges:
        prim_set.add(tuple(sorted([edge['source'], edge['destination']])))
    
    for edge in kruskal_edges:
        kruskal_set.add(tuple(sorted([edge['source'], edge['destination']])))
    
    # Draw Prim's MST
    draw_graph_on_axis(ax1, "Prim's Algorithm MST", prim_set, 'green')
    
    # Draw Kruskal's MST
    draw_graph_on_axis(ax2, "Kruskal's Algorithm MST", kruskal_set, 'blue')
    
    if save_path:
        plt.savefig(save_path, dpi=150, bbox_inches='tight')
        print(f"Saved MST comparison to: {save_path}")
    else:
        plt.show()
    
    plt.close()

def main():
    """Main function"""
    try:
        # Load graphs
        graphs = load_graphs('../data/assign_3_input.json')
        
        print("Generating graph visualizations...")
        
        # Visualize first 3 graphs
        for i, graph in enumerate(graphs[:3]):
            save_path = f'../report/graph_{i+1}_visualization.png'
            visualize_graph(graph, save_path)
        
        print(f"\nGenerated {min(3, len(graphs))} graph visualizations")
        print("Files saved in report/ directory")
        
    except Exception as e:
        print(f"Error generating visualizations: {e}")
        print("Make sure to install required packages:")
        print("  pip install matplotlib numpy")

if __name__ == '__main__':
    main()

