package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	
	private SimpleWeightedGraph <Director, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map <Integer, Director> idMap;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		dao.listAllDirectors(idMap);
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(grafo, dao.getVertici(anno, this.idMap));
		
		
		//aggiungo archi
		for( Adiacenza a : dao.getAdiacenze(anno, idMap)) {
			//if(this.grafo.containsVertex(a.getD1()) && this.grafo.containsVertex(a.getD2())) {
				Graphs.addEdgeWithVertices(grafo, a.getD1(), a.getD2(), a.getPeso());
			
			//}
		}
		
		
		System.out.println("GRAFO CREATO con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.");
	}
	
	public Graph <Director,DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
	
	public List <DirectorAdiacente> getDirectorAdiacenti(Director partenza){
		 List <Director> vicini = Graphs.neighborListOf(this.getGrafo(), partenza);
		 List <DirectorAdiacente> result = new ArrayList<>();
		 for(DefaultWeightedEdge e: this.grafo.edgesOf(partenza)) {
				
				int peso = (int) this.grafo.getEdgeWeight(e);
				
				Director dir = Graphs.getOppositeVertex(this.grafo, e, partenza);
				
				result.add(new DirectorAdiacente(dir, peso));
				}
		    	
		    	Collections.sort(result);
		    	return result;
	}
	
}
