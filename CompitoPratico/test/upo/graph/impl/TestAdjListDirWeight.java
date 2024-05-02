package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import upo.graph.base.WeightedGraph;

class TestAdjListDirWeight {

	AdjListDir graph = new AdjListDir();

	private void populateGraph(int num) {
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
	}

	@Test
	public void testAddVertex() {
		assertEquals(0, graph.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
	}

	@Test
	public void testContainsVertex() {
		assertEquals(0, graph.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
		assertTrue(graph.containsVertex("A"));
		assertTrue(graph.containsVertex("B"));
		assertTrue(graph.containsVertex("C"));
		assertEquals(3, graph.size());
	}

	@Test
	public void testEdgeMethods() {
		assertEquals(0, graph.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
		assertTrue(graph.containsVertex("A"));
		assertTrue(graph.containsVertex("B"));
		assertTrue(graph.containsVertex("C"));
		assertEquals(3, graph.size());

		graph.addEdge("A", "B");
		assertTrue(graph.containsEdge("A", "B"));
		assertFalse(graph.containsEdge("B", "A"));
		assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("C", "D"));
	}

	@Test
	public void testTrueEquals() {
		AdjListDir tempEqualsMatrix = new AdjListDir();
		assertEquals(0, graph.addVertex("A"));
		assertEquals(0, tempEqualsMatrix.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(1, tempEqualsMatrix.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
		assertEquals(2, tempEqualsMatrix.addVertex("C"));
		assertEquals(3, graph.addVertex("D"));
		assertEquals(3, tempEqualsMatrix.addVertex("D"));

		graph.addEdge("A", "B");
		tempEqualsMatrix.addEdge("A", "B");
		graph.addEdge("C", "D");
		tempEqualsMatrix.addEdge("C", "D");

		assertTrue(graph.equals(tempEqualsMatrix));
		assertThrows(NoSuchElementException.class, () -> graph.getEdgeWeight("A", "C"));

	}

	@Test
	public void testFalseEquals() {
		AdjListDir tempEqualsMatrix = new AdjListDir();
		assertEquals(0, graph.addVertex("A"));
		assertEquals(0, tempEqualsMatrix.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(1, tempEqualsMatrix.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
		assertEquals(2, tempEqualsMatrix.addVertex("C"));
		assertEquals(3, graph.addVertex("D"));
		assertEquals(3, tempEqualsMatrix.addVertex("D"));

		graph.addEdge("A", "B");
		tempEqualsMatrix.addEdge("A", "B");
		graph.addEdge("A", "D");
		tempEqualsMatrix.addEdge("C", "B");

		assertFalse(graph.equals(tempEqualsMatrix));

	}

	@Test
	public void testFalseEqualsDifferentClass() {
		AdjListDirWeight anotherMatrix = new AdjListDirWeight();
		assertFalse(graph.equals(anotherMatrix));
	}

	@Test
	public void testExceptionCallingEdgeWeight() {
		assertThrows(UnsupportedOperationException.class, () -> graph.setEdgeWeight("A", "B", 1.0));
		assertThrows(IllegalArgumentException.class, () -> graph.getEdgeWeight("A", "B"));
	}

	@Test
	public void testDFSTree() {
		int[] timings = { 6, 3, 5, 8, 12, 11 };
		populateGraph(6);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		var forest = graph.getDFSTOTForest("A");
		for (int i = 0; i < graph.size(); i++)
			assertEquals(timings[i], forest.getEndTime(graph.getVertexLabel(i)));

	}

	@Test
	public void testBFSTree() {
		populateGraph(6);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		graph.addEdge("A", "E");
		graph.addEdge("B", "D");
		var visit = graph.getBFSTree("A");
		assertNotNull(visit);
		String[] vertices = new String[graph.size()];
		for (int i = graph.getVertexIndex("A") + 1; i < graph.size(); i++) {
			char a = (char) ('A' + i);
			vertices[i] = visit.getPartent(new String(new char[] { a }));
		}
		var arrayResult = new String[] { null, "A", "A", "B", "A", "E" };
		assertArrayEquals(arrayResult, vertices);
	}

	@Test
	public void testStronglyConnectedComponents() {
		populateGraph(10);
		graph.addEdge("A", "F");
		graph.addEdge("A", "E");
		graph.addEdge("B", "A");
		graph.addEdge("C", "B");
		graph.addEdge("C", "D");
		graph.addEdge("C", "G");
		graph.addEdge("D", "C");
		graph.addEdge("E", "A");
		graph.addEdge("E", "H");
		graph.addEdge("F", "B");
		graph.addEdge("F", "E");
		graph.addEdge("F", "H");
		graph.addEdge("G", "C");
		graph.addEdge("G", "F");
		graph.addEdge("G", "I");
		graph.addEdge("I", "H");
		graph.addEdge("I", "J");
		graph.addEdge("J", "I");

		var components = graph.stronglyConnectedComponents();
		assertEquals(4, components.size());
	}

	@Test
	public void testEdgeWeight() {
		populateGraph(6);
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		assertEquals(1.0, graph.getEdgeWeight("A", "B"));
		assertThrows(NoSuchElementException.class, () -> graph.getEdgeWeight("A", "D"));
	}

	@Test
	public void testRemoves() {
		populateGraph(10);
		graph.addEdge("A", "F");
		graph.addEdge("A", "E");
		graph.addEdge("B", "A");
		graph.addEdge("C", "B");
		graph.addEdge("C", "D");
		graph.addEdge("C", "G");
		graph.addEdge("D", "C");
		graph.addEdge("E", "A");
		graph.addEdge("E", "H");
		graph.addEdge("F", "B");
		graph.addEdge("F", "E");
		graph.addEdge("F", "H");
		graph.addEdge("G", "C");
		graph.addEdge("G", "F");
		graph.addEdge("G", "I");
		graph.addEdge("I", "H");
		graph.addEdge("I", "J");
		graph.addEdge("J", "I");
		graph.removeVertex("H");
		assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("F", "H"));
		assertFalse(graph.containsVertex("H"));
		var cc = graph.stronglyConnectedComponents();
		assertEquals(3, cc.size());
	}

	@Test
	void testAdjListDirCreation() {
		AdjListDir graph = new AdjListDir();
		assertNotNull(graph);
	}

	@Test
	void testAdjListDirWeightCreation() {
		AdjListDirWeight graph = new AdjListDirWeight();
		assertNotNull(graph);
	}

	@Test
	void testAddEdge() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		assertTrue(graph.containsEdge("A", "B"));
	}

	@Test
	void testRemoveVertex() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.removeVertex("A");
		assertFalse(graph.containsVertex("A"));
	}

	@Test
	void testGetVertexIndex() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		int index = graph.getVertexIndex("A");
		assertEquals(0, index);
	}

	@Test
	void testGetEdgeWeight() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		double weight = graph.getEdgeWeight("A", "B");
		assertEquals(1.0, weight, 0.001); // Adjust delta as needed
	}

	@Test
	void testIsDirected() {
		AdjListDirWeight graph = new AdjListDirWeight();
		assertTrue(graph.isDirected());
	}

	@Test
	void testIsCyclic() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		assertFalse(graph.isCyclic());
	}

	@Test
	void testIsDAG() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		assertTrue(graph.isDAG());
	}

	@Test
	void testTopologicalSort() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addEdge("A", "B");
		graph.addEdge("B", "C");
		graph.addEdge("A", "C");
		String[] result = graph.topologicalSort();
		assertArrayEquals(new String[] { "A", "B", "C" }, result);
	}

	@Test
	void testSetEdgeWeight() {
		AdjListDirWeight graph = new AdjListDirWeight();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addEdge("A", "B");
		graph.setEdgeWeight("A", "B", 5.0);
		assertEquals(5.0, graph.getEdgeWeight("A", "B"));
	}

	@Test
    void testBellmanFordShortestPaths() {

        AdjListDirWeight graph = new AdjListDirWeight();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");
        graph.setEdgeWeight("A","B", 10); 
        graph.setEdgeWeight("B","C", 5);
        graph.setEdgeWeight("A","C", 6);

        WeightedGraph shortestPaths = graph.getBellmanFordShortestPaths("A");

        assertNotNull(shortestPaths);

        assertEquals(6, shortestPaths.getEdgeWeight("A", "C"));
        assertEquals(10, shortestPaths.getEdgeWeight("A", "B"));

    }

}