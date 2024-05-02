package upo.graph.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;

public class AdjListDirWeight implements WeightedGraph {

	int time = 0;
	ArrayList<AdjListItem> vertices = new ArrayList<>(); // lista adiacenze

	public AdjListDirWeight() {
	}

	public AdjListDirWeight(AdjListDirWeight graph) {
		for (AdjListItem adjList : graph.vertices) {
			var current = new AdjListItem(adjList.getVertex());
			vertices.add(current);
			for (AdjListVertex vertexAdjListItem : adjList.getAdjList())
				current.getAdjList()
						.add(new AdjListVertex(vertexAdjListItem.getVertex(), vertexAdjListItem.getWeight()));
		}
	}

	@Override
	public int getVertexIndex(String arg) {
		if(arg == null)
			return -1;
		for (AdjListItem adjListItem : vertices)
			if (adjListItem.getVertex().equals(arg))
				return vertices.indexOf(adjListItem);
		return -1;
	}

	@Override
	public int addVertex(String primo) {
		AdjListItem item = new AdjListItem(primo);
		vertices.add(item);
		return vertices.indexOf(item);
	}

	@Override
	public String getVertexLabel(Integer arg) {
		if(arg == null)
			return null;
		return vertices.get(arg).getVertex();
	}

	@Override
	public boolean containsVertex(String arg) {
		for (var adjListItem : vertices)
			if (adjListItem.getVertex().equals(arg))
				return true;
		return false;
	}

	@Override
	public void removeVertex(String arg) throws NoSuchElementException {
		if (!containsVertex(arg)) {
			throw new NoSuchElementException("Vertice non presente");
		}
	
		int vertexIndex = getVertexIndex(arg);
		vertices.remove(vertexIndex);
		for (AdjListItem vertex : vertices) {
			List<AdjListVertex> adjList = vertex.getAdjList();
			adjList.removeIf(adjVertex -> adjVertex.getVertex().equals(arg));
		}
	}

	@Override
	public void addEdge(String src, String dest) throws IllegalArgumentException {
		if (!containsVertex(src) || !containsVertex(dest))
			throw new IllegalArgumentException("Non sono presenti nel grafo i vertici");

		vertices.get(getVertexIndex(src)).getAdjList().add(new AdjListVertex(dest, defaultEdgeWeight));
	}

	@Override
	public boolean containsEdge(String source, String target) throws IllegalArgumentException {
		if (!containsVertex(source) || !containsVertex(target))
			throw new IllegalArgumentException("Vertice non presente");

		for (var adj : getAdjacent(source))
			if (adj.equals(target))
				return true;
		return false;
	}

