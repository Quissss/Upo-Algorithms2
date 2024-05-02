package upo.graph.impl;

/*
 * Implementazione di un singolo vertice di un grafo 	 
 */
public class AdjListVertex {
	
	private String vertex ; // vertice singolo 
	private double weight ; //peso 
	
	//costruttore 
	public AdjListVertex(String vertex, double weight) {
		this.vertex=vertex;
		this.weight=weight;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
        return vertex.length() + vertex.hashCode();
    }

	@Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj.getClass().equals(this.getClass()))
        {
            AdjListVertex comparedTo = (AdjListVertex)obj;
            return (vertex.equals(comparedTo.vertex) && weight == comparedTo.weight);
        }
        return false;
    }
	
	
}
