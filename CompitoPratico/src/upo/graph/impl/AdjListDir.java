package upo.graph.impl;

import java.util.NoSuchElementException;

import upo.graph.base.WeightedGraph;

public class AdjListDir extends AdjListDirWeight {

	@Override
	public void setEdgeWeight(String arg0, String arg1, double arg2)
			throws IllegalArgumentException, NoSuchElementException {
		throw new UnsupportedOperationException("Impossibile impostare il peso dell'arco sul grafo non pesato.");
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		return super.getPrimMST(startingVertex);
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.getBellmanFordShortestPaths(startingVertex);
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.getDijkstraShortestPaths(startingVertex);
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return super.getKruskalMST();
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return super.getFloydWarshallShortestPaths();
	}

	
	
}