	@Override
	public void removeEdge(String src, String dest) throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(src) || !containsVertex(dest))
			throw new IllegalArgumentException("Vertice non presente");
		if (!containsEdge(src, dest))
			throw new NoSuchElementException("Arco non è presente nel grafo");

		var weight = getEdgeWeight(src, dest);
		vertices.get(getVertexIndex(src)).getAdjList().remove(new AdjListVertex(dest, weight));
	}

	@Override
	public Set<String> getAdjacent(String vertex) throws NoSuchElementException {
		if (!containsVertex(vertex))
			throw new NoSuchElementException("Vertice non nel grafo");

		HashSet<String> set = new HashSet<>();
		for (var item : vertices.get(getVertexIndex(vertex)).getAdjList())
			set.add(item.getVertex());
		return set;
	}

	@Override
	public boolean isAdjacent(String targetVertex, String sourceVertex) throws IllegalArgumentException {
		return containsEdge(targetVertex, sourceVertex);
	}

	@Override
	public int size() {
		return vertices.size();
	}

	@Override
	public boolean isDirected() {
		return true;
	}

	@Override
	public boolean isCyclic() {
		var forest = new VisitForest(this, null);

		for (var element : vertices)
			if ((forest.getColor(element.getVertex()) == Color.WHITE)
					&& recursiveCycleVisit(forest, element.getVertex()))
				return true;

		return false;
	}

	private boolean recursiveCycleVisit(VisitForest forest, String item) {
		forest.setColor(item, Color.GRAY);
		for (var element : getAdjacent(item)) {
			if (forest.getColor(element) == Color.WHITE) {
				forest.setParent(element, item);
				if (recursiveCycleVisit(forest, element))
					return true;
			} else if (!element.equals(forest.getPartent(item)))
				return true;
		}
		forest.setColor(item, Color.BLACK);
		return false;
	}

	@Override
	public boolean isDAG() {
		VisitForest forest = new VisitForest(this, null);
		for (AdjListItem vertex : vertices) {
			if (forest.getColor(vertex.getVertex()) == Color.WHITE && hasCycle(forest, vertex.getVertex())) {
				return false;
			}
		}
		return true;
	}

	private boolean hasCycle(VisitForest forest, String vertex) {
		forest.setColor(vertex, Color.GRAY);
		for (String neighbor : getAdjacent(vertex)) {
			if (forest.getColor(neighbor) == Color.WHITE) {
				if (hasCycle(forest, neighbor)) {
					return true;
				}
			} else if (forest.getColor(neighbor) == Color.GRAY) {
				return true;
			}
		}
		forest.setColor(vertex, Color.BLACK);
		return false;
	}

	@Override
	public VisitForest getBFSTree(String vertex) throws UnsupportedOperationException, IllegalArgumentException {
		if (!containsVertex(vertex))
			throw new IllegalArgumentException("Vertice non contenuto");

		var forest = new VisitForest(this, VisitType.BFS);
		Queue<String> verticesQueue = new LinkedList<>();

		forest.setColor(vertex, Color.WHITE);
		verticesQueue.add(vertex);

		while (!verticesQueue.isEmpty()) {
			for (var adj : getAdjacent(verticesQueue.peek())) {
				if (forest.getColor(adj) == Color.WHITE) {
					forest.setColor(adj, Color.GRAY);
					forest.setParent(adj, verticesQueue.peek());
					verticesQueue.add(adj);
				}
			}
			forest.setColor(verticesQueue.peek(), Color.BLACK);
			verticesQueue.remove(verticesQueue.peek());
		}

		return forest;
	}

	@Override
	public VisitForest getDFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if (!containsVertex(startingVertex))
			throw new IllegalArgumentException("Vertice non nel grafo");
		time = 1;
		VisitForest forest = new VisitForest(this, VisitType.DFS);
		ricorsivoDFSTree(forest, startingVertex);
		return forest;
	}

	private void ricorsivoDFSTree(VisitForest forest, String vertex) {
		forest.setColor(vertex, Color.GRAY);
		forest.setStartTime(vertex, time++);
		for (var element : getAdjacent(vertex)) {
			if (forest.getColor(element) == Color.WHITE) {
				forest.setParent(element, vertex);
				ricorsivoDFSTree(forest, element);
			}
		}
		forest.setColor(vertex, Color.BLACK);
		forest.setEndTime(vertex, time++);
	}

	@Override
	public VisitForest getDFSTOTForest(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		if (!containsVertex(startingVertex))
			throw new IllegalArgumentException("Vertice non presente nel grafo");
		time = 1;
		VisitForest forest = new VisitForest(this, VisitType.DFS_TOT);
		ricorsivoDFSTree(forest, startingVertex);
		for (var element : vertices)
			if (forest.getColor(element.getVertex()) == Color.WHITE)
				ricorsivoDFSTree(forest, element.getVertex());
		return forest;
	}

	@Override
	public VisitForest getDFSTOTForest(String[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		if (vertexOrdering.length != vertices.size())
			throw new IllegalArgumentException("Il numero di vertici non coincide.");
		time = 1;
		VisitForest forest = new VisitForest(this, VisitType.DFS_TOT);
		for (var element : vertexOrdering)
			if (forest.getColor(element) == Color.WHITE)
				ricorsivoDFSTree(forest, element);
		return forest;
	}

	@Override
	public String[] topologicalSort() throws UnsupportedOperationException {
		if (!isDAG()) {
			throw new UnsupportedOperationException("L'ordinamento  non è applicabile ai grafi ciclici.");
		}

		List<String> result = new ArrayList<>();
		Set<String> visited = new HashSet<>();
		Deque<String> stack = new LinkedList<>();

		for (AdjListItem vertex : vertices) {
			String currentVertex = vertex.getVertex();
			if (!visited.contains(currentVertex)) {
				topologicalSortUtil(currentVertex, visited, stack);
			}
		}

		while (!stack.isEmpty()) {
			result.add(stack.pop());
		}

		return result.toArray(new String[0]);
	}

	private void topologicalSortUtil(String vertex, Set<String> visited, Deque<String> stack) {
		visited.add(vertex);
		for (String vicino : getAdjacent(vertex)) {
			if (!visited.contains(vicino)) {
				topologicalSortUtil(vicino, visited, stack);
			}
		}
		stack.push(vertex);

	}

	@Override
	public Set<Set<String>> stronglyConnectedComponents() throws UnsupportedOperationException {
		Set<Set<String>> components = new HashSet<>();
		var forest = getDFSTOTForest(vertices.get(0).getVertex());
		var transposedGraph = getTransposedGraph(forest);

		forest = new VisitForest(transposedGraph, VisitType.DFS);
		for (var element : transposedGraph.vertices) {
			if (forest.getColor(element.getVertex()) == Color.WHITE) {
				components.add(getDFSTree(forest, element.getVertex(), transposedGraph));
			}
		}

		return components;
	}

	private Set<String> getDFSTree(VisitForest forest, String vertex, WeightedGraph transposedGraph) {
		Set<String> setCFC = new HashSet<>();
		recDFSVisit(forest, vertex, transposedGraph, setCFC);
		return setCFC;
	}

	private void recDFSVisit(VisitForest forest, String vertex, WeightedGraph transposedGraph, Set<String> setCFC) {
		forest.setColor(vertex, Color.GRAY);
		forest.setStartTime(vertex, time);
		time++;

		setCFC.add(vertex);
		for (var element : transposedGraph.getAdjacent(vertex)) {
			if (forest.getColor(element) == Color.WHITE) {
				forest.setParent(element, vertex);
				recDFSVisit(forest, element, transposedGraph, setCFC);
			}
		}

		forest.setColor(vertex, Color.BLACK);
		forest.setEndTime(vertex, time);
		time++;
	}

	private AdjListDirWeight getTransposedGraph(VisitForest forest) {
		Integer[] array = new Integer[size()];
		Integer[] sortedArray = new Integer[size()];

		for (var element : vertices) {
			array[getVertexIndex(element.getVertex())] = forest.getEndTime(element.getVertex());
			sortedArray[getVertexIndex(element.getVertex())] = forest.getEndTime(element.getVertex());
		}

		Arrays.sort(sortedArray, Collections.reverseOrder());

		var transposedGraph = new AdjListDirWeight();

		for (var element : sortedArray)
			transposedGraph.vertices.add(new AdjListItem(getVertexLabel(Arrays.asList(array).indexOf(element))));

		for (var vert : vertices) {
			for (var vertex : vert.getAdjList()) {
				transposedGraph.addEdge(vertex.getVertex(), vert.getVertex());
			}
		}
		return transposedGraph;
	}

	@Override
	public Set<Set<String>> connectedComponents() 
			throws UnsupportedOperationException, IllegalArgumentException {
		return null;
	}
	

	@Override
	public double getEdgeWeight(String sourceVertex, String targetVertex)
			throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(sourceVertex) || !containsVertex(targetVertex))
			throw new IllegalArgumentException("Vertice non presente nel grafo");
		if (!containsEdge(sourceVertex, targetVertex))
			throw new NoSuchElementException("Arco non appartenente al grafo");

		for (AdjListItem adjListItem : vertices)
			if (adjListItem.getVertex().equals(sourceVertex) || adjListItem.getVertex().equals(targetVertex))
				for (var item : adjListItem.getAdjList())
					if (item.getVertex().equals(targetVertex) || item.getVertex().equals(sourceVertex))
						return item.getWeight();
		return 0;
	}

	@Override
	public void setEdgeWeight(String sourceVertex, String targetVertex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		if (!containsVertex(sourceVertex) || !containsVertex(targetVertex))
			throw new IllegalArgumentException("Vertice non nel grafto");
		if (!containsEdge(sourceVertex, targetVertex))
			throw new NoSuchElementException("Arco non appartiene al grafo");

		for (AdjListItem adjListItem : vertices)
			if (adjListItem.getVertex().equals(sourceVertex) || adjListItem.getVertex().equals(targetVertex))
				for (var item : adjListItem.getAdjList())
					if (item.getVertex().equals(targetVertex) || item.getVertex().equals(sourceVertex))
						item.setWeight(weight);
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(vertices);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AdjListDirWeight other = (AdjListDirWeight) obj;
       
		for (AdjListItem vertex : vertices)
            if (!other.containsVertex(vertex.getVertex()))
                return false;

        return compareEdges(other);
    }

    private boolean compareEdges(WeightedGraph graph) {
        for (AdjListItem vertex : vertices)
            for (var adjacent : vertex.getAdjList()) {
                if (graph.containsEdge(vertex.getVertex(), adjacent.getVertex()) != containsEdge(vertex.getVertex(),
                        adjacent.getVertex()))
                    return false;
                if (getEdgeWeight(vertex.getVertex(), adjacent.getVertex()) != graph.getEdgeWeight(vertex.getVertex(),
                        adjacent.getVertex()))
                    return false;
            }
        return true;
    }
	
	
    @Override
    public WeightedGraph getBellmanFordShortestPaths(String startingVertex)
            throws UnsupportedOperationException, IllegalArgumentException {

        if (!containsVertex(startingVertex))
            throw new IllegalArgumentException("Il vertice non è nel grafo");
        VisitForest forest = new VisitForest(this, null);
        AdjListDirWeight graph = new AdjListDirWeight();
        for (var element : vertices) {
            forest.setDistance(element.getVertex(), Integer.MAX_VALUE);
            graph.addVertex(element.getVertex());
        }
        forest.setDistance(startingVertex, 0);

        for (var element : vertices.get(getVertexIndex(startingVertex)).getAdjList()) {
            if (forest.getDistance(element.getVertex()) > forest.getDistance(startingVertex)
                    + getEdgeWeight(startingVertex, element.getVertex())) {
                forest.setParent(element.getVertex(), startingVertex);
                forest.setDistance(element.getVertex(),
                        forest.getDistance(startingVertex) + getEdgeWeight(startingVertex, element.getVertex()));
            }
        }

        for (AdjListItem vertex : vertices) {
            if (!vertex.getVertex().equals(startingVertex)) {
                graph.addEdge(forest.getPartent(vertex.getVertex()), vertex.getVertex());
                graph.setEdgeWeight(forest.getPartent(vertex.getVertex()), vertex.getVertex(),
                        forest.getDistance(vertex.getVertex()));
            }
        }
        return graph;
    }

	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementato");
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("Non implementato");
		
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementato");
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Non implementato");
	}

}
