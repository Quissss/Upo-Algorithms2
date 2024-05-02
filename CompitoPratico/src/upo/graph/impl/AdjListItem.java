package upo.graph.impl;

import java.util.ArrayList;

/*
 * Implementazione di una singola lista di adiacenza 
 */
public class AdjListItem {
	
	private String vertex; //vertice singolo
	private ArrayList <AdjListVertex> adjList; //nodi adiacenti
	
	public AdjListItem(String vertex) {
		this.vertex=vertex;
		adjList =new ArrayList<>();
	}

	public String getVertex() {
		return vertex;
	}
	

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public ArrayList<AdjListVertex> getAdjList() {
		return adjList;
	}

	public void setVertices(ArrayList<AdjListVertex> adjList) {
		this.adjList = adjList;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass().equals(this.getClass())) {
            AdjListItem comparedTo = (AdjListItem) obj;
            if (vertex == comparedTo.getVertex() && adjList.size() == comparedTo.adjList.size()) {
                for (int i = 0; i < adjList.size(); i++) {
                    var local = adjList.get(i);
                    var compared = comparedTo.adjList.get(i);
                    if (!local.equals(compared))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return vertex.length() + vertex.hashCode() + adjList.size() + adjList.hashCode();
    }
	
}
