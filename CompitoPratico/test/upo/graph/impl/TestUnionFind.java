package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.unionfind.QuickUnion;

class TestUnionFind {

	private QuickUnion quickUnion;
	
	
	@BeforeEach
	void setUp() {
		quickUnion = new QuickUnion(5);
		
		quickUnion.makeSet("A");
		quickUnion.makeSet("B");
		quickUnion.makeSet("C");
		quickUnion.makeSet("D");
		quickUnion.makeSet("E");
	}

	@Test
	void testMakeSet() {
		assertEquals(1, quickUnion.getSize("A"));
		assertEquals(1, quickUnion.getSize("B"));
		assertEquals(1, quickUnion.getSize("C"));
		assertEquals(1, quickUnion.getSize("D"));
		assertEquals(1, quickUnion.getSize("E"));
	}

	@Test
	void testFind() {
		assertEquals("A", quickUnion.find("A"));
		assertEquals("B", quickUnion.find("B"));
		assertEquals("C", quickUnion.find("C"));
		assertEquals("D", quickUnion.find("D"));
		assertEquals("E", quickUnion.find("E"));
	}

	@Test
	void testUnion() {
		quickUnion.union("A", "B");
		assertEquals("A", quickUnion.find("A"));
		assertEquals("A", quickUnion.find("B"));
		assertEquals(2, quickUnion.getSize("A"));
		assertEquals(2, quickUnion.getSize("B"));
		
		quickUnion.union("C", "D"); 
		assertEquals("C", quickUnion.find("C"));
		assertEquals("C", quickUnion.find("D")); 
		assertEquals(2,quickUnion.getSize("C")); 
		assertEquals(2, quickUnion.getSize("D"));
		quickUnion.union("A", "E"); 
		assertEquals("A", quickUnion.find("A"));
		assertEquals("A", quickUnion.find("B")); 
		assertEquals("A",quickUnion.find("E")); 
		assertEquals(3, quickUnion.getSize("A"));
		assertEquals(2, quickUnion.getSize("B")); 
		assertEquals(3,quickUnion.getSize("E"));

	}

	@Test
	void testUnionWithNullVertex() {
		assertThrows(IllegalArgumentException.class, () -> quickUnion.union("A", null));
		assertThrows(IllegalArgumentException.class, () -> quickUnion.union(null, "B"));
		assertThrows(IllegalArgumentException.class, () -> quickUnion.union(null, null));
	}

}
