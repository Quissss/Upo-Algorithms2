package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import upo.greedy.Greedy;


class TestIntervalliDisgiunti {

	@Test
    public void testGetMaxDisjointIntervals() {
        Integer[] start = {2, 5, 6};
        Integer[] end = {5, 7, 8};
        Integer[] expected = {0, 2};

        Integer[] risultato = Greedy.getMaxDisjointIntervals(start, end);

        assertArrayEquals(expected, risultato);
    }

}
