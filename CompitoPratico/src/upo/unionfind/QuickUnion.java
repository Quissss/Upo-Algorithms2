package upo.unionfind;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.impl.AdjListDir;

public class QuickUnion {
	protected Integer[] size;
	protected AdjListDir graph;
	protected VisitForest forest;

	public QuickUnion(int numberOfVertices) {
		graph = new AdjListDir();
		size = new Integer[numberOfVertices];
		forest = new VisitForest(graph, VisitType.DFS);
	}

	public int getSize(String vertex) {
		return size[graph.getVertexIndex(vertex)];
	}

	public void makeSet(String vertex) {
		if (vertex == null)
			throw new IllegalArgumentException("Vertici non può essere nullo");

		int index = graph.addVertex(vertex);
		size[index] = 1;
		VisitForest newforest = new VisitForest(graph, null);

		newforest.setParent(vertex, vertex);

		if (forest != null) {
			for (int i = 0; i < graph.size() - 1; i++) {
				newforest.setParent(graph.getVertexLabel(i), forest.getPartent(graph.getVertexLabel(i)));
			}
		}
		forest = newforest;
	}

	public String find(String vertex) {
		if (vertex == null)
			throw new IllegalArgumentException("Vertici non può essere nullo");
		if (forest.getPartent(vertex) != vertex) {
			String next = forest.getPartent(vertex);
			forest.setParent(vertex, next);
			vertex = next;
		}
		return vertex;
	}

	public void union(String vertex1, String vertex2) {
		if (vertex1 == null || vertex2 == null) {
            throw new IllegalArgumentException("Vertici non può essere nullo");
        }
        String parent1 = find(vertex1);
        String parent2 = find(vertex2);
        if (parent1.equals(parent2)) {
            return;
        }
        int size1 = getSize(parent1);
        int size2 = getSize(parent2);
        if (size1 < size2) {
            graph.addEdge(parent2, parent1);
            forest.setParent(parent1, parent2);
            size[graph.getVertexIndex(parent1)] = size2+size1;
            size[graph.getVertexIndex(parent2)] = size2+size1;
        } else {
            graph.addEdge(parent1, parent2);
            forest.setParent(parent2, parent1);
            size[graph.getVertexIndex(parent1)] = size1+size2;
            size[graph.getVertexIndex(parent2)] = size1+size2;
        }

	}

}