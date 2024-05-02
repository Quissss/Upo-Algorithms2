package upo.graph.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Test;

public class TestAdjListDir {
	AdjListDir graph = new AdjListDir();

	@Test
	public void testAddVertex() {
		assertEquals(0, graph.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
	}

	@Test
	public void testRemoves() {
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		graph.removeVertex("A");
		assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("A", "E"));
		assertFalse(graph.containsVertex("A"));
		assertTrue(graph.containsEdge("D", "B"));
		assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("A", "B"));
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
		AdjListDir tempAdjListDir = new AdjListDir();
		assertEquals(0, graph.addVertex("A"));
		assertEquals(0, tempAdjListDir.addVertex("A"));
		assertEquals(1, graph.addVertex("B"));
		assertEquals(1, tempAdjListDir.addVertex("B"));
		assertEquals(2, graph.addVertex("C"));
		assertEquals(2, tempAdjListDir.addVertex("C"));
		assertEquals(3, graph.addVertex("D"));
		assertEquals(3, tempAdjListDir.addVertex("D"));

		graph.addEdge("A", "B");
		tempAdjListDir.addEdge("A", "B");
		graph.addEdge("C", "D");
		tempAdjListDir.addEdge("C", "D");

		assertTrue(graph.equals(tempAdjListDir));
		assertThrows(NoSuchElementException.class, () -> graph.getEdgeWeight("A", "C"));

	}

	@Test
	public void testFalseEqualsDifferentClass() {
		AdjListDirWeight anotherList = new AdjListDirWeight();
		assertFalse(graph.equals(anotherList));
		// Different classes lead to false result
	}

	@Test
	public void testExceptionCallingEdgeWeight() {
		assertThrows(UnsupportedOperationException.class, () -> graph.setEdgeWeight("A", "B", 1.0));
		assertThrows(IllegalArgumentException.class, () -> graph.getEdgeWeight("A", "B"));
	}

	@Test
	public void testDFSTree() {
		int[] timings = { 6, 3, 5, -1, -1, -1 };
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		var forest = graph.getDFSTree("A");
		for (int i = 0; i < graph.size(); i++)
			assertEquals((Integer) timings[i], forest.getEndTime(graph.getVertexLabel(i)));
	}

	@Test
	public void testDFSTOTTree() {
		int[] timings = { 6, 3, 5, 8, 12, 11 };
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		var forest = graph.getDFSTOTForest("A");
		for (int i = 0; i < graph.size(); i++)
			assertEquals((Integer) timings[i], forest.getEndTime(graph.getVertexLabel(i)));

	}

	@Test
	public void testDFSTOTArrayTree() {
		int[] timings = { 6, 3, 5, 8, 12, 11 };
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "B");
		graph.addEdge("E", "A");
		graph.addEdge("E", "F");
		var forest = graph.getDFSTOTForest(new String[] { "A", "B", "C", "D", "E", "F" });
		for (int i = 0; i < graph.size(); i++)
			assertEquals((Integer) timings[i], forest.getEndTime(graph.getVertexLabel(i)));

	}

	@Test
	public void testBFSTree() {
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("B", "D");
		graph.addEdge("A", "E");
		graph.addEdge("E", "F");
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
	public void testCyclic() {
		int num = 6;
		for (char a = 'A'; a < 'A' + num; a++)
			graph.addVertex(new String(new char[] { a }));
		graph.addEdge("A", "B");
		graph.addEdge("A", "C");
		graph.addEdge("D", "E");
		graph.addEdge("E", "F");

		assertFalse(graph.isCyclic());
		graph.addEdge("C", "B");
		assertTrue(graph.isCyclic());
	}

	@Test
	public void testPrimException() {
		assertThrows(UnsupportedOperationException.class, () -> graph.getPrimMST("A"));
	}
}